package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.jei;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.WitherForgeRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PluginJEI
    implements IModPlugin {

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {

    IJeiHelpers jeiHelpers = registry.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

    registry.addRecipeCategories(
        new JEIRecipeCategoryBloomery(guiHelper, JEIRecipeCategoryBloomery.UID_BLOOMERY, "gui." + ModuleTechBloomery.MOD_ID + ".jei.category.bloomery"),
        new JEIRecipeCategoryBloomery(guiHelper, JEIRecipeCategoryBloomery.UID_WITHER_FORGE, "gui." + ModuleTechBloomery.MOD_ID + ".jei.category.wither.forge")
    );
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    subtypeRegistry.registerSubtypeInterpreter(
        Item.getItemFromBlock(ModuleTechBloomery.Blocks.BLOOM),
        BloomHelper::getCompareString
    );
  }

  @Override
  public void register(IModRegistry registry) {

    // --- Blacklist Ingredients

    IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleTechBloomery.Items.TONGS_STONE_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleTechBloomery.Items.TONGS_FLINT_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleTechBloomery.Items.TONGS_BONE_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleTechBloomery.Items.TONGS_IRON_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleTechBloomery.Items.TONGS_GOLD_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleTechBloomery.Items.TONGS_DIAMOND_FULL));
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleTechBloomery.Items.TONGS_OBSIDIAN_FULL));

    blacklist.addIngredientToBlacklist(new ItemStack(ModuleTechBloomery.Blocks.BLOOM));

    // --- Bloomery
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBloomery.Blocks.BLOOMERY), JEIRecipeCategoryBloomery.UID_BLOOMERY);
      registry.handleRecipes(BloomeryRecipe.class, JEIRecipeWrapperBloomery::new, JEIRecipeCategoryBloomery.UID_BLOOMERY);
      List<BloomeryRecipe> recipeList = new ArrayList<>(ModuleTechBloomery.Registries.BLOOMERY_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryBloomery.UID_BLOOMERY);
    }

    // --- Wither Forge
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBloomery.Blocks.WITHER_FORGE), JEIRecipeCategoryBloomery.UID_WITHER_FORGE);
      registry.handleRecipes(WitherForgeRecipe.class, JEIRecipeWrapperBloomery::new, JEIRecipeCategoryBloomery.UID_WITHER_FORGE);
      List<WitherForgeRecipe> recipeList = new ArrayList<>(ModuleTechBloomery.Registries.WITHER_FORGE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryBloomery.UID_WITHER_FORGE);
    }
  }

}
