package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCraftingTableTemplate
    extends BlockPartialBase {

  public static final String NAME = "crafting_table_template";

  public BlockCraftingTableTemplate() {

    super(Material.IRON);
    this.setHardness(5);
    this.setResistance(10);
    this.setSoundType(SoundType.METAL);
  }
}
