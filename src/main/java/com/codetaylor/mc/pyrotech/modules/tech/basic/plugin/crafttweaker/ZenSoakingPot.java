package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.*;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.SoakingPotRecipe;
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

@ZenDocClass("mods.pyrotech.SoakingPot")
@ZenDocPrepend({"docs/include/header.md"})
@ZenDocAppend({"docs/include/soaking_pot.example.md"})
@ZenClass("mods.pyrotech.SoakingPot")
public class ZenSoakingPot {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "inputFluid", info = "input fluid"),
          @ZenDocArg(arg = "inputItem", info = "input item"),
          @ZenDocArg(arg = "timeTicks", info = "recipe duration in ticks")
      }
  )
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

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredient to match")
      }
  )
  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenDocMethod(
      order = 3
  )
  @ZenMethod
  public static void removeAllRecipes() {

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBasic.Registries.SOAKING_POT_RECIPE, "soaking pot"));
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the device."
      }
  )
  @ZenMethod
  public static void setGameStages(ZenStages stages) {

    ModuleTechBasicConfig.STAGES_SOAKING_POT = stages.getStages();
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
      ModuleTechBasic.Registries.SOAKING_POT_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding soaking pot recipe for " + this.inputFluid;
    }
  }

}
