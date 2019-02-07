package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.recipe.CompoundIngredientPublic;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.Arrays;

public class BrickCrucibleRecipesAdd {

  public static void apply(IForgeRegistry<BrickCrucibleRecipe> registry) {

    // Lava
    //noinspection unchecked
    registry.register(new BrickCrucibleRecipe(
        new FluidStack(FluidRegistry.LAVA, 250),
        new CompoundIngredientPublic(Arrays.asList(new Ingredient[]{
            new OreIngredient("stone"),
            new OreIngredient("cobblestone")
        })),
        2 * 60 * 20
    ).setRegistryName(ModuleTechMachine.MOD_ID, "lava_from_stone"));
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<StoneCrucibleRecipe> stoneRegistry,
      IForgeRegistryModifiable<BrickCrucibleRecipe> brickRegistry
  ) {

    if (ModuleTechMachineConfig.BRICK_CRUCIBLE.INHERIT_STONE_TIER_RECIPES) {
      RecipeHelper.inherit(stoneRegistry, brickRegistry, recipe -> {
        int timeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.BRICK_CRUCIBLE.INHERITED_STONE_TIER_RECIPE_SPEED_MODIFIER);
        return new BrickCrucibleRecipe(
            recipe.getOutput(),
            recipe.getInput(),
            Math.max(1, timeTicks)
        ).setRegistryName("parent_stone_" + recipe.getRegistryName());
      });
    }

  }
}