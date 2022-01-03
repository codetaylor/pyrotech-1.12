package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.*;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompostBinRecipe;
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

import java.util.Arrays;

@ZenDocClass("mods.pyrotech.CompostBin")
@ZenDocPrepend({"docs/include/header.md"})
@ZenDocAppend({"docs/include/compost_bin.example.md"})
@ZenClass("mods.pyrotech.CompostBin")
public class ZenCompostBin {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "input", info = "input ingredient to match")
      }
  )
  @ZenMethod
  public static void removeRecipeByInput(IIngredient input) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipeByInput(CraftTweakerMC.getIngredient(input)));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredient to match")
      }
  )
  @ZenMethod
  public static void removeRecipesByOutput(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipeByOutput(CraftTweakerMC.getIngredient(output)));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input")
      }
  )
  @ZenMethod
  public static void addRecipe(IIngredient output, IItemStack input) {

    IItemStack[] itemArray = input.getItemArray();
    Arrays.stream(itemArray).map(CraftTweakerMC::getItemStack).forEach(itemStack -> {

      CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
          CraftTweakerMC.getItemStack(output),
          CraftTweakerMC.getItemStack(input)
      ));
    });
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "compostValue", info = "range [1,16]")
      }
  )
  @ZenMethod
  public static void addRecipe(IIngredient output, IItemStack input, int compostValue) {

    IItemStack[] itemArray = input.getItemArray();
    Arrays.stream(itemArray).map(CraftTweakerMC::getItemStack).forEach(itemStack -> {

      CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
          CraftTweakerMC.getItemStack(output),
          CraftTweakerMC.getItemStack(input),
          compostValue
      ));
    });
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

    ModuleTechBasicConfig.STAGES_COMPOST_BIN = stages.getStages();
  }

  public static class RemoveRecipeByOutput
      implements IAction {

    private final Ingredient output;

    public RemoveRecipeByOutput(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      CompostBinRecipe.removeRecipesByOutput(this.output);
    }

    @Override
    public String describe() {

      return "Removing compost recipes for the output " + this.output;
    }
  }

  public static class RemoveRecipeByInput
      implements IAction {

    private final Ingredient input;

    public RemoveRecipeByInput(Ingredient input) {

      this.input = input;
    }

    @Override
    public void apply() {

      CompostBinRecipe.removeRecipesByInput(this.input);
    }

    @Override
    public String describe() {

      return "Removing compost recipes for the input " + this.input;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final ResourceLocation resourceLocation;
    private final ItemStack output;
    private final ItemStack input;
    private final int compostValue;

    public AddRecipe(
        ItemStack output,
        ItemStack input
    ) {

      this(output, input, 0);
    }

    public AddRecipe(
        ItemStack output,
        ItemStack input,
        int compostValue
    ) {

      this.input = input;
      this.output = output;
      this.compostValue = compostValue;
      this.resourceLocation = CompostBinRecipe.getResourceLocation("crafttweaker", input, input.getMetadata());
    }

    @Override
    public void apply() {

      CompostBinRecipe recipe;

      if (this.compostValue > 0) {
        recipe = new CompostBinRecipe(this.output, this.input, this.compostValue);

      } else {
        recipe = new CompostBinRecipe(this.output, this.input);
      }

      recipe.setRegistryName(this.resourceLocation);
      ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.register(recipe);
    }

    @Override
    public String describe() {

      return "Adding compost recipe for " + this.input + " -> " + this.output;
    }
  }
}