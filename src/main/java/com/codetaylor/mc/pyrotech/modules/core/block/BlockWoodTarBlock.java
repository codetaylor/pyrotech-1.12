package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockWoodTarBlock
    extends Block {

  public static final String NAME = "wood_tar_block";

  public BlockWoodTarBlock() {

    super(Material.CLAY);
    this.setHarvestLevel("shovel", 0);
    this.setHardness(2);
    Blocks.FIRE.setFireInfo(this, 5, 5);
    this.setSoundType(SoundType.SLIME);
  }
}
