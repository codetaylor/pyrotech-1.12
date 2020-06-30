package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockOreDenseRedstoneBase;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockOreDenseRedstoneSmall
    extends BlockOreDenseRedstoneBase {

  public static final String NAME = "dense_redstone_ore_small_inactive";
  public static final String NAME_ACTIVATED = "dense_redstone_ore_small";

  private static final AxisAlignedBB AABB = AABBHelper.create(4, 0, 4, 12, 8, 12);

  public BlockOreDenseRedstoneSmall(boolean isActivated) {

    super(isActivated);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile.pyrotech.dense_redstone_ore";
  }

  @Override
  protected int getActiveLightValue() {

    return 9;
  }

  @Override
  public int quantityDropped(Random random) {

    return 2 + random.nextInt(4);
  }

  @Override
  public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {

    return 2 + RANDOM.nextInt(3);
  }

  protected IBlockState getInactiveState() {

    return ModuleCore.Blocks.ORE_DENSE_REDSTONE_SMALL.getDefaultState();
  }

  protected IBlockState getActiveState() {

    return ModuleCore.Blocks.ORE_DENSE_REDSTONE_SMALL_ACTIVATED.getDefaultState();
  }

  @Override
  protected int getParticleCount() {

    return 4;
  }

  @Override
  public int getProximityRepairAmount() {

    return ModuleToolConfig.REDSTONE_TOOLS.PROXIMITY_REPAIR_AMOUNTS[1];
  }
}
