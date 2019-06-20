package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.pyrotech.modules.core.init.WoodCompatInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.ChoppingBlockRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

public class ChoppingBlockRecipesAdd {

  public static final RecipeItemParser RECIPE_ITEM_PARSER = new RecipeItemParser();

  public static void applyCompatRecipes(Path configurationPath, IForgeRegistry<ChoppingBlockRecipe> registry) {

    WoodCompatInitializer.WoodCompatData woodCompatData = WoodCompatInitializer.read(configurationPath);

    if (woodCompatData == null) {
      return;
    }

    Iterator<Map.Entry<String, Map<String, String>>> iterator = woodCompatData.mods.entrySet().iterator();

    for (; iterator.hasNext(); ) {
      Map.Entry<String, Map<String, String>> modEntry = iterator.next();
      String modId = modEntry.getKey();

      if (!Loader.isModLoaded(modId)) {
        continue;
      }

      Map<String, String> woodCompatModData = modEntry.getValue();
      ChoppingBlockRecipesAdd.createRecipe(registry, modId, woodCompatModData);
    }
  }

  protected static void createRecipe(IForgeRegistry<ChoppingBlockRecipe> registry, String modId, Map<String, String> stringMap) {

    Iterator<Map.Entry<String, String>> planksIterator = stringMap.entrySet().iterator();

    for (; planksIterator.hasNext(); ) {
      Map.Entry<String, String> planksEntry = planksIterator.next();

      ItemStack inputItemStack = ChoppingBlockRecipesAdd.getItemStack(modId, planksEntry.getKey());
      ItemStack outputItemStack = ChoppingBlockRecipesAdd.getItemStack(modId, planksEntry.getValue());

      if (inputItemStack.isEmpty()
          || outputItemStack.isEmpty()) {
        continue;
      }

      // create a recipe

      Item outputItem = outputItemStack.getItem();
      ResourceLocation registryName = outputItem.getRegistryName();

      if (registryName == null) {
        ModuleTechBasic.LOGGER.error("Item missing registry name: " + outputItem);
        continue;
      }

      String recipeName = registryName.getResourceDomain() + "_" + registryName.getResourcePath() + "_" + outputItemStack.getMetadata();

      registry.register(new ChoppingBlockRecipe(
          outputItemStack,
          Ingredient.fromStacks(inputItemStack)
      ).setRegistryName(ModuleTechBasic.MOD_ID, recipeName));
    }
  }

  protected static ItemStack getItemStack(String modId, String key) {

    String inputString = key;

    if (!inputString.startsWith(modId)) {
      inputString = modId + ":" + inputString;
    }

    try {
      ParseResult parse = RECIPE_ITEM_PARSER.parse(inputString);

      Item inputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parse.getDomain(), parse.getPath()));

      if (inputItem == null) {
        ModuleTechBasic.LOGGER.error("Can't find registered item for: " + inputString);
        return ItemStack.EMPTY;
      }

      return new ItemStack(inputItem, 1, parse.getMeta());

    } catch (MalformedRecipeItemException e) {
      ModuleTechBasic.LOGGER.error("", e);
      return ItemStack.EMPTY;
    }
  }
}