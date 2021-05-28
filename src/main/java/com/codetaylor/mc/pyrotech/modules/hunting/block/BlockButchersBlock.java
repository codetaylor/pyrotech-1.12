package com.codetaylor.mc.pyrotech.modules.hunting.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import net.minecraft.block.material.Material;

public class BlockButchersBlock
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final String NAME = "butchers_block";

  public BlockButchersBlock() {

    super(Material.WOOD);
    this.setHardness(1);
  }
}
