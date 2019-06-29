package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.athenaeum.util.FacingHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileMechanicalBellows
    extends TileBellows {

  @Override
  protected float getBaseAirflow() {

    return (float) ModuleTechMachineConfig.MECHANICAL_BELLOWS.BASE_AIRFLOW;
  }

  @Override
  protected int getTotalTicksDown() {

    return ModuleTechMachineConfig.MECHANICAL_BELLOWS.TRAVEL_TIME_DOWN_TICKS;
  }

  @Override
  protected int getTotalTicksUp() {

    return ModuleTechMachineConfig.MECHANICAL_BELLOWS.TRAVEL_TIME_UP_TICKS;
  }

  @Override
  protected EnumFacing getFacing() {

    IBlockState blockState = this.world.getBlockState(this.pos);

    if (blockState.getBlock() == ModuleTechMachine.Blocks.MECHANICAL_BELLOWS) {
      return blockState.getValue(Properties.FACING_HORIZONTAL);
    }

    return EnumFacing.NORTH;
  }

  @Override
  protected boolean shouldProgress() {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.up());

    if (tileEntity instanceof TileMechanicalBellowsTop) {
      return ((TileMechanicalBellowsTop) tileEntity).isPushing();
    }

    return false;
  }

  @Override
  protected List<BlockPos> getAirflowPushPositions(List<BlockPos> result) {

    EnumFacing facing = this.getFacing();
    result.add(this.pos.offset(facing));
    result.add(this.pos.offset(FacingHelper.rotateFacingCW(facing)));
    result.add(this.pos.offset(FacingHelper.rotateFacingCW(facing, 3)));
    return result;
  }

  @Override
  protected List<EnumFacing> getAirflowPushFacings(List<EnumFacing> result) {

    EnumFacing facing = this.getFacing();
    result.add(facing);
    result.add(FacingHelper.rotateFacingCW(facing));
    result.add(FacingHelper.rotateFacingCW(facing, 3));
    return result;
  }
}
