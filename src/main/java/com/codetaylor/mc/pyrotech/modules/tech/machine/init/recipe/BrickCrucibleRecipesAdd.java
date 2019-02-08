package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.recipe.CompoundIngredientPublic;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.Arrays;

public class BrickCrucibleRecipesAdd {

  public static void apply(IForgeRegistry<BrickCrucibleRecipe> registry) {

    // Lava
    int lavaTimeOneBlockToOneQuarterBucket = 2 * 60 * 20;

    registry.register(new BrickCrucibleRecipe(
        new FluidStack(FluidRegistry.LAVA, 250),
        new CompoundIngredientPublic(Arrays.asList(
            new OreIngredient("stone"),
            new OreIngredient("stoneAndesite"),
            new OreIngredient("stoneAndesitePolished"),
            new OreIngredient("stoneDiorite"),
            new OreIngredient("stoneDioritePolished"),
            new OreIngredient("stoneGranite"),
            new OreIngredient("stoneGranitePolished"),
            new OreIngredient("cobblestone"),
            Ingredient.fromStacks(
                new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.ANDESITE.getMeta()),
                new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.DIORITE.getMeta()),
                new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.GRANITE.getMeta()),
                new ItemStack(ModuleCore.Blocks.COBBLESTONE, 1, BlockCobblestone.EnumType.LIMESTONE.getMeta())
            )
        )),
        lavaTimeOneBlockToOneQuarterBucket
    ).setRegistryName(ModuleTechMachine.MOD_ID, "lava_from_stone"));

    registry.register(new BrickCrucibleRecipe(
        new FluidStack(FluidRegistry.LAVA, 250),
        Ingredient.fromStacks(new ItemStack(Blocks.GRAVEL)),
        lavaTimeOneBlockToOneQuarterBucket
    ).setRegistryName(ModuleTechMachine.MOD_ID, "lava_from_gravel"));

    registry.register(new BrickCrucibleRecipe(
        new FluidStack(FluidRegistry.LAVA, 250),
        Ingredient.fromStacks(new ItemStack(Blocks.NETHERRACK)),
        lavaTimeOneBlockToOneQuarterBucket / 2
    ).setRegistryName(ModuleTechMachine.MOD_ID, "lava_from_netherrack"));

    registry.register(new BrickCrucibleRecipe(
        new FluidStack(FluidRegistry.LAVA, 25),
        Ingredient.fromStacks(
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.STONE.getMeta()),
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.GRANITE.getMeta()),
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.DIORITE.getMeta()),
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.ANDESITE.getMeta()),
            new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.LIMESTONE.getMeta())
        ),
        lavaTimeOneBlockToOneQuarterBucket / 8
    ).setRegistryName(ModuleTechMachine.MOD_ID, "lava_from_rocks"));
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<StoneCrucibleRecipe> stoneRegistry,
      IForgeRegistryModifiable<BrickCrucibleRecipe> brickRegistry
  ) {

    if (ModuleTechMachineConfig.BRICK_CRUCIBLE.INHERIT_STONE_TIER_RECIPES) {
      RecipeHelper.inherit("stone_crucible", stoneRegistry, brickRegistry, recipe -> {
        int timeTicks = (int) (recipe.getTimeTicks() * ModuleTechMachineConfig.BRICK_CRUCIBLE.INHERITED_STONE_TIER_RECIPE_SPEED_MODIFIER);
        return new BrickCrucibleRecipe(
            recipe.getOutput(),
            recipe.getInput(),
            Math.max(1, timeTicks)
        );
      });
    }

  }
}