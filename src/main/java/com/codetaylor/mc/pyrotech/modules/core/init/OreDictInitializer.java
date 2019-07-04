package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.pyrotech.library.JsonInitializer;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public final class OreDictInitializer {

  public static void onRegister(Registry registry, File configurationDirectory) {

    final Path configurationPath = Paths.get(configurationDirectory.toString(), ModuleCore.MOD_ID);

    registry.registerItemRegistrationStrategy(forgeRegistry -> {

      JsonOreDict jsonOreDict = JsonInitializer.generateAndReadCustom(
          configurationPath,
          "core.OreDict-Generated.json",
          "core.OreDict-Custom.json",
          JsonOreDict.class,
          JsonOreDict::generate,
          ModuleCore.LOGGER
      );

      if (jsonOreDict == null) {
        return;
      }

      for (Map.Entry<String, String[]> entry : jsonOreDict.getOreDict().entrySet()) {

        for (String itemString : entry.getValue()) {

          try {
            ParseResult parseResult = RecipeItemParser.INSTANCE.parse(itemString);
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parseResult.getDomain(), parseResult.getPath()));

            if (item != null) {
              OreDictHelper.register(entry.getKey(), new ItemStack(item, 1, parseResult.getMeta()));
            }

          } catch (MalformedRecipeItemException e) {
            ModuleCore.LOGGER.error("Unable to parse item string: " + itemString, e);
          }
        }
      }
    });
  }

  private OreDictInitializer() {
    //
  }
}
