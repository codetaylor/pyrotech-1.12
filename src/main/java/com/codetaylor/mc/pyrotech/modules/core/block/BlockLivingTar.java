package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockLivingTar
    extends Block {

  public static final String NAME = "living_tar";

  public BlockLivingTar() {

    super(Material.ROCK);
    this.setSoundType(SoundType.SLIME);
    this.setHarvestLevel("pickaxe", 0);
    this.setHardness(0.8f);
  }

  @Override
  public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {

    return (side == EnumFacing.UP);
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

    return 0;
  }

  @Override
  public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

    return 0;
  }
}
