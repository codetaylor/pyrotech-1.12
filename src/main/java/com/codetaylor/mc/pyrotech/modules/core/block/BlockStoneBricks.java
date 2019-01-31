package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockStoneBricks
    extends Block {

  public static final String NAME = "stone_bricks";

  public BlockStoneBricks() {

    super(Material.ROCK);
    this.setSoundType(SoundType.STONE);
    this.setHardness(1.5f);
    this.setResistance(10);
    this.setHarvestLevel("pickaxe", 0);
  }

}
