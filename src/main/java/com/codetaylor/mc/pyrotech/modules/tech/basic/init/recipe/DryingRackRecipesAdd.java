package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CrudeDryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class DryingRackRecipesAdd {

  public static void apply(IForgeRegistry<DryingRackRecipe> registry) {
    //
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<CrudeDryingRackRecipe> fromRegistry,
      IForgeRegistryModifiable<DryingRackRecipe> toRegistry
  ) {

    if (ModuleTechBasicConfig.DRYING_RACK.INHERIT_CRUDE_DRYING_RACK_RECIPES) {
      RecipeHelper.inherit("crude_drying_rack", fromRegistry, toRegistry, recipe -> new DryingRackRecipe(
          recipe.getOutput(),
          recipe.getInput(),
          (int) (recipe.getTimeTicks() * Math.max(0, ModuleTechBasicConfig.DRYING_RACK.INHERITED_CRUDE_DRYING_RACK_RECIPE_DURATION_MODIFIER))
      ));
    }
  }
}