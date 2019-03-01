package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemHoeBase
    extends ItemHoe {

  public ItemHoeBase(ToolMaterial material) {

    super(material);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (this.getDamage(stack) == 0) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", this.getMaxDamage(stack)));
    }
  }
}
