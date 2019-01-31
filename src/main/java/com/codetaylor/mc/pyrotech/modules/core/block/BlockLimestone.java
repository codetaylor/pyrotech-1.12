package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockLimestone
    extends Block {

  public static final String NAME = "limestone";

  public BlockLimestone() {

    super(Material.ROCK);
    this.setSoundType(SoundType.STONE);
    this.setHardness(1.5f);
    this.setResistance(10);
    this.setHarvestLevel("pickaxe", 1);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    drops.add(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.LIMESTONE.getMeta()));
  }
}
