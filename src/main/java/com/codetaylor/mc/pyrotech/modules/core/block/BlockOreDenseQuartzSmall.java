package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockOreDenseQuartzBase;
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

public class BlockOreDenseQuartzSmall
    extends BlockOreDenseQuartzBase {

  public static final String NAME = "dense_quartz_ore_small";

  private static final AxisAlignedBB AABB = AABBHelper.create(2, 0, 2, 14, 16, 14);

  public BlockOreDenseQuartzSmall() {

    super();
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile.pyrotech.dense_quartz_ore";
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  protected ItemStack getSilkTouchDrop(IBlockState state) {

    return new ItemStack(ModuleCore.Blocks.ORE_DENSE_QUARTZ_LARGE);
  }

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    return true;
  }

  @Override
  public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {

    return 2 + RANDOM.nextInt(4);
  }

  @Override
  public int quantityDropped(Random random) {

    return 2 + random.nextInt(3);
  }
}
