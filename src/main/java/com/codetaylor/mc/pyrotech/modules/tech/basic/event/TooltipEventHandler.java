package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompostBinRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class TooltipEventHandler {

  public static class CompostValue {

    @SubscribeEvent
    public void on(ItemTooltipEvent event) {

      ItemStack itemStack = event.getItemStack();
      CompostBinRecipe recipe = CompostBinRecipe.getRecipe(itemStack);

      if (recipe != null) {
        int compostValue = recipe.getCompostValue();

        if (compostValue > 0) {
          List<String> lines = event.getToolTip();

          if (lines.size() > 1) {
            lines.add(1, I18n.translateToLocalFormatted("gui.pyrotech.tooltip.compost.value", compostValue));

          } else {
            lines.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.compost.value", compostValue));
          }
        }
      }
    }
  }
}
