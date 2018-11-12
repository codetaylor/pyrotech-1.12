package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnBrickRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.PitBurnRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
        new JEIRecipeCategoryKilnPit(guiHelper),
        new JEIRecipeCategoryKilnBrick(guiHelper),
        new JEIRecipeCategoryPitBurn(guiHelper),
        new JEIRecipeCategoryRefractoryBurn(guiHelper),
        new JEIRecipeCategoryDryingRack(guiHelper)
    );
  }

  @Override
  public void register(IModRegistry registry) {

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

    registry.getJeiHelpers().getIngredientBlacklist()
        .addIngredientToBlacklist(new ItemStack(Item.getItemFromBlock(ModuleBlocks.CAMPFIRE)));

    // --- Drying Rack
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.DRYING_RACK), JEIRecipeCategoryUid.DRYING);
      registry.handleRecipes(DryingRackRecipe.class, JEIRecipeWrapperDryingRack::new, JEIRecipeCategoryUid.DRYING);
      List<DryingRackRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.DRYING_RACK_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.DRYING);
    }

    // --- Pit Kiln
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.KILN_PIT), JEIRecipeCategoryUid.PIT_KILN);
      registry.handleRecipes(KilnPitRecipe.class, JEIRecipeWrapperKilnPit::new, JEIRecipeCategoryUid.PIT_KILN);
      List<KilnPitRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.KILN_PIT_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.PIT_KILN);
    }

    // --- Brick Kiln
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.KILN_BRICK), JEIRecipeCategoryUid.BRICK_KILN);
      registry.handleRecipes(KilnBrickRecipe.class, JEIRecipeWrapperKilnBrick::new, JEIRecipeCategoryUid.BRICK_KILN);
      List<KilnBrickRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.KILN_BRICK_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.BRICK_KILN);
    }

    // --- Pit Burn
    {
      registry.addRecipeCatalyst(new ItemStack(Blocks.DIRT), JEIRecipeCategoryUid.PIT_BURN);
      registry.handleRecipes(PitBurnRecipe.class, JEIRecipeWrapperPitBurn::new, JEIRecipeCategoryUid.PIT_BURN);
      List<PitBurnRecipe> recipeList = ModulePyrotechRegistries.BURN_RECIPE.getValuesCollection()
          .stream()
          .filter(burnRecipe -> !burnRecipe.requiresRefractoryBlocks())
          .collect(Collectors.toList());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.PIT_BURN);
    }

    // --- Refractory Burn
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.REFRACTORY_BRICK), JEIRecipeCategoryUid.REFRACTORY_BURN);
      registry.handleRecipes(
          PitBurnRecipe.class,
          JEIRecipeWrapperRefractoryBurn::new,
          JEIRecipeCategoryUid.REFRACTORY_BURN
      );
      List<PitBurnRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.BURN_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.REFRACTORY_BURN);
    }
  }
}
