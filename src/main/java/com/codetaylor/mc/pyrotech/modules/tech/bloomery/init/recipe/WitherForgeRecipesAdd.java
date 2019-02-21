package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe;

import com.codetaylor.mc.athenaeum.util.IngredientHelper;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WitherForgeRecipesAdd {

  private static final int DEFAULT_BURN_TIME_TICKS = 24 * 60 * 20;
  private static final float DEFAULT_FAILURE_CHANCE = 0.25f;

  public static void apply(IForgeRegistry<WitherForgeRecipe> registry) {

    // Obsidian Shards
    registry.register(new WitherForgeRecipeBuilder(
        new ResourceLocation(ModuleTechBloomery.MOD_ID, "bloom_from_obsidian"),
        ItemMaterial.EnumType.OBSIDIAN_SHARD.asStack(),
        Ingredient.fromStacks(new ItemStack(Blocks.OBSIDIAN))
    )
        .setBurnTimeTicks(DEFAULT_BURN_TIME_TICKS)
        .setFailureChance(DEFAULT_FAILURE_CHANCE)
        .setBloomYield(8, 12)
        .setSlagItem(new ItemStack(ModuleTechBloomery.Items.SLAG), 2)
        .setLangKey(Blocks.OBSIDIAN.getUnlocalizedName())
        .create());
  }

  public static void registerBloomAnvilRecipes(
      IForgeRegistry<WitherForgeRecipe> registryBloomery,
      IForgeRegistry<AnvilRecipe> registryAnvil
  ) {

    Collection<WitherForgeRecipe> bloomeryRecipes = registryBloomery.getValuesCollection();
    List<WitherForgeRecipe> snapshot = new ArrayList<>(bloomeryRecipes);

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

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<BloomeryRecipe> bloomeryRegistry,
      IForgeRegistryModifiable<WitherForgeRecipe> witherForgeRegistry
  ) {

    if (ModuleTechBloomeryConfig.WITHER_FORGE.INHERIT_BLOOMERY_RECIPES) {
      RecipeHelper.inherit("bloomery", bloomeryRegistry, witherForgeRegistry, recipe -> {

        // the registry name can be null here because it will be set by the inherit method
        WitherForgeRecipeBuilder builder = new WitherForgeRecipeBuilder(null, recipe.getOutput(), recipe.getInput())
            .setBurnTimeTicks(recipe.getTimeTicks())
            .setFailureChance(recipe.getFailureChance())
            .setBloomYield(recipe.getBloomYieldMin(), recipe.getBloomYieldMax())
            .setSlagItem(recipe.getSlagItemStack(), recipe.getSlagCount())
            .setLangKey(recipe.getLangKey());

        for (BloomeryRecipeBase.FailureItem item : recipe.getFailureItems()) {
          builder.addFailureItem(item.getItemStack(), item.getWeight());
        }

        return builder.create();
      });
    }
  }
}