package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemButchersKnife
    extends ItemSword {

  public static final String BONE_NAME = "bone_butchers_knife";
  public static final String FLINT_NAME = "flint_butchers_knife";
  public static final String STONE_NAME = "stone_butchers_knife";
  public static final String IRON_NAME = "iron_butchers_knife";
  public static final String GOLD_NAME = "gold_butchers_knife";
  public static final String DIAMOND_NAME = "diamond_butchers_knife";
  public static final String OBSIDIAN_NAME = "obsidian_butchers_knife";

  public ItemButchersKnife(ToolMaterial material, int maxDamage) {

    super(material);
    this.setMaxDamage(maxDamage);
  }

  @Override
  public float getAttackDamage() {

    return Math.max(1, super.getAttackDamage() / 2);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (ModuleCoreConfig.CLIENT.SHOW_DURABILITY_TOOLTIPS && this.getDamage(stack) == 0) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", this.getMaxDamage(stack)));
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {

    return super.getIsRepairable(toRepair, repair);
  }
}
