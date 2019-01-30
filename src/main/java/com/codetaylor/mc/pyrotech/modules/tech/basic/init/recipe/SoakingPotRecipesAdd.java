package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.SoakingPotRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

public class SoakingPotRecipesAdd {

  public static void apply(IForgeRegistry<SoakingPotRecipe> registry) {

    // Sponge
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.SPONGE, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 0)),
        new FluidStack(FluidRegistry.WATER, 1000),
        1
    ).setRegistryName(ModulePyrotech.MOD_ID, "sponge"));

    // Durable Twine
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.TWINE_DURABLE.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.TWINE.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          2 * 60 * 20
      ).setRegistryName(ModulePyrotech.MOD_ID, "twine_durable"));
    }

    // Tarred Wool
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleBlocks.WOOL_TARRED),
          Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          8 * 60 * 20
      ).setRegistryName(ModulePyrotech.MOD_ID, "wool_tarred"));
    }

    // Pulp from reeds
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.PULP.asStack(),
        Ingredient.fromStacks(new ItemStack(Items.REEDS, 1, 0)),
        new FluidStack(FluidRegistry.WATER, 125),
        2 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "pulp_from_reeds"));

    // Pulp from wood chips
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.PULP.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        new FluidStack(FluidRegistry.WATER, 500),
        8 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "pulp_from_wood_chips"));

    // Tarred Planks
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleBlocks.PLANKS_TARRED),
          new OreIngredient("plankWood"),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 125),
          6 * 60 * 20
      ).setRegistryName(ModulePyrotech.MOD_ID, "planks_tarred"));
    }

    // Tarred Board
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.BOARD_TARRED.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.BOARD.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 50),
          (6 * 60 * 20) / 4
      ).setRegistryName(ModulePyrotech.MOD_ID, "board_tarred"));
    }

    // Slaked Lime
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.SLAKED_LIME.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.QUICKLIME.asStack()),
        new FluidStack(FluidRegistry.WATER, 125),
        2 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "slaked_lime"));

    // Podzol
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.DIRT, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.DIRT, 1, 1)),
        new FluidStack(FluidRegistry.WATER, 250),
        5 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "podzol"));

    // Mossy Stone Bricks
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.STONEBRICK, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.STONEBRICK, 1, 0)),
        new FluidStack(FluidRegistry.WATER, 250),
        5 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "mossy_stone_bricks"));

    // Mossy Cobblestone
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.MOSSY_COBBLESTONE),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE)),
        new FluidStack(FluidRegistry.WATER, 250),
        5 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "mossy_cobblestone"));

    // White Wool
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.WOOL, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)),
        new FluidStack(FluidRegistry.WATER, 250),
        5 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "white_wool"));

  }
}