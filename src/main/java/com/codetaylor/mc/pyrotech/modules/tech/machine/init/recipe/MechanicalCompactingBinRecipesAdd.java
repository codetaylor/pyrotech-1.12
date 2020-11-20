package com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompactingBinRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.MechanicalCompactingBinRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.function.Function;

public class MechanicalCompactingBinRecipesAdd {

  public static final Function<CompactingBinRecipe, MechanicalCompactingBinRecipe> INHERIT_TRANSFORMER = recipe -> new MechanicalCompactingBinRecipe(
      recipe.getOutput(),
      recipe.getInput(),
      recipe.getAmount(),
      recipe.getRequiredToolUses()
  );

  public static void apply(IForgeRegistry<MechanicalCompactingBinRecipe> registry) {

    // Coal Block
    registry.register(new MechanicalCompactingBinRecipe(
        new ItemStack(Blocks.COAL_BLOCK, 1, 0),
        Ingredient.fromStacks(
            new ItemStack(Items.COAL, 1, 0)
        ),
        9,
        ModuleTechBasicConfig.COMPACTING_BIN.TOOL_USES_REQUIRED_PER_HARVEST_LEVEL
    ).setRegistryName(ModuleTechBasic.MOD_ID, "coal_block"));

    registry.register(new MechanicalCompactingBinRecipe(
        new ItemStack(Blocks.COAL_BLOCK),
        Ingredient.fromStacks(ItemMaterial.EnumType.COAL_PIECES.asStack()),
        72,
        ModuleTechBasicConfig.COMPACTING_BIN.TOOL_USES_REQUIRED_PER_HARVEST_LEVEL
    ).setRegistryName(ModuleTechMachine.MOD_ID, "coal_block_from_pieces"));

    registry.register(new MechanicalCompactingBinRecipe(
        new ItemStack(ModuleCore.Blocks.COAL_COKE_BLOCK),
        new OreIngredient("fuelCoke"),
        9,
        ModuleTechBasicConfig.COMPACTING_BIN.TOOL_USES_REQUIRED_PER_HARVEST_LEVEL
    ).setRegistryName(ModuleTechMachine.MOD_ID, "coal_coke_block"));
  }

  public static void registerInheritedRecipes(
      IForgeRegistryModifiable<CompactingBinRecipe> from,
      IForgeRegistryModifiable<MechanicalCompactingBinRecipe> to
  ) {

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)
        && ModuleTechMachineConfig.MECHANICAL_COMPACTING_BIN.INHERIT_COMPACTING_BIN_RECIPES) {
      RecipeHelper.inherit("compacting_bin", from, to, INHERIT_TRANSFORMER);
    }
  }
}