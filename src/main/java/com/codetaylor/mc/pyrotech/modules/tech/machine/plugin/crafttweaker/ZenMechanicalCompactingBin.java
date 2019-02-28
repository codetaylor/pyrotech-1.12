package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.MechanicalCompactingBinRecipe;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.MechanicalCompacter")
@ZenClass("mods.pyrotech.MechanicalCompacter")
public class ZenMechanicalCompactingBin {

  @ZenDocMethod(
      order = 1,
      args = {"name", "output", "input", "amount"}
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int amount) {

    ZenMechanicalCompactingBin.addRecipe(
        name,
        output,
        input,
        amount,
        ModuleTechBasicConfig.COMPACTING_BIN.TOOL_USES_REQUIRED_PER_HARVEST_LEVEL
    );
  }

  @ZenDocMethod(
      order = 2,
      args = {"name", "output", "input", "amount", "toolUsesRequired"}
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int amount, int[] toolUsesRequired) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        amount,
        toolUsesRequired
    ));
  }

  @ZenDocMethod(
      order = 3,
      args = {"output"}
  )
  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenDocMethod(
      order = 4
  )
  @ZenMethod
  public static void removeAllRecipes() {

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechMachine.Registries.MECHANICAL_COMPACTING_BIN_RECIPES, "mechanical compacting bin"));
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      MechanicalCompactingBinRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing mechanical compacting recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final ItemStack output;
    private final Ingredient input;
    private final int amount;
    private final int[] uses;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int amount,
        int[] uses
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.amount = amount;
      this.uses = uses;
    }

    @Override
    public void apply() {

      MechanicalCompactingBinRecipe recipe = new MechanicalCompactingBinRecipe(
          this.output,
          this.input,
          this.amount,
          this.uses
      );
      ModuleTechMachine.Registries.MECHANICAL_COMPACTING_BIN_RECIPES.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding mechanical compacting recipe for " + this.output;
    }
  }

}
