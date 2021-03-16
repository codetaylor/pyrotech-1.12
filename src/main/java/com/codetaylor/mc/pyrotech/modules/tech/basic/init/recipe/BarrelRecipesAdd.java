package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.BarrelRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

public class BarrelRecipesAdd {

  public static void apply(IForgeRegistry<BarrelRecipe> registry) {

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleHunting.class)) {
      registry.register(new BarrelRecipe(
          new FluidStack(ModuleHunting.Fluids.TANNIN, 1000),
          new Ingredient[]{
              new OreIngredient("treeLeaves"),
              new OreIngredient("treeLeaves"),
              new OreIngredient("treeLeaves"),
              new OreIngredient("treeLeaves")
          },
          new FluidStack(FluidRegistry.WATER, 1000),
          20 * 60 * 10
      ).setRegistryName(new ResourceLocation(ModuleTechBasic.MOD_ID, "tannin")));
    }

    registry.register(new BarrelRecipe(
        new FluidStack(ModuleCore.Fluids.PYROBERRY_WINE, 1000),
        new Ingredient[]{
            Ingredient.fromItems(ModuleCore.Items.PYROBERRIES),
            Ingredient.fromItems(ModuleCore.Items.PYROBERRIES),
            Ingredient.fromItems(ModuleCore.Items.PYROBERRIES),
            Ingredient.fromItems(Items.SUGAR)
        },
        new FluidStack(FluidRegistry.WATER, 1000),
        20 * 60 * 10
    ).setRegistryName(new ResourceLocation(ModuleTechBasic.MOD_ID, "pyroberry_wine")));
  }
}