package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.JEIRecipeCategoryStoneCrucible;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.JEIRecipeCategoryStoneKiln;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.JEIRecipeCategoryStoneSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.JEIRecipeCategoryStoneOven;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperStoneCrucible;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperStoneKiln;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperStoneSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperStoneOven;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInItemOutBase;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class PluginJEI
    implements IModPlugin {

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {

    IJeiHelpers jeiHelpers = registry.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

    registry.addRecipeCategories(
        new JEIRecipeCategoryStoneKiln(guiHelper),
        new JEIRecipeCategoryStoneSawmill(guiHelper),
        new JEIRecipeCategoryStoneOven(guiHelper),
        new JEIRecipeCategoryStoneCrucible(guiHelper)
    );
  }

  @Override
  public void register(IModRegistry registry) {

    // --- Stone Oven
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.STONE_OVEN), JEIRecipeCategoryStoneOven.UID);
      registry.handleRecipes(StoneOvenRecipe.class, JEIRecipeWrapperStoneOven::new, JEIRecipeCategoryStoneOven.UID);
      List<JEIRecipeWrapperStoneOven> furnaceRecipes = PluginJEI.getFurnaceRecipesForStoneOven(input -> {

        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);

        if (StoneOvenRecipe.hasWhitelist()) {
          return StoneOvenRecipe.isWhitelisted(output);

        } else if (StoneOvenRecipe.hasBlacklist()) {
          return !StoneOvenRecipe.isBlacklisted(output);
        }

        return RecipeHelper.hasFurnaceFoodRecipe(input);
      });
      registry.addRecipes(furnaceRecipes, JEIRecipeCategoryStoneOven.UID);
      List<StoneOvenRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.STONE_OVEN_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryStoneOven.UID);
    }

    // --- Stone Crucible
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.STONE_CRUCIBLE), JEIRecipeCategoryStoneCrucible.UID);
      registry.handleRecipes(StoneCrucibleRecipe.class, JEIRecipeWrapperStoneCrucible::new, JEIRecipeCategoryStoneCrucible.UID);
      List<StoneCrucibleRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.STONE_CRUCIBLE_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryStoneCrucible.UID);
    }

    // --- Stone Mill
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.STONE_SAWMILL), JEIRecipeCategoryStoneSawmill.UID);
      registry.handleRecipes(StoneSawmillRecipe.class, JEIRecipeWrapperStoneSawmill::new, JEIRecipeCategoryStoneSawmill.UID);
      List<StoneSawmillRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.STONE_SAWMILL_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryStoneSawmill.UID);
    }

    // --- Stone Kiln
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.STONE_KILN), JEIRecipeCategoryStoneKiln.UID);
      registry.handleRecipes(StoneKilnRecipe.class, JEIRecipeWrapperStoneKiln::new, JEIRecipeCategoryStoneKiln.UID);
      List<MachineRecipeItemInItemOutBase> recipeList = new ArrayList<>(ModuleTechMachine.Registries.STONE_KILN_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryStoneKiln.UID);
    }
  }

  private static List<JEIRecipeWrapperStoneOven> getFurnaceRecipesForStoneOven(Predicate<ItemStack> filter) {

    FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
    Map<ItemStack, ItemStack> smeltingMap = furnaceRecipes.getSmeltingList();

    List<JEIRecipeWrapperStoneOven> recipes = new ArrayList<>();

    for (Map.Entry<ItemStack, ItemStack> entry : smeltingMap.entrySet()) {

      ItemStack input = entry.getKey();

      if (!filter.test(input)) {
        continue;
      }

      ItemStack output = entry.getValue();
      recipes.add(new JEIRecipeWrapperStoneOven(Ingredient.fromStacks(input), output, ModuleTechMachineConfig.STONE_OVEN.COOK_TIME_TICKS));
    }

    return recipes;
  }

}
