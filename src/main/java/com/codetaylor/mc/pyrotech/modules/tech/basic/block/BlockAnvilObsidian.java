package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.spi.BlockAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileAnvilGranite;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileAnvilObsidian;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockAnvilObsidian
    extends BlockAnvilBase {

  public static final String NAME = "anvil_obsidian";

  public BlockAnvilObsidian() {

    super(Material.ROCK);
    this.setHardness(50.0F);
    this.setResistance(2000.0F);
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 0);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileAnvilObsidian();
  }

  @Override
  protected Block getBlock() {

    return ModuleTechBasic.Blocks.ANVIL_GRANITE;
  }
}
