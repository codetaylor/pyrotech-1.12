package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CrudeDryingRackRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

public class CrudeDryingRackRecipesAdd {

  public static void apply(IForgeRegistry<CrudeDryingRackRecipe> registry) {

    // Straw
    registry.register(new CrudeDryingRackRecipe(
        ItemMaterial.EnumType.STRAW.asStack(),
        Ingredient.fromStacks(new ItemStack(Items.WHEAT)),
        12 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "straw"));

    // Dried Plant Fibers
    registry.register(new CrudeDryingRackRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.PLANT_FIBERS.asStack()),
        8 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "plant_fibers_dried"));

    // Dried Plant Fibers
    registry.register(new CrudeDryingRackRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        new OreIngredient("treeSapling"),
        10 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "plant_fibers_dried_from_sapling"));

    // Sponge
    registry.register(new CrudeDryingRackRecipe(
        new ItemStack(Blocks.SPONGE, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 1)),
        8 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "sponge"));

    // Paper
    registry.register(new CrudeDryingRackRecipe(
        new ItemStack(Items.PAPER, 1, 0),
        Ingredient.fromStacks(ItemMaterial.EnumType.PULP.asStack()),
        5 * 60 * 20
    ).setRegistryName(ModuleTechBasic.MOD_ID, "paper"));
  }
}