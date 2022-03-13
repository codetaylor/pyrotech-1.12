package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockFreckleberryPlant
    extends BlockCrops {

  public static final String NAME = "freckleberry_plant";

  private static final AxisAlignedBB[] AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0, 0, 0, 1, 0.125, 1),
      new AxisAlignedBB(0, 0, 0, 1, 0.375, 1),
      new AxisAlignedBB(0, 0, 0, 1, 0.5, 1),
      new AxisAlignedBB(0, 0, 0, 1, 0.5, 1),
      new AxisAlignedBB(0, 0, 0, 1, 0.5, 1),
      new AxisAlignedBB(0, 0, 0, 1, 0.5, 1),
      new AxisAlignedBB(0, 0, 0, 1, 0.5, 1),
      new AxisAlignedBB(0, 0, 0, 1, 0.5, 1)
  };

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB[state.getValue(this.getAgeProperty())];
  }

  @Nonnull
  @Override
  protected Item getSeed() {

    return ModuleCore.Items.FRECKLEBERRY_SEEDS;
  }

  @Nonnull
  @Override
  protected Item getCrop() {

    return ModuleCore.Items.FRECKLEBERRIES;
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    return 1 + random.nextInt(2 + fortune);
  }

  @Nonnull
  @Override
  public EnumOffsetType getOffsetType() {

    return EnumOffsetType.XZ;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    super.updateTick(world, pos, state, rand);

    IBlockState blockStateDown = world.getBlockState(pos.down());

    if (blockStateDown.getBlock().isFertile(world, pos.down())
        && this.getAge(state) == this.getMaxAge()) {
      super.updateTick(world, pos, state, rand);
    }
  }

  public boolean isValidBlock(IBlockState blockState) {

    if (blockState.getBlock() == Blocks.FARMLAND
        || blockState.getBlock() == ModuleCore.Blocks.FARMLAND_MULCHED) {
      return true;
    }

    return (blockState.getMaterial() == Material.GROUND
        || blockState.getMaterial() == Material.GRASS)
        && blockState.isFullBlock();
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (this.getAge(state) == this.getMaxAge()
        && hand == EnumHand.MAIN_HAND) {

      if (!world.isRemote) {
        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItemMainhand);
        StackHelper.spawnStackOnTop(world, new ItemStack(this.getCrop(), this.quantityDropped(state, fortune, RandomHelper.random())), pos, 0.25f);
        world.setBlockState(pos, this.withAge(this.getMaxAge() - 1));
      }

      return true;
    }

    return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }
}
