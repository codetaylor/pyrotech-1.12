package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemAxeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class ItemCrudeAxe
    extends ItemAxeBase {

  public static final String NAME = "crude_axe";

  public ItemCrudeAxe() {

    super(ToolMaterial.STONE, "crude");
    this.setMaxDamage(ToolMaterial.STONE.getMaxUses() / 4);
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {

    return super.getDestroySpeed(stack, state) * 0.5f;
  }
}
