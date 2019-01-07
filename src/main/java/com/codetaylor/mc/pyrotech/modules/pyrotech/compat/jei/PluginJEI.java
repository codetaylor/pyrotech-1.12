package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.category.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.*;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
        new JEIRecipeCategoryKilnStone(guiHelper),
        new JEIRecipeCategoryPitBurn(guiHelper),
        new JEIRecipeCategoryRefractoryBurn(guiHelper),
        new JEIRecipeCategoryDryingRack(guiHelper),
        new JEIRecipeCategoryDryingRackCrude(guiHelper),
        new JEIRecipeCategoryChoppingBlock(guiHelper),
        new JEIRecipeCategoryGraniteAnvil(guiHelper),
        new JEIRecipeCategoryMillStone(guiHelper),
        new JEIRecipeCategoryCompactingBin(guiHelper)
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

    // --- Crafting Catalyst

    registry.addRecipeCatalyst(new ItemStack(ModuleBlocks.WORKTABLE), VanillaRecipeCategoryUid.CRAFTING);

    // --- Blacklist Ingredients

    IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
    blacklist.addIngredientToBlacklist(new ItemStack(Item.getItemFromBlock(ModuleBlocks.CAMPFIRE)));

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
}
