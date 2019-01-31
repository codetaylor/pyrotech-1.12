package com.codetaylor.mc.pyrotech.modules.core.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class VanillaCraftingRecipesRemove {

  public static void apply(IForgeRegistry<IRecipe> registry) {

    IForgeRegistryModifiable modifiableRegistry = (IForgeRegistryModifiable) registry;

    for (String resourceName : ModuleCoreConfig.RECIPES.VANILLA_CRAFTING_REMOVE) {
      modifiableRegistry.remove(new ResourceLocation(resourceName));
    }
  }
}