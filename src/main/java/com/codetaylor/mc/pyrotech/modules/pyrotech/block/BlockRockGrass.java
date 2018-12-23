package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockRockGrass
    extends Block
    implements IGrowable {

  public static final String NAME = "rock_grass";

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);

  public BlockRockGrass() {

    super(Material.GROUND);
    this.setSoundType(SoundType.PLANT);
    this.setTickRandomly(true);
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (world.isRemote) {
      return;
    }

    if (world.getLightFromNeighbors(pos.up()) < 4
        && world.getBlockState(pos.up()).getLightOpacity(world, pos.up()) > 2) {

      world.setBlockState(pos, ModuleBlocks.ROCK.getDefaultState()
          .withProperty(BlockRock.VARIANT, BlockRock.EnumType.DIRT), 3);

    } else if (world.getLightFromNeighbors(pos) >= 9) {
      this.grow(world, pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Growth
  // ---------------------------------------------------------------------------

  @Override
  public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

    return true;
  }

  @Override
  public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {

    return true;
  }

  @Override
  public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

    this.grow(world, pos);
  }

  private void grow(@Nonnull World world, @Nonnull BlockPos pos) {

    BlockPos blockpos = pos.down();

    if (blockpos.getY() >= 0
        && blockpos.getY() < 256
        && !world.isBlockLoaded(blockpos)) {
      return;
    }

    IBlockState blockState = world.getBlockState(blockpos);

    if (blockState.getBlock() == Blocks.DIRT
        && blockState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) {
      world.setBlockState(blockpos, Blocks.GRASS.getDefaultState(), 3);
      world.setBlockToAir(pos);

    } else if (blockState.getBlock() == Blocks.GRASS) {
      world.setBlockToAir(pos);
    }

  }

  // ---------------------------------------------------------------------------
  // - Placement
  // ---------------------------------------------------------------------------

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    return world.isSideSolid(pos.down(), EnumFacing.UP)
        && super.canPlaceBlockAt(world, pos);
  }

  @Override
  public boolean isReplaceable(IBlockAccess world, @Nonnull BlockPos pos) {

    return true;
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {

    if (!this.canPlaceBlockAt(world, pos)) {
      world.destroyBlock(pos, true);
    }
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
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {

    return BlockFaceShape.UNDEFINED;
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {

    return NULL_AABB;
  }

}
