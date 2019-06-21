package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.category.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInItemOutBase;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        new JEIRecipeCategoryStoneCrucible(guiHelper),

        new JEIRecipeCategoryBrickKiln(guiHelper),
        new JEIRecipeCategoryBrickSawmill(guiHelper),
        new JEIRecipeCategoryBrickOven(guiHelper),
        new JEIRecipeCategoryBrickCrucible(guiHelper),

        new JEIRecipeCategoryMechanicalCompactingBin(guiHelper)
    );
  }

  @Override
  public void register(IModRegistry registry) {

    // --- Stone Oven
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.STONE_OVEN), JEIRecipeCategoryStoneOven.UID);
      registry.handleRecipes(StoneOvenRecipe.class, JEIRecipeWrapperOven::new, JEIRecipeCategoryStoneOven.UID);
      List<JEIRecipeWrapperOven> furnaceRecipes = PluginJEI.getFurnaceRecipesForOven(input -> {

        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);

        if (StoneOvenRecipe.hasWhitelist()) {
          return StoneOvenRecipe.isWhitelisted(output);

        } else if (StoneOvenRecipe.hasBlacklist()) {
          return !StoneOvenRecipe.isBlacklisted(output);
        }

        return RecipeHelper.hasFurnaceFoodRecipe(input);
      }, ModuleTechMachineConfig.STONE_OVEN.COOK_TIME_TICKS);
      registry.addRecipes(furnaceRecipes, JEIRecipeCategoryStoneOven.UID);
      List<StoneOvenRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.STONE_OVEN_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryStoneOven.UID);
    }

    // --- Brick Oven
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.BRICK_OVEN), JEIRecipeCategoryBrickOven.UID);
      registry.handleRecipes(BrickOvenRecipe.class, JEIRecipeWrapperOven::new, JEIRecipeCategoryBrickOven.UID);
      List<JEIRecipeWrapperOven> furnaceRecipes = PluginJEI.getFurnaceRecipesForOven(input -> {

        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);

        if (BrickOvenRecipe.hasWhitelist()) {
          return BrickOvenRecipe.isWhitelisted(output);

        } else if (BrickOvenRecipe.hasBlacklist()) {
          return !BrickOvenRecipe.isBlacklisted(output);
        }

        return RecipeHelper.hasFurnaceFoodRecipe(input);
      }, ModuleTechMachineConfig.BRICK_OVEN.COOK_TIME_TICKS);
      registry.addRecipes(furnaceRecipes, JEIRecipeCategoryBrickOven.UID);
      List<BrickOvenRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.BRICK_OVEN_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryBrickOven.UID);
    }

    // --- Mechanical Compacting Bin
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.MECHANICAL_COMPACTING_BIN), JEIRecipeCategoryMechanicalCompactingBin.UID);
      registry.handleRecipes(MechanicalCompactingBinRecipe.class, JEIRecipeWrapperMechanicalCompactingBin::new, JEIRecipeCategoryMechanicalCompactingBin.UID);
      List<MechanicalCompactingBinRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.MECHANICAL_COMPACTING_BIN_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryMechanicalCompactingBin.UID);
    }

    // --- Stone Crucible
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.STONE_CRUCIBLE), JEIRecipeCategoryStoneCrucible.UID);
      registry.handleRecipes(StoneCrucibleRecipe.class, JEIRecipeWrapperCrucible::new, JEIRecipeCategoryStoneCrucible.UID);
      List<StoneCrucibleRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.STONE_CRUCIBLE_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryStoneCrucible.UID);
    }

    // --- Brick Crucible
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.BRICK_CRUCIBLE), JEIRecipeCategoryBrickCrucible.UID);
      registry.handleRecipes(BrickCrucibleRecipe.class, JEIRecipeWrapperCrucible::new, JEIRecipeCategoryBrickCrucible.UID);
      List<BrickCrucibleRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.BRICK_CRUCIBLE_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryBrickCrucible.UID);
    }

    // --- Stone Sawmill
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.STONE_SAWMILL), JEIRecipeCategoryStoneSawmill.UID);
      registry.handleRecipes(StoneSawmillRecipe.class, JEIRecipeWrapperSawmill::new, JEIRecipeCategoryStoneSawmill.UID);
      List<StoneSawmillRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.STONE_SAWMILL_RECIPES.getValuesCollection());

      String[] validBladeStrings = ModuleTechMachineConfig.STONE_SAWMILL.SAWMILL_BLADES;
      List<ItemStack> validBlades = Stream.of(validBladeStrings)
          .map(s -> {
            ResourceLocation resourceLocation = new ResourceLocation(s);
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            return (item == null) ? ItemStack.EMPTY : new ItemStack(item);
          })
          .filter(itemStack -> !itemStack.isEmpty())
          .collect(Collectors.toList());
      List<StoneSawmillRecipe> validRecipeList = recipeList.stream()
          .filter(recipe -> {
            for (ItemStack validBlade : validBlades) {
              if (recipe.getBlade().apply(validBlade)) {
                return true;
              }
            }
            return false;
          })
          .collect(Collectors.toList());

      registry.addRecipes(validRecipeList, JEIRecipeCategoryStoneSawmill.UID);
    }

    // --- Brick Sawmill
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.BRICK_SAWMILL), JEIRecipeCategoryBrickSawmill.UID);
      registry.handleRecipes(BrickSawmillRecipe.class, JEIRecipeWrapperSawmill::new, JEIRecipeCategoryBrickSawmill.UID);
      List<BrickSawmillRecipe> recipeList = new ArrayList<>(ModuleTechMachine.Registries.BRICK_SAWMILL_RECIPES.getValuesCollection());

      String[] validBladeStrings = ModuleTechMachineConfig.BRICK_SAWMILL.SAWMILL_BLADES;
      List<ItemStack> validBlades = Stream.of(validBladeStrings)
          .map(s -> {
            ResourceLocation resourceLocation = new ResourceLocation(s);
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            return (item == null) ? ItemStack.EMPTY : new ItemStack(item);
          })
          .filter(itemStack -> !itemStack.isEmpty())
          .collect(Collectors.toList());
      List<BrickSawmillRecipe> validRecipeList = recipeList.stream()
          .filter(recipe -> {
            for (ItemStack validBlade : validBlades) {
              if (recipe.getBlade().apply(validBlade)) {
                return true;
              }
            }
            return false;
          })
          .collect(Collectors.toList());

      registry.addRecipes(validRecipeList, JEIRecipeCategoryBrickSawmill.UID);
    }

    // --- Stone Kiln
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.STONE_KILN), JEIRecipeCategoryStoneKiln.UID);
      registry.handleRecipes(StoneKilnRecipe.class, JEIRecipeWrapperKiln::new, JEIRecipeCategoryStoneKiln.UID);
      List<MachineRecipeItemInItemOutBase> recipeList = new ArrayList<>(ModuleTechMachine.Registries.STONE_KILN_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryStoneKiln.UID);
    }

    // --- Brick Kiln
    {
      registry.addRecipeCatalyst(new ItemStack(ModuleTechMachine.Blocks.BRICK_KILN), JEIRecipeCategoryBrickKiln.UID);
      registry.handleRecipes(BrickKilnRecipe.class, JEIRecipeWrapperKiln::new, JEIRecipeCategoryBrickKiln.UID);
      List<MachineRecipeItemInItemOutBase> recipeList = new ArrayList<>(ModuleTechMachine.Registries.BRICK_KILN_RECIPES.getValuesCollection());
      registry.addRecipes(recipeList, JEIRecipeCategoryBrickKiln.UID);
    }
  }

  private static List<JEIRecipeWrapperOven> getFurnaceRecipesForOven(Predicate<ItemStack> filter, int cookTimeTicks) {

    FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
    Map<ItemStack, ItemStack> smeltingMap = furnaceRecipes.getSmeltingList();

    List<JEIRecipeWrapperOven> recipes = new ArrayList<>();

    for (Map.Entry<ItemStack, ItemStack> entry : smeltingMap.entrySet()) {

      ItemStack input = entry.getKey();

      if (!filter.test(input)) {
        continue;
      }

      ItemStack output = entry.getValue();
      recipes.add(new JEIRecipeWrapperOven(Ingredient.fromStacks(input), output, cookTimeTicks));
    }

    return recipes;
  }

}
