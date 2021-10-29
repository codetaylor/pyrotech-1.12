package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockMud
    extends Block {

  public static final String NAME = "mud";

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0);

  public BlockMud() {

    super(Material.GROUND);
    this.setSoundType(SoundType.SLIME);
    this.setHardness(0.4f);
    this.setHarvestLevel("shovel", 0);
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
}
