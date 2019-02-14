package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class StoneSawmillRecipesAdd {

  public static void apply(IForgeRegistryModifiable<StoneSawmillRecipe> registry) {

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

    // --- Misc ---

    // board
    registerSawmillRecipeWood(registry, "board",
        ItemMaterial.EnumType.BOARD.asStack(),
        new OreIngredient("slabWood")
    );

    // stick
    registerSawmillRecipeWood(registry, "stick",
        new ItemStack(Items.STICK),
        Ingredient.fromStacks(ItemMaterial.EnumType.BOARD.asStack())
    );

    // tarred board
    registerSawmillRecipeWood(registry, "board_tarred",
        ItemMaterial.EnumType.BOARD_TARRED.asStack(),
        Ingredient.fromStacks(new ItemStack(ModuleCore.Blocks.PLANKS_TARRED))
    );
  }

  private static void registerSawmillRecipeWood(IForgeRegistryModifiable<StoneSawmillRecipe> registry, String name, ItemStack output, Ingredient input) {

    int woodChips = 8;
    output = output.copy();
    output.setCount(1);

    registry.register(new StoneSawmillRecipe(
        output,
        input,
        ModuleTechMachineConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 8 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleTechMachine.Items.STONE_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        woodChips
    ).setRegistryName(ModuleTechMachine.MOD_ID, name + "_tier_0"));

    output = output.copy();
    output.setCount(2);

    registry.register(new StoneSawmillRecipe(
        output,
        input,
        ModuleTechMachineConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 6 * 20,
        Ingredient.fromStacks(
            new ItemStack(ModuleTechMachine.Items.FLINT_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE),
            new ItemStack(ModuleTechMachine.Items.BONE_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)
        ),
        woodChips
    ).setRegistryName(ModuleTechMachine.MOD_ID, name + "_tier_1"));

    output = output.copy();
    output.setCount(3);

    registry.register(new StoneSawmillRecipe(
        output,
        input,
        ModuleTechMachineConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 4 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleTechMachine.Items.IRON_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        woodChips
    ).setRegistryName(ModuleTechMachine.MOD_ID, name + "_tier_2"));

    output = output.copy();
    output.setCount(4);

    registry.register(new StoneSawmillRecipe(
        output,
        input,
        ModuleTechMachineConfig.STONE_SAWMILL.INPUT_SLOT_SIZE * 2 * 20,
        Ingredient.fromStacks(new ItemStack(ModuleTechMachine.Items.DIAMOND_MILL_BLADE, 1, OreDictionary.WILDCARD_VALUE)),
        woodChips
    ).setRegistryName(ModuleTechMachine.MOD_ID, name + "_tier_3"));
  }
}