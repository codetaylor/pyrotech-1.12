package com.codetaylor.mc.pyrotech.modules.tech.bloomery.event;

import com.codetaylor.mc.athenaeum.util.TooltipHelper;
import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemTooltipEventHandler {

  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public void on(ItemTooltipEvent event) {

    ItemStack itemStack = event.getItemStack();
    List<String> tooltip = event.getToolTip();

    double bloomeryModifier = ModuleTechBloomeryConfig.BLOOMERY.getSpecialFuelBurnTimeModifier(itemStack);
    double witherForgeModifier = ModuleTechBloomeryConfig.WITHER_FORGE.getSpecialFuelBurnTimeModifier(itemStack);

    boolean hasBloomeryModifier = Double.compare(bloomeryModifier, 1) != 0;
    boolean hasWitherForgeModifier = Double.compare(witherForgeModifier, 1) != 0;

    if (!hasBloomeryModifier && !hasWitherForgeModifier) {
      return;
    }

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

      TooltipHelper.addTooltip(tooltip, TextFormatting.GOLD + I18n.translateToLocal("gui.pyrotech.tooltip.burn.time.efficiency"), 1);

      if (hasBloomeryModifier) {
        String unlocalizedName = Item.getItemFromBlock(ModuleTechBloomery.Blocks.BLOOMERY).getUnlocalizedName() + ".name";
        String localizedName = I18n.translateToLocal(unlocalizedName);
        String text = TextFormatting.GOLD + " " + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.burn.time.modifier", localizedName, TextFormatting.YELLOW, ((int) (bloomeryModifier * 100)));
        TooltipHelper.addTooltip(tooltip, text, 2);
      }

      if (hasWitherForgeModifier) {
        String unlocalizedName = Item.getItemFromBlock(ModuleTechBloomery.Blocks.WITHER_FORGE).getUnlocalizedName() + ".name";
        String localizedName = I18n.translateToLocal(unlocalizedName);
        String text = TextFormatting.GOLD + " " + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.burn.time.modifier", localizedName, TextFormatting.YELLOW, ((int) (witherForgeModifier * 100)));
        TooltipHelper.addTooltip(tooltip, text, (hasBloomeryModifier ? 3 : 2));
      }

    } else {
      TooltipHelper.addTooltip(tooltip, I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY), 1);
    }
  }
}
