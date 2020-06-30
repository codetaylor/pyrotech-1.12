package com.codetaylor.mc.pyrotech.modules.core.block.spi;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public abstract class BlockOreDenseQuartzBase
    extends BlockPartialBase {

  public BlockOreDenseQuartzBase() {

    super(Material.ROCK);
    this.setResistance(5.0F);
    this.setHardness(3);
    this.setHarvestLevel("pickaxe", 2);
    this.setSoundType(SoundType.GLASS);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    Random rand = world instanceof World ? ((World) world).rand : RANDOM;
    int count = this.quantityDroppedWithBonus(fortune, rand);

    for (int i = 0; i < count; i++) {

      if (rand.nextFloat() < 0.25) {
        drops.add(new ItemStack(Items.QUARTZ));

      } else {
        drops.add(ItemMaterial.EnumType.DENSE_QUARTZ.asStack());
      }
    }
  }

  @Override
  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {

    return super.canPlaceBlockOnSide(world, pos, side)
        && world.isSideSolid(pos.down(), EnumFacing.UP);
  }

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    return false;
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, @Nonnull Random random) {

    return this.quantityDropped(random) + random.nextInt(fortune + 1);
  }
}
