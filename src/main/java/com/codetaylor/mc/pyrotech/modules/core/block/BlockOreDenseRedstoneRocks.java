package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockOreDenseRedstoneBase;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockOreDenseRedstoneRocks
    extends BlockOreDenseRedstoneBase {

  public static final String NAME = "dense_redstone_ore_rocks_inactive";
  public static final String NAME_ACTIVATED = "dense_redstone_ore_rocks";

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);

  public BlockOreDenseRedstoneRocks(boolean isActivated) {

    super(isActivated);
  }

  @Override
  public String getUnlocalizedName() {

    return "tile.pyrotech.dense_redstone_ore";
  }

  @Override
  protected int getActiveLightValue() {

    return 7;
  }

  @Override
  public int quantityDropped(Random random) {

    return 1 + random.nextInt(2);
  }

  @Override
  public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {

    return 1 + RANDOM.nextInt(2);
  }

  protected IBlockState getInactiveState() {

    return ModuleCore.Blocks.ORE_DENSE_REDSTONE_ROCKS.getDefaultState();
  }

  protected IBlockState getActiveState() {

    return ModuleCore.Blocks.ORE_DENSE_REDSTONE_ROCKS_ACTIVATED.getDefaultState();
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

  @Override
  public boolean isPassable(IBlockAccess world, BlockPos pos) {

    return true;
  }

  @Override
  protected int getParticleCount() {

    return 2;
  }


  @Override
  public int getProximityRepairAmount() {

    return ModuleToolConfig.REDSTONE_TOOLS.PROXIMITY_REPAIR_AMOUNTS[0];
  }
}
