package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.WorktableRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import crafttweaker.mc1120.CraftTweaker;
import crafttweaker.mc1120.recipes.MCRecipeBase;
import crafttweaker.mc1120.recipes.MCRecipeManager;
import crafttweaker.mc1120.recipes.MCRecipeShaped;
import crafttweaker.mc1120.recipes.MCRecipeShapeless;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.HashSet;

@ZenClass("mods.pyrotech.Worktable")
public class ZenWorktable {

  private static final HashSet<String> USED_RECIPE_NAMES = new HashSet<>();
  private static final TIntSet USED_HASHES = new TIntHashSet();

  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenMethod
  public void addShaped(IItemStack output, IIngredient[][] ingredients, @Optional IRecipeFunction function, @Optional IRecipeAction action) {

    CraftTweaker.LATE_ACTIONS.add(new ActionAddShapedRecipe(output, ingredients, function, action, false, false));
  }

  @ZenMethod
  void addShaped(String name, IItemStack output, IIngredient[][] ingredients, @Optional IRecipeFunction function, @Optional IRecipeAction action) {

    CraftTweaker.LATE_ACTIONS.add(new ActionAddShapedRecipe(name, output, ingredients, function, action, false, false));
  }

  @ZenMethod
  public void addShapedMirrored(IItemStack output, IIngredient[][] ingredients, @Optional IRecipeFunction function, @Optional IRecipeAction action) {

    CraftTweaker.LATE_ACTIONS.add(new ActionAddShapedRecipe(output, ingredients, function, action, true, false));
  }

  @ZenMethod
  public void addShapedMirrored(String name, IItemStack output, IIngredient[][] ingredients, @Optional IRecipeFunction function, @Optional IRecipeAction action) {

    CraftTweaker.LATE_ACTIONS.add(new ActionAddShapedRecipe(name, output, ingredients, function, action, true, false));
  }

  @ZenMethod
  public void addShapeless(IItemStack output, IIngredient[] ingredients, @Optional IRecipeFunction function, @Optional IRecipeAction action) {

    boolean valid = (output != null);

    for (IIngredient ingredient : ingredients) {

      if (ingredient == null) {
        valid = false;
      }
    }

    if (!valid) {
      CraftTweakerAPI.logError("Null not allowed in shapeless recipes! Recipe for: " + output + " not created!");
      return;
    }

    CraftTweaker.LATE_ACTIONS.add(new ActionAddShapelessRecipe(output, ingredients, function, action));
  }

  @ZenMethod
  public void addShapeless(String name, IItemStack output, IIngredient[] ingredients, @Optional IRecipeFunction function, @Optional IRecipeAction action) {

    boolean valid = output != null;

    for (IIngredient ingredient : ingredients) {

      if (ingredient == null) {
        valid = false;
      }
    }

    if (!valid) {
      CraftTweakerAPI.logError("Null not allowed in shapeless recipes! Recipe for: " + output + " not created!");
      return;
    }

    CraftTweaker.LATE_ACTIONS.add(new ActionAddShapelessRecipe(name, output, ingredients, function, action, false));
  }

  @ZenMethod
  public static void blacklistVanillaRecipes(String[] resourceLocations) {

    CraftTweaker.LATE_ACTIONS.add(new IAction() {

      @Override
      public void apply() {

        for (String resourceLocation : resourceLocations) {
          WorktableRecipe.blacklistVanillaRecipe(new ResourceLocation(resourceLocation));
        }
      }

      @Override
      public String describe() {

        return "Blacklisting vanilla crafting recipes for worktable by resource locations: " + Arrays.toString(resourceLocations);
      }
    });
  }

