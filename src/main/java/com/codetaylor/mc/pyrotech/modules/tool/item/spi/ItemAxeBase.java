package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAxeBase
    extends ItemAxe {

  public static final String NAME_BONE = "bone_axe";
  public static final String NAME_FLINT = "flint_axe";
  public static final String NAME_OBSIDIAN = "obsidian_axe";

  public ItemAxeBase(ToolMaterial material, String toolTierName) {

    super(material);
    this.init(toolTierName);
  }

  public ItemAxeBase(ToolMaterial material, float damage, float speed, String toolTierName) {

    super(material, damage, speed);
    this.init(toolTierName);
  }

  protected void init(String toolTierName) {

    Integer maxDamage = ModuleToolConfig.DURABILITY.get(toolTierName);

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }

    int harvestLevel = ModuleToolConfig.getHarvestLevel(toolTierName);
    this.setHarvestLevel("axe", harvestLevel);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (this.getDamage(stack) == 0) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", this.getMaxDamage(stack)));
    }
  }
}
