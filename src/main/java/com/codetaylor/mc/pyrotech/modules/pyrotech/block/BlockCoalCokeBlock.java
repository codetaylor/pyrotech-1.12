package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockCoalCokeBlock
    extends Block {

  public static final String NAME = "coal_coke_block";

  public BlockCoalCokeBlock() {

    super(Material.ROCK);
    this.setHarvestLevel("pickaxe", 0);
    this.setHardness(5);
    Blocks.FIRE.setFireInfo(this, 5, 5);
  }
}
