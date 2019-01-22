package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.GraniteAnvilRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class BloomeryRecipesAdd {

  public static void apply(IForgeRegistry<BloomeryRecipe> registry) {

    int defaultBurnTimeTicks = 30 * 20; //12 * 60 * 20; // TODO: dev
    float defaultFailureChance = 0.33f;

    // Iron Nugget
    registry.register(new BloomeryRecipe(
        new ItemStack(Items.IRON_NUGGET),
        Ingredient.fromStacks(new ItemStack(Blocks.IRON_ORE)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        8,
        10,
        new ItemStack[]{
            // TODO: slag
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
        },
        null
    ).setRegistryName(ModulePyrotech.MOD_ID, "iron_nugget"));

    // Gold Nugget
    registry.register(new BloomeryRecipe(
        new ItemStack(Items.GOLD_NUGGET),
        Ingredient.fromStacks(new ItemStack(Blocks.GOLD_ORE)),
        defaultBurnTimeTicks,
        defaultFailureChance,
        8,
        10,
        new ItemStack[]{
            // TODO: slag
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
        },
        null
    ).setRegistryName(ModulePyrotech.MOD_ID, "gold_nugget"));
  }

  public static void applyBloomRecipes(IForgeRegistry<BloomeryRecipe> registryBloomery, IForgeRegistry<GraniteAnvilRecipe> registryAnvil) {

    for (BloomeryRecipe bloomeryRecipe : registryBloomery.getValuesCollection()) {
      //noinspection ConstantConditions
      registryAnvil.register(new GraniteAnvilRecipe.BloomAnvilRecipe(
          bloomeryRecipe.getOutput(),
          Ingredient.fromStacks(bloomeryRecipe.getOutputBloom()),
          8,
          GraniteAnvilRecipe.EnumType.HAMMER,
          bloomeryRecipe
      ).setRegistryName(bloomeryRecipe.getRegistryName()));
    }

  }
}