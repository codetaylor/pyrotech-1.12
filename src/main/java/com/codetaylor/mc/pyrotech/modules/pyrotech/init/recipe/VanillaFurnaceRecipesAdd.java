package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class VanillaFurnaceRecipesAdd {

  public static void apply() {

    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.ANDESITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.getMetadata()),
        0.1f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.GRANITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.GRANITE.getMetadata()),
        0.1f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.DIORITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.DIORITE.getMetadata()),
        0.1f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.LIMESTONE.asStack(),
        new ItemStack(ModuleBlocks.LIMESTONE),
        0.1f
    );
  }
}