package com.codetaylor.mc.pyrotech.modules.tech.refractory.init.recipe;

import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class BurnPitRecipesAdd {

  public static void apply(IForgeRegistry<PitBurnRecipe> registry) {

    // Charcoal
    registry.register(new PitBurnRecipe(
        new ItemStack(Items.COAL, 1, 1),
        new BlockMetaMatcher(ModuleBlocks.LOG_PILE, OreDictionary.WILDCARD_VALUE),
        10,
        12 * 60 * 20,
        new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 50),
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(8)
        },
        false,
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, "charcoal"));

    // Charcoal from Pile of Wood Chips
    registry.register(new PitBurnRecipe(
        new ItemStack(Items.COAL, 1, 1),
        new BlockMetaMatcher(ModuleBlocks.PILE_WOOD_CHIPS, 8),
        4,
        6 * 60 * 20,
        new FluidStack(ModuleTechRefractory.Fluids.WOOD_TAR, 50),
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(4)
        },
        false,
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, "charcoal_from_pile_wood_chips"));

    // Coal Coke
    registry.register(new PitBurnRecipe(
        ItemMaterial.EnumType.COAL_COKE.asStack(),
        new BlockMetaMatcher(Blocks.COAL_BLOCK, OreDictionary.WILDCARD_VALUE),
        10,
        24 * 60 * 20,
        new FluidStack(ModuleTechRefractory.Fluids.COAL_TAR, 50),
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.COAL_PIECES.asStack(2)
        },
        true,
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, "coal_coke"));
  }
}