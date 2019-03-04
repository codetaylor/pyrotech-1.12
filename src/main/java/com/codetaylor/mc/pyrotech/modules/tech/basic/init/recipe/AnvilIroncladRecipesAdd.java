package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class AnvilIroncladRecipesAdd {

  public static void apply(IForgeRegistry<AnvilRecipe> registry) {

    AnvilIroncladRecipesAdd.registerPickaxeRecipes(registry);
    AnvilIroncladRecipesAdd.registerHammerRecipes(registry);
  }

  private static void registerHammerRecipes(IForgeRegistry<AnvilRecipe> registry) {
    //
  }

  private static void registerPickaxeRecipes(IForgeRegistry<AnvilRecipe> registry) {
    //
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<AnvilRecipe> anvilRegistry
  ) {

    if (ModuleTechBasicConfig.IRONCLAD_ANVIL.INHERIT_GRANITE_ANVIL_RECIPES) {
      RecipeHelper.inherit("granite_anvil", anvilRegistry, anvilRegistry, anvilRecipe -> new AnvilRecipe(
          anvilRecipe.getOutput(),
          anvilRecipe.getInput(),
          Math.max(1, (int) (anvilRecipe.getHits() * Math.max(0, ModuleTechBasicConfig.IRONCLAD_ANVIL.INHERITED_GRANITE_ANVIL_RECIPE_HIT_MODIFIER))),
          anvilRecipe.getType(),
          AnvilRecipe.EnumTier.IRONCLAD
      ));
    }
  }
}