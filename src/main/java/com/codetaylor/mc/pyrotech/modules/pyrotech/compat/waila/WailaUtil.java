package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila;

import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WailaUtil {

  public static final String WAILA_STACK = "waila.stack";
  public static final String WAILA_PROGRESS = "waila.progress";

  public static String getProgressRenderString(int progress, int total) {

    return SpecialChars.getRenderString(WAILA_PROGRESS, String.valueOf(progress), String.valueOf(total));
  }

  public static String getStackRenderString(ItemStack stack) {

    String result = "";

    if (stack.isEmpty()) {
      result += SpecialChars.getRenderString(WAILA_STACK, "2");

    } else {
      ResourceLocation registryName = stack.getItem().getRegistryName();
      String name = "MISSING";

      if (registryName != null) {
        name = registryName.toString();
      }

      result += SpecialChars.getRenderString(
          WAILA_STACK,
          "1",
          name,
          String.valueOf(stack.getCount()),
          String.valueOf(stack.getItemDamage())
      );
    }

    return result;
  }

  private WailaUtil() {
    //
  }

}
