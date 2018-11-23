package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

/**
 * This is a partial block, ie. not a full cube.
 */
public abstract class BlockPartialBase
    extends Block {

  public BlockPartialBase(Material material) {

    super(material);
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

    return false;
  }

  @Override
  public boolean isFullBlock(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {

    return this.isFullBlock(state);
  }

  @Nonnull
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {

    return BlockFaceShape.UNDEFINED;
  }

  @Override
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {

    return true;
  }
}
