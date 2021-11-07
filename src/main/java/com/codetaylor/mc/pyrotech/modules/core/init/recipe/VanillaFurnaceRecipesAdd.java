package com.codetaylor.mc.pyrotech.modules.core.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class VanillaFurnaceRecipesAdd {

  public static void apply() {

    // andesite from cobbled andesite
    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.ANDESITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.getMetadata()),
        0.1f
    );

    // granite from cobbled granite
    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.GRANITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.GRANITE.getMetadata()),
        0.1f
    );

    // diorite from cobbled diorite
    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.DIORITE.asStack(),
        new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.DIORITE.getMetadata()),
        0.1f
    );

    // limestone from cobbled limestone
    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockCobblestone.EnumType.LIMESTONE.asStack(),
        new ItemStack(ModuleCore.Blocks.LIMESTONE),
        0.1f
    );

    // brick from unfired brick
    FurnaceRecipes.instance().addSmeltingRecipe(
        ItemMaterial.EnumType.UNFIRED_BRICK.asStack(),
        new ItemStack(Items.BRICK),
        0.4f
    );

    // baked apple from apple
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Items.APPLE),
        new ItemStack(ModuleCore.Items.APPLE_BAKED),
        0.1f
    );

    // roasted carrot from carrot
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Items.CARROT),
        new ItemStack(ModuleCore.Items.CARROT_ROASTED),
        0.1f
    );

    // roasted egg from egg
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Items.EGG),
        new ItemStack(ModuleCore.Items.EGG_ROASTED),
        0.1f
    );

    // roasted brown mushroom from brown mushroom
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Blocks.BROWN_MUSHROOM),
        new ItemStack(ModuleCore.Items.MUSHROOM_BROWN_ROASTED),
        0.1f
    );

    // roasted red mushroom from red mushroom
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Blocks.RED_MUSHROOM),
        new ItemStack(ModuleCore.Items.MUSHROOM_RED_ROASTED),
        0.1f
    );

    // roasted beetroot from beetroot
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Items.BEETROOT),
        new ItemStack(ModuleCore.Items.BEETROOT_ROASTED),
        0.1f
    );

    // refractory brick from unfired refractory brick
    FurnaceRecipes.instance().addSmeltingRecipe(
        ItemMaterial.EnumType.UNFIRED_REFRACTORY_BRICK.asStack(),
        ItemMaterial.EnumType.REFRACTORY_BRICK.asStack(),
        0.1f
    );

    // stone slab from cobblestone slab
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Blocks.STONE_SLAB, 1, 3),
        new ItemStack(Blocks.STONE_SLAB, 1, 0),
        0.1f
    );

    // charcoal flakes from wood chips
    FurnaceRecipes.instance().addSmeltingRecipe(
        BlockRock.EnumType.WOOD_CHIPS.asStack(),
        ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(),
        0.1f
    );

    // quicklime from crushed limestone
    FurnaceRecipes.instance().addSmeltingRecipe(
        ItemMaterial.EnumType.DUST_LIMESTONE.asStack(),
        ItemMaterial.EnumType.QUICKLIME.asStack(),
        0.1f
    );

    // cobblestone from gravel
    FurnaceRecipes.instance().addSmeltingRecipe(
        new ItemStack(Blocks.GRAVEL),
        new ItemStack(Blocks.COBBLESTONE),
        0.1f
    );

    // bread from bread dough
    FurnaceRecipes.instance().addSmeltingRecipe(
        ItemMaterial.EnumType.BREAD_DOUGH.asStack(),
        new ItemStack(Items.BREAD),
        0.1f
    );

    // cookies from cookie dough
    FurnaceRecipes.instance().addSmeltingRecipe(
        ItemMaterial.EnumType.COOKIE_DOUGH.asStack(),
        new ItemStack(Items.COOKIE, 8),
        0.1f
    );
  }
}