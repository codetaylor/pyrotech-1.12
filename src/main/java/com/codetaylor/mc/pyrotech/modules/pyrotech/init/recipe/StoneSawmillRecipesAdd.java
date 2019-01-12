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

    // --- Wood Planks ---

    registerSawmillRecipeWood(registry, "planks_oak",
        new ItemStack(Blocks.PLANKS, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 0))
    );

    registerSawmillRecipeWood(registry, "planks_spruce",
        new ItemStack(Blocks.PLANKS, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 1))
    );

    registerSawmillRecipeWood(registry, "planks_birch",
        new ItemStack(Blocks.PLANKS, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 2))
    );

    registerSawmillRecipeWood(registry, "planks_jungle",
        new ItemStack(Blocks.PLANKS, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG, 1, 3))
    );

    registerSawmillRecipeWood(registry, "planks_acacia",
        new ItemStack(Blocks.PLANKS, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 0))
    );

    registerSawmillRecipeWood(registry, "planks_dark_oak",
        new ItemStack(Blocks.PLANKS, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 1))
    );

    // --- Wood Slabs ---

    registerSawmillRecipeWood(registry, "slab_oak",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 0))
    );

    registerSawmillRecipeWood(registry, "slab_spruce",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 1),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 1))
    );

    registerSawmillRecipeWood(registry, "slab_birch",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 2),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 2))
    );

    registerSawmillRecipeWood(registry, "slab_jungle",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 3))
    );

    registerSawmillRecipeWood(registry, "slab_acacia",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 4),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 4))
    );

    registerSawmillRecipeWood(registry, "slab_dark_oak",
        new ItemStack(Blocks.WOODEN_SLAB, 1, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.PLANKS, 1, 5))
    );

    // --- Stone ---

    // Sandstone Slab
    registerSawmillRecipeStone(registry, "sandstone_slab",
        new ItemStack(Blocks.STONE_SLAB, 2, 1),
        Ingredient.fromStacks(
            new ItemStack(Blocks.SANDSTONE, 1, 0),
            new ItemStack(Blocks.SANDSTONE, 1, 1),
            new ItemStack(Blocks.SANDSTONE, 1, 2)
        )
    );

    // Red Sandstone Slab
    registerSawmillRecipeStone(registry, "red_sandstone_slab",
        new ItemStack(Blocks.STONE_SLAB2, 2, 0),
        Ingredient.fromStacks(
            new ItemStack(Blocks.RED_SANDSTONE, 1, 0),
            new ItemStack(Blocks.RED_SANDSTONE, 1, 1),
            new ItemStack(Blocks.RED_SANDSTONE, 1, 2)
        )
    );

    // Brick Slab
    registerSawmillRecipeStone(registry, "brick_slab",
        new ItemStack(Blocks.STONE_SLAB, 2, 4),
        Ingredient.fromStacks(
            new ItemStack(Blocks.BRICK_BLOCK)
        )
    );

    // Nether Brick Slab
    registerSawmillRecipeStone(registry, "nether_brick_slab",
        new ItemStack(Blocks.STONE_SLAB, 2, 6),
        Ingredient.fromStacks(
            new ItemStack(Blocks.NETHER_BRICK)
        )
    );

    // Quartz Slab
    registerSawmillRecipeStone(registry, "quartz_slab",
        new ItemStack(Blocks.STONE_SLAB, 2, 7),
        Ingredient.fromStacks(
            new ItemStack(Blocks.QUARTZ_BLOCK, 1, 0),
            new ItemStack(Blocks.QUARTZ_BLOCK, 1, 1),
            new ItemStack(Blocks.QUARTZ_BLOCK, 1, 2)
        )
    );

    // Purpur Slab
    registerSawmillRecipeStone(registry, "purpur_slab",
        new ItemStack(Blocks.PURPUR_SLAB, 2, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.PURPUR_BLOCK, 1, 0))
    );

    registerSawmillRecipeStone(registry, "stone_slab",
        new ItemStack(Blocks.STONE_SLAB, 2, 0),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE, 1, 0))
    );

    registerSawmillRecipeStone(registry, "cobblestone_slab",
        new ItemStack(Blocks.STONE_SLAB, 2, 3),
        Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE, 1, 0))
    );

    registerSawmillRecipeStone(registry, "stone_bricks_slab",
        new ItemStack(Blocks.STONE_SLAB, 2, 5),
        Ingredient.fromStacks(new ItemStack(Blocks.STONEBRICK, 1, 0))
    );

    registerSawmillRecipeStone(registry, "stone_brick",
        ItemMaterial.EnumType.BRICK_STONE.asStack(2),
        Ingredient.fromStacks(new ItemStack(Blocks.STONE_SLAB, 1, 5))
    );

    // --- Misc ---

    // board
    registerSawmillRecipeWood(registry, "board",
        ItemMaterial.EnumType.BOARD.asStack(),
        new OreIngredient("slabWood")
    );

    // tarred board
    registerSawmillRecipeWood(registry, "board_tarred",
        ItemMaterial.EnumType.BOARD_TARRED.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleBlocks.PLANKS_TARRED))
    );
  }

  private static void registerSawmillRecipeStone(IForgeRegistryModifiable<MillStoneRecipe> registry, String name, ItemStack output, Ingredient input) {

    registry.register(new MillStoneRecipe(
        output.copy(),
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 4 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.IRON_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        false
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_iron"));

    registry.register(new MillStoneRecipe(
        output.copy(),
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 2 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.DIAMOND_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        false
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_diamond"));
  }

  private static void registerSawmillRecipeWood(IForgeRegistryModifiable<MillStoneRecipe> registry, String name, ItemStack output, Ingredient input) {

    output = output.copy();
    output.setCount(1);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 4 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.STONE_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        true
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
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_1"));

    output = output.copy();
    output.setCount(3);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 2 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.IRON_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_2"));

    output = output.copy();
    output.setCount(4);

    registry.register(new MillStoneRecipe(
        output,
        input,
        ModulePyrotechConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 20,
        Ingredient.fromStacks(new ItemStack(ModuleItems.DIAMOND_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        true
    ).setRegistryName(ModulePyrotech.MOD_ID, name + "_tier_3"));
  }
}