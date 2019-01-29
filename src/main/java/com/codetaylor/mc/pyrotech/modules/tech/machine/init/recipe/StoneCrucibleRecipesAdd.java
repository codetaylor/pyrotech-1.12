package com.codetaylor.mc.pyrotech.modules.tech.machine.init;

import com.codetaylor.mc.athenaeum.recipe.CompoundIngredientPublic;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.CrucibleStoneRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.Arrays;

public class StoneCrucibleRecipesAdd {

  public static void apply(IForgeRegistryModifiable<CrucibleStoneRecipe> registry) {

    // Lava
    //noinspection unchecked
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.LAVA, 250),
        new CompoundIngredientPublic(Arrays.asList(new Ingredient[]{
            new OreIngredient("stone"),
            new OreIngredient("cobblestone")
        })),
        2 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "lava_from_stone"));

    // Water from Ice
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.WATER, 1000),
        Ingredient.fromStacks(
            new ItemStack(Blocks.ICE, 1, 0)
        ),
        60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "water_from_ice"));

    // Water from Snow
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.WATER, 500),
        Ingredient.fromStacks(
            new ItemStack(Blocks.SNOW, 1, 0)
        ),
        15 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "water_from_snow"));

    // Water from Snowballs
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.WATER, 125),
        Ingredient.fromStacks(
            new ItemStack(Items.SNOWBALL, 1, 0)
        ),
        15 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "water_from_snowballs"));

    // Water from Packed Ice
    registry.register(new CrucibleStoneRecipe(
        new FluidStack(FluidRegistry.WATER, 2000),
        Ingredient.fromStacks(
            new ItemStack(Blocks.PACKED_ICE, 1, 0)
        ),
        4 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "water_from_packed_ice"));
  }
}