package com.codetaylor.mc.pyrotech.modules.tech.refractory.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class VanillaFurnaceRecipesAdd {

  public static void apply() {

    // slag glass from slag
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(ModuleTechBloomery.Blocks.PILE_SLAG, 1, 0),
        new ItemStack(ModuleCore.Blocks.SLAG_GLASS),
        0.1f
    );
  }
}