package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackCrudeRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class DryingRackCrudeRecipesAdd {

  public static void apply(IForgeRegistryModifiable<DryingRackCrudeRecipe> registry) {

    // Dried Plant Fibers
    registry.register(new DryingRackCrudeRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.PLANT_FIBERS.asStack()),
        12 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "plant_fibers_dried"));

    // Dried Plant Fibers
    registry.register(new DryingRackCrudeRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        new OreIngredient("treeSapling"),
        12 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "plant_fibers_dried_from_sapling"));

    // Sponge
    registry.register(new DryingRackCrudeRecipe(
        new ItemStack(Blocks.SPONGE, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 1)),
        8 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "sponge"));
  }
}