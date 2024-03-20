package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemHoeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class ItemCrudeHoe
    extends ItemHoeBase {

  public static final String NAME = "crude_hoe";

  public ItemCrudeHoe() {

    super(ToolMaterial.STONE, "crude");
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {

    return super.getDestroySpeed(stack, state) * 0.5f;
  }
}
