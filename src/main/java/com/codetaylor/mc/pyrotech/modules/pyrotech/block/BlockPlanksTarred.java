package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockPlanksTarred
    extends Block {

  public static final String NAME = "planks_tarred";

  public BlockPlanksTarred() {

    super(Material.WOOD);
    this.setSoundType(SoundType.WOOD);
    this.setHardness(2.0f);
    this.setResistance(5.0f);
    Blocks.FIRE.setFireInfo(this, 5, 20);
  }
}
