package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.pyrotech.modules.tech.basic.block.spi.BlockAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileAnvilGranite;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockAnvilGranite
    extends BlockAnvilBase {

  public static final String NAME = "anvil_granite";

  public BlockAnvilGranite() {

    super(Material.ROCK);
    this.setHardness(3.0F);
    this.setResistance(5.0F);
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 0);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileAnvilGranite();
  }
}
