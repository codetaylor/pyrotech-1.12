package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.LIFOStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockCampfire;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCampfire
    extends TileEntity
    implements ITickable {

  private ItemStackHandler stackHandler;
  private ItemStackHandler outputStackHandler;
  private LIFOStackHandler fuelStackHandler;
  private int cookTime;
  private int cookTimeTotal;
  private int burnTimeRemaining;
  private boolean active;
  private boolean dead;
  private boolean doused;

  // transient
  private EntityItem entityItem;
  private EntityItem entityItemOutput;
  private int ticksSinceLastClientSync;
  private int rainTimeRemaining;

  public TileCampfire() {

    this.stackHandler = new ItemStackHandler(1) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 1;
      }

      @Override
      protected void onContentsChanged(int slot) {

        TileCampfire.this.setCookTime(TileCampfire.this.getCookTime(this.getStackInSlot(slot)));
        TileCampfire.this.markDirty();
        BlockHelper.notifyBlockUpdate(TileCampfire.this.world, TileCampfire.this.pos);
      }
    };

    this.outputStackHandler = new ItemStackHandler(1) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 1;
      }

      @Override
      protected void onContentsChanged(int slot) {

        TileCampfire.this.markDirty();
        BlockHelper.notifyBlockUpdate(TileCampfire.this.world, TileCampfire.this.pos);
      }
    };

    this.fuelStackHandler = new LIFOStackHandler(8) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 1;
      }
    };
    this.fuelStackHandler.addObserver((handler, slot) -> {
      this.markDirty();
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    });
    this.fuelStackHandler.addObserverContentsCleared(handler -> {
      this.markDirty();
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    });

    this.burnTimeRemaining = ModulePyrotechConfig.FUEL.TINDER_BURN_TIME_TICKS;
    this.cookTime = -1;
    this.cookTimeTotal = -1;
    this.rainTimeRemaining = ModulePyrotechConfig.CAMPFIRE.TICKS_BEFORE_EXTINGUISHED;
  }

  public void setCookTime(int cookTime) {

    this.cookTime = cookTime;
    this.cookTimeTotal = cookTime;
  }

  public int getCookTime(ItemStack stack) {

    return (stack.isEmpty()) ? -1 : ModulePyrotechConfig.CAMPFIRE.COOK_TIME_TICKS;
  }

  public EntityItem getEntityItem() {

    if (this.entityItem == null) {
      ItemStack stackInSlot = this.stackHandler.getStackInSlot(0);
      this.entityItem = new EntityItem(this.world);
      this.entityItem.setItem(stackInSlot);
    }

    return this.entityItem;
  }

  public EntityItem getEntityItemOutput() {

    if (this.entityItemOutput == null) {
      ItemStack stackInSlot = this.outputStackHandler.getStackInSlot(0);
      this.entityItemOutput = new EntityItem(this.world);
      this.entityItemOutput.setItem(stackInSlot);
    }

    return this.entityItemOutput;
  }

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public LIFOStackHandler getFuelStackHandler() {

    return this.fuelStackHandler;
  }

  public BlockCampfire.EnumType getState() {

    if (this.isActive()) {
      return BlockCampfire.EnumType.LIT;

    } else if (this.isDead()) {
      return BlockCampfire.EnumType.ASH;
    }

    return BlockCampfire.EnumType.NORMAL;
  }

  public int getFuelRemaining() {

    // 0 is no wood
    // [0, 8]

    int index = this.fuelStackHandler.getLastNonEmptyIndex();
    return (index + 1);
  }

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (this.isDead()) {
      return;
    }

    if (this.isActive()) {

      if (ModulePyrotechConfig.CAMPFIRE.EXTINGUISHED_BY_RAIN) {

        if (this.world.isRainingAt(this.pos.up())) {

          if (this.rainTimeRemaining > 0) {
            this.rainTimeRemaining -= 1;
          }

          if (this.rainTimeRemaining == 0) {
            this.setActive(false);
            this.doused = true;
          }

        } else {
          this.rainTimeRemaining = ModulePyrotechConfig.CAMPFIRE.TICKS_BEFORE_EXTINGUISHED;
        }
      }

      if (this.cookTime > 0) {
        this.cookTime -= 1;
      }

      if (this.cookTime == 0) {
        ItemStack itemStack = this.stackHandler.extractItem(0, 1, false);

        if (!itemStack.isEmpty()) {
          ItemStack result = FurnaceRecipes.instance().getSmeltingResult(itemStack);
          this.outputStackHandler.insertItem(0, result, false);
        }
      }

      this.burnTimeRemaining -= 1;

      if (this.burnTimeRemaining <= 0) {

        // consume fuel

        ItemStack itemStack = this.fuelStackHandler.extractItem(0, 1, false);

        if (!itemStack.isEmpty()) {
          this.burnTimeRemaining = ModulePyrotechConfig.CAMPFIRE.BURN_TIME_TICKS_PER_LOG;

        } else {
          this.setActive(false);
          this.dead = true;

          ItemStackHandler stackHandler = this.getStackHandler();
          ItemStack contents = stackHandler.extractItem(0, 64, false);

          if (!contents.isEmpty()) {
            StackHelper.spawnStackOnTop(this.world, contents, this.pos);
          }

          stackHandler = this.getOutputStackHandler();
          contents = stackHandler.extractItem(0, 64, false);

          if (!contents.isEmpty()) {
            StackHelper.spawnStackOnTop(this.world, contents, this.pos);
          }

        }
      }

      this.ticksSinceLastClientSync += 1;

      if (this.ticksSinceLastClientSync >= 20) {
        this.ticksSinceLastClientSync = 0;
        BlockHelper.notifyBlockUpdate(this.world, this.pos);
      }

      this.markDirty();
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setTag("fuelStackHandler", this.fuelStackHandler.serializeNBT());
    compound.setInteger("burnTimeRemaining", this.burnTimeRemaining);
    compound.setBoolean("active", this.active);
    compound.setBoolean("dead", this.dead);
    compound.setInteger("cookTime", this.cookTime);
    compound.setInteger("cookTimeTotal", this.cookTimeTotal);
    compound.setBoolean("doused", this.doused);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.fuelStackHandler.deserializeNBT(compound.getCompoundTag("fuelStackHandler"));
    this.burnTimeRemaining = compound.getInteger("burnTimeRemaining");
    this.active = compound.getBoolean("active");
    this.dead = compound.getBoolean("dead");
    this.cookTime = compound.getInteger("cookTime");
    this.cookTimeTotal = compound.getInteger("cookTimeTotal");
    this.doused = compound.getBoolean("doused");
  }

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {

    this.readFromNBT(packet.getNbtCompound());
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
    world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
  }

  @Override
  public boolean shouldRefresh(
      World world,
      BlockPos pos,
      @Nonnull IBlockState oldState,
      @Nonnull IBlockState newState
  ) {

    if (oldState.getBlock() == newState.getBlock()) {
      return false;
    }

    return super.shouldRefresh(world, pos, oldState, newState);
  }

  public boolean isDead() {

    return this.dead;
  }

  public boolean isActive() {

    return this.active;
  }

  public void setActive(boolean active) {

    if (this.isDead()) {
      return;
    }

    if (!this.active && active) {
      this.active = true;
      this.markDirty();
      BlockHelper.notifyBlockUpdate(this.world, this.pos);

    } else if (this.active && !active) {
      this.active = false;
      this.markDirty();
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }
  }

  public void removeItems() {

    if (this.getState() == BlockCampfire.EnumType.ASH) {
      StackHelper.spawnStackOnTop(this.world, ItemMaterial.EnumType.PIT_ASH.asStack(1), this.pos, -0.125);

    } else if (!this.doused
        && this.getState() == BlockCampfire.EnumType.NORMAL) {
      StackHelper.spawnStackOnTop(this.world, new ItemStack(ModuleItems.TINDER), this.pos, -0.125);
    }

    ItemStackHandler stackHandler = this.getFuelStackHandler();
    ItemStack itemStack;

    for (int i = 0; i < stackHandler.getSlots(); i++) {

      itemStack = stackHandler.extractItem(i, 64, false);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);

      } else {
        break;
      }
    }

    stackHandler = this.getStackHandler();
    itemStack = stackHandler.extractItem(0, 64, false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
    }

    stackHandler = this.getOutputStackHandler();
    itemStack = stackHandler.extractItem(0, 64, false);

    if (!itemStack.isEmpty()) {
      StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
    }

    BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  public float getProgress() {

    if (this.cookTime < 0) {
      return 0;
    }

    return 1f - (this.cookTime / (float) this.cookTimeTotal);
  }

  public int getRemainingBurnTimeTicks() {

    return this.burnTimeRemaining;
  }
}
