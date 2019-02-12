package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBuilder;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.BloomeryBuilder")
@ZenClass("mods.pyrotech.BloomeryBuilder")
public class ZenBloomeryBuilder {

  @ZenDocMethod(
      order = 1,
      args = {"output"}
  )
  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenDocMethod(
      order = 2,
      args = {"name", "output", "input"}
  )
  @ZenMethod
  public static ZenBloomeryBuilder create(String name, IItemStack output, IIngredient input) {

    return new ZenBloomeryBuilder(new BloomeryRecipeBuilder(
        new ResourceLocation("crafttweaker", name),
        CTInputHelper.toStack(output),
        CTInputHelper.toIngredient(input)
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

      BloomeryRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing bloomery recipes for " + this.output;
    }
  }

  private final BloomeryRecipeBuilder builder;

  private ZenBloomeryBuilder(BloomeryRecipeBuilder builder) {

    this.builder = builder;
  }

  @ZenDocMethod(
      order = 3,
      args = {"burnTimeTicks"}
  )
  @ZenMethod
  public ZenBloomeryBuilder setBurnTimeTicks(int burnTimeTicks) {

    this.builder.setBurnTimeTicks(burnTimeTicks);
    return this;
  }

  @ZenDocMethod(
      order = 4,
      args = {"failureChance"}
  )
  @ZenMethod
  public ZenBloomeryBuilder setFailureChance(float failureChance) {

    this.builder.setFailureChance(failureChance);
    return this;
  }

  @ZenDocMethod(
      order = 5,
      args = {"bloomYield"}
  )
  @ZenMethod
  public ZenBloomeryBuilder setBloomYield(int min, int max) {

    this.builder.setBloomYield(min, max);
    return this;
  }

  @ZenDocMethod(
      order = 6,
      args = {"slagItem", "slagCount"}
  )
  @ZenMethod
  public ZenBloomeryBuilder setSlagItem(IItemStack slagItem, int slagCount) {

    this.builder.setSlagItem(CTInputHelper.toStack(slagItem), slagCount);
    return this;
  }

  @ZenDocMethod(
      order = 7,
      args = {"itemStack", "weight"}
  )
  @ZenMethod
  public ZenBloomeryBuilder addFailureItem(IItemStack itemStack, int weight) {

    this.builder.addFailureItem(CTInputHelper.toStack(itemStack), weight);
    return this;
  }

  @ZenDocMethod(
      order = 8,
      args = {"langKey"}
  )
  @ZenMethod
  public ZenBloomeryBuilder setLangKey(String langKey) {

    this.builder.setLangKey(langKey);
    return this;
  }

  @ZenDocMethod(
      order = 9
  )
  @ZenMethod
  public void register() {

    ModuleBloomery.Registries.BLOOMERY_RECIPE.register(this.builder.create());
  }
}
