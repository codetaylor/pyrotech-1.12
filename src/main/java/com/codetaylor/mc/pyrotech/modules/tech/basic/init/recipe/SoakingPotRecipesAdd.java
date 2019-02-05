package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
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
    ).setRegistryName(ModuleTechBasic.MOD_ID, "sponge"));

    // Tarred Kindling
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.KINDLING_TARRED.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.KINDLING.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          7 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "tarred_kindling_from_wood_tar"));
    }

    // Block of Wood Tar
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleCore.Blocks.WOOD_TAR_BLOCK),
          Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.THATCH)),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 1000),
          7 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "wood_tar_block_from_straw"));
    }

    // Durable Twine
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.TWINE_DURABLE.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.TWINE.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          4 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "twine_durable"));
    }

    // Tarred Wool
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleCore.Blocks.WOOL_TARRED),
          Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          4 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "wool_tarred"));
    }

    // Pulp from reeds
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.PULP.asStack(),
        Ingredient.fromStacks(new ItemStack(Items.REEDS, 1, 0)),
        new FluidStack(FluidRegistry.WATER, 125),
        4 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "pulp_from_reeds"));

    // Pulp from wood chips
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.PULP.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        new FluidStack(FluidRegistry.WATER, 500),
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "pulp_from_wood_chips"));

    // Tarred Planks
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleCore.Blocks.PLANKS_TARRED),
          new OreIngredient("plankWood"),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 125),
          7 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "planks_tarred"));
    }

    // Tarred Board
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.BOARD_TARRED.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.BOARD.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 50),
          (7 * 60 * 20) / 4
      ).setRegistryName(ModuleTechBasic.MOD_ID, "board_tarred"));
    }

    // Slaked Lime
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.SLAKED_LIME.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.QUICKLIME.asStack()),
        new FluidStack(FluidRegistry.WATER, 125),
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "slaked_lime"));

    // Podzol
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.DIRT, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.DIRT, 1, 1)),
        new FluidStack(FluidRegistry.WATER, 250),
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "podzol"));

    // Mossy Stone Bricks
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.STONEBRICK, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.STONEBRICK, 1, 0)),
        new FluidStack(FluidRegistry.WATER, 250),
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "mossy_stone_bricks"));

    // Mossy Cobblestone
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.MOSSY_COBBLESTONE),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE)),
        new FluidStack(FluidRegistry.WATER, 250),
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "mossy_cobblestone"));

    // White Wool
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.WOOL, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)),
        new FluidStack(FluidRegistry.WATER, 250),
        4 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "white_wool"));

  }
}