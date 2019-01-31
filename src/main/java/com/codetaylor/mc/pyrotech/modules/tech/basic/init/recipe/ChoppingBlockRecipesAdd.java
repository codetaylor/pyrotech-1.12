package com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.ChoppingBlockRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;

public class ChoppingBlockRecipesAdd {

  public static void apply(IForgeRegistry<ChoppingBlockRecipe> registry) {

    // --- Planks ---

    // Oak
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 0))
    ).setRegistryName(ModuleCore.MOD_ID, "planks_oak"));

    // Spruce
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 1))
    ).setRegistryName(ModuleCore.MOD_ID, "planks_spruce"));

    // Birch
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 2))
    ).setRegistryName(ModuleCore.MOD_ID, "planks_birch"));

    // Jungle
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 3))
    ).setRegistryName(ModuleCore.MOD_ID, "planks_jungle"));

    // Acacia
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 0))
    ).setRegistryName(ModuleCore.MOD_ID, "planks_acacia"));

    // Dark Oak
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 1))
    ).setRegistryName(ModuleCore.MOD_ID, "planks_dark_oak"));

    // --- Slabs ---

    String[] slabs = new String[]{
        "slab_oak",
        "slab_spruce",
        "slab_birch",
        "slab_jungle",
        "slab_acacia",
        "slab_dark_oak"
    };

    for (int i = 0; i < slabs.length; i++) {
      registry.register(new ChoppingBlockRecipe(
          new ItemStack(Blocks.WOODEN_SLAB, 1, i),
          Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, i)),
          ModuleTechBasicConfig.CHOPPING_BLOCK.CHOPS_REQUIRED_PER_HARVEST_LEVEL,
          new int[]{1, 2, 2, 3}
      ).setRegistryName(ModuleCore.MOD_ID, slabs[i]));
    }
  }
}