package com.codetaylor.mc.pyrotech.modules.core.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
        new ItemStack(ModuleCore.Blocks.LIMESTONE),
        0.1f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        ItemMaterial.EnumType.UNFIRED_BRICK.asStack(),
        new ItemStack(Items.BRICK),
        0.4f
    );

    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Items.APPLE),
        new ItemStack(ModuleCore.Items.APPLE_BAKED),
        0.1f
    );
  }
}