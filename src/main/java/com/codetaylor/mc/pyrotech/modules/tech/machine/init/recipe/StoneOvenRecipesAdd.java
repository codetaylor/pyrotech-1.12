package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.function.Function;

public class StoneOvenRecipesAdd {

  public static final Function<DryingRackRecipe, StoneOvenRecipe> INHERIT_TRANSFORMER = recipe -> {
    int timeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.STONE_OVEN.INHERITED_DRYING_RACK_RECIPE_DURATION_MODIFIER);
    return new StoneOvenRecipe(
        recipe.getOutput(),
        recipe.getInput(),
        Math.max(1, timeTicks)
    );
  };

  public static void apply(IForgeRegistry<StoneOvenRecipe> registry) {

    registry.register(new StoneOvenRecipe(
        new ItemStack(Items.BREAD),
        Ingredient.fromStacks(ItemMaterial.EnumType.BREAD_DOUGH.asStack())
    ).setRegistryName(ModuleTechBasic.MOD_ID, "bread"));
  }

  public static void registerInheritedDryingRackRecipes(
      IForgeRegistryModifiable<DryingRackRecipe> dryingRackRegistry,
      IForgeRegistryModifiable<StoneOvenRecipe> stoneOvenRegistry
  ) {

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)
        && ModuleTechMachineConfig.STONE_OVEN.INHERIT_DRYING_RACK_RECIPES) {
      RecipeHelper.inherit("drying_rack", dryingRackRegistry, stoneOvenRegistry, INHERIT_TRANSFORMER);
    }
  }
}