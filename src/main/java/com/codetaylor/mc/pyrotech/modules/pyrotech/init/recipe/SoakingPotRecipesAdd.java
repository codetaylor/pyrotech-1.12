package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.SoakingPotRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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

    // Slaked Lime
    registry.register(new SoakingPotRecipe(
        ItemMaterial.EnumType.SLAKED_LIME.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleItems.QUICKLIME)),
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
        Ingredient.fromStacks(
            new ItemStack(Blocks.WOOL, 1, 1),
            new ItemStack(Blocks.WOOL, 1, 2),
            new ItemStack(Blocks.WOOL, 1, 3),
            new ItemStack(Blocks.WOOL, 1, 4),
            new ItemStack(Blocks.WOOL, 1, 5),
            new ItemStack(Blocks.WOOL, 1, 6),
            new ItemStack(Blocks.WOOL, 1, 7),
            new ItemStack(Blocks.WOOL, 1, 8),
            new ItemStack(Blocks.WOOL, 1, 9),
            new ItemStack(Blocks.WOOL, 1, 10),
            new ItemStack(Blocks.WOOL, 1, 11),
            new ItemStack(Blocks.WOOL, 1, 12),
            new ItemStack(Blocks.WOOL, 1, 13),
            new ItemStack(Blocks.WOOL, 1, 14),
            new ItemStack(Blocks.WOOL, 1, 15)
        ),
        new FluidStack(FluidRegistry.WATER, 250),
        5 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "white_wool"));

  }
}