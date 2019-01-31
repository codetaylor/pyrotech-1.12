package com.codetaylor.mc.pyrotech.modules.core.init.recipe;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;

import java.util.Iterator;
import java.util.Map;

public class VanillaFurnaceRecipesRemove {

  private static final RecipeItemParser PARSER = new RecipeItemParser();

  public static void apply() {

    for (String itemString : ModuleCoreConfig.RECIPES.VANILLA_FURNACE_REMOVE) {

      try {
        ParseResult parse = PARSER.parse(itemString);

        for (Iterator<Map.Entry<ItemStack, ItemStack>> it = FurnaceRecipes.instance().getSmeltingList().entrySet().iterator(); it.hasNext(); ) {
          Map.Entry<ItemStack, ItemStack> next = it.next();
          ItemStack itemStack = next.getValue();
          Item item = itemStack.getItem();
          ResourceLocation registryName = item.getRegistryName();

          if (registryName == null) {
            ModuleCore.LOGGER.error("Null registry name for item: " + itemStack);
            continue;
          }

          if (registryName.getResourceDomain().equals(parse.getDomain())
              && registryName.getResourcePath().equals(parse.getPath())
              && itemStack.getMetadata() == parse.getMeta()) {
            it.remove();
          }
        }

      } catch (MalformedRecipeItemException e) {
        ModuleCore.LOGGER.error("Unable to remove vanilla furnace recipe for output: " + itemString, e);
      }
    }

  }
}