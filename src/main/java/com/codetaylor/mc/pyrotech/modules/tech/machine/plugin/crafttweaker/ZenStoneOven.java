package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.tools.ZenDocAppend;
import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.BrickOvenRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
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

@ZenDocClass("mods.pyrotech.StoneOven")
@ZenDocAppend({"docs/include/stone_oven.example.md"})
@ZenClass("mods.pyrotech.StoneOven")
public class ZenStoneOven {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output"),
          @ZenDocArg(arg = "input"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, @Optional boolean inherited) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        inherited
    ));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "output")
      }
  )
  @ZenMethod
  public static void blacklistSmeltingRecipes(IIngredient[] output) {

    CraftTweaker.LATE_ACTIONS.add(new IAction() {

      @Override
      public void apply() {

        for (IIngredient ingredient : output) {
          StoneOvenRecipe.blacklistSmeltingRecipe(CraftTweakerMC.getIngredient(ingredient));
        }
      }

      @Override
      public String describe() {

        return "Blacklisting smelting recipes for stone oven: " + CTLogHelper.getStackDescription(output);
      }
    });
  }

  @ZenDocMethod(
      order = 3,
      description = "Blacklist all smelting recipes."
  )
  @ZenMethod
  public static void blacklistAllSmeltingRecipes() {

    StoneOvenRecipe.blacklistAll();
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "output")
      }
  )
  @ZenMethod
  public static void whitelistSmeltingRecipes(IIngredient[] output) {

    CraftTweaker.LATE_ACTIONS.add(new IAction() {

      @Override
      public void apply() {

        for (IIngredient ingredient : output) {
          StoneOvenRecipe.whitelistSmeltingRecipe(CraftTweakerMC.getIngredient(ingredient));
        }
      }

      @Override
      public String describe() {

        return "Whitelisting smelting recipes for stone oven: " + CTLogHelper.getStackDescription(output);
      }
    });
  }

  @ZenDocMethod(
      order = 5,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredient to match")
      }
  )
  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenDocMethod(
      order = 6,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the device."
      }
  )
  @ZenMethod
  public static void setGameStages(ZenStages stages) {

    ModuleTechMachineConfig.STAGES_STONE_OVEN = stages.getStages();
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      StoneOvenRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing stone oven recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final ItemStack output;
    private final boolean inherited;
    private final Ingredient input;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        boolean inherited
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.inherited = inherited;
    }

    @Override
    public void apply() {

      StoneOvenRecipe recipe = new StoneOvenRecipe(
          this.output,
          this.input
      );

      ModuleTechMachine.Registries.STONE_OVEN_RECIPES.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));

      if (this.inherited) {
        RecipeHelper.inherit("stone_oven", ModuleTechMachine.Registries.BRICK_OVEN_RECIPES, BrickOvenRecipesAdd.INHERIT_TRANSFORMER, recipe);
      }
    }

    @Override
    public String describe() {

      return "Adding stone oven recipe for " + this.output + ", inherited=" + this.inherited;
    }
  }

}
