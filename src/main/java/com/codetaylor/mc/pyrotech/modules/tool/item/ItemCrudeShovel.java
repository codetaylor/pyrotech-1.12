package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShovelBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class ItemCrudeShovel
    extends ItemShovelBase {

  public static final String NAME = "crude_shovel";

  public ItemCrudeShovel() {

    super(ToolMaterial.STONE, "crude");
    this.setMaxDamage(ToolMaterial.STONE.getMaxUses() / 4);
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {

    return super.getDestroySpeed(stack, state) * 0.5f;
  }
}
