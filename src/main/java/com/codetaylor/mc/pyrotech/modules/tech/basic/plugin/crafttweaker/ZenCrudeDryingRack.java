package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.*;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe.DryingRackRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CrudeDryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.BrickOvenRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneOvenRecipesAdd;
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

@ZenDocClass("mods.pyrotech.CrudeDryingRack")
@ZenDocPrepend({"docs/include/header.md"})
@ZenDocAppend({"docs/include/drying_rack_crude.example.md"})
@ZenClass("mods.pyrotech.CrudeDryingRack")
public class ZenCrudeDryingRack {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "dryTimeTicks", info = "recipe duration in ticks"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int dryTimeTicks, @Optional boolean inherited) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        dryTimeTicks,
        inherited
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

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBasic.Registries.CRUDE_DRYING_RACK_RECIPE, "crude drying rack"));
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

    ModuleTechBasicConfig.STAGES_DRYING_RACK_CRUDE = stages.getStages();
  }

  @ZenDocMethod(
      order = 5,
      args = {
          @ZenDocArg(arg = "speed"),
          @ZenDocArg(arg = "biome")
      },
      description = {
          "Sets the device's base speed in the given biome."
      }
  )
  @ZenMethod
  public static void setBiomeSpeed(float speed, String biome) {

    ModuleTechBasicConfig.CrudeDryingRack.BIOME_MODIFIERS.put(biome, speed);
  }

  @ZenDocMethod(
      order = 6,
      args = {
          @ZenDocArg(arg = "speed"),
          @ZenDocArg(arg = "biomes")
      },
      description = {
          "Sets the device's base speed in the given biomes."
      }
  )
  @ZenMethod
  public static void setBiomeSpeed(float speed, String[] biomes) {

    for (String biome : biomes) {
      ModuleTechBasicConfig.CrudeDryingRack.BIOME_MODIFIERS.put(biome, speed);
    }
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      CrudeDryingRackRecipe.removeRecipes(this.output);
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
    private final boolean inherited;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int timeTicks,
        boolean inherited
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.timeTicks = timeTicks;
      this.inherited = inherited;
    }

    @Override
    public void apply() {

      CrudeDryingRackRecipe recipe = new CrudeDryingRackRecipe(
          this.output,
          this.input,
          this.timeTicks
      );

      ModuleTechBasic.Registries.CRUDE_DRYING_RACK_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));

      if (this.inherited) {
        DryingRackRecipe dryingRackRecipe = RecipeHelper.inherit("crude_drying_rack", ModuleTechBasic.Registries.DRYING_RACK_RECIPE, DryingRackRecipesAdd.INHERIT_TRANSFORMER, recipe);

        if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechMachine.class)) {
          StoneOvenRecipe stoneOvenRecipe = RecipeHelper.inherit("drying_rack", ModuleTechMachine.Registries.STONE_OVEN_RECIPES, StoneOvenRecipesAdd.INHERIT_TRANSFORMER, dryingRackRecipe);

          if (stoneOvenRecipe != null) {
            RecipeHelper.inherit("stone_oven", ModuleTechMachine.Registries.BRICK_OVEN_RECIPES, BrickOvenRecipesAdd.INHERIT_TRANSFORMER, stoneOvenRecipe);
          }
        }
      }
    }

    @Override
    public String describe() {

      return "Adding crude drying rack recipe for " + this.output + ", inherited=" + this.inherited;
    }
  }

}
