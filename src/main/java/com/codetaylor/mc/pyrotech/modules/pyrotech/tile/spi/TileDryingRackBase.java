package com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public abstract class TileDryingRackBase
    extends TileEntity
    implements ITickable {

  protected static final int SPEED_CHECK_INTERVAL_TICKS = 20;
  private static final int CLIENT_SYNC_INTERVAL_TICKS = 20;

  protected ItemStackHandler stackHandler;
  protected ItemStackHandler outputStackHandler;

  protected int[] dryTimeTotal;
  protected int[] dryTimeRemaining;

  // Transient
  protected float[] partialTicks;
  private float speed;

  private int ticksSinceLastSpeedCheck;
  private int ticksSinceLastClientSync;

  public TileDryingRackBase() {

    this.stackHandler = new ItemStackHandler(this.getSlotCount()) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 1;
      }

      @Override
      protected void onContentsChanged(int slot) {

        ItemStack itemStack = this.getStackInSlot(slot);
        TileDryingRackBase _this = TileDryingRackBase.this;

        if (!itemStack.isEmpty()) {
          DryingRackRecipe recipe = DryingRackRecipe.getRecipe(itemStack);

          if (recipe != null) {
            _this.initSlotDryTime(slot, recipe.getTimeTicks());
          }

        } else {
          _this.initSlotDryTime(slot, -1);
        }

        _this.markDirty();
        BlockHelper.notifyBlockUpdate(_this.world, _this.pos);
      }
    };

    this.outputStackHandler = new ItemStackHandler(this.getSlotCount()) {

      @Override
      protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

        return 1;
      }

      @Override
      protected void onContentsChanged(int slot) {

        TileDryingRackBase.this.markDirty();
        BlockHelper.notifyBlockUpdate(TileDryingRackBase.this.world, TileDryingRackBase.this.pos);
      }
    };

    this.dryTimeTotal = new int[this.getSlotCount()];
    this.dryTimeRemaining = new int[this.getSlotCount()];
    this.partialTicks = new float[this.getSlotCount()];
    Arrays.fill(this.dryTimeTotal, -1);
    Arrays.fill(this.dryTimeRemaining, -1);
    this.ticksSinceLastSpeedCheck = SPEED_CHECK_INTERVAL_TICKS; // Trigger immediately
  }

  protected abstract int getSlotCount();

  public ItemStackHandler getStackHandler() {

    return this.stackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  protected void initSlotDryTime(int slot, int dryTimeTicks) {

    this.dryTimeTotal[slot] = dryTimeTicks;
    this.dryTimeRemaining[slot] = dryTimeTicks;
    this.partialTicks[slot] = 0;
  }

  public float getProgress(int slot) {

    if (this.dryTimeTotal[slot] == -1) {
      return 0;
    }

    return 1 - (this.dryTimeRemaining[slot] / (float) this.dryTimeTotal[slot]);
  }

  public void removeItems() {

    if (this.world.isRemote) {
      return;
    }

    for (int i = 0; i < this.stackHandler.getSlots(); i++) {
      ItemStack itemStack = this.stackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
      }
    }

    for (int i = 0; i < this.outputStackHandler.getSlots(); i++) {
      ItemStack itemStack = this.outputStackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos);
      }
    }
  }

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    boolean isDirty = false;

    { // Update the rack's drying speed.
      this.ticksSinceLastSpeedCheck += 1;

      if (this.ticksSinceLastSpeedCheck >= SPEED_CHECK_INTERVAL_TICKS) {

        this.ticksSinceLastSpeedCheck = 0;

        float lastSpeed = this.speed;
        Biome biome = this.world.getBiome(this.pos);
        boolean canRain = biome.canRain();

        if (canRain && this.world.isRainingAt(this.pos.up())) {
          this.speed = this.getRainSpeed();

        } else if ((canRain && this.world.isRaining()) || biome.isHighHumidity()) {
          this.speed = 0.0f;

        } else if (this.world.provider.isNether()) {
          this.speed = 2.0f;

        } else {
          this.speed = 1.0f;

          if (BiomeDictionary.getBiomes(BiomeDictionary.Type.HOT).contains(biome)) {
            this.speed += 0.2f;
          }

          if (BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY).contains(biome)) {
            this.speed += 0.2f;
          }

          if (BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD).contains(biome)) {
            this.speed -= 0.2f;
          }

          if (BiomeDictionary.getBiomes(BiomeDictionary.Type.WET).contains(biome)) {
            this.speed -= 0.2f;
          }
        }

        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
          BlockPos offset = this.pos.offset(facing);
          IBlockState blockState = this.world.getBlockState(offset);

          if (this.isSpeedBonusBlock(blockState, offset, facing)) {
            // Add speed bonus for each adjacent fire source block.
            this.speed += 0.2f;
          }
        }

        if (!this.world.isRaining()
            && this.world.canSeeSky(this.pos.up())
            && this.world.getWorldTime() % 24000 > 3000
            && this.world.getWorldTime() % 24000 < 9000) {
          // Add extra speed bonus if the rack it's not raining, the rack is
          // under the sky, and it's daytime.
          this.speed += 0.2f;
        }

        this.speed = this.getSpeedModified(this.speed);

        if (this.speed != lastSpeed) {
          isDirty = true;
        }
      }
    }

    for (int slotIndex = 0; slotIndex < this.stackHandler.getSlots(); slotIndex++) {

      ItemStack itemStack = this.stackHandler.getStackInSlot(slotIndex);

      if (itemStack.isEmpty()) {
        continue;
      }

      if (this.dryTimeRemaining[slotIndex] > 0) {

        this.partialTicks[slotIndex] += this.speed;

        double fullTicks = Math.floor(this.partialTicks[slotIndex]);

        if (Math.abs(fullTicks) > 0) {
          this.partialTicks[slotIndex] -= fullTicks;
          this.dryTimeRemaining[slotIndex] -= fullTicks;
          this.dryTimeRemaining[slotIndex] = Math.max(0, this.dryTimeRemaining[slotIndex]);
          this.dryTimeRemaining[slotIndex] = Math.min(this.dryTimeTotal[slotIndex], this.dryTimeRemaining[slotIndex]);

          isDirty = true;
        }
      }

      if (this.dryTimeRemaining[slotIndex] == 0) {
        this.initSlotDryTime(slotIndex, -1);

        this.stackHandler.extractItem(slotIndex, 64, false);

        DryingRackRecipe recipe = DryingRackRecipe.getRecipe(itemStack);

        if (recipe != null) {
          this.outputStackHandler.insertItem(slotIndex, recipe.getOutput(), false);
        }
      }

    }

    this.ticksSinceLastClientSync += 1;

    if (this.ticksSinceLastClientSync >= CLIENT_SYNC_INTERVAL_TICKS) {
      this.ticksSinceLastClientSync = 0;
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }

    if (isDirty) {
      this.markDirty();
    }
  }

  protected float getRainSpeed() {

    return -1.0f;
  }

  protected abstract float getSpeedModified(float speed);

  public float getSpeed() {

    return this.speed;
  }

  private boolean isSpeedBonusBlock(IBlockState blockState, BlockPos pos, EnumFacing facing) {

    return blockState.getBlock().isFireSource(this.world, pos, facing.getOpposite());
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
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.dryTimeTotal = compound.getIntArray("dryTimeTotal");
    this.dryTimeRemaining = compound.getIntArray("dryTimeRemaining");
    this.speed = compound.getFloat("speed");
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setIntArray("dryTimeTotal", this.dryTimeTotal);
    compound.setIntArray("dryTimeRemaining", this.dryTimeRemaining);
    compound.setFloat("speed", this.speed);
    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Synchronization
  // ---------------------------------------------------------------------------

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
  }
}
