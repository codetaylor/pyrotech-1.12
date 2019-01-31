package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.category.JEIRecipeCategoryPitBurn;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.category.JEIRecipeCategoryRefractoryBurn;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.wrapper.JEIRecipeWrapperPitBurn;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.wrapper.JEIRecipeWrapperRefractoryBurn;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PluginJEI
    implements IModPlugin {

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {

    IJeiHelpers jeiHelpers = registry.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

    registry.addRecipeCategories(
        new JEIRecipeCategoryPitBurn(guiHelper),
        new JEIRecipeCategoryRefractoryBurn(guiHelper)
    );
  }

  @Override
  public void register(IModRegistry registry) {

    final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
    final IJeiHelpers jeiHelpers = registry.getJeiHelpers();

    // Leave as an example in case I decide to add info later.
    /*
    {
      List<ItemStack> outputList = Registries.BURN_RECIPE.getValuesCollection()
          .stream()
          .filter(burnRecipe -> !burnRecipe.requiresRefractoryBlocks())
          .map(PitBurnRecipe::getOutput)
          .collect(Collectors.toList());
      registry.addIngredientInfo(outputList, ItemStack.class, "gui." + ModuleCharcoal.MOD_ID + ".jei.info.burn.pit");
    }
    */

    // --- Pit Burn
    {
      registry.addRecipeCatalyst(new ItemStack(Blocks.DIRT), JEIRecipeCategoryPitBurn.UID);
      registry.handleRecipes(PitBurnRecipe.class, JEIRecipeWrapperPitBurn::new, JEIRecipeCategoryPitBurn.UID);
      List<PitBurnRecipe> recipeList = ModuleTechRefractory.Registries.BURN_RECIPE.getValuesCollection()
          .stream()
          .filter(burnRecipe -> !burnRecipe.requiresRefractoryBlocks())
          .collect(Collectors.toList());
      registry.addRecipes(recipeList, JEIRecipeCategoryPitBurn.UID);
    }

    // --- Refractory Burn
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleCore.Blocks.REFRACTORY_BRICK), JEIRecipeCategoryRefractoryBurn.UID);
      registry.handleRecipes(
          PitBurnRecipe.class,
          JEIRecipeWrapperRefractoryBurn::new,
          JEIRecipeCategoryRefractoryBurn.UID
      );
      List<PitBurnRecipe> recipeList = new ArrayList<>(ModuleTechRefractory.Registries.BURN_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryRefractoryBurn.UID);
    }
  }

}
