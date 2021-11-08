package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.function.Function;

public class AnvilObsidianRecipesAdd {

  public static final Function<AnvilRecipe, AnvilRecipe> INHERIT_TRANSFORMER = anvilRecipe -> new AnvilRecipe(
      anvilRecipe.getOutput(),
      anvilRecipe.getInput(),
      Math.max(1, (int) (anvilRecipe.getHits() * Math.max(0, ModuleTechBasicConfig.OBSIDIAN_ANVIL.INHERITED_IRONCLAD_ANVIL_RECIPE_HIT_MODIFIER))),
      anvilRecipe.getType(),
      AnvilRecipe.EnumTier.OBSIDIAN
  );

  public static void apply(IForgeRegistry<AnvilRecipe> registry) {

    AnvilObsidianRecipesAdd.registerPickaxeRecipes(registry);
    AnvilObsidianRecipesAdd.registerHammerRecipes(registry);
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

    if (ModuleTechBasicConfig.OBSIDIAN_ANVIL.INHERIT_IRONCLAD_ANVIL_RECIPES) {
      RecipeHelper.inherit("ironclad_anvil", anvilRegistry, anvilRegistry, INHERIT_TRANSFORMER, anvilRecipe -> {

        // if the recipe is extended, like a bloom recipe, then defer
        // inheritance to the recipe

        if (anvilRecipe instanceof AnvilRecipe.IExtendedRecipe) {
          return ((AnvilRecipe.IExtendedRecipe<?>) anvilRecipe).allowInheritance()
              && anvilRecipe.isTier(AnvilRecipe.EnumTier.IRONCLAD);
        }

        return anvilRecipe.isTier(AnvilRecipe.EnumTier.IRONCLAD);
      });
    }
  }
}