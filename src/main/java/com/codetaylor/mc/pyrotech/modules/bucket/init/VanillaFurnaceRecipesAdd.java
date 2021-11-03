package com.codetaylor.mc.pyrotech.modules.bucket.init;

import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class VanillaFurnaceRecipesAdd {

  public static void apply() {

    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(ModuleBucket.Items.BUCKET_CLAY_UNFIRED),
        new ItemStack(ModuleBucket.Items.BUCKET_CLAY),
        0.1f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(ModuleBucket.Items.BUCKET_REFRACTORY_UNFIRED),
        new ItemStack(ModuleBucket.Items.BUCKET_REFRACTORY),
        0.1f
    );
  }
}