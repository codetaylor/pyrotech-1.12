package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemShearsBase
    extends ItemShears {

  public static final String NAME_CLAY = "clay_shears";
  public static final String NAME_STONE = "stone_shears";
  public static final String NAME_BONE = "bone_shears";
  public static final String NAME_FLINT = "flint_shears";
  public static final String NAME_DIAMOND = "diamond_shears";
  public static final String NAME_OBSIDIAN = "obsidian_shears";

  public ItemShearsBase(String toolTierName) {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get(toolTierName));
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (this.getDamage(stack) == 0) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", this.getMaxDamage(stack)));
    }
  }
}
