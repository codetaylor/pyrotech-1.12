package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.*;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompactingBinRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.MechanicalCompactingBinRecipesAdd;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.CompactingBin")
@ZenDocPrepend({"docs/include/header.md"})
@ZenDocAppend({"docs/include/compacting_bin.example.md"})
@ZenClass("mods.pyrotech.CompactingBin")
public class ZenCompactingBin {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "amount", info = "number of input items required"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int amount, @Optional boolean inherited) {

    ZenCompactingBin.addRecipe(
        name,
        output,
        input,
        amount,
        ModuleTechBasicConfig.COMPACTING_BIN.TOOL_USES_REQUIRED_PER_HARVEST_LEVEL,
        inherited
    );
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "amount", info = "number of input items required"),
          @ZenDocArg(arg = "toolUsesRequired", info = "overrides default provided in config"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int amount, int[] toolUsesRequired, @Optional boolean inherited) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        amount,
        toolUsesRequired,
        inherited
    ));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredient to match")
      }
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

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE, "compacting bin"));
  }

  @ZenDocMethod(
      order = 5,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the device."
      }
  )
  @ZenMethod
  public static void setGameStages(ZenStages stages) {

    ModuleTechBasicConfig.STAGES_COMPACTING_BIN = stages.getStages();
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      CompactingBinRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing compacting recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final ItemStack output;
    private final Ingredient input;
    private final int amount;
    private final int[] uses;
    private final boolean inherited;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int amount,
        int[] uses,
        boolean inherited
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.amount = amount;
      this.uses = uses;
      this.inherited = inherited;
    }

    @Override
    public void apply() {

      CompactingBinRecipe recipe = new CompactingBinRecipe(
          this.output,
          this.input,
          this.amount,
          this.uses
      );

      ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));

      if (this.inherited) {

        if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechMachine.class)) {
          RecipeHelper.inherit("compacting_bin", ModuleTechMachine.Registries.MECHANICAL_COMPACTING_BIN_RECIPES, MechanicalCompactingBinRecipesAdd.INHERIT_TRANSFORMER, recipe);
        }
      }
    }

    @Override
    public String describe() {

      return "Adding compacting recipe for " + this.output + ", inherited=" + this.inherited;
    }
  }

}
