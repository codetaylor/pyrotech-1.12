package com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.util.TickCounter;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileNetWorkerBase;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;

public abstract class TileDryingRackBase
    extends TileNetWorkerBase
    implements ITickable {

  private static final int SPEED_CHECK_INTERVAL_TICKS = 20;

  protected InputStackHandler inputStackHandler;
  protected OutputStackHandler outputStackHandler;

  private int[] dryTimeTotal;
  private int[] dryTimeRemaining;

  // Transient
  private float[] partialTicks;
  private TileDataFloat speed;

  private TickCounter speedCheckTickCounter;

  public TileDryingRackBase(int taskCount) {

    super(ModuleCore.TILE_DATA_SERVICE, taskCount);

    this.inputStackHandler = new InputStackHandler(this.getSlotCount());
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.initSlotDryTime(slot, this.getDryTimeTicks(handler.getStackInSlot(slot)));
      this.markDirty();
    });

    this.outputStackHandler = new OutputStackHandler(this.getSlotCount());
    this.outputStackHandler.addObserver((handler, slot) -> this.markDirty());

    this.dryTimeTotal = new int[this.getSlotCount()];
    this.dryTimeRemaining = new int[this.getSlotCount()];
    this.partialTicks = new float[this.getSlotCount()];
    Arrays.fill(this.dryTimeTotal, -1);
    Arrays.fill(this.dryTimeRemaining, -1);

    // Supplying the max as initial value makes this check immediate on create
    this.speedCheckTickCounter = new TickCounter(SPEED_CHECK_INTERVAL_TICKS, SPEED_CHECK_INTERVAL_TICKS);

    this.speed = new TileDataFloat(0, 20);

    // --- Network ---
    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.inputStackHandler),
        new TileDataItemStackHandler<>(this.outputStackHandler),
        this.speed
    });
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public ItemStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public ItemStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public float getSpeed() {

    return this.speed.get();
  }

  // ---------------------------------------------------------------------------
  // - Container
  // ---------------------------------------------------------------------------

  public void dropContents() {

    for (int i = 0; i < this.inputStackHandler.getSlots(); i++) {
      ItemStack itemStack = this.inputStackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos, 0.0);
      }
    }

    for (int i = 0; i < this.outputStackHandler.getSlots(); i++) {
      ItemStack itemStack = this.outputStackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        StackHelper.spawnStackOnTop(this.world, itemStack, this.pos, 0.0);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Worker
  // ---------------------------------------------------------------------------

  @Override
  public boolean workerIsActive() {

    // Worker is always active.
    return true;
  }

  @Override
  public void workerSetActive(boolean active) {

    throw new UnsupportedOperationException("Drying rack can't be deactivated.");
  }

  @Override
  protected float workerCalculateProgress(int taskIndex) {

    if (this.dryTimeTotal[taskIndex] == -1) {
      return 0;
    }

    return 1 - (this.dryTimeRemaining[taskIndex] / (float) this.dryTimeTotal[taskIndex]);
  }

  @Override
  public boolean workerDoWork() {

    boolean isDirty = false;

    // Update the rack's drying speed.
    if (this.speedCheckTickCounter.increment()) {
      this.speed.set(this.updateSpeed());
    }

    boolean shouldSendParticleProgressPacket = false;

    for (int slotIndex = 0; slotIndex < this.inputStackHandler.getSlots(); slotIndex++) {

      ItemStack itemStack = this.inputStackHandler.getStackInSlot(slotIndex);

      if (itemStack.isEmpty()) {
        continue;
      }

      if (this.dryTimeRemaining[slotIndex] > 0) {

        if (this.world.getTotalWorldTime() % 40 == 0) {
          shouldSendParticleProgressPacket = true;
        }

        this.partialTicks[slotIndex] += this.speed.get();

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
        this.inputStackHandler.extractItem(slotIndex, 64, false);
        DryingRackRecipe recipe = this.getRecipe(itemStack);

        if (recipe != null) {
          this.outputStackHandler.insertItem(slotIndex, recipe.getOutput(), false);
        }
      }

    }

    if (isDirty) {
      this.markDirty();
    }

    if (shouldSendParticleProgressPacket) {
      this.sendParticleProgressPacket();
    }

    return true;
  }

  protected abstract void sendParticleProgressPacket();

  // ---------------------------------------------------------------------------
  // - Internal
  // ---------------------------------------------------------------------------

  protected abstract int getSlotCount();

  protected abstract float getMultiplicativeSpeedModifier();

  private DryingRackRecipe getRecipe(ItemStack itemStack) {

    return DryingRackRecipe.getRecipe(itemStack);
  }

  private void initSlotDryTime(int slot, int dryTimeTicks) {

    this.dryTimeTotal[slot] = dryTimeTicks;
    this.dryTimeRemaining[slot] = dryTimeTicks;
    this.partialTicks[slot] = 0;
  }

  private float updateSpeed() {

    Biome biome = this.world.getBiome(this.pos);
    boolean canRain = biome.canRain();
    ModuleTechBasicConfig.DryingRackConditionalModifiers modifiers = this.getConditionalModifiers();

    double calculatedSpeed = 0;

    // -------------------------------------------------------------------------
    // First, look for an explicit biome base speed set with CraftTweaker.
    // -------------------------------------------------------------------------

    Map<String, Float> biomeSpeeds = this.getBiomeSpeeds();
    ResourceLocation registryName = biome.getRegistryName();
    boolean isExplicitBiomeSpeedSet = false;

    if (registryName != null) {
      String registryNameString = registryName.toString();
      Float explicitSpeed = biomeSpeeds.get(registryNameString);

      if (explicitSpeed != null) {
        calculatedSpeed = explicitSpeed;
        isExplicitBiomeSpeedSet = true;
      }
    }

    // -------------------------------------------------------------------------
    // If an explicit speed was not found, derive the base speed of the device
    // using conditional modifiers.
    // -------------------------------------------------------------------------

    if (!isExplicitBiomeSpeedSet) {

      if (canRain && this.world.isRainingAt(this.pos.up())) {
        // If the biome can rain and the device is being directly rained on,
        // set the new speed to this device's rain speed.
        // A negative speed will reduce recipe progress.
        calculatedSpeed = modifiers.DIRECT_RAIN;

      } else if ((canRain && this.world.isRaining()) || biome.isHighHumidity()) {
        // If the biome can rain and it is raining somewhere, or the biome is high
        // humidity, the new speed is set to this:
        calculatedSpeed = modifiers.INDIRECT_RAIN;

      } else if (this.world.provider.isNether()) {
        // If the device is in the Nether, set the new speed to this:
        calculatedSpeed = modifiers.NETHER;

      } else {
        // Otherwise, set the new speed and then additively modify it based
        // on further criteria.

        calculatedSpeed = modifiers.BASE_DERIVED;

        if (BiomeDictionary.getBiomes(BiomeDictionary.Type.HOT).contains(biome)) {
          // If the biome is hot, increase the speed:
          calculatedSpeed += modifiers.DERIVED_HOT;
        }

        if (BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY).contains(biome)) {
          // If the biome is dry, increase the speed:
          calculatedSpeed += modifiers.DERIVED_DRY;
        }

        if (BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD).contains(biome)) {
          // If the biome is cold, decrease the speed:
          calculatedSpeed += modifiers.DERIVED_COLD;
        }

        if (BiomeDictionary.getBiomes(BiomeDictionary.Type.WET).contains(biome)) {
          // If the biome is wet, decrease the speed:
          calculatedSpeed += modifiers.DERIVED_WET;
        }
      }
    }

    // -------------------------------------------------------------------------
    // Next, scan a region around the device and look for a fire block,
    // or a block that counts as a fire source. Add speed bonus for each
    // adjacent fire or fire source block.
    // -------------------------------------------------------------------------

    final float[] fireSourceBonus = new float[1];

    int range = modifiers.FIRE_SOURCE_BONUS_RANGE;

    BlockHelper.forBlocksInCube(this.world, this.pos, range, range, range, (w, p, bs) -> {

      if (this.isSpeedBonusBlock(bs, p)) {
        fireSourceBonus[0] += modifiers.FIRE_SOURCE_BONUS;
      }
      return true;
    });

    calculatedSpeed += fireSourceBonus[0];

    // -------------------------------------------------------------------------
    // Next, add an extra speed bonus if it's not raining, the rack is
    // under the sky, and it's daytime.
    // -------------------------------------------------------------------------

    if (!this.world.isRaining()
        && this.world.canSeeSky(this.pos.up())
        && this.world.getWorldTime() % 24000 > 3000
        && this.world.getWorldTime() % 24000 < 9000) {
      calculatedSpeed += modifiers.DAYTIME;
    }

    // -------------------------------------------------------------------------
    // Finally, apply the master SPEED_MODIFIER from the device's config.
    // -------------------------------------------------------------------------

    return (float) (calculatedSpeed * this.getMultiplicativeSpeedModifier());
  }

  protected abstract ModuleTechBasicConfig.DryingRackConditionalModifiers getConditionalModifiers();

  protected abstract Map<String, Float> getBiomeSpeeds();

  private boolean isSpeedBonusBlock(IBlockState blockState, BlockPos pos) {

    Block block = blockState.getBlock();

    return block == Blocks.FIRE
        || block.isFireSource(this.world, pos, null);
  }

  private int getDryTimeTicks(ItemStack itemStack) {

    if (!itemStack.isEmpty()) {
      DryingRackRecipe recipe = this.getRecipe(itemStack);

      if (recipe != null) {
        int durationTicks = (int) (recipe.getTimeTicks() * this.getBaseDurationModifier());
        return Math.max(1, durationTicks);
      }
    }

    return -1;
  }

  protected abstract double getBaseDurationModifier();

  // ---------------------------------------------------------------------------
  // - Miscellaneous
  // ---------------------------------------------------------------------------

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
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.dryTimeTotal = compound.getIntArray("dryTimeTotal");
    this.dryTimeRemaining = compound.getIntArray("dryTimeRemaining");
    this.speed.set(compound.getFloat("speed"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setIntArray("dryTimeTotal", this.dryTimeTotal);
    compound.setIntArray("dryTimeRemaining", this.dryTimeRemaining);
    compound.setFloat("speed", this.speed.get());
    return compound;
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  private class InputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ InputStackHandler(int size) {

      super(size);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }
  }

  private class OutputStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputStackHandler(int size) {

      super(size);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {

      return 1;
    }
  }

}
