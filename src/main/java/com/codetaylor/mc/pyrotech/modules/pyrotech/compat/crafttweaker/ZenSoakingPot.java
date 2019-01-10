package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.SoakingPotRecipe;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.pyrotech.SoakingPot")
public class ZenSoakingPot {

  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenMethod
  public static void addRecipe(
      String name,
      IItemStack output,
      ILiquidStack inputFluid,
      IIngredient inputItem,
      int timeTicks
  ) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getLiquidStack(inputFluid),
        CraftTweakerMC.getIngredient(inputItem),
        timeTicks
    ));
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      SoakingPotRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing soaking pot recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final ItemStack output;
    private final FluidStack inputFluid;
    private final Ingredient inputItem;
    private final int timeTicks;

    public AddRecipe(
        String name,
        ItemStack output,
        FluidStack inputFluid,
        Ingredient inputItem,
        int timeTicks
    ) {

      this.name = name;
      this.output = output;
      this.inputItem = inputItem;
      this.inputFluid = inputFluid;
      this.timeTicks = timeTicks;
    }

    @Override
    public void apply() {

      SoakingPotRecipe recipe = new SoakingPotRecipe(
          this.output,
          this.inputItem,
          this.inputFluid,
          this.timeTicks
      );
      ModulePyrotechRegistries.SOAKING_POT_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding soaking pot recipe for " + this.inputFluid;
    }
  }

}
