package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StoneOvenRecipesAdd {

  public static void registerInheritedDryingRackRecipes(
      IForgeRegistryModifiable<DryingRackRecipe> dryingRackRegistry,
      IForgeRegistryModifiable<StoneOvenRecipe> stoneOvenRegistry
  ) {

    Collection<DryingRackRecipe> valuesCollection = dryingRackRegistry.getValuesCollection();
    List<DryingRackRecipe> snapshot = new ArrayList<>(valuesCollection);

    for (DryingRackRecipe dryingRackRecipe : snapshot) {
      int cookTimeTicks = (int) (dryingRackRecipe.getTimeTicks() * ModuleTechMachineConfig.STONE_OVEN.INHERITED_DRYING_RACK_RECIPE_SPEED_MODIFIER);

      stoneOvenRegistry.register(new StoneOvenRecipe(
          dryingRackRecipe.getOutput(),
          dryingRackRecipe.getInput(),
          Math.max(1, cookTimeTicks)
      ).setRegistryName("inherited_" + dryingRackRecipe.getRegistryName()));
    }
  }
}