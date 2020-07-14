package com.codetaylor.mc.pyrotech.modules.tech.machine.event;

import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemCog;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CogTooltipEventHandler {

  @SubscribeEvent
  public void on(ItemTooltipEvent event) {

    ItemStack itemStack = event.getItemStack();

    if (itemStack.getItem() instanceof ItemCog || !ItemCog.isCog(itemStack)) {
      return;
    }

    List<String> toAdd = ItemCog.getTooltip(itemStack, new ArrayList<>(3));

    if (toAdd.isEmpty()) {
      return;
    }

    List<String> toolTip = event.getToolTip();

    if (toolTip.size() > 1) {
      Collections.reverse(toAdd);

      for (String line : toAdd) {
        toolTip.add(1, line);
      }

    } else {
      toolTip.addAll(toAdd);
    }
  }

}
