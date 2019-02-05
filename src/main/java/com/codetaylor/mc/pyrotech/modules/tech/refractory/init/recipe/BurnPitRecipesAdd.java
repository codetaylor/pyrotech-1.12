package com.codetaylor.mc.pyrotech.modules.tech.refractory.init.recipe;

import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
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
        new BlockMetaMatcher(ModuleCore.Blocks.LOG_PILE, OreDictionary.WILDCARD_VALUE),
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
    ).setRegistryName(ModuleTechRefractory.MOD_ID, "charcoal"));

    // Charcoal from Pile of Wood Chips
    registry.register(new PitBurnRecipe(
        new ItemStack(Items.COAL, 1, 1),
        new BlockMetaMatcher(ModuleCore.Blocks.PILE_WOOD_CHIPS, 8),
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
    ).setRegistryName(ModuleTechRefractory.MOD_ID, "charcoal_from_pile_wood_chips"));

    // Coal Coke
    registry.register(new PitBurnRecipe(
        ItemMaterial.EnumType.COAL_COKE.asStack(),
        new BlockMetaMatcher(Blocks.COAL_BLOCK, OreDictionary.WILDCARD_VALUE),
        10,
        24 * 60 * 20,
        new FluidStack(ModuleTechRefractory.Fluids.COAL_TAR, 50),
        0.15f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.COAL_PIECES.asStack(2)
        },
        true,
        true
    ).setRegistryName(ModuleTechRefractory.MOD_ID, "coal_coke"));

    // Wood Tar Block
    registry.register(new PitBurnRecipe(
        new ItemStack(Items.COAL, 1, 1),
        new BlockMetaMatcher(ModuleCore.Blocks.WOOD_TAR_BLOCK, OreDictionary.WILDCARD_VALUE),
        10,
        10 * 60 * 20,
        null,
        0.15f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack(),
            ItemMaterial.EnumType.CHARCOAL_FLAKES.asStack(4)
        },
        true,
        false
    ).setRegistryName(ModuleTechRefractory.MOD_ID, "charcoal_from_wood_tar_block"));
  }
}