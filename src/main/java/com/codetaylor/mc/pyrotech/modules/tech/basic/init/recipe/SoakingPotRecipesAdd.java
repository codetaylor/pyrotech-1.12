package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemRock;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
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

    // Dough
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.DOUGH.asStack(),
        new OreIngredient("dustWheat"),
        new FluidStack(FluidRegistry.WATER, 125),
        false,
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "dough"));

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleHunting.class)) {
      // Leather
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleHunting.Items.HIDE_TANNED),
          Ingredient.fromStacks(new ItemStack(ModuleHunting.Items.HIDE_WASHED)),
          new FluidStack(ModuleHunting.Fluids.TANNIN, 500),
          false,
          10 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "leather"));

      // Small Leather
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleHunting.Items.HIDE_SMALL_TANNED),
          Ingredient.fromStacks(new ItemStack(ModuleHunting.Items.HIDE_SMALL_WASHED)),
          new FluidStack(ModuleHunting.Fluids.TANNIN, 250),
          false,
          5 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "leather_small"));
    }

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      // Durable Leather
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.LEATHER_DURABLE.asStack(),
          new OreIngredient("leather"),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          true,
          5 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "durable_leather"));

      // Durable Leather Sheet
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.LEATHER_DURABLE_SHEET.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.LEATHER_SHEET.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          true,
          5 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "durable_leather_sheet"));

      // Durable Leather Strap
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.LEATHER_DURABLE_STRAP.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.LEATHER_STRAP.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          true,
          5 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "durable_leather_strap"));

      // Durable Leather Cord
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.LEATHER_DURABLE_CORD.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.LEATHER_CORD.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          true,
          5 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "durable_leather_cord"));
    }

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleHunting.class)) {
      // Small washed hide
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleHunting.Items.HIDE_SMALL_WASHED),
          Ingredient.fromStacks(new ItemStack(ModuleHunting.Items.HIDE_SMALL_SCRAPED)),
          new FluidStack(FluidRegistry.WATER, 250),
          true,
          2 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "hide_small_washed"));

      // Washed hide
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleHunting.Items.HIDE_WASHED),
          Ingredient.fromStacks(new ItemStack(ModuleHunting.Items.HIDE_SCRAPED)),
          new FluidStack(FluidRegistry.WATER, 250),
          true,
          2 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "hide_washed"));
    }

    // Sponge
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.SPONGE, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 0)),
        new FluidStack(FluidRegistry.WATER, 1000),
        false,
        1
    ).setRegistryName(ModuleTechBasic.MOD_ID, "sponge"));

    // Coal Block
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(Blocks.COAL_BLOCK),
          new OreIngredient("blockCharcoal"),
          new FluidStack(ModuleTechRefractory.Fluids.COAL_TAR, 1000),
          true,
          7 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "coal_block"));
    }

    // Mud
    registry.register(new SoakingPotRecipe(
        new ItemStack(ModuleCore.Blocks.MUD),
        new OreIngredient("dirt"),
        new FluidStack(FluidRegistry.WATER, 250),
        false,
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "mud"));

    // Mud Clump
    registry.register(new SoakingPotRecipe(
        new ItemStack(ModuleCore.Items.ROCK, 1, BlockRock.EnumType.MUD.getMeta()),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Items.ROCK, 1, BlockRock.EnumType.DIRT.getMeta())),
        new FluidStack(FluidRegistry.WATER, 125),
        false,
        4 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "mud_clump"));

    // Living Tar
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleCore.Blocks.LIVING_TAR),
          Ingredient.fromStacks(new ItemStack(Blocks.NETHERRACK)),
          new FluidStack(ModuleTechRefractory.Fluids.COAL_TAR, 1000),
          true,
          14 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "living_tar"));
    }

    // Flint Clay
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.FLINT_CLAY_BALL.asStack(),
        new OreIngredient("dustFlint"),
        new FluidStack(ModuleCore.Fluids.CLAY, 250),
        false,
        5 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "flint_clay"));

    // Tarred Kindling
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.KINDLING_TARRED.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.KINDLING.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 125),
          true,
          7 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "tarred_kindling_from_wood_tar"));
    }

    // Block of Wood Tar
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleCore.Blocks.WOOD_TAR_BLOCK),
          Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.THATCH)),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 1000),
          true,
          7 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "wood_tar_block_from_straw"));
    }

    // Durable Twine
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.TWINE_DURABLE.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.TWINE.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 125),
          true,
          4 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "twine_durable"));
    }

    // Tarred Wool
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleCore.Blocks.WOOL_TARRED),
          Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 250),
          true,
          4 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "wool_tarred"));
    }

    // Pulp from reeds
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.PULP.asStack(),
        Ingredient.fromStacks(new ItemStack(Items.REEDS, 1, 0)),
        new FluidStack(FluidRegistry.WATER, 125),
        false,
        4 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "pulp_from_reeds"));

    // Pulp from wood chips
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.PULP.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta())),
        new FluidStack(FluidRegistry.WATER, 500),
        false,
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "pulp_from_wood_chips"));

    // Tarred Planks
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          new ItemStack(ModuleCore.Blocks.PLANKS_TARRED),
          new OreIngredient("plankWood"),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 125),
          true,
          7 * 60 * 20
      ).setRegistryName(ModuleTechBasic.MOD_ID, "planks_tarred"));
    }

    // Tarred Board
    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechRefractory.class)) {
      registry.register(new SoakingPotRecipe(
          ItemMaterial.EnumType.BOARD_TARRED.asStack(),
          Ingredient.fromStacks(ItemMaterial.EnumType.BOARD.asStack()),
          new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 50),
          true,
          (7 * 60 * 20) / 4
      ).setRegistryName(ModuleTechBasic.MOD_ID, "board_tarred"));
    }

    // Slaked Lime
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.SLAKED_LIME.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.QUICKLIME.asStack()),
        new FluidStack(FluidRegistry.WATER, 125),
        false,
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "slaked_lime"));

    // Podzol
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.DIRT, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.DIRT, 1, 1)),
        new FluidStack(FluidRegistry.WATER, 250),
        false,
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "podzol"));

    // Mossy Stone Bricks
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.STONEBRICK, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.STONEBRICK, 1, 0)),
        new FluidStack(FluidRegistry.WATER, 250),
        false,
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "mossy_stone_bricks"));

    // Mossy Cobblestone
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.MOSSY_COBBLESTONE),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE)),
        new FluidStack(FluidRegistry.WATER, 250),
        false,
        7 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "mossy_cobblestone"));

    // White Wool
    registry.register(new SoakingPotRecipe(
        new ItemStack(Blocks.WOOL, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)),
        new FluidStack(FluidRegistry.WATER, 250),
        false,
        4 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "white_wool"));

  }
}