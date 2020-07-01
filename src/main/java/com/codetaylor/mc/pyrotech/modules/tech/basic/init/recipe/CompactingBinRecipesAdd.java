package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompactingBinRecipe;
import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class CompactingBinRecipesAdd {

  public static void apply(IForgeRegistryModifiable<CompactingBinRecipe> registry) {

    // Netherrack
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.NETHERRACK),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Items.ROCK_NETHERRACK)),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "netherrack"));

    // Ash Pile
    registry.register(new CompactingBinRecipe(
        new ItemStack(ModuleCore.Blocks.PILE_ASH),
        Ingredient.fromStacks(ItemMaterial.EnumType.PIT_ASH.asStack()),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "ash_pile"));

    // Lapis Block
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.LAPIS_BLOCK),
        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage())),
        9
    ).setRegistryName(ModuleTechBasic.MOD_ID, "lapis_block"));

    // Redstone Block
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.REDSTONE_BLOCK),
        Ingredient.fromStacks(new ItemStack(Items.REDSTONE)),
        9
    ).setRegistryName(ModuleTechBasic.MOD_ID, "redstone_block"));

    // Charcoal Block
    registry.register(new CompactingBinRecipe(
        new ItemStack(ModuleCore.Blocks.CHARCOAL_BLOCK),
        Ingredient.fromStacks(ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack()),
        72
    ).setRegistryName(ModuleTechBasic.MOD_ID, "charcoal_block_from_flakes"));

    // Gravel
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.GRAVEL),
        Ingredient.fromStacks(
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()),
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.DIORITE.getMeta()),
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.GRANITE.getMeta()),
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.ANDESITE.getMeta())
        ),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "gravel"));

    // Dirt
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.DIRT, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.DIRT.getMeta())
        ),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "dirt"));

    // Charcoal Block
    registry.register(new CompactingBinRecipe(
        new ItemStack(ModuleCore.Blocks.CHARCOAL_BLOCK, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.COAL, 1, 1)
        ),
        9
    ).setRegistryName(ModuleTechBasic.MOD_ID, "charcoal_block"));

    // Sand
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.SAND, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.SAND.getMeta())
        ),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "sand"));

    // Red Sand
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.SAND, 1, BlockSand.EnumType.RED_SAND.getMetadata()),
        Ingredient.fromStacks(
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.SAND_RED.getMeta())
        ),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "sand_red"));

    // Grass
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.GRASS, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleCore.Blocks.ROCK_GRASS, 1, 0)
        ),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "grass"));

    // Clay
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.CLAY, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.CLAY_BALL, 1, 0)
        ),
        4
    ).setRegistryName(ModuleTechBasic.MOD_ID, "clay"));

    // Snow
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.SNOW, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.SNOWBALL, 1, 0)
        ),
        4
    ).setRegistryName(ModuleTechBasic.MOD_ID, "snow"));

    // Bone Block
    registry.register(new CompactingBinRecipe(
        new ItemStack(Blocks.BONE_BLOCK, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.DYE, 1, 15)
        ),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "bone_block"));

    // Pile of Wood Chips
    registry.register(new CompactingBinRecipe(
        new ItemStack(ModuleCore.Blocks.PILE_WOOD_CHIPS, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())
        ),
        8
    ).setRegistryName(ModuleTechBasic.MOD_ID, "pile_wood_chips"));
  }
}