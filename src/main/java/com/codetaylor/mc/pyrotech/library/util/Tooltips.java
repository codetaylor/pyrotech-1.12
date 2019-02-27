package com.codetaylor.mc.pyrotech.library.util;

import com.codetaylor.mc.athenaeum.util.TooltipHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public final class Tooltips {

  public static void addValidRefractoryBlock(List<String> tooltip, int preferredIndex) {

    TooltipHelper.addTooltip(tooltip, I18n.translateToLocal("gui.pyrotech.tooltip.refractory.valid"), preferredIndex);
  }

  public static void addCapacity(List<String> tooltip, int capacity, int preferredIndex) {

    TooltipHelper.addTooltip(tooltip, I18n.translateToLocalFormatted("gui.pyrotech.tooltip.fluid.capacity", capacity), preferredIndex);
  }

  public static void addHotFluids(List<String> tooltip, boolean holdsHotFluids, int preferredIndex) {

    TooltipHelper.addTooltip(tooltip, (holdsHotFluids ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocal("gui.pyrotech.tooltip.hot.fluids." + holdsHotFluids), preferredIndex);
  }

  public static void addRange(List<String> tooltip, int range, int preferredIndex) {

    TooltipHelper.addTooltip(tooltip, I18n.translateToLocalFormatted("gui.pyrotech.tooltip.drain", range * 2 + 1, range * 2 + 1), preferredIndex);
  }

  private Tooltips() {
    //
  }
}
