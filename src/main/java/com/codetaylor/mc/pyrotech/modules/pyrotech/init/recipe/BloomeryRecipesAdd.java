package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.athenaeum.util.IngredientHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.CompactingBinRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.GraniteAnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.util.BloomHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BloomeryRecipesAdd {

  private static final int DEFAULT_BURN_TIME_TICKS = 30 * 20;//12 * 60 * 20; // TODO: dev
  private static final float DEFAULT_FAILURE_CHANCE = 0.33f;

  public static void apply(IForgeRegistry<BloomeryRecipe> registry) {

    // Iron Nugget
    registry.register(new BloomeryRecipe(
        new ResourceLocation(ModulePyrotech.MOD_ID, "iron_nugget"),
        new ItemStack(Items.IRON_NUGGET),
        Ingredient.fromStacks(new ItemStack(Blocks.IRON_ORE)),
        DEFAULT_BURN_TIME_TICKS,
        DEFAULT_FAILURE_CHANCE,
        8,
        10,
        4 * 4,
        true,
        new Color(Integer.decode("0xd8af93")).getRGB(),
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
        DEFAULT_BURN_TIME_TICKS,
        DEFAULT_FAILURE_CHANCE,
        8,
        10,
        4 * 4,
        true,
        new Color(Integer.decode("0xfcee4b")).getRGB(),
        new ItemStack[]{
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta())
        },
        null
    ));
  }

  public static void applyBloomRecipes(
      IForgeRegistry<BloomeryRecipe> registryBloomery,
      IForgeRegistry<GraniteAnvilRecipe> registryAnvil,
      IForgeRegistryModifiable<CompactingBinRecipe> registryCompacting
  ) {

    Collection<BloomeryRecipe> bloomeryRecipes = registryBloomery.getValuesCollection();
    List<BloomeryRecipe> snapshot = new ArrayList<>(bloomeryRecipes);

    for (BloomeryRecipe bloomeryRecipe : snapshot) {

      // --- Anvil Recipes ---

      //noinspection ConstantConditions
      registryAnvil.register(new GraniteAnvilRecipe.BloomAnvilRecipe(
          bloomeryRecipe.getOutput(),
          IngredientHelper.fromStackWithNBT(bloomeryRecipe.getOutputBloom()),
          ModulePyrotechConfig.BLOOM.HAMMER_HITS_IN_ANVIL_REQUIRED,
          GraniteAnvilRecipe.EnumType.HAMMER,
          bloomeryRecipe
      ).setRegistryName(bloomeryRecipe.getRegistryName()));

      // --- Compacting Bin Recipes ---

      ItemStack slagPile = new ItemStack(ModuleBlocks.PILE_SLAG, 1);
      NBTTagCompound tag = new NBTTagCompound();
      tag.setString("recipeId", bloomeryRecipe.getRegistryName().toString());
      tag.setString("langKey", bloomeryRecipe.getLangKey());
      tag.setInteger("color", bloomeryRecipe.getSlagColor());
      slagPile.setTagCompound(tag);

      registryCompacting.register(new CompactingBinRecipe(
          slagPile.copy(),
          IngredientHelper.fromStackWithNBT(BloomHelper.createSlagItem(
              bloomeryRecipe.getRegistryName(),
              bloomeryRecipe.getLangKey(),
              bloomeryRecipe.getSlagColor()
          )),
          8
      ).setRegistryName(bloomeryRecipe.getRegistryName()));

      // --- Bloomery Recipes ---

      registryBloomery.register(new BloomeryRecipe(
          new ResourceLocation(bloomeryRecipe.getRegistryName() + ".slag"),
          bloomeryRecipe.getOutput(),
          IngredientHelper.fromStackWithNBT(slagPile.copy()),
          DEFAULT_BURN_TIME_TICKS,
          DEFAULT_FAILURE_CHANCE,
          4,
          5,
          2,
          false,
          bloomeryRecipe.getSlagColor(),
          new ItemStack[]{
              new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta())
          },
          bloomeryRecipe.getLangKey()
      ));
    }

  }
}