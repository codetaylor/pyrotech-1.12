package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.GraniteAnvilRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class BloomeryRecipesAdd {

  public static void apply(IForgeRegistry<BloomeryRecipe> registry) {

    int defaultBurnTimeTicks = 30 * 20; //12 * 60 * 20; // TODO: dev
    float defaultFailureChance = 0.33f;

    // Iron Nugget
    registry.register(new BloomeryRecipe(
        new ResourceLocation(ModulePyrotech.MOD_ID, "iron_nugget"),
        new ItemStack(Items.IRON_NUGGET),
        Ingredient.fromStacks(new ItemStack(Blocks.IRON_ORE)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        8,
        10,
        4,
        true,
        new ItemStack[]{
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta())
        },
        null
    ));

    // Gold Nugget
    registry.register(new BloomeryRecipe(
        new ResourceLocation(ModulePyrotech.MOD_ID, "gold_nugget"),
        new ItemStack(Items.GOLD_NUGGET),
        Ingredient.fromStacks(new ItemStack(Blocks.GOLD_ORE)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        8,
        10,
        4,
        true,
        new ItemStack[]{
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta())
        },
        null
    ));
  }

  public static void applyBloomRecipes(IForgeRegistry<BloomeryRecipe> registryBloomery, IForgeRegistry<GraniteAnvilRecipe> registryAnvil) {

    for (BloomeryRecipe bloomeryRecipe : registryBloomery.getValuesCollection()) {
      //noinspection ConstantConditions
      registryAnvil.register(new GraniteAnvilRecipe.BloomAnvilRecipe(
          bloomeryRecipe.getOutput(),
          Ingredient.fromStacks(bloomeryRecipe.getOutputBloom()),
          ModulePyrotechConfig.BLOOM.HAMMER_HITS_IN_ANVIL_REQUIRED,
          GraniteAnvilRecipe.EnumType.HAMMER,
          bloomeryRecipe
      ).setRegistryName(bloomeryRecipe.getRegistryName()));
    }

  }
}