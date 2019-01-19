package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockWoolTarred
    extends Block {

  public static final String NAME = "wool_tarred";

  public BlockWoolTarred() {

    super(Material.CLOTH);
    this.setSoundType(SoundType.CLOTH);
    this.setHardness(0.8f);
  }
}
