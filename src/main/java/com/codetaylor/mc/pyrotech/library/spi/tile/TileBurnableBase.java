package com.codetaylor.mc.pyrotech.library.spi.tile;

import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class TileBurnableBase
    extends TileNetBase
    implements ITickable {

  private static final int DEFAULT_MAX_INVALID_TICKS = 100;
  private static final int DEFAULT_STRUCTURE_VALIDATION_INTERVAL = 20;

  private boolean needStructureValidation;
  private int invalidTicks;
  private int nextStructureValidationTicks;
  protected int burnTimeTicksPerStage;
  protected int remainingStages;

  public TileBurnableBase(ITileDataService tileDataService) {

    super(tileDataService);

    this.remainingStages = this.getBurnStages();
    this.invalidTicks = 0;
    this.reset();
  }

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (!this.isActive()) {
      return;
    }

    this.onUpdate();

    if (!this.needStructureValidation) {
      this.nextStructureValidationTicks -= 1;

      if (this.nextStructureValidationTicks <= 0) {
        this.nextStructureValidationTicks = DEFAULT_STRUCTURE_VALIDATION_INTERVAL;
        this.setNeedStructureValidation();
      }
    }

    if (this.needStructureValidation) {

      if (this.isStructureValid()) {
        this.invalidTicks = 0;
        this.needStructureValidation = false;
        this.nextStructureValidationTicks = DEFAULT_STRUCTURE_VALIDATION_INTERVAL;
      }
    }

    if (!this.needStructureValidation) {
      this.onUpdateValid();

    } else {

      if (this.invalidTicks < this.getMaxInvalidTicks()) {
        this.invalidTicks += 1;
        this.onUpdateInvalid();

      } else {
        this.onInvalidDelayExpired();
      }
    }

    if (this.remainingStages <= 0) {
      this.onAllBurnStagesComplete();

    } else if (this.burnTimeTicksPerStage > 0) {
      this.burnTimeTicksPerStage -= 1;

    } else {
      this.remainingStages -= 1;
      this.onBurnStageComplete();
      this.reset();
    }
  }

  protected void reset() {

    this.setNeedStructureValidation();
    this.burnTimeTicksPerStage = this.getTotalBurnTimeTicks() / this.getBurnStages();
  }

  protected boolean isStructureValid() {

    for (EnumFacing facing : EnumFacing.VALUES) {

      BlockPos offset = this.pos.offset(facing);

      if (!this.isValidStructureBlock(this.world, offset, this.world.getBlockState(offset), facing)) {
        return false;
      }
    }
    return true;
  }

  protected boolean isValidStructureBlock(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    return blockState.isSideSolid(world, pos, facing.getOpposite()) &&
        !blockState.getBlock().isFlammable(world, pos, facing.getOpposite());
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);

    compound.setBoolean("needStructureValidation", this.needStructureValidation);
    compound.setInteger("burnTimeTicksPerStage", this.burnTimeTicksPerStage);
    compound.setInteger("invalidTicks", this.invalidTicks);
    compound.setInteger("remainingStages", this.remainingStages);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);

    this.needStructureValidation = compound.getBoolean("needStructureValidation");
    this.burnTimeTicksPerStage = compound.getInteger("burnTimeTicksPerStage");
    this.invalidTicks = compound.getInteger("invalidTicks");
    this.remainingStages = compound.getInteger("remainingStages");
  }

  protected int getMaxInvalidTicks() {

    return DEFAULT_MAX_INVALID_TICKS;
  }

  public void setNeedStructureValidation() {

    this.needStructureValidation = true;
  }

  /**
   * @return false to prevent the update loop from running
   */
  protected abstract boolean isActive();

  /**
   * Called on the server on every update tick.
   */
  protected abstract void onUpdate();

  /**
   * Called on the server for every tick this burnable is valid.
   */
  protected abstract void onUpdateValid();

  /**
   * Called on the server for every tick this burnable has an invalid structure.
   */
  protected abstract void onUpdateInvalid();

  /**
   * Called when the invalid delay expires.
   */
  protected abstract void onInvalidDelayExpired();

  /**
   * Called once for each completed burn stage.
   */
  protected abstract void onBurnStageComplete();

  /**
   * @return the total burn time for all stages in ticks
   */
  protected abstract int getTotalBurnTimeTicks();

  /**
   * Called when all burn stages are complete.
   */
  protected abstract void onAllBurnStagesComplete();

  /**
   * @return the total number of burn stages
   */
  protected abstract int getBurnStages();

}
