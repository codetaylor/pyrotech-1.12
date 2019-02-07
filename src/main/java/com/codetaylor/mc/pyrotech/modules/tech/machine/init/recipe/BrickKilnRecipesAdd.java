package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class BrickKilnRecipesAdd {

  public static void apply(IForgeRegistry<BrickKilnRecipe> registry) {
    //
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<StoneKilnRecipe> stoneRegistry,
      IForgeRegistryModifiable<BrickKilnRecipe> brickRegistry
  ) {

    if (ModuleTechMachineConfig.BRICK_KILN.INHERIT_STONE_TIER_RECIPES) {
      RecipeHelper.inherit(stoneRegistry, brickRegistry, recipe -> {
        int timeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.BRICK_KILN.INHERITED_STONE_TIER_RECIPE_SPEED_MODIFIER);
        float failureChance = (float) (recipe.getFailureChance() * ModuleTechMachineConfig.BRICK_KILN.INHERITED_STONE_TIER_RECIPE_FAILURE_CHANCE_MODIFIER);
        return new BrickKilnRecipe(
            recipe.getOutput(),
            recipe.getInput(),
            Math.max(1, timeTicks),
            failureChance,
            recipe.getFailureItems()
        ).setRegistryName("parent_stone_" + recipe.getRegistryName());
      });
    }
  }
}