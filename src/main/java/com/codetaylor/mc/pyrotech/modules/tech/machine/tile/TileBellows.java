package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.network.tile.data.TileDataFloat;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.IAirflowConsumerCapability;
import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.athenaeum.integration.gamestages.GameStages;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileBellows
    extends TileEntityDataBase
    implements ITickable {

  private final TileDataFloat progress;

  private float time;

  public TileBellows() {

    super(ModuleTechMachine.TILE_DATA_SERVICE);

    this.progress = new TileDataFloat(0, 1);

    // --- Network ---

    this.registerTileDataForNetwork(new ITileData[]{
        this.progress
    });
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public float getProgress() {

    return Math.max(0, Math.min(1, this.progress.get()));
  }

  protected float getProgressIncrementDown() {

    return 1f / this.getTotalTicksDown();
  }

  protected float getProgressIncrementUp() {

    return -1f / this.getTotalTicksUp();
  }

  protected int getTotalTicksDown() {

    return ModuleTechMachineConfig.BELLOWS.TRAVEL_TIME_DOWN_TICKS;
  }

  protected int getTotalTicksUp() {

    return ModuleTechMachineConfig.BELLOWS.TRAVEL_TIME_UP_TICKS;
  }

  protected float getAirflow() {

    float baseAirflow = this.getBaseAirflow();
    float time = MathHelper.clamp(this.time, 0, 1);
    float modulatedAirflow;

    if (time < 0.25) {
      modulatedAirflow = (float) (baseAirflow * (512 * Math.pow(time, 5)));

    } else {
      modulatedAirflow = (float) (baseAirflow * (4 * Math.pow(time - 0.75, 3) + 1));
    }

    return modulatedAirflow * 2;
  }

  protected float getBaseAirflow() {

    return (float) ModuleTechMachineConfig.BELLOWS.BASE_AIRFLOW;
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    if (this.shouldProgress()) {

      if (this.progress.get() < 1) {
        this.progress.add(this.getProgressIncrementDown());

        if (this.progress.get() > 1) {
          this.progress.set(1);
        }

        List<BlockPos> airflowPushPositions = this.getAirflowPushPositions(new ArrayList<>(3));
        List<EnumFacing> airflowPushFacings = this.getAirflowPushFacings(new ArrayList<>(3));

        for (int i = 0; i < airflowPushPositions.size(); i++) {
          BlockPos blockPos = airflowPushPositions.get(i);
          EnumFacing facing = (i >= airflowPushFacings.size()) ? EnumFacing.NORTH : airflowPushFacings.get(i);
          this.pushAirflow(blockPos, facing);
        }
      }

      this.time += this.getProgressIncrementDown();

      if (this.time > 1) {
        this.time = 1;
      }

    } else {

      if (this.progress.get() > 0) {
        this.progress.add(this.getProgressIncrementUp());

        if (this.progress.get() < 0) {
          this.progress.set(0);
        }
      }

      this.time = 0;
    }
  }

  protected void pushAirflow(BlockPos blockPos, EnumFacing facing) {

    TileEntity tileEntity = this.world.getTileEntity(blockPos);

    if (tileEntity != null) {
      IAirflowConsumerCapability airflowConsumer = tileEntity.getCapability(ModuleCore.CAPABILITY_AIRFLOW_CONSUMER, facing.getOpposite());

      if (airflowConsumer != null) {
        airflowConsumer.consumeAirflow(this.getAirflow(), false);
      }
    }
  }

  protected List<BlockPos> getAirflowPushPositions(List<BlockPos> result) {

    result.add(this.pos.offset(this.getFacing()));
    return result;
  }

  protected List<EnumFacing> getAirflowPushFacings(List<EnumFacing> result) {

    result.add(this.getFacing());
    return result;
  }

  protected EnumFacing getFacing() {

    IBlockState blockState = this.world.getBlockState(this.pos);

    if (blockState.getBlock() == ModuleTechMachine.Blocks.BELLOWS) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return EnumFacing.NORTH;
  }

  protected boolean shouldProgress() {

    AxisAlignedBB bounds = new AxisAlignedBB(this.pos)
        .offset(0, 1 - this.getProgress() * 0.5, 0);

    List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

    if (!entities.isEmpty()) {

      for (EntityLivingBase entity : entities) {
        double bellowsHeight = 1 + this.pos.getY() - this.getProgress() * 0.5;

        if (entity.posY < bellowsHeight) {
          entity.posY = bellowsHeight + 0.001f;
        }

        if (entity instanceof EntityPlayer
            && entity.posY - 0.05 < bellowsHeight) {

          if (Loader.isModLoaded("gamestages")) {
            return GameStages.allowed((EntityPlayer) entity, this.getStages());
          }

          return true;
        }
      }
    }

    return false;
  }

  private Stages getStages() {

    return ModuleTechMachineConfig.STAGES_BELLOWS;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setFloat("progress", this.progress.get());
    return compound;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.progress.set(compound.getFloat("progress"));
  }

}
