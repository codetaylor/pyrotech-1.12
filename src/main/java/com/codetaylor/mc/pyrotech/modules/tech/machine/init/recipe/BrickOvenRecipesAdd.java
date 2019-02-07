package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickOvenRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class BrickOvenRecipesAdd {

  public static void apply(IForgeRegistry<BrickOvenRecipe> registry) {
    //
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<StoneOvenRecipe> stoneRegistry,
      IForgeRegistryModifiable<BrickOvenRecipe> brickRegistry
  ) {

    if (ModuleTechMachineConfig.BRICK_OVEN.INHERIT_STONE_TIER_RECIPES) {
      RecipeHelper.inherit(stoneRegistry, brickRegistry, recipe -> {
        int cookTimeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.BRICK_OVEN.INHERITED_STONE_TIER_RECIPE_SPEED_MODIFIER);
        return new BrickOvenRecipe(
            recipe.getOutput(),
            recipe.getInput(),
            Math.max(1, cookTimeTicks)
        ).setRegistryName("parent_stone_" + recipe.getRegistryName());
      });
    }
  }
}