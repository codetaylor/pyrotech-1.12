package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.spi.BlockAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileAnvilIronPlated;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockAnvilIronPlated
    extends BlockAnvilBase {

  public static final String NAME = "anvil_iron_plated";

  public BlockAnvilIronPlated() {

    super(Material.IRON);
    this.setHardness(5.0F);
    this.setResistance(10.0F);
    this.setSoundType(SoundType.METAL);
    this.setHarvestLevel("pickaxe", 1);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileAnvilIronPlated();
  }

  @Override
  protected Block getBlock() {

    return ModuleTechBasic.Blocks.ANVIL_IRON_PLATED;
  }
}
