package com.codetaylor.mc.pyrotech.modules.core.plugin.jei;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PluginJEI
    implements IModPlugin {

  @Override
  public void register(IModRegistry registry) {

    RecipeItemParser parser = new RecipeItemParser();
    IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();

    for (String itemString : ModuleCoreConfig.CLIENT.JEI_BLACKLIST) {

      try {
        ParseResult result = parser.parse(itemString);
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(result.getDomain(), result.getPath()));

        if (item != null) {
          blacklist.addIngredientToBlacklist(new ItemStack(item, 1, result.getMeta()));
        }

      } catch (MalformedRecipeItemException e) {
        ModuleCore.LOGGER.error("", e);
      }
    }
  }
}
