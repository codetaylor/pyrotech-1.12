package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockThatch
    extends Block {

  public static final String NAME = "thatch";

  public BlockThatch() {

    super(Material.GRASS);
    this.setSoundType(SoundType.PLANT);
    this.setHardness(0.5F);
    this.setHarvestLevel(null, 0);
    Blocks.FIRE.setFireInfo(this, 60, 100);
  }

  @Override
  public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {

    entity.fall(fallDistance, 0.2F);
  }

}
