package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.JEIRecipeCategoryCrucibleStone;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.JEIRecipeCategoryKilnStone;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.JEIRecipeCategoryMillStone;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.JEIRecipeCategoryOvenStone;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperCrucibleStone;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperKilnStone;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperMillStone;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.JEIRecipeWrapperOvenStone;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInItemOutBase;
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
        new JEIRecipeCategoryKilnStone(guiHelper),
        new JEIRecipeCategoryMillStone(guiHelper),
        new JEIRecipeCategoryOvenStone(guiHelper),
        new JEIRecipeCategoryCrucibleStone(guiHelper)
    );
  }

  @Override
  public void register(IModRegistry registry) {

    // --- Stone Oven
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.OVEN_STONE), JEIRecipeCategoryOvenStone.UID);
      registry.handleRecipes(StoneOvenRecipe.class, JEIRecipeWrapperOvenStone::new, JEIRecipeCategoryOvenStone.UID);
      List<JEIRecipeWrapperOvenStone> furnaceRecipes = PluginJEI.getFurnaceRecipesForStoneOven(input -> {

        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);

        if (StoneOvenRecipe.hasWhitelist()) {
          return StoneOvenRecipe.isWhitelisted(output);

        } else if (StoneOvenRecipe.hasBlacklist()) {
          return !StoneOvenRecipe.isBlacklisted(output);
        }

        return RecipeHelper.hasFurnaceFoodRecipe(input);
      });
      registry.addRecipes(furnaceRecipes, JEIRecipeCategoryOvenStone.UID);
      List<StoneOvenRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.OVEN_STONE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryOvenStone.UID);
    }

    // --- Stone Crucible
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.CRUCIBLE_STONE), JEIRecipeCategoryCrucibleStone.UID);
      registry.handleRecipes(StoneCrucibleRecipe.class, JEIRecipeWrapperCrucibleStone::new, JEIRecipeCategoryCrucibleStone.UID);
      List<StoneCrucibleRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.CRUCIBLE_STONE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryCrucibleStone.UID);
    }

    // --- Stone Mill
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.MILL_STONE), JEIRecipeCategoryMillStone.UID);
      registry.handleRecipes(StoneSawmillRecipe.class, JEIRecipeWrapperMillStone::new, JEIRecipeCategoryMillStone.UID);
      List<StoneSawmillRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.MILL_STONE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryMillStone.UID);
    }

    // --- Stone Kiln
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.KILN_STONE), JEIRecipeCategoryKilnStone.UID);
      registry.handleRecipes(StoneKilnRecipe.class, JEIRecipeWrapperKilnStone::new, JEIRecipeCategoryKilnStone.UID);
      List<StoneMachineRecipeItemInItemOutBase> recipeList = new ArrayList<>(ModuleTechMachine.Registries.KILN_STONE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryKilnStone.UID);
    }
  }

  private static List<JEIRecipeWrapperOvenStone> getFurnaceRecipesForStoneOven(Predicate<ItemStack> filter) {

    FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
    Map<ItemStack, ItemStack> smeltingMap = furnaceRecipes.getSmeltingList();

    List<JEIRecipeWrapperOvenStone> recipes = new ArrayList<>();

    for (Map.Entry<ItemStack, ItemStack> entry : smeltingMap.entrySet()) {

      ItemStack input = entry.getKey();

      if (!filter.test(input)) {
        continue;
      }

      ItemStack output = entry.getValue();
      recipes.add(new JEIRecipeWrapperOvenStone(Ingredient.fromStacks(input), output));
    }

    return recipes;
  }

}
