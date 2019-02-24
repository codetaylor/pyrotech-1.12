package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.util.StringHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class TooltipEventHandler {

  public static class BurnTime {

    @SubscribeEvent
    public void on(ItemTooltipEvent event) {

      int itemBurnTime = StackHelper.getItemBurnTime(event.getItemStack());

      if (itemBurnTime > 0) {
        List<String> lines = event.getToolTip();

        if (lines.size() > 1) {
          lines.add(1, I18n.translateToLocalFormatted("gui.pyrotech.tooltip.burn.time", StringHelper.ticksToHMS(itemBurnTime)));

        } else {
          lines.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.burn.time", StringHelper.ticksToHMS(itemBurnTime)));
        }
      }
    }
  }

}