  @ZenMethod
  public static void whitelistVanillaRecipes(String[] resourceLocations) {

    CraftTweaker.LATE_ACTIONS.add(new IAction() {

      @Override
      public void apply() {

        for (String resourceLocation : resourceLocations) {
          WorktableRecipe.whitelistVanillaRecipe(new ResourceLocation(resourceLocation));
        }
      }

      @Override
      public String describe() {

        return "Whitelisting vanilla crafting recipes for worktable by resource locations: " + Arrays.toString(resourceLocations);
      }
    });
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      WorktableRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing worktable recipes for " + this.output;
    }
  }

  private static class ActionBaseAddWorktableRecipe
      implements IAction {

    // this is != null only _after_ it has been applied and is actually registered
    protected MCRecipeBase recipe;
    protected IItemStack output;
    protected boolean isShaped;
    protected String name;

    private ActionBaseAddWorktableRecipe(MCRecipeBase recipe, IItemStack output, boolean isShaped) {

      this.recipe = recipe;
      this.output = output;
      this.isShaped = isShaped;

      if (recipe.hasTransformers()) {
        MCRecipeManager.transformerRecipes.add(recipe);
      }
    }

    public IItemStack getOutput() {

      return output;
    }

    public void setOutput(IItemStack output) {

      this.output = output;
    }

    public String getName() {

      return name;
    }

    protected void setName(String name) {

      if (name != null) {
        String proposedName = MCRecipeManager.cleanRecipeName(name);

        if (USED_RECIPE_NAMES.contains(proposedName)) {
          this.name = calculateName();
          CraftTweakerAPI.logWarning("Recipe name [" + name + "] has duplicate uses, defaulting to calculated hash!");

        } else {
          this.name = proposedName;
        }

      } else {
        this.name = calculateName();
      }

      USED_RECIPE_NAMES.add(this.name);
    }

    public String calculateName() {

      int hash = this.recipe.toCommandString().hashCode();

      while (USED_HASHES.contains(hash)) {
        ++hash;
      }

      USED_HASHES.add(hash);
      return (this.isShaped ? "ct_shaped" : "ct_shapeless") + hash;
    }

    @Override
    public void apply() {

      ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", this.name);
      WorktableRecipe recipe = new WorktableRecipe(this.recipe.setRegistryName(resourceLocation)).setRegistryName(resourceLocation);
      ModulePyrotechRegistries.WORKTABLE_RECIPE.register(recipe);
    }

    @Override
    public String describe() {

      if (this.output != null) {
        return "Adding worktable " + (this.isShaped ? "shaped" : "shapeless") + " recipe for " + this.output.getDisplayName() + " with name " + this.name;

      } else {
        return "Trying to add worktable " + (this.isShaped ? "shaped" : "shapeless") + " recipe without correct output";
      }
    }

    public MCRecipeBase getRecipe() {

      return recipe;
    }
  }

  private static class ActionAddShapedRecipe
      extends ActionBaseAddWorktableRecipe {

    public ActionAddShapedRecipe(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function, IRecipeAction action, boolean mirrored, boolean hidden) {

      this(null, output, ingredients, function, action, mirrored, hidden);
    }

    public ActionAddShapedRecipe(String name, IItemStack output, IIngredient[][] ingredients, IRecipeFunction function, IRecipeAction action, boolean mirrored, boolean hidden) {

      super(new MCRecipeShaped(ingredients, output, function, action, mirrored, hidden), output, true);
      this.setName(name);
    }
  }

  private static class ActionAddShapelessRecipe
      extends ActionBaseAddWorktableRecipe {

    public ActionAddShapelessRecipe(IItemStack output, IIngredient[] ingredients, @Optional IRecipeFunction function, @Optional IRecipeAction action) {

      this(null, output, ingredients, function, action, false);
    }

    public ActionAddShapelessRecipe(String name, IItemStack output, IIngredient[] ingredients, @Optional IRecipeFunction function, @Optional IRecipeAction action, boolean hidden) {

      super(new MCRecipeShapeless(ingredients, output, function, action, hidden), output, false);
      this.setName(name);
    }
  }
}
