package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.category.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.*;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PluginJEI
    implements IModPlugin {

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {

    IJeiHelpers jeiHelpers = registry.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

    registry.addRecipeCategories(
        new JEIRecipeCategoryKilnPit(guiHelper),
        new JEIRecipeCategoryKilnStone(guiHelper),
        new JEIRecipeCategoryPitBurn(guiHelper),
        new JEIRecipeCategoryRefractoryBurn(guiHelper),
        new JEIRecipeCategoryDryingRack(guiHelper),
        new JEIRecipeCategoryDryingRackCrude(guiHelper),
        new JEIRecipeCategoryChoppingBlock(guiHelper),
        new JEIRecipeCategoryGraniteAnvil(guiHelper),
        new JEIRecipeCategoryMillStone(guiHelper),
        new JEIRecipeCategoryCompactingBin(guiHelper),
        new JEIRecipeCategoryCampfire(guiHelper),
        new JEIRecipeCategoryOvenStone(guiHelper),
        new JEIRecipeCategoryWorktable(guiHelper),
        new JEIRecipeCategoryCrucibleStone(guiHelper),
        new JEIRecipeCategorySoakingPot(guiHelper)
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

    // --- Crafting Catalyst

    //registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.WORKTABLE), VanillaRecipeCategoryUid.CRAFTING);

    // --- Blacklist Ingredients

    IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
    blacklist.addIngredientToBlacklist(new ItemStack(ModuleItems.TONGS_FULL));
    //blacklist.addIngredientToBlacklist(new ItemStack(Item.getItemFromBlock(ModuleBlocks.CAMPFIRE)));

    RecipeItemParser parser = new RecipeItemParser();

    for (String itemString : ModulePyrotechConfig.CLIENT.JEI_BLACKLIST) {

      try {
        ParseResult result = parser.parse(itemString);
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(result.getDomain(), result.getPath()));

        if (item != null) {
          blacklist.addIngredientToBlacklist(new ItemStack(item, 1, result.getMeta()));
        }

      } catch (MalformedRecipeItemException e) {
        ModulePyrotech.LOGGER.error("", e);
      }

    }

    // --- Worktable
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.WORKTABLE), JEIRecipeCategoryUid.WORKTABLE);
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.WORKTABLE_STONE), JEIRecipeCategoryUid.WORKTABLE);
      registry.handleRecipes(ShapedOreRecipe.class, recipe -> new ShapedOreRecipeWrapper(jeiHelpers, recipe), JEIRecipeCategoryUid.WORKTABLE);
      registry.handleRecipes(ShapedRecipes.class, recipe -> new ShapedRecipesWrapper(jeiHelpers, recipe), JEIRecipeCategoryUid.WORKTABLE);
      registry.handleRecipes(ShapelessOreRecipe.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), JEIRecipeCategoryUid.WORKTABLE);
      registry.handleRecipes(ShapelessRecipes.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), JEIRecipeCategoryUid.WORKTABLE);
      registry.handleRecipes(WorktableRecipe.class, new WorktableRecipeHandler(jeiHelpers), JEIRecipeCategoryUid.WORKTABLE);
      List<IRecipe> vanillaRecipes = CraftingRecipeChecker.getValidRecipes(jeiHelpers)
          .stream()
          .filter(recipe -> {
            ResourceLocation resourceLocation = recipe.getRegistryName();

            if (WorktableRecipe.hasWhitelist()) {
              return WorktableRecipe.isWhitelisted(resourceLocation);

            } else if (WorktableRecipe.hasBlacklist()) {
              return !WorktableRecipe.isBlacklisted(resourceLocation);
            }

            return true;
          })
          .collect(Collectors.toList());
      registry.addRecipes(vanillaRecipes, JEIRecipeCategoryUid.WORKTABLE);

      ResourceLocation resourceLocation = new ResourceLocation("minecraft:tipped_arrow");

      if (WorktableRecipe.hasWhitelist()) {

        if (WorktableRecipe.isWhitelisted(resourceLocation)) {
          registry.addRecipes(TippedArrowRecipeMaker.getTippedArrowRecipes(), JEIRecipeCategoryUid.WORKTABLE);
        }

      } else if (WorktableRecipe.hasBlacklist()) {

        if (!WorktableRecipe.isBlacklisted(resourceLocation)) {
          registry.addRecipes(TippedArrowRecipeMaker.getTippedArrowRecipes(), JEIRecipeCategoryUid.WORKTABLE);
        }

      } else {
        registry.addRecipes(TippedArrowRecipeMaker.getTippedArrowRecipes(), JEIRecipeCategoryUid.WORKTABLE);
      }

      List<WorktableRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.WORKTABLE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.WORKTABLE);
    }

    // --- Stone Oven
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.OVEN_STONE), JEIRecipeCategoryUid.STONE_OVEN);
      registry.handleRecipes(OvenStoneRecipe.class, JEIRecipeWrapperOvenStone::new, JEIRecipeCategoryUid.STONE_OVEN);
      List<JEIRecipeWrapperOvenStone> furnaceRecipes = PluginJEI.getFurnaceRecipesForStoneOven(input -> {

        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);

        if (OvenStoneRecipe.hasWhitelist()) {
          return OvenStoneRecipe.isWhitelisted(output);

        } else if (OvenStoneRecipe.hasBlacklist()) {
          return !OvenStoneRecipe.isBlacklisted(output);
        }

        return RecipeHelper.hasFurnaceFoodRecipe(input);
      });
      registry.addRecipes(furnaceRecipes, JEIRecipeCategoryUid.STONE_OVEN);
      List<OvenStoneRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.OVEN_STONE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.STONE_OVEN);
    }

    // --- Campfire
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.CAMPFIRE), JEIRecipeCategoryUid.CAMPFIRE);
      registry.handleRecipes(CampfireRecipe.class, JEIRecipeWrapperCampfire::new, JEIRecipeCategoryUid.CAMPFIRE);
      List<JEIRecipeWrapperCampfire> furnaceRecipes = PluginJEI.getFurnaceRecipesForCampfire(input -> {

        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);

        if (CampfireRecipe.hasWhitelist()) {
          return CampfireRecipe.isWhitelisted(output);

        } else if (CampfireRecipe.hasBlacklist()) {
          return !CampfireRecipe.isBlacklisted(output);
        }

        return RecipeHelper.hasFurnaceFoodRecipe(input);
      });
      registry.addRecipes(furnaceRecipes, JEIRecipeCategoryUid.CAMPFIRE);
      List<CampfireRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.CAMPFIRE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.CAMPFIRE);
    }

    // --- Soaking Pot
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.SOAKING_POT), JEIRecipeCategoryUid.SOAKING_POT);
      registry.handleRecipes(SoakingPotRecipe.class, JEIRecipeWrapperSoakingPot::new, JEIRecipeCategoryUid.SOAKING_POT);
      List<SoakingPotRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.SOAKING_POT_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.SOAKING_POT);
    }

    // --- Stone Crucible
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.CRUCIBLE_STONE), JEIRecipeCategoryUid.STONE_CRUCIBLE);
      registry.handleRecipes(CrucibleStoneRecipe.class, JEIRecipeWrapperCrucibleStone::new, JEIRecipeCategoryUid.STONE_CRUCIBLE);
      List<CrucibleStoneRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.CRUCIBLE_STONE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.STONE_CRUCIBLE);
    }

    // --- Compacting Bin
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.COMPACTING_BIN), JEIRecipeCategoryUid.COMPACTING_BIN);
      registry.handleRecipes(CompactingBinRecipe.class, JEIRecipeWrapperCompactingBin::new, JEIRecipeCategoryUid.COMPACTING_BIN);
      List<CompactingBinRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.COMPACTING_BIN_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.COMPACTING_BIN);
    }

    // --- Stone Mill
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.MILL_STONE), JEIRecipeCategoryUid.STONE_MILL);
      registry.handleRecipes(MillStoneRecipe.class, JEIRecipeWrapperMillStone::new, JEIRecipeCategoryUid.STONE_MILL);
      List<MillStoneRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.MILL_STONE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.STONE_MILL);
    }

    // --- Granite Anvil
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.GRANITE_ANVIL), JEIRecipeCategoryUid.GRANITE_ANVIL);
      registry.handleRecipes(GraniteAnvilRecipe.class, JEIRecipeWrapperGraniteAnvil::new, JEIRecipeCategoryUid.GRANITE_ANVIL);
      List<GraniteAnvilRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.GRANITE_ANVIL_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.GRANITE_ANVIL);
    }

    // --- Chopping Block
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.CHOPPING_BLOCK), JEIRecipeCategoryUid.CHOPPING);
      registry.handleRecipes(ChoppingBlockRecipe.class, JEIRecipeWrapperChoppingBlock::new, JEIRecipeCategoryUid.CHOPPING);
      List<ChoppingBlockRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.CHOPPING_BLOCK_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.CHOPPING);
    }

    // --- Crude Drying Rack
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.DRYING_RACK, 1, BlockDryingRack.EnumType.CRUDE.getMeta()), JEIRecipeCategoryUid.DRYING_CRUDE);
      registry.handleRecipes(DryingRackCrudeRecipe.class, JEIRecipeWrapperDryingRackCrude::new, JEIRecipeCategoryUid.DRYING_CRUDE);
      List<DryingRackCrudeRecipe> recipeList = new ArrayList<>(ModulePyrotechRegistries.DRYING_RACK_CRUDE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.DRYING_CRUDE);
    }

    // --- Drying Rack
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.DRYING_RACK, 1, BlockDryingRack.EnumType.NORMAL.getMeta()), JEIRecipeCategoryUid.DRYING);
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

    // --- Stone Kiln
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.KILN_STONE), JEIRecipeCategoryUid.STONE_KILN);
      registry.handleRecipes(KilnStoneRecipe.class, JEIRecipeWrapperKilnStone::new, JEIRecipeCategoryUid.STONE_KILN);
      List<StoneMachineRecipeItemInItemOutBase> recipeList = new ArrayList<>(ModulePyrotechRegistries.KILN_STONE_RECIPE.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryUid.STONE_KILN);
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
      recipes.add(new JEIRecipeWrapperCampfire(Ingredient.fromStacks(input), output));
    }

    return recipes;
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

  private static class WorktableRecipeHandler
      implements IRecipeWrapperFactory<WorktableRecipe> {

    private final IJeiHelpers jeiHelpers;

    public WorktableRecipeHandler(IJeiHelpers jeiHelpers) {

      this.jeiHelpers = jeiHelpers;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull WorktableRecipe recipe) {

      IRecipe wrappedRecipe = recipe.getRecipe();

      if (wrappedRecipe instanceof IShapedRecipe) {
        return new ShapedRecipeWrapper(this.jeiHelpers, (IShapedRecipe) wrappedRecipe);

      } else {
        return new ShapelessRecipeWrapper<>(this.jeiHelpers, wrappedRecipe);
      }
    }
  }

  private static class ShapedRecipeWrapper
      extends ShapelessRecipeWrapper<IShapedRecipe>
      implements IShapedCraftingRecipeWrapper {

    public ShapedRecipeWrapper(IJeiHelpers jeiHelpers, IShapedRecipe recipe) {

      super(jeiHelpers, recipe);
    }

    @Override
    public int getWidth() {

      return this.recipe.getRecipeWidth();
    }

    @Override
    public int getHeight() {

      return this.recipe.getRecipeHeight();
    }
  }
}
