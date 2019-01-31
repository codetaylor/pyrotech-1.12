package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockRefractoryBrick
    extends Block {

  public static final String NAME = "refractory_brick_block";

  public BlockRefractoryBrick() {

    super(Material.ROCK, MapColor.SAND);
    this.setHarvestLevel("pickaxe", 0);
    this.setHardness(3);
    this.setResistance(10);
  }
}
