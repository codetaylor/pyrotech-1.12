package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe;

import com.codetaylor.mc.athenaeum.util.IngredientHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.ItemSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomAnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBuilder;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BloomeryRecipesAdd {

  private static final int DEFAULT_BURN_TIME_TICKS = 24 * 60 * 20;
  private static final float DEFAULT_FAILURE_CHANCE = 0.25f;

  public static void apply(IForgeRegistry<BloomeryRecipe> registry) {

    Item itemSlagIron = ForgeRegistries.ITEMS.getValue(new ResourceLocation(
        ModuleTechBloomery.MOD_ID,
        "generated_" + ItemSlag.NAME + "_iron"
    ));

    Block blockSlagIron = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(
        ModuleTechBloomery.MOD_ID,
        "generated_" + BlockPileSlag.NAME + "_iron"
    ));

    if (itemSlagIron != null
        && blockSlagIron != null) {

      // Iron Bloom
      registry.register(
          new BloomeryRecipeBuilder(
              new ResourceLocation(ModuleTechBloomery.MOD_ID, "bloom_from_iron_ore"),
              new ItemStack(Items.IRON_NUGGET),
              Ingredient.fromStacks(new ItemStack(Blocks.IRON_ORE))
          )
              .setBurnTimeTicks(DEFAULT_BURN_TIME_TICKS)
              .setFailureChance(DEFAULT_FAILURE_CHANCE)
              .setBloomYield(16, 20)
              .setSlagItem(new ItemStack(itemSlagIron), 4)
              .addFailureItem(new ItemStack(ModuleTechBloomery.Items.SLAG, 1, 0), 1)
              .addFailureItem(new ItemStack(itemSlagIron, 1, 0), 2)
              .create()
      );

      // Iron Slag Bloom
      registry.register(
          new BloomeryRecipeBuilder(
              new ResourceLocation(ModuleTechBloomery.MOD_ID, "bloom_from_iron_slag"),
              new ItemStack(Items.IRON_NUGGET),
              Ingredient.fromStacks(new ItemStack(blockSlagIron))
          )
              .setBurnTimeTicks(DEFAULT_BURN_TIME_TICKS / 2)
              .setFailureChance(DEFAULT_FAILURE_CHANCE)
              .setBloomYield(8, 10)
              .setSlagItem(new ItemStack(itemSlagIron), 2)
              .addFailureItem(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()), 1)
              .addFailureItem(new ItemStack(ModuleTechBloomery.Items.SLAG, 1, 0), 2)
              .setLangKey(Blocks.IRON_ORE.getUnlocalizedName() + ";" + itemSlagIron.getUnlocalizedName() + ".unique")
              .create()
      );
    }

    Item itemSlagGold = ForgeRegistries.ITEMS.getValue(new ResourceLocation(
        ModuleTechBloomery.MOD_ID,
        "generated_" + ItemSlag.NAME + "_gold"
    ));

    Block blockSlagGold = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(
        ModuleTechBloomery.MOD_ID,
        "generated_" + BlockPileSlag.NAME + "_gold"
    ));

    if (itemSlagGold != null
        && blockSlagGold != null) {

      // Gold Nugget
      registry.register(
          new BloomeryRecipeBuilder(
              new ResourceLocation(ModuleTechBloomery.MOD_ID, "bloom_from_gold_ore"),
              new ItemStack(Items.GOLD_NUGGET),
              Ingredient.fromStacks(new ItemStack(Blocks.GOLD_ORE))
          )
              .setBurnTimeTicks(DEFAULT_BURN_TIME_TICKS)
              .setFailureChance(DEFAULT_FAILURE_CHANCE)
              .setBloomYield(16, 20)
              .setSlagItem(new ItemStack(itemSlagGold), 4)
              .addFailureItem(new ItemStack(ModuleTechBloomery.Items.SLAG, 1, 0), 1)
              .addFailureItem(new ItemStack(itemSlagGold, 1, 0), 2)
              .create()
      );

      // Gold Slag Bloom
      registry.register(
          new BloomeryRecipeBuilder(
              new ResourceLocation(ModuleTechBloomery.MOD_ID, "bloom_from_gold_slag"),
              new ItemStack(Items.GOLD_NUGGET),
              Ingredient.fromStacks(new ItemStack(blockSlagGold))
          )
              .setBurnTimeTicks(DEFAULT_BURN_TIME_TICKS / 2)
              .setFailureChance(DEFAULT_FAILURE_CHANCE)
              .setBloomYield(8, 10)
              .setSlagItem(new ItemStack(itemSlagGold), 2)
              .addFailureItem(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()), 1)
              .addFailureItem(new ItemStack(ModuleTechBloomery.Items.SLAG, 1, 0), 2)
              .setLangKey(Blocks.GOLD_ORE.getUnlocalizedName() + ";" + itemSlagGold.getUnlocalizedName() + ".unique")
              .create()
      );
    }
  }

  public static void registerBloomAnvilRecipes(
      IForgeRegistry<BloomeryRecipe> registryBloomery,
      IForgeRegistry<AnvilRecipe> registryAnvil
  ) {

    Collection<BloomeryRecipe> bloomeryRecipes = registryBloomery.getValuesCollection();
    List<BloomeryRecipe> snapshot = new ArrayList<>(bloomeryRecipes);

    for (BloomeryRecipeBase bloomeryRecipe : snapshot) {

      // --- Anvil Recipes ---

      //noinspection ConstantConditions
      registryAnvil.register(new BloomAnvilRecipe(
          bloomeryRecipe.getOutput(),
          IngredientHelper.fromStackWithNBT(bloomeryRecipe.getOutputBloom()),
          ModuleTechBloomeryConfig.BLOOM.HAMMER_HITS_IN_ANVIL_REQUIRED,
          AnvilRecipe.EnumType.HAMMER,
          bloomeryRecipe
      ).setRegistryName(bloomeryRecipe.getRegistryName()));
    }
  }

}