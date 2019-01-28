package com.codetaylor.mc.pyrotech.modules.bloomery.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.recipe.BloomeryRecipe;
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

import javax.annotation.Nullable;

@ZenDocClass("mods.pyrotech.Bloomery")
@ZenClass("mods.pyrotech.Bloomery")
public class ZenBloomery {

  @ZenDocMethod(
      order = 1,
      args = {
          "name",
          "output",
          "input",
          "burnTimeTicks",
          "slagCount",
          "slagItem",
          "bloomYieldMin",
          "bloomYieldMax",
          "failureChance",
          "failureItems",
          "langKey",
      }
  )
  @ZenMethod
  public static void addRecipe(
      String name,
      IItemStack output,
      IIngredient input,
      int burnTimeTicks,
      int slagCount,
      IItemStack slagItem,
      int bloomYieldMin,
      int bloomYieldMax,
      float failureChance,
      IItemStack[] failureItems,
      String langKey
  ) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        burnTimeTicks,
        slagCount,
        CraftTweakerMC.getItemStack(slagItem),
        bloomYieldMin,
        bloomYieldMax,
        failureChance,
        CraftTweakerMC.getItemStacks(failureItems),
        langKey
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

      BloomeryRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing bloomery recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final ItemStack output;
    private final String name;
    private final Ingredient input;
    private final int timeTicks;
    private final float failureChance;
    private final int bloomYieldMin;
    private final int bloomYieldMax;
    private final int slagCount;
    private final ItemStack slagItem;
    private final ItemStack[] failureItems;
    @Nullable
    private final String langKey;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int timeTicks,
        int slagCount,
        ItemStack slagItem,
        int bloomYieldMin,
        int bloomYieldMax,
        float failureChance,
        ItemStack[] failureItems,
        @Nullable String langKey
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.timeTicks = timeTicks;
      this.failureChance = failureChance;
      this.bloomYieldMin = bloomYieldMin;
      this.bloomYieldMax = bloomYieldMax;
      this.slagCount = slagCount;
      this.slagItem = slagItem;
      this.failureItems = failureItems;
      this.langKey = langKey;
    }

    @Override
    public void apply() {

      BloomeryRecipe recipe = new BloomeryRecipe(
          new ResourceLocation("crafttweaker", this.name),
          this.output,
          this.input,
          this.timeTicks,
          this.failureChance,
          this.bloomYieldMin,
          this.bloomYieldMax,
          this.slagCount,
          this.slagItem,
          this.failureItems,
          this.langKey
      );
      ModuleBloomery.Registries.BLOOMERY_RECIPE.register(recipe);
    }

    @Override
    public String describe() {

      return "Adding bloomery recipe for " + this.output;
    }
  }

}
