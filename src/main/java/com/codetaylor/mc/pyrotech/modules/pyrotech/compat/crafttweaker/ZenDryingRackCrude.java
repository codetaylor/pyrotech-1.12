package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackCrudeRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
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

@ZenDocClass("mods.pyrotech.CrudeDryingRack")
@ZenClass("mods.pyrotech.CrudeDryingRack")
public class ZenDryingRackCrude {

  @ZenDocMethod(
      order = 1,
      args = {"name", "output", "input", "dryTimeTicks"}
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int dryTimeTicks) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        dryTimeTicks
    ));
  }

  @ZenDocMethod(
      order = 2,
      args = {"output"}
  )
  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      DryingRackRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing crude drying rack recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final ItemStack output;
    private final String name;
    private final Ingredient input;
    private final int timeTicks;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int timeTicks
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.timeTicks = timeTicks;
    }

    @Override
    public void apply() {

      DryingRackCrudeRecipe recipe = new DryingRackCrudeRecipe(
          this.output,
          this.input,
          this.timeTicks
      );
      ModPyrotechRegistries.DRYING_RACK_CRUDE_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding crude drying rack recipe for " + this.output;
    }
  }

}
