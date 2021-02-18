package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.BarrelRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistry;

public class BarrelRecipesAdd {

  public static void apply(IForgeRegistry<BarrelRecipe> registry) {

    registry.register(new BarrelRecipe(
        new FluidStack(ModuleCore.Fluids.CLAY, 1000),
        new Ingredient[]{
            Ingredient.fromStacks(new ItemStack(Blocks.DIRT)),
            Ingredient.fromStacks(new ItemStack(Blocks.DIRT)),
            Ingredient.fromStacks(new ItemStack(Blocks.DIRT))
        },
        new FluidStack(FluidRegistry.WATER, 1000),
        20 * 10
    ).setRegistryName(new ResourceLocation(ModuleTechBasic.MOD_ID, "clay")));
  }
}