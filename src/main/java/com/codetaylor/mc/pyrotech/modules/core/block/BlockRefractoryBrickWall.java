package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockWallBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockRefractoryBrickWall
    extends BlockWallBase {

  public static final String NAME = "refractory_brick_wall";

  public BlockRefractoryBrickWall() {

    super(Material.ROCK, MapColor.SAND);
    this.setSoundType(SoundType.STONE);
    this.setHardness(3);
    this.setResistance(10);
    this.setHarvestLevel("pickaxe", 0);
  }
}
