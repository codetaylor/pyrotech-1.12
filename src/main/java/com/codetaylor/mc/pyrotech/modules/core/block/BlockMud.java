package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockMud
    extends Block {

  public static final String NAME = "mud";

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0);

  public BlockMud() {

    super(Material.GROUND);
    this.setSoundType(SoundType.SLIME);
    this.setHardness(0.4f);
    this.setHarvestLevel("shovel", 0);
    this.setTickRandomly(true);
  }

  @SuppressWarnings("deprecation")
  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {

    return AABB;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

    entity.motionX *= 0.4D;
    entity.motionZ *= 0.4D;
  }

  @Override
  public int tickRate(@Nonnull World world) {

    return 20;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (world.getBlockState(pos.up()).getBlock() != Blocks.WATER
        && !world.isRainingAt(pos.up())) {
      world.setBlockState(pos, Blocks.DIRT.getDefaultState());
    }
  }
}
