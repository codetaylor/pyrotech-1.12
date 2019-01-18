package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.ChoppingBlockRecipe;
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

@ZenDocClass("mods.pyrotech.Chopping")
@ZenClass("mods.pyrotech.Chopping")
public class ZenChoppingBlock {

  @ZenDocMethod(
      order = 1,
      args = {"name", "output", "input"}
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        ModulePyrotechConfig.CHOPPING_BLOCK.CHOPS_REQUIRED_PER_HARVEST_LEVEL,
        ModulePyrotechConfig.CHOPPING_BLOCK.RECIPE_RESULT_QUANTITY_PER_HARVEST_LEVEL
    ));
  }

  @ZenDocMethod(
      order = 2,
      description = {
          "|Parameter|Description|\n" +
              "|---------|-----------|\n" +
              "|name|the name of the recipe|\n" +
              "|output|the recipe output|\n" +
              "|input|the recipe input|\n" +
              "|chops|the int array provided here will override the array provided in the config file; see the config file for an explanation|\n" +
              "|quantities|the int array provided here will override the array provided in the config file; see the config file for an explanation|"
      },
      args = {"name", "output", "input", "chops", "quantities"}
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int[] chops, int[] quantities) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        chops,
        quantities
    ));
  }

  @ZenDocMethod(
      order = 3,
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
    private final String name;
    private final Ingredient input;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int[] chops,
        int[] quantities
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.chops = chops;
      this.quantities = quantities;
    }

    @Override
    public void apply() {

      ChoppingBlockRecipe recipe = new ChoppingBlockRecipe(
          this.output,
          this.input,
          this.chops,
          this.quantities
      );
      ModulePyrotechRegistries.CHOPPING_BLOCK_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding chopping recipe for " + this.output;
    }
  }

}
