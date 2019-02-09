package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class StoneKilnRecipesAdd {

  public static void apply(IForgeRegistry<StoneKilnRecipe> registry) {

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

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<KilnPitRecipe> pitKilnRegistry,
      IForgeRegistryModifiable<StoneKilnRecipe> stoneKilnRegistry
  ) {

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)
        && ModuleTechMachineConfig.STONE_KILN.INHERIT_PIT_KILN_RECIPES) {
      RecipeHelper.inherit("pit_kiln", pitKilnRegistry, stoneKilnRegistry, recipe -> {
        int timeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.STONE_KILN.INHERITED_PIT_KILN_RECIPE_DURATION_MODIFIER);
        float failureChance = (float) (recipe.getFailureChance() * ModuleTechMachineConfig.STONE_KILN.INHERITED_PIT_KILN_RECIPE_FAILURE_CHANCE_MODIFIER);
        return new StoneKilnRecipe(
            recipe.getOutput(),
            recipe.getInput(),
            Math.max(1, timeTicks),
            failureChance,
            recipe.getFailureItems()
        );
      });
    }
  }
}