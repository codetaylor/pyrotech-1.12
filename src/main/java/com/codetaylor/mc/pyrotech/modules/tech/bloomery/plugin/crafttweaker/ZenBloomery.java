package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
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

import java.util.HashSet;
import java.util.Set;

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
      order = -1
  )
  @ZenMethod
  public static void removeAllBloomeryRecipes() {

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBloomery.Registries.BLOOMERY_RECIPE, "bloomery"));
  }

  @ZenDocMethod(
      order = 0
  )
  @ZenMethod
  public static void removeAllWitherForgeRecipes() {

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechBloomery.Registries.WITHER_FORGE_RECIPE, "wither forge"));
  }

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "output", info = "the output ingredients to match")
      },
      description = {
          "Recipes that have an output that matches any of the given ingredients will be removed."
      }
  )
  @ZenMethod
  public static void removeBloomeryRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output), EnumRecipeType.Bloomery));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "output", info = "the output ingredients to match")
      },
      description = {
          "Recipes that have an output that matches any of the given ingredients will be removed."
      }
  )
  @ZenMethod
  public static void removeWitherForgeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output), EnumRecipeType.Bloomery));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe - should be unique"),
          @ZenDocArg(arg = "output", info = "the output item received from the bloom"),
          @ZenDocArg(arg = "input", info = "the recipe input")
      },
      description = {
          "Creates and returns a new bloomery recipe builder."
      }
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
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe - should be unique"),
          @ZenDocArg(arg = "output", info = "the output item received from the bloom"),
          @ZenDocArg(arg = "input", info = "the recipe input")
      },
      description = {
          "Creates and returns a new wither forge recipe builder."
      }
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
      args = {
          @ZenDocArg(arg = "burnTimeTicks", info = "the base time in ticks to produce a bloom")
      },
      description = {
          "Sets the base time in ticks that this recipe takes to produce a bloom. This value is further modified by fuel level and airflow."
      }
  )
  @ZenMethod
  public ZenBloomery setBurnTimeTicks(int burnTimeTicks) {

    this.builder.setBurnTimeTicks(burnTimeTicks);
    return this;
  }

  @ZenDocMethod(
      order = 6,
      args = {
          @ZenDocArg(arg = "failureChance", info = "the recipe's failure chance")
      },
      description = {
          "Sets the recipe's chance to fail and produce an item from the recipe's failure items. This is applied to items received from hammering a bloom."
      }
  )
  @ZenMethod
  public ZenBloomery setFailureChance(float failureChance) {

    this.builder.setFailureChance(failureChance);
    return this;
  }

  @ZenDocMethod(
      order = 7,
      args = {
          @ZenDocArg(arg = "min", info = "the minimum output yield"),
          @ZenDocArg(arg = "max", info = "the maximum output yield")
      },
      description = {
          "Sets the random range for the total number of output items produced by hammering a bloom."
      }
  )
  @ZenMethod
  public ZenBloomery setBloomYield(int min, int max) {

    this.builder.setBloomYield(min, max);
    return this;
  }

  @ZenDocMethod(
      order = 8,
      args = {
          @ZenDocArg(arg = "slagItem", info = "the item to use as slag"),
          @ZenDocArg(arg = "slagCount", info = "the amount of slag produced in-world during processing")
      },
      description = {
          "Sets the slag item and the amount of in-world slag produced during operation."
      }
  )
  @ZenMethod
  public ZenBloomery setSlagItem(IItemStack slagItem, int slagCount) {

    this.builder.setSlagItem(CTInputHelper.toStack(slagItem), slagCount);
    return this;
  }

  @ZenDocMethod(
      order = 9,
      args = {
          @ZenDocArg(arg = "itemStack", info = "the failure item"),
          @ZenDocArg(arg = "weight", info = "the weight")
      },
      description = {
          "Adds a weighted item to the list of items chosen as a failure item."
      }
  )
  @ZenMethod
  public ZenBloomery addFailureItem(IItemStack itemStack, int weight) {

    this.builder.addFailureItem(CTInputHelper.toStack(itemStack), weight);
    return this;
  }

  @ZenDocMethod(
      order = 10,
      args = {
          @ZenDocArg(arg = "langKey", info = "the lang key")
      },
      description = {
          "The lang key provided here will be used to construct the display name of the output bloom.",
          "",
          "**NOTE:** The '.name' suffix is added internally and should not be included here."
      }
  )
  @ZenMethod
  public ZenBloomery setLangKey(String langKey) {

    this.builder.setLangKey(langKey);
    return this;
  }

  @ZenDocMethod(
      order = 11,
      args = {
          @ZenDocArg(arg = "tiers", info = "valid enums: granite, ironclad")
      },
      description = {
          "Provide an array of `granite` and / or `ironclad`.",
          "Anvil recipe inheritance does not apply to bloom recipes. That means recipes created for the granite anvil will not be inherited by the ironclad anvil. A bloom recipe's anvil tier must be set here."
      }
  )
  @ZenMethod
  public ZenBloomery setAnvilTiers(String[] tiers) {

    Set<AnvilRecipe.EnumTier> set = new HashSet<>(tiers.length);

    for (String tier : tiers) {
      set.add(AnvilRecipe.EnumTier.valueOf(tier.toUpperCase()));
    }

    this.builder.setAnvilTiers(set.toArray(new AnvilRecipe.EnumTier[0]));
    return this;
  }

  @ZenDocMethod(
      order = 200,
      description = {
          "This must be called on the builder last to actually register the recipe defined in the builder."
      }
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
