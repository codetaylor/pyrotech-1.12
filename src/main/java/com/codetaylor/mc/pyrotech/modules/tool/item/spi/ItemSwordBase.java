package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSwordBase
    extends ItemSword {

  public static final String NAME_BONE = "bone_sword";
  public static final String NAME_FLINT = "flint_sword";
  public static final String NAME_OBSIDIAN = "obsidian_sword";

  public ItemSwordBase(ToolMaterial material, String toolTierName) {

    super(material);

    Integer maxDamage = ModuleToolConfig.DURABILITY.get(toolTierName);

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (ModuleCoreConfig.CLIENT.SHOW_DURABILITY_TOOLTIPS && this.getDamage(stack) == 0) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", this.getMaxDamage(stack)));
    }
  }
}
