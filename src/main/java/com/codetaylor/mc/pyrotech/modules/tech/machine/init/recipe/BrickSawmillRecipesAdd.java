package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickSawmillRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class BrickSawmillRecipesAdd {

  public static void apply(IForgeRegistryModifiable<BrickSawmillRecipe> registry) {

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

  }

  private static void registerSawmillRecipeStone(IForgeRegistryModifiable<BrickSawmillRecipe> registry, String name, ItemStack output, Ingredient input) {

    registry.register(new BrickSawmillRecipe(
        output.copy(),
        input,
        ModuleTechMachineConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 8 * 20,
        Ingredient.fromStacks(
            new ItemStack(ModuleTechMachine.Items.IRON_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE),
            new ItemStack(ModuleTechMachine.Items.OBSIDIAN_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)
        ),
        0
    ).setRegistryName(ModuleTechMachine.MOD_ID, name + "_iron"));

    registry.register(new BrickSawmillRecipe(
        output.copy(),
        input,
        ModuleTechMachineConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 4 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleTechMachine.Items.DIAMOND_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        0
    ).setRegistryName(ModuleTechMachine.MOD_ID, name + "_diamond"));
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<StoneSawmillRecipe> stoneRegistry,
      IForgeRegistryModifiable<BrickSawmillRecipe> brickRegistry
  ) {

    if (ModuleTechMachineConfig.BRICK_SAWMILL.INHERIT_STONE_TIER_RECIPES) {
      RecipeHelper.inherit("stone_sawmill", stoneRegistry, brickRegistry, recipe -> {
        int cookTimeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.BRICK_SAWMILL.INHERITED_STONE_TIER_RECIPE_DURATION_MODIFIER);
        return new BrickSawmillRecipe(
            recipe.getOutput(),
            recipe.getInput(),
            Math.max(1, cookTimeTicks),
            recipe.getBlade(),
            recipe.getWoodChips()
        );
      });
    }
  }
}