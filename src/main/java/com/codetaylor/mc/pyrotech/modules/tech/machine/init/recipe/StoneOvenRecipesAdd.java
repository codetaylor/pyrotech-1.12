package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class StoneOvenRecipesAdd {

  public static void apply(IForgeRegistry<StoneOvenRecipe> registry) {
    //
  }

  public static void registerInheritedDryingRackRecipes(
      IForgeRegistryModifiable<DryingRackRecipe> dryingRackRegistry,
      IForgeRegistryModifiable<StoneOvenRecipe> stoneOvenRegistry
  ) {

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)
        && ModuleTechMachineConfig.STONE_OVEN.INHERIT_DRYING_RACK_RECIPES) {
      RecipeHelper.inherit(dryingRackRegistry, stoneOvenRegistry, recipe -> {
        int timeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.STONE_OVEN.INHERITED_DRYING_RACK_RECIPE_SPEED_MODIFIER);
        return new StoneOvenRecipe(
            recipe.getOutput(),
            recipe.getInput(),
            Math.max(1, timeTicks)
        ).setRegistryName("parent_drying_" + recipe.getRegistryName());
      });
    }
  }
}