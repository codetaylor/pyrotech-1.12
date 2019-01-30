package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe;

import com.codetaylor.mc.athenaeum.util.IngredientHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.ItemSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomAnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
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

  private static final int DEFAULT_BURN_TIME_TICKS = 30 * 20;//12 * 60 * 20; // TODO: dev
  private static final float DEFAULT_FAILURE_CHANCE = 0.33f;

  public static void apply(IForgeRegistry<BloomeryRecipe> registry) {

    Item itemSlagIron = ForgeRegistries.ITEMS.getValue(new ResourceLocation(
        ModuleBloomery.MOD_ID,
        "generated_" + ItemSlag.NAME + "_iron"
    ));

    Block blockSlagIron = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(
        ModuleBloomery.MOD_ID,
        "generated_" + BlockPileSlag.NAME + "_iron"
    ));

    if (itemSlagIron != null
        && blockSlagIron != null) {

      // Iron Bloom
      registry.register(new BloomeryRecipe(
          new ResourceLocation(ModuleBloomery.MOD_ID, "bloom_from_iron_ore"),
          new ItemStack(Items.IRON_NUGGET),
          Ingredient.fromStacks(new ItemStack(Blocks.IRON_ORE)),
          DEFAULT_BURN_TIME_TICKS,
          DEFAULT_FAILURE_CHANCE,
          8,
          10,
          4,
          new ItemStack(itemSlagIron),
          new ItemStack[]{
              new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()),
              new ItemStack(itemSlagIron)
          },
          null
      ));

      // Iron Slag Bloom
      registry.register(new BloomeryRecipe(
          new ResourceLocation(ModuleBloomery.MOD_ID, "bloom_from_iron_slag"),
          new ItemStack(Items.IRON_NUGGET),
          Ingredient.fromStacks(new ItemStack(blockSlagIron)),
          DEFAULT_BURN_TIME_TICKS,
          DEFAULT_FAILURE_CHANCE,
          4,
          5,
          2,
          new ItemStack(itemSlagIron),
          new ItemStack[]{
              new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta())
          },
          Blocks.IRON_ORE.getUnlocalizedName()
      ));
    }

    Item itemSlagGold = ForgeRegistries.ITEMS.getValue(new ResourceLocation(
        ModuleBloomery.MOD_ID,
        "generated_" + ItemSlag.NAME + "_gold"
    ));

    Block blockSlagGold = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(
        ModuleBloomery.MOD_ID,
        "generated_" + BlockPileSlag.NAME + "_gold"
    ));

    if (itemSlagGold != null
        && blockSlagGold != null) {

      // Gold Nugget
      registry.register(new BloomeryRecipe(
          new ResourceLocation(ModuleBloomery.MOD_ID, "bloom_from_gold_ore"),
          new ItemStack(Items.GOLD_NUGGET),
          Ingredient.fromStacks(new ItemStack(Blocks.GOLD_ORE)),
          DEFAULT_BURN_TIME_TICKS,
          DEFAULT_FAILURE_CHANCE,
          8,
          10,
          4,
          new ItemStack(itemSlagGold),
          new ItemStack[]{
              new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()),
              new ItemStack(itemSlagGold)
          },
          null
      ));

      // Gold Slag Bloom
      registry.register(new BloomeryRecipe(
          new ResourceLocation(ModuleBloomery.MOD_ID, "bloom_from_gold_slag"),
          new ItemStack(Items.GOLD_NUGGET),
          Ingredient.fromStacks(new ItemStack(blockSlagGold)),
          DEFAULT_BURN_TIME_TICKS,
          DEFAULT_FAILURE_CHANCE,
          4,
          5,
          2,
          new ItemStack(itemSlagGold),
          new ItemStack[]{
              new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta())
          },
          Blocks.GOLD_ORE.getUnlocalizedName()
      ));
    }
  }

  public static void registerBloomAnvilRecipes(
      IForgeRegistry<BloomeryRecipe> registryBloomery,
      IForgeRegistry<AnvilRecipe> registryAnvil
  ) {

    Collection<BloomeryRecipe> bloomeryRecipes = registryBloomery.getValuesCollection();
    List<BloomeryRecipe> snapshot = new ArrayList<>(bloomeryRecipes);

    for (BloomeryRecipe bloomeryRecipe : snapshot) {

      // --- Anvil Recipes ---

      //noinspection ConstantConditions
      registryAnvil.register(new BloomAnvilRecipe(
          bloomeryRecipe.getOutput(),
          IngredientHelper.fromStackWithNBT(bloomeryRecipe.getOutputBloom()),
          ModuleBloomeryConfig.BLOOM.HAMMER_HITS_IN_ANVIL_REQUIRED,
          AnvilRecipe.EnumType.HAMMER,
          bloomeryRecipe
      ).setRegistryName(bloomeryRecipe.getRegistryName()));
    }
  }

}