package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.*;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.BrickKilnRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneKilnRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
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

@ZenDocClass("mods.pyrotech.PitKiln")
@ZenDocPrepend({"docs/include/header.md"})
@ZenDocAppend({"docs/include/pit_kiln.example.md"})
@ZenClass("mods.pyrotech.PitKiln")
public class ZenKilnPit {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "burnTimeTicks", info = "recipe duration in ticks"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int burnTimeTicks, @Optional boolean inherited) {

    ZenKilnPit.addRecipe(name, output, input, burnTimeTicks, 0, new IItemStack[0], inherited);
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "burnTimeTicks", info = "recipe duration in ticks"),
          @ZenDocArg(arg = "failureChance", info = "chance for item to fail conversion"),
          @ZenDocArg(arg = "failureItems", info = "array of randomly chosen failure items"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      }
  )
  @ZenMethod
  public static void addRecipe(
      String name,
      IItemStack output,
      IIngredient input,
      int burnTimeTicks,
      float failureChance,
      IItemStack[] failureItems,
      @Optional boolean inherited
  ) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        burnTimeTicks,
        failureChance,
        CraftTweakerMC.getItemStacks(failureItems),
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

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBasic.Registries.KILN_PIT_RECIPE, "pit kiln"));
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

    ModuleTechBasicConfig.STAGES_PIT_KILN = stages.getStages();
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      KilnPitRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing pit kiln recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final ItemStack output;
    private final String name;
    private final Ingredient input;
    private final int burnTimeTicks;
    private final float failureChance;
    private final ItemStack[] failureItems;
    private final boolean inherited;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int burnTimeTicks,
        float failureChance,
        ItemStack[] failureItems,
        boolean inherited
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.burnTimeTicks = burnTimeTicks;
      this.failureChance = failureChance;
      this.failureItems = failureItems;
      this.inherited = inherited;
    }

    @Override
    public void apply() {

      KilnPitRecipe recipe = new KilnPitRecipe(
          this.output, this.input,
          this.burnTimeTicks,
          this.failureChance,
          this.failureItems
      );

      ModuleTechBasic.Registries.KILN_PIT_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));

      if (this.inherited) {

        if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechMachine.class)) {
          StoneKilnRecipe stoneKilnRecipe = RecipeHelper.inherit("pit_kiln", ModuleTechMachine.Registries.STONE_KILN_RECIPES, StoneKilnRecipesAdd.INHERIT_TRANSFORMER, recipe);
          RecipeHelper.inherit("stone_kiln", ModuleTechMachine.Registries.BRICK_KILN_RECIPES, BrickKilnRecipesAdd.INHERIT_TRANSFORMER, stoneKilnRecipe);
        }
      }
    }

    @Override
    public String describe() {

      return "Adding pit kiln recipe for " + this.output + ", inherited=" + this.inherited;
    }
  }

}
