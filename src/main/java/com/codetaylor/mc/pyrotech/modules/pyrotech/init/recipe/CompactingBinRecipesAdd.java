package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.CompactingBinRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class CompactingBinRecipesAdd {

  public static void apply(IForgeRegistryModifiable<CompactingBinRecipe> registry) {

    // Gravel
    registry.register(new CompactingBinRecipe(
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.DIORITE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.GRANITE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.ANDESITE.getMeta())
        ), new ItemStack(Blocks.GRAVEL),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "gravel"));

    // Dirt
    registry.register(new CompactingBinRecipe(
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.DIRT.getMeta())
        ), new ItemStack(Blocks.DIRT, 1, 0),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "dirt"));

    // Sand
    registry.register(new CompactingBinRecipe(
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.SAND.getMeta())
        ), new ItemStack(Blocks.SAND, 1, 0),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "sand"));

    // Grass
    registry.register(new CompactingBinRecipe(
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK_GRASS, 1, 0)
        ), new ItemStack(Blocks.GRASS, 1, 0),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "grass"));

    // Clay
    registry.register(new CompactingBinRecipe(
        Ingredient.fromStacks(
            new ItemStack(Items.CLAY_BALL, 1, 0)
        ), new ItemStack(Blocks.CLAY, 1, 0),
        4
    ).setRegistryName(ModulePyrotech.MOD_ID, "clay"));

    // Snow
    registry.register(new CompactingBinRecipe(
        Ingredient.fromStacks(
            new ItemStack(Items.SNOWBALL, 1, 0)
        ), new ItemStack(Blocks.SNOW, 1, 0),
        4
    ).setRegistryName(ModulePyrotech.MOD_ID, "snow"));

    // Bone Block
    registry.register(new CompactingBinRecipe(
        Ingredient.fromStacks(
            new ItemStack(Items.DYE, 1, 15)
        ), new ItemStack(Blocks.BONE_BLOCK, 1, 0),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "bone_block"));
  }
}