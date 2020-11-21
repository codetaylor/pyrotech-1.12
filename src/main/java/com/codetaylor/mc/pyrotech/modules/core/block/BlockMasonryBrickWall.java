package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockWallBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMasonryBrickWall
    extends BlockWallBase {

  public static final String NAME = "masonry_brick_wall";

  public BlockMasonryBrickWall() {

    super(Material.ROCK);
    this.setSoundType(SoundType.STONE);
    this.setHardness(1.5f);
    this.setResistance(10);
    this.setHarvestLevel("pickaxe", 0);
  }
}
