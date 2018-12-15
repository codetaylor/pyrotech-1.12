package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class ModuleRecipes {

  public static void onRegisterRecipes(IForgeRegistry<IRecipe> registry) {

    IForgeRegistryModifiable modifiableRegistry = (IForgeRegistryModifiable) registry;

    for (String resourceName : ModulePyrotechConfig.RECIPES.VANILLA_REMOVE) {
      modifiableRegistry.remove(new ResourceLocation(resourceName));
    }
  }

  public static void onRegisterGraniteAnvilRecipes(IForgeRegistry<GraniteAnvilRecipe> registry) {

    // Stone Bricks
    registry.register(new GraniteAnvilRecipe(
        ItemMaterial.EnumType.BRICK_STONE.asStack(4),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE)),
        8
    ).setRegistryName(ModulePyrotech.MOD_ID, "brick_stone"));
  }

  public static void onRegisterChoppingBlockRecipes(IForgeRegistry<ChoppingBlockRecipe> registry) {

    // --- Planks ---

    // Oak
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 0))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_oak"));

    // Spruce
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 1))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_spruce"));

    // Birch
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 2))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_birch"));

    // Jungle
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 3))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_jungle"));

    // Acacia
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 0))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_acacia"));

    // Dark Oak
    registry.register(new ChoppingBlockRecipe(
        new ItemStack(Blocks.PLANKS, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 1))
    ).setRegistryName(ModulePyrotech.MOD_ID, "planks_dark_oak"));

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
          ModulePyrotechConfig.CHOPPING_BLOCK.CHOPS_REQUIRED_PER_HARVEST_LEVEL,
          new int[]{1, 2, 2, 3}
      ).setRegistryName(ModulePyrotech.MOD_ID, slabs[i]));
    }
  }

  public static void onRegisterDryingRackRecipes(IForgeRegistry<DryingRackRecipe> registry) {

    // Straw
    registry.register(new DryingRackRecipe(
        ItemMaterial.EnumType.STRAW.asStack(),
        Ingredient.fromStacks(new ItemStack(Items.WHEAT)),
        12 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "straw"));

    // Dried Plant Fibers
    registry.register(new DryingRackRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.PLANT_FIBERS.asStack()),
        6 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "plant_fibers_dried"));

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

  public static void onRegisterKilnStoneRecipe(IForgeRegistry<KilnStoneRecipe> registry) {

    // Refractory Brick
    registry.register(new KilnStoneRecipe(
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
    registry.register(new KilnStoneRecipe(
        new ItemStack(ModuleItems.QUICKLIME, 1, 0),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.LIMESTONE)),
        8 * 60 * 20,
        0.05f,
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

  public static void onRegisterDryingRackCrudeRecipes(IForgeRegistryModifiable<DryingRackCrudeRecipe> registry) {

    // Dried Plant Fibers
    registry.register(new DryingRackCrudeRecipe(
        ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack(),
        Ingredient.fromStacks(ItemMaterial.EnumType.PLANT_FIBERS.asStack()),
        12 * 60 * 20
    ).setRegistryName(ModulePyrotech.MOD_ID, "plant_fibers_dried"));

  }
}
