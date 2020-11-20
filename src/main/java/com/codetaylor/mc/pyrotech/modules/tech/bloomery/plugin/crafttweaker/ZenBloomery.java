package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.tools.*;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe.BloomeryRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.*;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashSet;
import java.util.Set;

import static com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe.WitherForgeRecipesAdd.INHERIT_TRANSFORMER;

@ZenDocClass("mods.pyrotech.Bloomery")
@ZenDocPrepend({"docs/include/header.md"})
@ZenDocAppend({"docs/include/bloomery.example.md"})
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

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output), EnumRecipeType.WitherForge));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe - should be unique"),
          @ZenDocArg(arg = "output", info = "the output item received from the bloom"),
          @ZenDocArg(arg = "input", info = "the recipe input"),
          @ZenDocArg(arg = "inherited", info = "true if the recipe should be inherited")
      },
      description = {
          "Creates and returns a new bloomery recipe builder."
      }
  )
  @ZenMethod
  public static ZenBloomery createBloomeryBuilder(String name, IItemStack output, IIngredient input, @Optional boolean inherited) {

    return new ZenBloomery(
        new BloomeryRecipeBuilder(
            new ResourceLocation("crafttweaker", name),
            CTInputHelper.toStack(output),
            CTInputHelper.toIngredient(input)
        ),
        EnumRecipeType.Bloomery,
        inherited
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
        EnumRecipeType.WitherForge,
        false
    );
  }

  @ZenDocMethod(
      order = 5,
      args = {
          @ZenDocArg(arg = "fuel"),
          @ZenDocArg(arg = "modifier")
      }
  )
  @ZenMethod
  public static void addBloomeryFuelModifier(IIngredient fuel, double modifier) {

    ModuleTechBloomeryConfig.BLOOMERY_FUEL_MODIFIERS.add(
        new Tuple<>(
            CTInputHelper.toIngredient(fuel),
            modifier
        )
    );
  }

  @ZenDocMethod(
      order = 6,
      args = {
          @ZenDocArg(arg = "fuel"),
          @ZenDocArg(arg = "modifier")
      }
  )
  @ZenMethod
  public static void addWitherForgeFuelModifier(IIngredient fuel, double modifier) {

    ModuleTechBloomeryConfig.WITHER_FORGE_FUEL_MODIFIERS.add(
        new Tuple<>(
            CTInputHelper.toIngredient(fuel),
            modifier
        )
    );
  }

  @ZenDocMethod(
      order = 7,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the bloom."
      }
  )
  @ZenMethod
  public static void setBloomGameStages(ZenStages stages) {

    ModuleTechBloomeryConfig.STAGES_BLOOM = stages.getStages();
  }

  @ZenDocMethod(
      order = 8,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the bloomery."
      }
  )
  @ZenMethod
  public static void setBloomeryGameStages(ZenStages stages) {

    ModuleTechBloomeryConfig.STAGES_BLOOMERY = stages.getStages();
  }

  @ZenDocMethod(
      order = 9,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the wither forge."
      }
  )
  @ZenMethod
  public static void setWitherForgeGameStages(ZenStages stages) {

    ModuleTechBloomeryConfig.STAGES_WITHER_FORGE = stages.getStages();
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
  private final boolean inherited;

  private ZenBloomery(BloomeryRecipeBuilderBase builder, EnumRecipeType recipeType, boolean inherited) {

    this.builder = builder;
    this.recipeType = recipeType;
    this.inherited = inherited;
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
          @ZenDocArg(arg = "experience", info = "the total experience produced by a bloom")
      },
      description = {
          "Sets the total experience rewarded for hammering on the bloom."
      }
  )
  @ZenMethod
  public ZenBloomery setExperience(float experience) {

    this.builder.setExperience(experience);
    return this;
  }

  @ZenDocMethod(
      order = 7,
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
      order = 8,
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
      order = 9,
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
      order = 10,
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
      order = 11,
      args = {
          @ZenDocArg(arg = "langKey", info = "the lang key")
      },
      description = {
          "The lang key provided here will be used to construct the display name of the output bloom.",
          "If this parameter is omitted, the recipe will use the lang key of the input item.",
          "",
          "When more than one lang key is provided, separated by a semicolon `;`, the first lang key is resolved, then passed into the next lang key and so on.",
          "",
          "For example, if supplied with the parameter `tile.oreIron;item.pyrotech.slag.unique`, `tile.oreIron` will first be resolved to `Iron Ore` before being passed into `item.pyrotech.slag.unique`, resulting in `Iron Ore Slag`, which is then passed into `tile.pyrotech.bloom.unique.name` and ultimately resolved to `Iron Ore Slag Bloom`.",
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
      order = 12,
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

    if (this.recipeType != EnumRecipeType.Bloomery
        && this.recipeType != EnumRecipeType.WitherForge) {
      throw new RuntimeException("Unknown recipe type: " + this.recipeType);

    } else {
      CraftTweaker.LATE_ACTIONS.add(new AddRecipe(this.recipeType, this.builder.create(), this.inherited));
    }
  }

  public class AddRecipe
      implements IAction {

    private final EnumRecipeType recipeType;
    private final BloomeryRecipeBase recipe;
    private final boolean inherited;

    public AddRecipe(EnumRecipeType recipeType, BloomeryRecipeBase recipe, boolean inherited) {

      this.recipeType = recipeType;
      this.recipe = recipe;
      this.inherited = inherited;
    }

    @Override
    public void apply() {

      if (this.recipeType == EnumRecipeType.Bloomery) {
        ModuleTechBloomery.Registries.BLOOMERY_RECIPE.register((BloomeryRecipe) this.recipe);

        if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)) {
          BloomeryRecipesAdd.registerBloomAnvilRecipe(ModuleTechBasic.Registries.ANVIL_RECIPE, this.recipe);
        }

        if (this.inherited) {
          WitherForgeRecipe witherForgeRecipe = RecipeHelper.inherit("bloomery", ModuleTechBloomery.Registries.WITHER_FORGE_RECIPE, INHERIT_TRANSFORMER, (BloomeryRecipe) this.recipe);

          if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)) {
            BloomeryRecipesAdd.registerBloomAnvilRecipe(ModuleTechBasic.Registries.ANVIL_RECIPE, witherForgeRecipe);
          }
        }

      } else if (this.recipeType == EnumRecipeType.WitherForge) {
        ModuleTechBloomery.Registries.WITHER_FORGE_RECIPE.register((WitherForgeRecipe) this.recipe);

        if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)) {
          BloomeryRecipesAdd.registerBloomAnvilRecipe(ModuleTechBasic.Registries.ANVIL_RECIPE, this.recipe);
        }
      }
    }

    @Override
    public String describe() {

      return "Registering bloomery recipe: " + this.recipe.getRegistryName().toString();
    }
  }
}
