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

public class ItemHuntingKnife
    extends ItemSword {

  public static final String BONE_NAME = "bone_hunting_knife";
  public static final String FLINT_NAME = "flint_hunting_knife";
  public static final String STONE_NAME = "stone_hunting_knife";
  public static final String IRON_NAME = "iron_hunting_knife";
  public static final String GOLD_NAME = "gold_hunting_knife";
  public static final String DIAMOND_NAME = "diamond_hunting_knife";
  public static final String OBSIDIAN_NAME = "obsidian_hunting_knife";

  public ItemHuntingKnife(ToolMaterial material, int maxDamage) {

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
}
