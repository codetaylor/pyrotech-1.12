package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.tools.*;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.BarrelRecipe;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.Barrel")
@ZenDocPrepend({"docs/include/header.md"})
@ZenDocAppend({"docs/include/barrel.example.md"})
@ZenClass("mods.pyrotech.Barrel")
public class ZenBarrel {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "outputFluid", info = "output fluid"),
          @ZenDocArg(arg = "inputFluid", info = "input fluid"),
          @ZenDocArg(arg = "inputItems", info = "input items"),
          @ZenDocArg(arg = "timeTicks", info = "recipe duration in ticks")
      }
  )
  @ZenMethod
  public static void addRecipe(
      String name,
      ILiquidStack outputFluid,
      ILiquidStack inputFluid,
      IIngredient[] inputItems,
      int timeTicks
  ) {

    FluidStack outputFluidStack = CraftTweakerMC.getLiquidStack(outputFluid);
    outputFluidStack.amount = Fluid.BUCKET_VOLUME;

    FluidStack inputFluidStack = CraftTweakerMC.getLiquidStack(inputFluid);
    inputFluidStack.amount = Fluid.BUCKET_VOLUME;

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        outputFluidStack,
        inputFluidStack,
        CTInputHelper.toIngredientArray(inputItems),
        timeTicks
    ));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredient to match")
      }
  )
  @ZenMethod
  public static void removeRecipes(ILiquidStack output) {

    FluidStack fluidStack = CraftTweakerMC.getLiquidStack(output);
    fluidStack.amount = Fluid.BUCKET_VOLUME;
    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(fluidStack));
  }

  @ZenDocMethod(
      order = 4
  )
  @ZenMethod
  public static void removeAllRecipes() {

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBasic.Registries.BARREL_RECIPE, "barrel"));
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

    ModuleTechBasicConfig.STAGES_BARREL = stages.getStages();
  }

  public static class RemoveRecipe
      implements IAction {

    private final FluidStack output;

    public RemoveRecipe(FluidStack output) {

      this.output = output;
    }

    @Override
    public void apply() {

      BarrelRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing barrel recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final FluidStack outputFluid;
    private final FluidStack inputFluid;
    private final Ingredient[] inputItems;
    private final int timeTicks;

    public AddRecipe(
        String name,
        FluidStack outputFluid,
        FluidStack inputFluid,
        Ingredient[] inputItems,
        int timeTicks
    ) {

      this.name = name;
      this.outputFluid = outputFluid;
      this.inputItems = inputItems;
      this.inputFluid = inputFluid;
      this.timeTicks = timeTicks;
    }

    @Override
    public void apply() {

      BarrelRecipe recipe = new BarrelRecipe(
          this.outputFluid,
          this.inputItems,
          this.inputFluid,
          this.timeTicks
      );
      ModuleTechBasic.Registries.BARREL_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding barrel recipe for " + this.inputFluid;
    }
  }

}
