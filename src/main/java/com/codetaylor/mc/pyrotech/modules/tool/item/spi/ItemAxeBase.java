package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemAxeBase
    extends ItemAxe {

  public ItemAxeBase(ToolMaterial material) {

    super(material);
  }

  public ItemAxeBase(ToolMaterial material, float damage, float speed) {

    super(material, damage, speed);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (this.getDamage(stack) == 0) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", this.getMaxDamage(stack)));
    }
  }
}
