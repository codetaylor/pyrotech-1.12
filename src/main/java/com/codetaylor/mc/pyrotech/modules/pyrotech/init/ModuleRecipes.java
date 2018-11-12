package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnBrickRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.PitBurnRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class ModuleRecipes {

  public static void onRegisterRecipes(IForgeRegistry<IRecipe> registry) {

  }

  public static void onRegisterDryingRackRecipes(IForgeRegistry<DryingRackRecipe> registry) {

    // Straw
    registry.register(new DryingRackRecipe(
        ItemMaterial.EnumType.STRAW.asStack(),
        Ingredient.fromStacks(new ItemStack(Items.WHEAT)),
        12 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "straw"));

  }

  public static void onRegisterPitBurnRecipes(IForgeRegistry<PitBurnRecipe> registry) {

    // Coal
    registry.register(new PitBurnRecipe(
        new ItemStack(Items.COAL, 1, 1),
        new BlockMetaMatcher(ModuleBlocks.LOG_PILE, OreDictionary.WILDCARD_VALUE),
        10,
        12 * 60 * 20,
        new FluidStack(ModuleFluids.WOOD_TAR, 50),
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        },
        false,
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, "coal"));

    // Coal Coke
    registry.register(new PitBurnRecipe(
        ItemMaterial.EnumType.COAL_COKE.asStack(),
        new BlockMetaMatcher(Blocks.COAL_BLOCK, OreDictionary.WILDCARD_VALUE),
        10,
        24 * 60 * 20,
        new FluidStack(ModuleFluids.COAL_TAR, 50),
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        },
        true,
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, "coal_coke"));
  }

  public static void onRegisterKilnPitRecipes(IForgeRegistry<KilnPitRecipe> registry) {

    // Refractory Brick
    registry.register(new KilnPitRecipe(
        ItemMaterial.EnumType.REFRACTORY_BRICK.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_REFRACTORY_BRICK.asStack()),
        10 * 60 * 20,
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "refractory_brick"));

    // Quicklime
    registry.register(new KilnPitRecipe(
        new ItemStack(ModuleItems.QUICKLIME, 1, 0),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.LIMESTONE)),
        10 * 60 * 20,
        0.33f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "quicklime"));

  }

  public static void onRegisterKilnBrickRecipe(IForgeRegistry<KilnBrickRecipe> registry) {

    // Refractory Brick
    registry.register(new KilnBrickRecipe(
        ItemMaterial.EnumType.REFRACTORY_BRICK.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.UNFIRED_REFRACTORY_BRICK.asStack()),
        8 * 60 * 20,
        0.05f,
        new ItemStack[]{
            ItemMaterial.EnumType.POTTERY_FRAGMENTS.asStack(),
            ItemMaterial.EnumType.POTTERY_SHARD.asStack(),
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "refractory_brick"));

    // Quicklime
    registry.register(new KilnBrickRecipe(
        new ItemStack(ModuleItems.QUICKLIME, 1, 0),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.LIMESTONE)),
        8 * 60 * 20,
        0.05f,
        new ItemStack[]{
            ItemMaterial.EnumType.PIT_ASH.asStack()
        }
    ).setRegistryName(ModulePyrotech.MOD_ID, "quicklime"));

  }
}
