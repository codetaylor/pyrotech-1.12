package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.KilnStoneRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class StoneKilnRecipesAdd {

  public static void apply(IForgeRegistry<KilnStoneRecipe> registry) {

    // Brick
    registry.register(new KilnStoneRecipe(
        new ItemStack(Items.BRICK),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_BRICK.asStack()),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
        }
    ).setRegistryName(ModuleCore.MOD_ID, "brick"));

    // Refractory Brick
    registry.register(new KilnStoneRecipe(
        ItemMaterial.EnumType.REFRACTORY_BRICK.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_REFRACTORY_BRICK.asStack()),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModuleCore.MOD_ID, "refractory_brick"));

    // Charcoal Flakes
    registry.register(new KilnStoneRecipe(
        ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModuleCore.MOD_ID, "charcoal_flakes"));

    // Quicklime
    registry.register(new KilnStoneRecipe(
        ItemMaterial.EnumType.QUICKLIME.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.LIMESTONE.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModuleCore.MOD_ID, "quicklime"));

    // Stone Slab
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1, 3)),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 3, 0)
        }
    ).setRegistryName(ModuleCore.MOD_ID, "stone_slab"));

    // Stone
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE, 1, 0)),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.STONE.getMeta())
        }
    ).setRegistryName(ModuleCore.MOD_ID, "stone"));

    // Stone - Andesite
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE, 1, 5),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.ANDESITE.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.ANDESITE.getMeta())
        }
    ).setRegistryName(ModuleCore.MOD_ID, "stone_andesite"));

    // Stone - Granite
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE, 1, 1),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.GRANITE.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.GRANITE.getMeta())
        }
    ).setRegistryName(ModuleCore.MOD_ID, "stone_granite"));

    // Stone - Diorite
    registry.register(new KilnStoneRecipe(
        new ItemStack(Blocks.STONE, 1, 3),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.DIORITE.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.DIORITE.getMeta())
        }
    ).setRegistryName(ModuleCore.MOD_ID, "stone_diorite"));

  }
}