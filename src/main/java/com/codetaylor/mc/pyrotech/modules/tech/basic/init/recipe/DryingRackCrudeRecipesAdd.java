package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackCrudeRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

    // Paper
    registry.register(new DryingRackCrudeRecipe(
        new ItemStack(Items.PAPER, 1, 0),
        Ingredient.fromStacks(ItemMaterial.EnumType.PULP.asStack()),
        8 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "paper"));
  }
}