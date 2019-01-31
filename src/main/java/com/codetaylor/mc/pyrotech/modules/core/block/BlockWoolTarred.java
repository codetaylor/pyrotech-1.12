package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockWoolTarred
    extends Block {

  public static final String NAME = "wool_tarred";

  public BlockWoolTarred() {

    super(Material.CLOTH);
    this.setSoundType(SoundType.CLOTH);
    this.setHardness(0.8f);
    Blocks.FIRE.setFireInfo(this, 5, 20);
  }
}
