package com.codetaylor.mc.pyrotech.modules.tool.init;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleTool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class VanillaFurnaceRecipesAdd {

  public static void apply() {

    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(ModuleTool.Items.UNFIRED_CLAY_SHEARS),
        new ItemStack(ModuleTool.Items.CLAY_SHEARS),
        0.1f
    );
  }
}