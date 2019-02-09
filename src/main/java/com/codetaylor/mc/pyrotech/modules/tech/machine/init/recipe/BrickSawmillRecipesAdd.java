package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickSawmillRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class BrickSawmillRecipesAdd {

  public static void apply(IForgeRegistry<BrickSawmillRecipe> registry) {
    //
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<StoneSawmillRecipe> stoneRegistry,
      IForgeRegistryModifiable<BrickSawmillRecipe> brickRegistry
  ) {

    if (ModuleTechMachineConfig.BRICK_SAWMILL.INHERIT_STONE_TIER_RECIPES) {
      RecipeHelper.inherit("stone_sawmill", stoneRegistry, brickRegistry, recipe -> {
        int cookTimeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.BRICK_SAWMILL.INHERITED_STONE_TIER_RECIPE_DURATION_MODIFIER);
        return new BrickSawmillRecipe(
            recipe.getOutput(),
            recipe.getInput(),
            Math.max(1, cookTimeTicks),
            recipe.getBlade(),
            recipe.createWoodChips()
        );
      });
    }
  }
}