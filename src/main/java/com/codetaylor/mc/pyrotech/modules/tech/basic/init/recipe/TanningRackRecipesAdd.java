package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.TanningRackRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class TanningRackRecipesAdd {

  public static void apply(IForgeRegistry<TanningRackRecipe> registry) {

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleHunting.class)) {
      registry.register(new TanningRackRecipe(
          new ItemStack(Items.LEATHER),
          Ingredient.fromStacks(new ItemStack(ModuleHunting.Items.HIDE_WASHED)),
          ItemStack.EMPTY,
          20 * 10
      ).setRegistryName(new ResourceLocation(ModuleTechBasic.MOD_ID, "test_recipe")));
    }
  }
}