package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.LIFOStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockCampfire;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCampfire
    extends TileEntity
    implements ITickable,
    ITileInteractable {

  private ItemStackHandler stackHandler;
  private ItemStackHandler outputStackHandler;
  private LIFOStackHandler fuelStackHandler;
  private int cookTime;
  private int cookTimeTotal;
  private int burnTimeRemaining;
  private int ashLevel;
  private boolean active;
  private boolean dead;
  private boolean doused;

  // transient
  private EntityItem entityItem;
  private EntityItem entityItemOutput;
  private int ticksSinceLastClientSync;
  private int rainTimeRemaining;

  private IInteraction[] interactions;

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

    this.interactions = new IInteraction[]{
        new TileCampfire.InteractionFood(new ItemStackHandler[]{
            this.stackHandler,
            this.outputStackHandler
        }),
        new TileCampfire.InteractionShovel(),
        new InteractionUseFlintAndSteel(),
        new TileCampfire.InteractionLog()
    };
  }

  public int getAshLevel() {

    return this.ashLevel;
  }

  public void setAshLevel(int ashLevel) {

    this.ashLevel = ashLevel;
    this.markDirty();
    BlockHelper.notifyBlockUpdate(this.world, this.pos);
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

      if (this.ashLevel == 8) {
        this.setActive(false);
      }

      if (Math.random() < 0.05
          && this.world.getBlockState(this.pos.down()).getBlock().isFlammable(this.world, this.pos.down(), EnumFacing.UP)) {

        this.world.setBlockState(this.pos.down(), Blocks.FIRE.getDefaultState(), 3);
        return;
      }

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
          ItemStack result = FurnaceRecipes.instance().getSmeltingResult(itemStack).copy();
          this.outputStackHandler.insertItem(0, result, false);
        }
      }

      this.burnTimeRemaining -= 1;

      if (this.burnTimeRemaining <= 0) {

        if (Math.random() < ModulePyrotechConfig.CAMPFIRE.ASH_CHANCE) {
          this.ashLevel += 1;
        }

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
    compound.setInteger("ashLevel", this.ashLevel);
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
    this.ashLevel = compound.getInteger("ashLevel");
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
    this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
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

    if (this.ashLevel > 0) {
      ItemStack ashStack = ItemMaterial.EnumType.PIT_ASH.asStack(this.ashLevel);
      StackHelper.spawnStackOnTop(this.world, ashStack, this.pos);
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

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  private class InteractionFood
      extends InteractionItemStack<TileCampfire> {

    /* package */ InteractionFood(ItemStackHandler[] stackHandlers) {

      super(
          stackHandlers,
          0,
          new EnumFacing[]{EnumFacing.UP},
          InteractionBounds.INFINITE,
          new Transform(
              Transform.translate(0.5, 0.5, 0.5),
              Transform.rotate(),
              Transform.scale(0.75, 0.75, 0.75)
          )
      );
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return (itemStack.getItem() instanceof ItemFood)
          && !FurnaceRecipes.instance().getSmeltingResult(itemStack).isEmpty();
    }

    @Override
    public boolean interact(TileCampfire tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (player.isSneaking()) {
        return false;
      }

      return super.interact(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);
    }
  }

  private class InteractionLog
      extends InteractionBase<TileCampfire> {

    /* package */ InteractionLog() {

      super(EnumFacing.VALUES, InteractionBounds.INFINITE);
    }

    @Override
    public boolean interact(TileCampfire tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (heldItem.isEmpty()
          && player.isSneaking()) {

        // If the player is sneaking with an empty hand, remove logs and damage the player.

        ItemStack itemStack = tile.getFuelStackHandler().extractItem(0, 1, world.isRemote);

        if (!itemStack.isEmpty()) {

          if (!world.isRemote) {

            if (Math.random() < ModulePyrotechConfig.CAMPFIRE.PLAYER_BURN_CHANCE) {

              if (!player.isImmuneToFire()
                  && !EnchantmentHelper.hasFrostWalkerEnchantment(player)
                  && ModuleBlocks.CAMPFIRE.getActualState(state, world, hitPos).getValue(BlockCampfire.VARIANT) == BlockCampfire.EnumType.LIT) {
                player.attackEntityFrom(DamageSource.HOT_FLOOR, (float) ModulePyrotechConfig.CAMPFIRE.PLAYER_BURN_DAMAGE);
              }
            }

            StackHelper.addToInventoryOrSpawn(world, player, itemStack, hitPos, -0.125);
          }

          return true;
        }

      } else if (!heldItem.isEmpty()
          && !player.isSneaking()) {

        // If the player is not sneaking with a full hand, attempt to add wood.

        if (OreDictHelper.contains("logWood", heldItem)) {
          LIFOStackHandler fuelStackHandler = tile.getFuelStackHandler();

          if (!world.isRemote) {
            int firstEmptyIndex = fuelStackHandler.getFirstEmptyIndex();

            if (firstEmptyIndex > -1) {

              if (!player.isCreative()) {
                heldItem.setCount(heldItem.getCount() - 1);
              }

              fuelStackHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1, heldItem.getMetadata()), false);
              world.playSound(null, hitPos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);

              // TODO: remove?
              // The fuel stack handler is set up to do a block notify when
              // its contents change. We should test that removing this doesn't
              // break anything.
              BlockHelper.notifyBlockUpdate(world, hitPos);
            }
          }

          return true;
        }
      }

      return false;
    }

  }

  private class InteractionShovel
      extends InteractionBase<TileCampfire> {

    /* package */ InteractionShovel() {

      super(EnumFacing.VALUES, InteractionBounds.INFINITE);
    }

    @Override
    public boolean interact(TileCampfire tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      ItemStack heldItem = player.getHeldItemMainhand();

      if (tile.getAshLevel() > 0
          && heldItem.getItem().getToolClasses(heldItem).contains("shovel")) {

        if (!world.isRemote) {
          tile.setAshLevel(tile.getAshLevel() - 1);
          StackHelper.spawnStackOnTop(world, ItemMaterial.EnumType.PIT_ASH.asStack(), hitPos);
          heldItem.damageItem(1, player);
          world.playSound(null, hitPos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1, 1);
        }

        return true;
      }

      return false;
    }
  }

  private class InteractionUseFlintAndSteel
      extends InteractionUseItemBase<TileCampfire> {

    /* package */ InteractionUseFlintAndSteel() {

      super(EnumFacing.VALUES, InteractionBounds.INFINITE);
    }

    @Override
    protected boolean isItemStackValid(ItemStack itemStack) {

      return (itemStack.getItem() == Items.FLINT_AND_STEEL);
    }

    @Override
    protected void doInteraction(TileCampfire tile, World world, BlockPos hitPos, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      tile.setActive(true);

      world.playSound(
          null,
          hitPos,
          SoundEvents.ITEM_FLINTANDSTEEL_USE,
          SoundCategory.BLOCKS,
          1.0F,
          Util.RANDOM.nextFloat() * 0.4F + 0.8F
      );
    }
  }
}
