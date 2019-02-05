package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.pyrotech.library.JsonInitializer;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
          ".core.OreDict-Generated.json",
          ".core.OreDict-Custom.json",
          JsonOreDict.class,
          JsonOreDict::generate,
          ModuleCore.LOGGER
      );

      if (jsonOreDict == null) {
        return;
      }

      RecipeItemParser parser = new RecipeItemParser();

      for (Map.Entry<String, String[]> entry : jsonOreDict.getOreDict().entrySet()) {

        for (String itemString : entry.getValue()) {

          try {
            ParseResult parseResult = parser.parse(itemString);
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

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleCore.Items.MULCH,

          ModuleCore.Items.APPLE_BAKED,
          ModuleCore.Items.BURNED_FOOD,

          ModuleCore.Items.CRUDE_HAMMER,
          ModuleCore.Items.STONE_HAMMER,
          ModuleCore.Items.FLINT_HAMMER,
          ModuleCore.Items.BONE_HAMMER,
          ModuleCore.Items.IRON_HAMMER,
          ModuleCore.Items.DIAMOND_HAMMER
      );

      // Material
      ModelRegistrationHelper.registerVariantItemModels(
          ModuleCore.Items.MATERIAL,
          "variant",
          ItemMaterial.EnumType.values()
      );
    });
  }

  private OreDictInitializer() {
    //
  }
}
