package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocAppend;
import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.ChoppingBlockRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneSawmillRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
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

import java.util.List;

import static com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.BrickSawmillRecipesAdd.INHERIT_TRANSFORMER;

@ZenDocClass("mods.pyrotech.Chopping")
@ZenDocAppend({"docs/include/chopping.example.md"})
@ZenClass("mods.pyrotech.Chopping")
public class ZenChoppingBlock {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, @Optional boolean inherited) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        ModuleTechBasicConfig.CHOPPING_BLOCK.CHOPS_REQUIRED_PER_HARVEST_LEVEL,
        ModuleTechBasicConfig.CHOPPING_BLOCK.RECIPE_RESULT_QUANTITY_PER_HARVEST_LEVEL,
        inherited
    ));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "chops", info = "overrides the default chops array in config"),
          @ZenDocArg(arg = "quantities", info = "overrides the default quantities array in config"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int[] chops, int[] quantities, @Optional boolean inherited) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        chops,
        quantities,
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

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBasic.Registries.CHOPPING_BLOCK_RECIPE, "chopping block"));
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

    ModuleTechBasicConfig.STAGES_CHOPPING_BLOCK = stages.getStages();
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      ChoppingBlockRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing chopping recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final ItemStack output;
    private final int[] chops;
    private final int[] quantities;
    private final boolean inherited;
    private final String name;
    private final Ingredient input;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int[] chops,
        int[] quantities,
        boolean inherited
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.chops = chops;
      this.quantities = quantities;
      this.inherited = inherited;
    }

    @Override
    public void apply() {

      ChoppingBlockRecipe recipe = new ChoppingBlockRecipe(
          this.output,
          this.input,
          this.chops,
          this.quantities
      );

      ResourceLocation registryName = new ResourceLocation("crafttweaker", this.name);
      ModuleTechBasic.Registries.CHOPPING_BLOCK_RECIPE.register(recipe.setRegistryName(registryName));

      if (this.inherited) {

        if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechMachine.class)) {

          List<StoneSawmillRecipe> stoneSawmillRecipes = StoneSawmillRecipesAdd.registerSawmillRecipeWood(
              ModuleTechMachine.Registries.STONE_SAWMILL_RECIPES,
              "chopping_block/" + registryName.getResourcePath(),
              recipe.getOutput(),
              recipe.getInput(),
              ModuleTechMachineConfig.STONE_SAWMILL.INHERITED_CHOPPING_BLOCK_RECIPE_DURATION_MODIFIER
          );

          for (StoneSawmillRecipe stoneSawmillRecipe : stoneSawmillRecipes) {
            RecipeHelper.inherit("stone_sawmill", ModuleTechMachine.Registries.BRICK_SAWMILL_RECIPES, INHERIT_TRANSFORMER, stoneSawmillRecipe);
          }
        }
      }
    }

    @Override
    public String describe() {

      return "Adding chopping recipe for " + this.output + ", inherited=" + this.inherited;
    }
  }

}
