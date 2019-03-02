package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockOreDenseNetherCoal
    extends Block {

  public static final String NAME = "dense_nether_coal_ore";

  public BlockOreDenseNetherCoal() {

    super(Material.ROCK);
    this.setResistance(5.0F);
    this.setHardness(10);
    this.setHarvestLevel("pickaxe", 3);
    this.setSoundType(SoundType.STONE);
  }

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {

    Random rand = world instanceof World ? ((World) world).rand : RANDOM;
    int count = rand.nextInt(16) + 8 + fortune;

    for (int i = 0; i < count; i++) {
      Item item = Items.COAL;

      if (item != Items.AIR) {
        drops.add(new ItemStack(item, 1, 0));
      }
    }
  }
}
