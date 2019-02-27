package com.codetaylor.mc.pyrotech.modules.tech.refractory.event;

import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.pyrotech.library.util.Tooltips;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemTooltipEventHandler {

  public static final List<ParseResult> VALID_REFRACTORY_ITEM_BLOCKS = new ArrayList<>();

  @SubscribeEvent
  public void on(ItemTooltipEvent event) {

    ItemStack itemStack = event.getItemStack();
    List<String> tooltip = event.getToolTip();

    for (ParseResult parseResult : VALID_REFRACTORY_ITEM_BLOCKS) {

      if (parseResult.matches(itemStack, true)) {
        Tooltips.addValidRefractoryBlock(tooltip, 1);
        break;
      }
    }
  }
}
