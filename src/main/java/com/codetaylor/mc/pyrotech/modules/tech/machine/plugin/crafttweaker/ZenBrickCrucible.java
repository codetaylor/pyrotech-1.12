package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickCrucibleRecipe;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.BrickCrucible")
@ZenClass("mods.pyrotech.BrickCrucible")
public class ZenBrickCrucible {

  @ZenDocMethod(
      order = 1,
      args = {"name", "output", "input", "burnTimeTicks"}
  )
  @ZenMethod
  public static void addRecipe(
      String name,
      ILiquidStack output,
      IIngredient input,
      int burnTimeTicks
  ) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getLiquidStack(output),
        CraftTweakerMC.getIngredient(input),
        burnTimeTicks
    ));
  }

  @ZenDocMethod(
      order = 2,
      args = {"output"}
  )
  @ZenMethod
  public static void removeRecipes(ILiquidStack output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getLiquidStack(output)));
  }

  @ZenDocMethod(
      order = 3
  )
  @ZenMethod
  public static void removeAllRecipes() {

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechMachine.Registries.BRICK_CRUCIBLE_RECIPES, "brick crucible"));
  }

  public static class RemoveRecipe
      implements IAction {

    private final FluidStack output;

    public RemoveRecipe(FluidStack output) {

      this.output = output;
    }

    @Override
    public void apply() {

      BrickCrucibleRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing brick crucible recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final FluidStack output;
    private final Ingredient input;
    private final int burnTimeTicks;

    public AddRecipe(
        String name,
        FluidStack output,
        Ingredient input,
        int burnTimeTicks
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.burnTimeTicks = burnTimeTicks;
    }

    @Override
    public void apply() {

      BrickCrucibleRecipe recipe = new BrickCrucibleRecipe(
          this.output,
          this.input,
          this.burnTimeTicks
      );
      ModuleTechMachine.Registries.BRICK_CRUCIBLE_RECIPES.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding brick crucible recipe for " + this.output;
    }
  }

}
