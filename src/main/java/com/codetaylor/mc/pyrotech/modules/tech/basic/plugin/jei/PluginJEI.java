package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.*;
import crafttweaker.mc1120.recipes.MCRecipeBase;
import crafttweaker.mc1120.recipes.MCRecipeShaped;
import crafttweaker.mc1120.recipes.MCRecipeShapeless;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PluginJEI
    implements IModPlugin {

  public static IRecipeRegistry RECIPE_REGISTRY;

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    // Expose the recipe registry for use in the game stages event handler.
    RECIPE_REGISTRY = jeiRuntime.getRecipeRegistry();

    this.hideRecipes();
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {

    IJeiHelpers jeiHelpers = registry.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

    registry.addRecipeCategories(
        new JEIRecipeCategoryKilnPit(guiHelper),
        new JEIRecipeCategoryCrudeDryingRack(guiHelper),
        new JEIRecipeCategoryDryingRack(guiHelper),
        new JEIRecipeCategoryChoppingBlock(guiHelper),
        new JEIRecipeCategoryAnvilGranite(guiHelper),
        new JEIRecipeCategoryAnvilIronclad(guiHelper),
        new JEIRecipeCategoryCompactingBin(guiHelper),
        new JEIRecipeCategoryCampfire(guiHelper),
        new JEIRecipeCategoryWorktable(guiHelper),
        new JEIRecipeCategorySoakingPot(guiHelper)
    );
  }

  @Override
  public void register(IModRegistry registry) {

    final IJeiHelpers jeiHelpers = registry.getJeiHelpers();

    jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModuleTechBasic.Items.MARSHMALLOW_STICK));

    // Pit Kiln Info
    registry.addIngredientInfo(new ItemStack(ModuleTechBasic.Blocks.KILN_PIT), ItemStack.class, "gui.pyrotech.jei.info.pit.kiln");

    // Campfire Info
    registry.addIngredientInfo(new ItemStack(ModuleTechBasic.Blocks.CAMPFIRE), VanillaTypes.ITEM, "gui.pyrotech.jei.info.campfire");

    // --- Worktable
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.WORKTABLE), JEIRecipeCategoryWorktable.UID);
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.WORKTABLE_STONE), JEIRecipeCategoryWorktable.UID);
      registry.handleRecipes(ShapedOreRecipe.class, recipe -> new ShapedOreRecipeWrapper(jeiHelpers, recipe), JEIRecipeCategoryWorktable.UID);
      registry.handleRecipes(ShapedRecipes.class, recipe -> new ShapedRecipesWrapper(jeiHelpers, recipe), JEIRecipeCategoryWorktable.UID);
      registry.handleRecipes(ShapelessOreRecipe.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), JEIRecipeCategoryWorktable.UID);
      registry.handleRecipes(ShapelessRecipes.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), JEIRecipeCategoryWorktable.UID);
      registry.handleRecipes(WorktableRecipe.class, new WorktableRecipeFactory(jeiHelpers), JEIRecipeCategoryWorktable.UID);

      if (Loader.isModLoaded("crafttweaker")) {
        registry.handleRecipes(MCRecipeShapeless.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), JEIRecipeCategoryWorktable.UID);
        registry.handleRecipes(MCRecipeShaped.class, CraftingRecipeWrapperShaped::new, JEIRecipeCategoryWorktable.UID);
      }

      List<IRecipe> vanillaRecipes = CraftingRecipeChecker.getValidRecipes(jeiHelpers)
          .stream()
          .filter(recipe -> {
            ResourceLocation resourceLocation = recipe.getRegistryName();

            if (Loader.isModLoaded("crafttweaker") && recipe instanceof MCRecipeBase) {

              if (!((MCRecipeBase) recipe).isVisible()) {
                return false;
              }

              ((MCRecipeBase) recipe).update();
            }

            if (WorktableRecipe.hasWhitelist()) {
              return WorktableRecipe.isWhitelisted(resourceLocation);

            } else if (WorktableRecipe.hasBlacklist()) {
              return !WorktableRecipe.isBlacklisted(resourceLocation);
            }

            return true;
          })
          .collect(Collectors.toList());
      registry.addRecipes(vanillaRecipes, JEIRecipeCategoryWorktable.UID);

      ResourceLocation resourceLocation = new ResourceLocation("minecraft:tipped_arrow");

      if (WorktableRecipe.hasWhitelist()) {

        if (WorktableRecipe.isWhitelisted(resourceLocation)) {
          registry.addRecipes(TippedArrowRecipeMaker.getTippedArrowRecipes(), JEIRecipeCategoryWorktable.UID);
        }

      } else if (WorktableRecipe.hasBlacklist()) {

        if (!WorktableRecipe.isBlacklisted(resourceLocation)) {
          registry.addRecipes(TippedArrowRecipeMaker.getTippedArrowRecipes(), JEIRecipeCategoryWorktable.UID);
        }

      } else {
        registry.addRecipes(TippedArrowRecipeMaker.getTippedArrowRecipes(), JEIRecipeCategoryWorktable.UID);
      }

      List<WorktableRecipe> recipeList = new ArrayList<>(ModuleTechBasic.Registries.WORKTABLE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryWorktable.UID);
    }

    // --- Campfire
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.CAMPFIRE), JEIRecipeCategoryCampfire.UID);
      registry.handleRecipes(CampfireRecipe.class, JEIRecipeWrapperCampfire::new, JEIRecipeCategoryCampfire.UID);
      List<JEIRecipeWrapperCampfire> furnaceRecipes = PluginJEI.getFurnaceRecipesForCampfire(input -> {

        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);

        if (output.isEmpty()) {
          return false;
        }

        if (CampfireRecipe.hasWhitelist()) {
          return CampfireRecipe.isWhitelisted(output);

        } else if (CampfireRecipe.hasBlacklist()) {
          return !CampfireRecipe.isBlacklisted(output)
              && RecipeHelper.hasFurnaceFoodRecipe(input);
        }

        return RecipeHelper.hasFurnaceFoodRecipe(input);
      });
      registry.addRecipes(furnaceRecipes, JEIRecipeCategoryCampfire.UID);
      List<CampfireRecipe> recipeList = new ArrayList<>(ModuleTechBasic.Registries.CAMPFIRE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryCampfire.UID);
    }

    // --- Soaking Pot
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.SOAKING_POT), JEIRecipeCategorySoakingPot.UID);
      registry.handleRecipes(SoakingPotRecipe.class, JEIRecipeWrapperSoakingPot::new, JEIRecipeCategorySoakingPot.UID);
      List<SoakingPotRecipe> recipeList = new ArrayList<>(ModuleTechBasic.Registries.SOAKING_POT_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategorySoakingPot.UID);
    }

    // --- Compacting Bin
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.COMPACTING_BIN), JEIRecipeCategoryCompactingBin.UID);
      registry.handleRecipes(CompactingBinRecipe.class, JEIRecipeWrapperCompactingBin::new, JEIRecipeCategoryCompactingBin.UID);
      List<CompactingBinRecipe> recipeList = new ArrayList<>(ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryCompactingBin.UID);
    }

    // --- Granite Anvil
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.ANVIL_GRANITE), JEIRecipeCategoryAnvilGranite.UID);
      registry.handleRecipes(AnvilRecipe.class, JEIRecipeWrapperAnvil::new, JEIRecipeCategoryAnvilGranite.UID);
      List<AnvilRecipe> recipeList = ModuleTechBasic.Registries.ANVIL_RECIPE.getValuesCollection().stream()
          .filter(anvilRecipe -> anvilRecipe.isTier(AnvilRecipe.EnumTier.GRANITE))
          .collect(Collectors.toList());
      registry.addRecipes(recipeList, JEIRecipeCategoryAnvilGranite.UID);
    }

    // --- Ironclad Anvil
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.ANVIL_IRON_PLATED), JEIRecipeCategoryAnvilIronclad.UID);
      registry.handleRecipes(AnvilRecipe.class, JEIRecipeWrapperAnvil::new, JEIRecipeCategoryAnvilIronclad.UID);
      List<AnvilRecipe> recipeList = ModuleTechBasic.Registries.ANVIL_RECIPE.getValuesCollection().stream()
          .filter(anvilRecipe -> anvilRecipe.isTier(AnvilRecipe.EnumTier.IRONCLAD))
          .collect(Collectors.toList());
      registry.addRecipes(recipeList, JEIRecipeCategoryAnvilIronclad.UID);
    }

    // --- Chopping Block
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.CHOPPING_BLOCK), JEIRecipeCategoryChoppingBlock.UID);
      registry.handleRecipes(ChoppingBlockRecipe.class, JEIRecipeWrapperChoppingBlock::new, JEIRecipeCategoryChoppingBlock.UID);
      List<ChoppingBlockRecipe> recipeList = new ArrayList<>(ModuleTechBasic.Registries.CHOPPING_BLOCK_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryChoppingBlock.UID);
    }

    // --- Crude Drying Rack
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.DRYING_RACK, 1, BlockDryingRack.EnumType.CRUDE.getMeta()), JEIRecipeCategoryCrudeDryingRack.UID);
      registry.handleRecipes(CrudeDryingRackRecipe.class, JEIRecipeWrapperCrudeDryingRack::new, JEIRecipeCategoryCrudeDryingRack.UID);
      List<CrudeDryingRackRecipe> recipeList = new ArrayList<>(ModuleTechBasic.Registries.CRUDE_DRYING_RACK_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryCrudeDryingRack.UID);
    }

    // --- Drying Rack
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.DRYING_RACK, 1, BlockDryingRack.EnumType.NORMAL.getMeta()), JEIRecipeCategoryDryingRack.UID);
      registry.handleRecipes(DryingRackRecipe.class, JEIRecipeWrapperDryingRack::new, JEIRecipeCategoryDryingRack.UID);
      List<DryingRackRecipe> recipeList = new ArrayList<>(ModuleTechBasic.Registries.DRYING_RACK_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryDryingRack.UID);
    }

    // --- Pit Kiln
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechBasic.Blocks.KILN_PIT), JEIRecipeCategoryKilnPit.UID);
      registry.handleRecipes(KilnPitRecipe.class, JEIRecipeWrapperKilnPit::new, JEIRecipeCategoryKilnPit.UID);
      List<KilnPitRecipe> recipeList = new ArrayList<>(ModuleTechBasic.Registries.KILN_PIT_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryKilnPit.UID);
    }
  }

  private static List<JEIRecipeWrapperCampfire> getFurnaceRecipesForCampfire(Predicate<ItemStack> filter) {

    FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
    Map<ItemStack, ItemStack> smeltingMap = furnaceRecipes.getSmeltingList();

    List<JEIRecipeWrapperCampfire> recipes = new ArrayList<>();

    for (Map.Entry<ItemStack, ItemStack> entry : smeltingMap.entrySet()) {

      ItemStack input = entry.getKey();

      if (!filter.test(input)) {
        continue;
      }

      ItemStack output = entry.getValue();
      recipes.add(new JEIRecipeWrapperCampfire(Ingredient.fromStacks(input), output, ModuleTechBasicConfig.CAMPFIRE.COOK_TIME_TICKS));
    }

    return recipes;
  }

  private static class WorktableRecipeFactory
      implements IRecipeWrapperFactory<WorktableRecipe> {

    private final IJeiHelpers jeiHelpers;

    public WorktableRecipeFactory(IJeiHelpers jeiHelpers) {

      this.jeiHelpers = jeiHelpers;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull WorktableRecipe recipe) {

      IRecipe wrappedRecipe = recipe.getRecipe();

      if (wrappedRecipe instanceof IShapedRecipe) {
        return new JEIRecipeWrapperWorktableShaped(this.jeiHelpers, (IShapedRecipe) wrappedRecipe, recipe.getToolList(), recipe.getToolDamage());

      } else {
        return new JEIRecipeWrapperWorktableShapeless<>(this.jeiHelpers, wrappedRecipe, recipe.getToolList(), recipe.getToolDamage());
      }
    }
  }

  private void hideRecipes() {

    Set<Map.Entry<ResourceLocation, WorktableRecipe>> entries = ModuleTechBasic.Registries.WORKTABLE_RECIPE.getEntries();

    for (Map.Entry<ResourceLocation, WorktableRecipe> entry : entries) {
      WorktableRecipe recipe = entry.getValue();

      if (this.shouldHideWorktableRecipe(recipe)) {
        IRecipeWrapper recipeWrapper = RECIPE_REGISTRY.getRecipeWrapper(recipe, JEIRecipeCategoryWorktable.UID);

        if (recipeWrapper != null) {
          RECIPE_REGISTRY.hideRecipe(recipeWrapper, JEIRecipeCategoryWorktable.UID);
        }
      }
    }
  }

  private boolean shouldHideWorktableRecipe(WorktableRecipe recipe) {

    // If gamestages is loaded, hide all of the staged worktable recipes from JEI.

    return Loader.isModLoaded("gamestages")
        && recipe.getStages() != null;
  }

  public static class CraftingRecipeWrapperShaped
      implements IShapedCraftingRecipeWrapper {

    private final MCRecipeShaped recipe;

    public CraftingRecipeWrapperShaped(MCRecipeShaped recipe) {

      this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {

      ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
      ingredients.setInputLists(VanillaTypes.ITEM, recipe.getIngredients().stream().map(Ingredient::getMatchingStacks).map(Arrays::asList).collect(Collectors.toList()));
    }

    @Override
    public int getWidth() {

      return recipe.getRecipeWidth();
    }

    @Override
    public int getHeight() {

      return recipe.getRecipeHeight();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {

      return recipe.getRegistryName();
    }
  }
}
