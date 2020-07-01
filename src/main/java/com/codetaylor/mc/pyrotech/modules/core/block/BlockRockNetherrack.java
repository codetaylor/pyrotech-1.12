package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.block.Block;
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
public class BlockRockNetherrack
    extends Block {

  public static final String NAME = "rock_netherrack";

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);

  public BlockRockNetherrack() {

    super(Material.ROCK);
    this.setHardness(0.1f);
    this.setSoundType(SoundType.STONE);
    this.setTickRandomly(true);
  }

  @Override
  public int tickRate(World world) {

    return 40;
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (world.isRemote) {
      return;
    }

    {
      IBlockState bs = world.getBlockState(pos.down());
      Material material = bs.getMaterial();

      if (material == Material.ROCK || material == Material.GROUND || material == Material.GRASS) {

        if (bs.isFullBlock()) {
          world.setBlockState(pos.down(), Blocks.NETHERRACK.getDefaultState(), 1 | 2);
        }
      }
    }

    int range = Math.max(0, Math.min(16, ModuleCoreConfig.ROCKS.NETHERRACK_SPREAD_RADIUS));

    BlockHelper.forBlocksInRangeShuffled(world, pos, range, (w, p, bs) -> {

      Material material = bs.getMaterial();

      if (material == Material.ROCK || material == Material.GROUND || material == Material.GRASS) {

        boolean isNetherrackAdjacent = false;

        if (bs.isFullBlock()) {
          for (EnumFacing facing : EnumFacing.values()) {

            if (w.getBlockState(p.offset(facing)).getBlock() == Blocks.NETHERRACK) {
              isNetherrackAdjacent = true;
              break;
            }
          }

          if (isNetherrackAdjacent) {
            w.setBlockState(p, Blocks.NETHERRACK.getDefaultState(), 1 | 2);
            return false;
          }
        }
      }

      return true; // keep processing
    });
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
