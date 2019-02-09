package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleTool;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class StoneKilnRecipesAdd {

  public static void apply(IForgeRegistry<StoneKilnRecipe> registry) {

    // Clay Shears
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTool.class)) {
      registry.register(new StoneKilnRecipe(
          new ItemStack(ModuleTool.Items.CLAY_SHEARS),
          Ingredient.fromStacks(new ItemStack(ModuleTool.Items.UNFIRED_CLAY_SHEARS)),
          Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.PIT_ASH.asStack(),
              ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
              ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
          }
      ).setRegistryName(ModuleTechBasic.MOD_ID, "clay_shears"));
    }

    // Cobblestone
    registry.register(new StoneKilnRecipe(
        new ItemStack(Blocks.COBBLESTONE),
        Ingredient.fromStacks(new ItemStack(Blocks.GRAVEL)),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            BlockRock.EnumType.STONE.asStack(6),
            BlockRock.EnumType.ANDESITE.asStack(6),
            BlockRock.EnumType.GRANITE.asStack(6),
            BlockRock.EnumType.DIORITE.asStack(6)
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "cobblestone_from_gravel"));

    // Brick
    registry.register(new StoneKilnRecipe(
        new ItemStack(Items.BRICK),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_BRICK.asStack()),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack()
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "brick"));

    // Refractory Brick
    registry.register(new StoneKilnRecipe(
        ItemMaterial.EnumType.REFRACTORY_BRICK.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_REFRACTORY_BRICK.asStack()),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "refractory_brick"));

    // Charcoal Flakes
    registry.register(new StoneKilnRecipe(
        ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "charcoal_flakes"));

    // Quicklime
    registry.register(new StoneKilnRecipe(
        ItemMaterial.EnumType.QUICKLIME.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.DUST_LIMESTONE.asStack()),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "quicklime"));

    // Stone Slab
    registry.register(new StoneKilnRecipe(
        new ItemStack(Blocks.STONE_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1, 3)),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 3, 0)
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "stone_slab"));

    // Stone
    registry.register(new StoneKilnRecipe(
        new ItemStack(Blocks.STONE, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE, 1, 0)),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.STONE.getMeta())
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "stone"));

    // Stone - Andesite
    registry.register(new StoneKilnRecipe(
        new ItemStack(Blocks.STONE, 1, 5),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.ANDESITE.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.ANDESITE.getMeta())
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "stone_andesite"));

    // Stone - Granite
    registry.register(new StoneKilnRecipe(
        new ItemStack(Blocks.STONE, 1, 1),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.GRANITE.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.GRANITE.getMeta())
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "stone_granite"));

    // Stone - Diorite
    registry.register(new StoneKilnRecipe(
        new ItemStack(Blocks.STONE, 1, 3),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.DIORITE.getMeta())),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            new ItemStack(ModuleCore.Blocks.ROCK, 5, BlockRock.EnumType.DIORITE.getMeta())
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "stone_diorite"));

    // Glass
    registry.register(new StoneKilnRecipe(
        new ItemStack(Blocks.GLASS, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Blocks.SAND, 1, 0),
            new ItemStack(Blocks.SAND, 1, 1)
        ),
        Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
        Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
        new ItemStack[]{
            ItemMaterial.EnumType.GLASS_SHARD.asStack(4)
        }
    ).setRegistryName(ModuleTechMachine.MOD_ID, "glass"));

    // Slag Glass
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleBloomery.class)) {
      registry.register(new StoneKilnRecipe(
          new ItemStack(ModuleCore.Blocks.SLAG_GLASS, 1, 0),
          Ingredient.fromStacks(
              new ItemStack(ModuleBloomery.Blocks.PILE_SLAG, 1, 0)
          ),
          Reference.StoneKiln.DEFAULT_BURN_TIME_TICKS,
          Reference.StoneKiln.DEFAULT_FAILURE_CHANCE,
          new ItemStack[]{
              ItemMaterial.EnumType.GLASS_SHARD.asStack(4),
              new ItemStack(ModuleBloomery.Items.SLAG, 4, 0)
          }
      ).setRegistryName(ModuleTechMachine.MOD_ID, "slag_glass"));
    }
  }
}