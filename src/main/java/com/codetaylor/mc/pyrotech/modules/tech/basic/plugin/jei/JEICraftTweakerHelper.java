package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei;

import crafttweaker.mc1120.recipes.MCRecipeBase;
import net.minecraft.item.crafting.IRecipe;

public final class JEICraftTweakerHelper {

  public static boolean isCraftTweakerRecipe(IRecipe recipe) {

    return recipe instanceof MCRecipeBase;
  }

  private JEICraftTweakerHelper() {
    //
  }
}
