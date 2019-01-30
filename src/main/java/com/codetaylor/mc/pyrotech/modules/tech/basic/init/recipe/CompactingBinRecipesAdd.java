package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompactingBinRecipe;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class CompactingBinRecipesAdd {

  public static void apply(IForgeRegistryModifiable<CompactingBinRecipe> registry) {

    // Gravel
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.GRAVEL),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.DIORITE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.GRANITE.getMeta()),
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.ANDESITE.getMeta())
        ),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "gravel"));

    // Dirt
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.DIRT, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.DIRT.getMeta())
        ),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "dirt"));

    // Sand
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.SAND, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.SAND.getMeta())
        ),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "sand"));

    // Red Sand
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.SAND, 1, BlockSand.EnumType.RED_SAND.getMetadata()),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.SAND_RED.getMeta())
        ),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "sand_red"));

    // Grass
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.GRASS, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK_GRASS, 1, 0)
        ),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "grass"));

    // Clay
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.CLAY, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.CLAY_BALL, 1, 0)
        ),
        4
    ).setRegistryName(ModulePyrotech.MOD_ID, "clay"));

    // Snow
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.SNOW, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.SNOWBALL, 1, 0)
        ),
        4
    ).setRegistryName(ModulePyrotech.MOD_ID, "snow"));

    // Bone Block
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.BONE_BLOCK, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.DYE, 1, 15)
        ),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "bone_block"));

    // Pile of Wood Chips
    registry.register(new CompactingBinRecipe(
        new ItemStack(ModuleBlocks.PILE_WOOD_CHIPS, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())
        ),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "pile_wood_chips"));
  }
}