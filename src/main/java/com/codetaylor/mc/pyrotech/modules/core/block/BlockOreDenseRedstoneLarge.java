package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockOreDenseRedstoneLarge
    extends BlockOreDenseRedstoneBase {

  public static final String NAME = "dense_redstone_ore_large_inactive";
  public static final String NAME_ACTIVATED = "dense_redstone_ore_large";

  private static final AxisAlignedBB AABB = AABBHelper.create(2, 0, 2, 14, 16, 14);

  public BlockOreDenseRedstoneLarge(boolean isActivated) {

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

    return 11;
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  protected ItemStack getSilkTouchDrop(IBlockState state) {

    return new ItemStack(ModuleCore.Blocks.ORE_DENSE_REDSTONE_LARGE_ACTIVATED);
  }

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    return true;
  }

  @Override
  public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {

    return 5 + RANDOM.nextInt(10);
  }

  @Override
  public int quantityDropped(Random random) {

    return 4 + random.nextInt(8);
  }

  @Override
  protected IBlockState getInactiveState() {

    return ModuleCore.Blocks.ORE_DENSE_REDSTONE_LARGE.getDefaultState();
  }

  @Override
  protected IBlockState getActiveState() {

    return ModuleCore.Blocks.ORE_DENSE_REDSTONE_LARGE_ACTIVATED.getDefaultState();
  }

  @Override
  protected int getParticleCount() {

    return 8;
  }

  @Override
  protected int getProximityRepairAmount() {

    return ModuleToolConfig.REDSTONE_TOOLS.PROXIMITY_REPAIR_AMOUNTS[2];
  }
}
