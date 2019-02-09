package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleTool;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class PitKilnRecipesAdd {

  public static void apply(IForgeRegistry<KilnPitRecipe> registry) {

    // Clay Shears
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTool.class)) {
      registry.register(new KilnPitRecipe(
          new ItemStack(ModuleTool.Items.CLAY_SHEARS),
          Ingredient.fromStacks(new ItemStack(ModuleTool.Items.UNFIRED_CLAY_SHEARS)),
          Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModuleTechBasic.MOD_ID, "clay_shears"));
    }

    // Brick
    registry.register(new KilnPitRecipe(
        new ItemStack(Items.BRICK),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_BRICK.asStack()),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "brick"));

    // Charcoal Flakes
    registry.register(new KilnPitRecipe(
        ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "charcoal_flakes"));

    // Stone Slab
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1, 3)),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 3, 0)
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone_slab"));

    // Stone
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE, 1, 0)),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.STONE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone"));

    // Stone - Andesite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 5),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.ANDESITE.getMeta())),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.ANDESITE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone_andesite"));

    // Stone - Granite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 1),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.GRANITE.getMeta())),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.GRANITE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone_granite"));

    // Stone - Diorite
    registry.register(new KilnPitRecipe(
        new ItemStack(Blocks.STONE, 1, 3),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.DIORITE.getMeta())),
        Reference.PitKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.PitKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.DIORITE.getMeta())
        }
    ).setRegistryName(ModuleTechBasic.MOD_ID, "stone_diorite"));

  }
}