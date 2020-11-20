package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe;

import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.util.IngredientHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.init.CompatInitializerOre;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.SlagInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.ItemSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomAnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

import java.nio.file.Path;
import java.util.*;

public class BloomeryRecipesAdd {

  public static final int[] DEFAULT_BLOOM_YIELD_MIN_MAX = new int[]{12, 15};
  public static final int[] DEFAULT_SLAG_BLOOM_YIELD_MIN_MAX = new int[]{12, 15};

  private static final int DEFAULT_BURN_TIME_TICKS = 24 * 60 * 20;
  private static final float DEFAULT_FAILURE_CHANCE = 0.25f;
  private static final float DEFAULT_EXPERIENCE = 0.5f;

  private static final RecipeItemParser RECIPE_ITEM_PARSER = new RecipeItemParser();

  public static void applyCompatRecipes(Path configurationPath, IForgeRegistry<BloomeryRecipe> registry) {

    CompatInitializerOre.OreCompatData data = CompatInitializerOre.read(configurationPath);

    if (data == null) {
      return;
    }

    for (Map.Entry<String, CompatInitializerOre.OreCompatEntry> entry : data.oredict.entrySet()) {

      String oreDictKey = entry.getKey();
      ItemSlag itemSlag = SlagInitializer.SLAG_BY_OREDICT.get(oreDictKey);
      BlockPileSlag blockPileSlag = SlagInitializer.SLAG_PILE_BY_OREDICT.get(oreDictKey);

      if (itemSlag == null
          || blockPileSlag == null) {
        continue;
      }

      ItemSlag.Properties properties = ModuleTechBloomery.Items.GENERATED_SLAG.get(itemSlag);
      String langKey = properties.langKey;
      CompatInitializerOre.OreCompatEntry oreCompatEntry = entry.getValue();
      ItemStack outputItemStack = BloomeryRecipesAdd.getFirstValidOutput(oreCompatEntry.output);

      if (outputItemStack.isEmpty()) {
        continue;
      }

      int bloomYieldMin;
      int bloomYieldMax;

      if (oreCompatEntry.bloomYieldMinMax == null || oreCompatEntry.bloomYieldMinMax.length != 2) {
        ModuleCore.LOGGER.error(String.format("Invalid value for key %s in oredict %s in file %s, expected integer array of length 2", "bloomYieldMinMax", oreDictKey, "core.compat.Ore-Custom.json"));
        bloomYieldMin = DEFAULT_BLOOM_YIELD_MIN_MAX[0];
        bloomYieldMax = DEFAULT_BLOOM_YIELD_MIN_MAX[1];

      } else {
        bloomYieldMin = oreCompatEntry.bloomYieldMinMax[0];
        bloomYieldMax = oreCompatEntry.bloomYieldMinMax[1];
      }

      int slagBloomYieldMin;
      int slagBloomYieldMax;

      if (oreCompatEntry.slagBloomYieldMinMax == null || oreCompatEntry.slagBloomYieldMinMax.length != 2) {
        ModuleCore.LOGGER.error(String.format("Invalid value for key %s in oredict %s in file %s, expected integer array of length 2", "slagBloomYieldMinMax", oreDictKey, "core.compat.Ore-Custom.json"));
        slagBloomYieldMin = DEFAULT_SLAG_BLOOM_YIELD_MIN_MAX[0];
        slagBloomYieldMax = DEFAULT_SLAG_BLOOM_YIELD_MIN_MAX[1];

      } else {
        slagBloomYieldMin = oreCompatEntry.slagBloomYieldMinMax[0];
        slagBloomYieldMax = oreCompatEntry.slagBloomYieldMinMax[1];
      }

      // 2019-07-20: (#133) We have to set the lang key for the ore bloom recipe
      // because items may be missing from the oredict during recipe registration.
      // If an oredict entry is empty, the bloomery recipe will not correctly
      // deduce the lang key from the input.

      // ore bloom
      registry.register(
          new BloomeryRecipeBuilder(
              new ResourceLocation(ModuleTechBloomery.MOD_ID, "bloom_from_" + oreDictKey.toLowerCase()),
              outputItemStack.copy(),
              new OreIngredient(oreDictKey)
          )
              .setBurnTimeTicks(DEFAULT_BURN_TIME_TICKS)
              .setExperience(DEFAULT_EXPERIENCE)
              .setFailureChance(DEFAULT_FAILURE_CHANCE)
              .setBloomYield(bloomYieldMin, bloomYieldMax)
              .setSlagItem(new ItemStack(itemSlag), 4)
              .addFailureItem(new ItemStack(ModuleTechBloomery.Items.SLAG), 1)
              .addFailureItem(new ItemStack(itemSlag), 2)
              .setLangKey(langKey)
              .create()
      );

      // slag bloom
      registry.register(
          new BloomeryRecipeBuilder(
              new ResourceLocation(ModuleTechBloomery.MOD_ID, "bloom_from_" + oreDictKey.toLowerCase() + "_slag"),
              outputItemStack.copy(),
              Ingredient.fromStacks(new ItemStack(blockPileSlag))
          )
              .setBurnTimeTicks(DEFAULT_BURN_TIME_TICKS / 2)
              .setExperience(DEFAULT_EXPERIENCE)
              .setFailureChance(DEFAULT_FAILURE_CHANCE)
              .setBloomYield(slagBloomYieldMin, slagBloomYieldMax)
              .setSlagItem(new ItemStack(itemSlag), 2)
              .addFailureItem(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()), 1)
              .addFailureItem(new ItemStack(ModuleTechBloomery.Items.SLAG, 1, 0), 2)
              .setLangKey(langKey + ";" + itemSlag.getUnlocalizedName() + ".unique")
              .create()
      );
    }
  }

  private static ItemStack getFirstValidOutput(String[] outputs) {

    for (String itemString : outputs) {

      try {
        ParseResult parseResult = BloomeryRecipesAdd.RECIPE_ITEM_PARSER.parse(itemString);
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parseResult.getDomain(), parseResult.getPath()));

        if (item == null) {
          ModuleTechBloomery.LOGGER.error("Unable to find registered item for: " + itemString);
          continue;
        }

        return new ItemStack(item, 1, parseResult.getMeta());

      } catch (Exception e) {
        ModuleTechBloomery.LOGGER.error("Invalid item string: " + itemString);
      }
    }

    return ItemStack.EMPTY;
  }

  public static void registerBloomAnvilRecipes(
      IForgeRegistry<BloomeryRecipe> registryBloomery,
      IForgeRegistry<AnvilRecipe> registryAnvil
  ) {

    Collection<BloomeryRecipe> bloomeryRecipes = registryBloomery.getValuesCollection();
    List<BloomeryRecipe> snapshot = new ArrayList<>(bloomeryRecipes);

    for (BloomeryRecipeBase recipe : snapshot) {
      BloomeryRecipesAdd.registerBloomAnvilRecipe(registryAnvil, recipe);
    }
  }

  public static void registerBloomAnvilRecipe(IForgeRegistry<AnvilRecipe> registryAnvil, BloomeryRecipeBase recipe) {

    ResourceLocation registryName = recipe.getRegistryName();

    if (registryName == null) {
      return;
    }

    registryAnvil.register(new BloomAnvilRecipe(
        recipe.getOutput(),
        IngredientHelper.fromStackWithNBT(recipe.getOutputBloom()),
        ModuleTechBloomeryConfig.BLOOM.HAMMER_HITS_IN_ANVIL_REQUIRED,
        AnvilRecipe.EnumType.HAMMER,
        Arrays.copyOf(recipe.getAnvilTiers(), recipe.getAnvilTiers().length),
        recipe
    ).setRegistryName(registryName));
  }
}