package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocAppend;
import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompactingBinRecipe;
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

@ZenDocClass("mods.pyrotech.CompactingBin")
@ZenDocAppend({"docs/include/compacting_bin.example.md"})
@ZenClass("mods.pyrotech.CompactingBin")
public class ZenCompactingBin {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "amount", info = "number of input items required")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int amount) {

    ZenCompactingBin.addRecipe(
        name,
        output,
        input,
        amount,
        ModuleTechBasicConfig.COMPACTING_BIN.TOOL_USES_REQUIRED_PER_HARVEST_LEVEL
    );
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "amount", info = "number of input items required"),
          @ZenDocArg(arg = "toolUsesRequired", info = "overrides default provided in config")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int amount, int[] toolUsesRequired) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        amount,
        toolUsesRequired
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

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE, "compacting bin"));
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      CompactingBinRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing compacting recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final ItemStack output;
    private final Ingredient input;
    private final int amount;
    private final int[] uses;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int amount,
        int[] uses
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.amount = amount;
      this.uses = uses;
    }

    @Override
    public void apply() {

      CompactingBinRecipe recipe = new CompactingBinRecipe(
          this.output,
          this.input,
          this.amount,
          this.uses
      );
      ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding compacting recipe for " + this.output;
    }
  }

}
