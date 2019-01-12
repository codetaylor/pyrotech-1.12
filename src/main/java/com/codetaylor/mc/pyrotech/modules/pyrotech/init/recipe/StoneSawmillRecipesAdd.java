package com.codetaylor.mc.pyrotech.modules.pyrotech.init.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.MillStoneRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class StoneSawmillRecipesAdd {

  public static void apply(IForgeRegistryModifiable<MillStoneRecipe> registry) {

    // --- Planks ---

    registerMillStoneRecipe(registry, "planks_oak",
        new ItemStack(Blocks.PLANKS, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 0)),
        true
    );

    registerMillStoneRecipe(registry, "planks_spruce",
        new ItemStack(Blocks.PLANKS, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 1)),
        true
    );

    registerMillStoneRecipe(registry, "planks_birch",
        new ItemStack(Blocks.PLANKS, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 2)),
        true
    );

    registerMillStoneRecipe(registry, "planks_jungle",
        new ItemStack(Blocks.PLANKS, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 3)),
        true
    );

    registerMillStoneRecipe(registry, "planks_acacia",
        new ItemStack(Blocks.PLANKS, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 0)),
        true
    );

    registerMillStoneRecipe(registry, "planks_dark_oak",
        new ItemStack(Blocks.PLANKS, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 1)),
        true
    );

    // --- Slabs ---

    registerMillStoneRecipe(registry, "slab_oak",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 0)),
        true
    );

    registerMillStoneRecipe(registry, "slab_spruce",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 1)),
        true
    );

    registerMillStoneRecipe(registry, "slab_birch",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 2)),
        true
    );

    registerMillStoneRecipe(registry, "slab_jungle",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 3)),
        true
    );

    registerMillStoneRecipe(registry, "slab_acacia",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 4)),
        true
    );

    registerMillStoneRecipe(registry, "slab_dark_oak",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 5)),
        true
    );

    // --- Misc ---

    // board
    registerMillStoneRecipe(registry, "board",
        ItemMaterial.EnumType.BOARD.asStack(),
        new OreIngredient("slabWood"),
        true
    );

    // tarred board
    registerMillStoneRecipe(registry, "board_tarred",
        ItemMaterial.EnumType.BOARD_TARRED.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.PLANKS_TARRED)),
        true
    );
  }

  private static void registerMillStoneRecipe(IForgeRegistryModifiable<MillStoneRecipe> registry, String name, ItemStack output, Ingredient input, boolean createWoodChips) {

    output = output.copy();
    output.setCount(1);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 4 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.STONE_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        createWoodChips
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_0"));

    output = output.copy();
    output.setCount(2);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 3 * 20,
        Ingredient.fromStacks(
            new ItemStack(ModuleItems.FLINT_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE),
            new ItemStack(ModuleItems.BONE_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)
        ),
        createWoodChips
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_1"));

    output = output.copy();
    output.setCount(3);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 2 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.IRON_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        createWoodChips
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_2"));

    output = output.copy();
    output.setCount(4);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.DIAMOND_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        createWoodChips
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_3"));
  }
}