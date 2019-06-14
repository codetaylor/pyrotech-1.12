package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.ModulePluginPatchouli;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CampfireRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class CampfireRecipesAdd {

  public static void apply(IForgeRegistry<CampfireRecipe> registry) {

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModulePluginPatchouli.class)) {
      registry.register(new CampfireRecipe(
          new ItemStack(ModuleCore.Items.BOOK),
          Ingredient.fromStacks(new ItemStack(Items.BOOK))
      ).setRegistryName(ModuleTechBasic.MOD_ID, "book"));
    }
  }
}