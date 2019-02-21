package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.*;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.Bloomery")
@ZenClass("mods.pyrotech.Bloomery")
public class ZenBloomery {

  enum EnumRecipeType {

    Bloomery("bloomery"),
    WitherForge("wither forge");

    private final String name;

    EnumRecipeType(String name) {

      this.name = name;
    }
  }

  @ZenDocMethod(
      order = 1,
      args = {"output"}
  )
  @ZenMethod
  public static void removeBloomeryRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output), EnumRecipeType.Bloomery));
  }

  @ZenDocMethod(
      order = 2,
      args = {"output"}
  )
  @ZenMethod
  public static void removeWitherForgeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output), EnumRecipeType.Bloomery));
  }

  @ZenDocMethod(
      order = 3,
      args = {"name", "output", "input"}
  )
  @ZenMethod
  public static ZenBloomery createBloomeryBuilder(String name, IItemStack output, IIngredient input) {

    return new ZenBloomery(
        new BloomeryRecipeBuilder(
            new ResourceLocation("crafttweaker", name),
            CTInputHelper.toStack(output),
            CTInputHelper.toIngredient(input)
        ),
        EnumRecipeType.Bloomery
    );
  }

  @ZenDocMethod(
      order = 4,
      args = {"name", "output", "input"}
  )
  @ZenMethod
  public static ZenBloomery createWitherForgeBuilder(String name, IItemStack output, IIngredient input) {

    return new ZenBloomery(
        new WitherForgeRecipeBuilder(
            new ResourceLocation("crafttweaker", name),
            CTInputHelper.toStack(output),
            CTInputHelper.toIngredient(input)
        ),
        EnumRecipeType.WitherForge
    );
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;
    private final EnumRecipeType type;

    public RemoveRecipe(Ingredient output, EnumRecipeType type) {

      this.output = output;
      this.type = type;
    }

    @Override
    public void apply() {

      if (this.type == EnumRecipeType.Bloomery) {
        BloomeryRecipe.removeRecipes(this.output);

      } else if (this.type == EnumRecipeType.WitherForge) {
        WitherForgeRecipe.removeRecipes(this.output);

      } else {
        throw new RuntimeException("Unknown type: " + this.type);
      }
    }

    @Override
    public String describe() {

      return "Removing " + this.type.name + " recipes for " + this.output;
    }
  }

  private final BloomeryRecipeBuilderBase builder;
  private final EnumRecipeType recipeType;

  private ZenBloomery(BloomeryRecipeBuilderBase builder, EnumRecipeType recipeType) {

    this.builder = builder;
    this.recipeType = recipeType;
  }

  @ZenDocMethod(
      order = 5,
      args = {"burnTimeTicks"}
  )
  @ZenMethod
  public ZenBloomery setBurnTimeTicks(int burnTimeTicks) {

    this.builder.setBurnTimeTicks(burnTimeTicks);
    return this;
  }

  @ZenDocMethod(
      order = 6,
      args = {"failureChance"}
  )
  @ZenMethod
  public ZenBloomery setFailureChance(float failureChance) {

    this.builder.setFailureChance(failureChance);
    return this;
  }

  @ZenDocMethod(
      order = 7,
      args = {"bloomYield"}
  )
  @ZenMethod
  public ZenBloomery setBloomYield(int min, int max) {

    this.builder.setBloomYield(min, max);
    return this;
  }

  @ZenDocMethod(
      order = 8,
      args = {"slagItem", "slagCount"}
  )
  @ZenMethod
  public ZenBloomery setSlagItem(IItemStack slagItem, int slagCount) {

    this.builder.setSlagItem(CTInputHelper.toStack(slagItem), slagCount);
    return this;
  }

  @ZenDocMethod(
      order = 9,
      args = {"itemStack", "weight"}
  )
  @ZenMethod
  public ZenBloomery addFailureItem(IItemStack itemStack, int weight) {

    this.builder.addFailureItem(CTInputHelper.toStack(itemStack), weight);
    return this;
  }

  @ZenDocMethod(
      order = 10,
      args = {"langKey"}
  )
  @ZenMethod
  public ZenBloomery setLangKey(String langKey) {

    this.builder.setLangKey(langKey);
    return this;
  }

  @ZenDocMethod(
      order = 11
  )
  @ZenMethod
  public void register() {

    if (this.recipeType == EnumRecipeType.Bloomery) {
      ModuleTechBloomery.Registries.BLOOMERY_RECIPE.register((BloomeryRecipe) this.builder.create());

    } else if (this.recipeType == EnumRecipeType.WitherForge) {
      ModuleTechBloomery.Registries.WITHER_FORGE_RECIPE.register((WitherForgeRecipe) this.builder.create());

    } else {
      throw new RuntimeException("Unknown recipe type: " + this.recipeType);
    }
  }
}
