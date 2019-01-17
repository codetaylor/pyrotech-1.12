package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockPileBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockPileWoodChips
    extends BlockPileBase {

  public static final String NAME = "pile_wood_chips";

  public BlockPileWoodChips() {

    super(Material.WOOD);
    this.setHardness(0.25f);
    this.setResistance(0);
    this.setHarvestLevel("shovel", 0);
    this.setSoundType(SoundType.GROUND);
  }

  @Override
  protected ItemStack getDrop() {

    return new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta());
  }
}
