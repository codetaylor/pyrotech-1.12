package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLogPile
    extends BlockRotatedPillar {

  public static final String NAME = "log_pile";

  public BlockLogPile() {

    super(Material.WOOD);
    this.setHardness(2);
    this.setSoundType(SoundType.WOOD);
    this.setHarvestLevel("axe", 0);

    Blocks.FIRE.setFireInfo(this, 5, 5);
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return super.createBlockState();
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

    if (worldIn.isAreaLoaded(pos.add(-5, -5, -5), pos.add(5, 5, 5))) {

      for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-4, -4, -4), pos.add(4, 4, 4))) {
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos)) {
          iblockstate.getBlock().beginLeavesDecay(iblockstate, worldIn, blockpos);
        }
      }
    }
  }

  @Override
  public IBlockState getStateForPlacement(
      World worldIn,
      BlockPos pos,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      EntityLivingBase placer
  ) {

    return this.getStateFromMeta(meta).withProperty(BlockRotatedPillar.AXIS, facing.getAxis());
  }

  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {

    switch (rot) {
      case COUNTERCLOCKWISE_90:
      case CLOCKWISE_90:

        switch (state.getValue(BlockRotatedPillar.AXIS)) {
          case X:
            return state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
          case Z:
            return state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
          default:
            return state;
        }

      default:
        return state;
    }
  }

  @Override
  public boolean canSustainLeaves(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos) {

    return true;
  }

  @Override
  public boolean isWood(net.minecraft.world.IBlockAccess world, BlockPos pos) {

    return true;
  }

}
