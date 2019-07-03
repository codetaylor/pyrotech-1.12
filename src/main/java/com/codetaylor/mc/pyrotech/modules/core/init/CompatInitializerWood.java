package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.pyrotech.library.JsonInitializer;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public final class CompatInitializerWood {

  public static void create(Path configurationPath) {

    JsonInitializer.create(
        configurationPath.resolve(ModuleCore.MOD_ID),
        "core.compat.Wood-Generated.json",
        "core.compat.Wood-Custom.json",
        () -> CompatInitializerWood.createGeneratedData(new WoodCompatData()),
        ModuleCore.LOGGER
    );
  }

  @Nullable
  public static WoodCompatData read(Path configurationPath) {

    return JsonInitializer.read(
        configurationPath.resolve(ModuleTechMachine.MOD_ID),
        "core.compat.Wood-Custom.json",
        CompatInitializerWood.WoodCompatData.class,
        null // prevent logging errors
    );
  }

  private static WoodCompatData createGeneratedData(WoodCompatData data) {

    Collection<IRecipe> recipes = ForgeRegistries.RECIPES.getValuesCollection();

    recipeSearch:
    for (IRecipe recipe : recipes) {
      NonNullList<Ingredient> ingredients = recipe.getIngredients();

      if (ingredients.size() == 1) {

        Ingredient ingredient = ingredients.get(0);

        ItemStack[] matchingStacks = ingredient.getMatchingStacks();

        if (matchingStacks.length != 1) {
          continue;
        }

        ItemStack input = matchingStacks[0];

        if (OreDictHelper.contains("logWood", input)) {
          ItemStack output = recipe.getRecipeOutput();

          if (OreDictHelper.contains("plankWood", output)) {
            CompatInitializerWood.generateEntry(data, recipe, input, output);
          }

        }

      } else if (ingredients.size() == 3) {

        ItemStack input = null;

        for (int i = 0; i < 3; i++) {
          Ingredient ingredient = ingredients.get(i);
          ItemStack[] matchingStacks = ingredient.getMatchingStacks();

          if (matchingStacks.length != 1) {
            continue recipeSearch;
          }

          if (!OreDictHelper.contains("plankWood", matchingStacks[0])) {
            continue recipeSearch;
          }

          if (input == null) {
            input = matchingStacks[0];

          } else if (input.getItem() != matchingStacks[0].getItem()
              || input.getMetadata() != matchingStacks[0].getMetadata()) {
            continue recipeSearch;
          }
        }

        ItemStack output = recipe.getRecipeOutput();

        if (OreDictHelper.contains("slabWood", output)) {
          CompatInitializerWood.generateEntry(data, recipe, input, output);
        }
      }
    }

    return data;
  }

  private static void generateEntry(WoodCompatData data, IRecipe recipe, ItemStack input, ItemStack output) {

    ResourceLocation recipeRegistryName = recipe.getRegistryName();

    if (recipeRegistryName == null) {
      return;
    }

    ResourceLocation inputRegistryName = input.getItem().getRegistryName();

    if (inputRegistryName == null) {
      return;
    }

    String inputResourcePath = inputRegistryName.getResourcePath();
    String inputResourceDomain = inputRegistryName.getResourceDomain();

    ResourceLocation outputRegistryName = output.getItem().getRegistryName();

    if (outputRegistryName == null) {
      return;
    }

    String outputResourcePath = outputRegistryName.getResourcePath();
    String outputResourceDomain = outputRegistryName.getResourceDomain();

    String inputString = inputResourceDomain + ":" + inputResourcePath + ":" + input.getMetadata();

    if (data.entries.containsKey(inputString)) {
      ModuleCore.LOGGER.warn("Duplicate recipe input item found, skipping recipe: " + recipeRegistryName);
      return;
    }

    data.entries.put(
        inputString,
        outputResourceDomain + ":" + outputResourcePath + ":" + output.getMetadata()
    );
  }

  public static class WoodCompatData {

    private String[] __comments = {
        "WARNING: All changes should be made to the file with the name Custom",
        "in the title. Changes made to the Generated file will be overwritten.",
        "",
        "This file defines input and output pairs for auto-generating recipes",
        "for the Chopping Block.",
        "",
        "Entries are in the format (input): (output)",
        "Entry item strings are in the format: (domain):(path):(meta)"
    };

    public Map<String, String> entries = new TreeMap<>();
  }

  private CompatInitializerWood() {
    //
  }

}
