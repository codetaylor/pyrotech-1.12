package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.tools.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.WorktableRecipe;
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

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;

@ZenDocClass("mods.pyrotech.Worktable")
@ZenDocAppend({"docs/include/worktable.example.md"})
@ZenClass("mods.pyrotech.Worktable")
public class ZenWorktable {

  private static final HashSet<String> USED_RECIPE_NAMES = new HashSet<>();
  private static final TIntSet USED_HASHES = new TIntHashSet();

  // ---------------------------------------------------------------------------
  // --- Start: Builder

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "output"),
          @ZenDocArg(arg = "ingredients")
      }
  )
  @ZenMethod
  public static ZenWorktable buildShaped(IItemStack output, IIngredient[][] ingredients) {

    return new ZenWorktable(output, ingredients);
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "output"),
          @ZenDocArg(arg = "ingredients")
      }
  )
  @ZenMethod
  public static ZenWorktable buildShapeless(IItemStack output, IIngredient[] ingredients) {

    return new ZenWorktable(output, ingredients);
  }

  private String name;
  private IItemStack output;
  private IIngredient[][] shaped;
  private IIngredient[] shapeless;
  private IIngredient tool;
  private int toolDamage;
  private boolean mirrored;
  private boolean hidden;
  private IRecipeFunction recipeFunction;
  private IRecipeAction recipeAction;

  public ZenWorktable(IItemStack output, IIngredient[][] shaped) {

    this.output = output;
    this.shaped = shaped;
  }

  public ZenWorktable(IItemStack output, IIngredient[] shapeless) {

    this.output = output;
    this.shapeless = shapeless;
  }

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name")
      }
  )
  @ZenMethod
  public ZenWorktable setName(String name) {

    this.name = name;
    return this;
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "tool"),
          @ZenDocArg(arg = "toolDamage")
      }
  )
  @ZenMethod
  public ZenWorktable setTool(IIngredient tool, int damage) {

    this.tool = tool;
    this.toolDamage = damage;
    return this;
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "mirrored")
      }
  )
  @ZenMethod
  public ZenWorktable setMirrored(boolean mirrored) {

    this.mirrored = mirrored;
    return this;
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "hidden")
      }
  )
  @ZenMethod
  public ZenWorktable setHidden(boolean hidden) {

    this.hidden = hidden;
    return this;
  }

  @ZenDocMethod(
      order = 5,
      args = {
          @ZenDocArg(arg = "recipeFunction")
      }
  )
  @ZenMethod
  public ZenWorktable setRecipeFunction(IRecipeFunction recipeFunction) {

    this.recipeFunction = recipeFunction;
    return this;
  }

  @ZenDocMethod(
      order = 6,
      args = {
          @ZenDocArg(arg = "recipeAction")
      }
  )
  @ZenMethod
  public ZenWorktable setRecipeAction(IRecipeAction recipeAction) {

    this.recipeAction = recipeAction;
    return this;
  }

  @ZenDocMethod(
      order = 7
  )
  @ZenMethod
  public void register() {

    if (this.shaped != null) {
      ZenWorktable.addShaped(this.name, this.output, this.shaped, this.tool, this.toolDamage, this.mirrored, this.hidden, this.recipeFunction, this.recipeAction);

    } else {
      ZenWorktable.addShapeless(this.name, this.output, this.shapeless, this.tool, this.toolDamage, this.hidden, this.recipeFunction, this.recipeAction);
    }
  }

  // --- End: Builder
  // ---------------------------------------------------------------------------

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name"),
          @ZenDocArg(arg = "output"),
          @ZenDocArg(arg = "ingredients")
      },
      description = {
          "If the `name` parameter is `null`, a name will be generated."
      }
  )
  @ZenMethod
  public static void addShaped(
      @ZenDocNullable String name,
      IItemStack output,
      IIngredient[][] ingredients
  ) {

    ZenWorktable.addShaped(name, output, ingredients, null, 0, false, false, null, null);
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "name"),
          @ZenDocArg(arg = "output"),
          @ZenDocArg(arg = "ingredients"),
          @ZenDocArg(arg = "tool"),
          @ZenDocArg(arg = "toolDamage"),
          @ZenDocArg(arg = "mirrored"),
          @ZenDocArg(arg = "hidden"),
          @ZenDocArg(arg = "function"),
          @ZenDocArg(arg = "action")
      },
      description = {
          "If the `name` parameter is `null`, a name will be generated.",
          "If the `tool` parameter is `null`, the recipe will default to using",
          "the hammers provided in the config and will ignore the `toolDamage`",
          "parameter."
      }
  )
  @ZenMethod
  public static void addShaped(
      @ZenDocNullable String name,
      IItemStack output,
      IIngredient[][] ingredients,
      @ZenDocNullable IIngredient tool,
      int toolDamage,
      @Optional boolean mirrored,
      @Optional boolean hidden,
      @Optional IRecipeFunction function,
      @Optional IRecipeAction action
  ) {

    CraftTweaker.LATE_ACTIONS.add(new ActionAddShapedRecipe(name, output, ingredients, function, action, mirrored, hidden, tool, toolDamage));
  }

  @ZenDocMethod(
      order = 5,
      args = {
          @ZenDocArg(arg = "name"),
          @ZenDocArg(arg = "output"),
          @ZenDocArg(arg = "ingredients")
      },
      description = {
          "If the `name` parameter is `null`, a name will be generated."
      }
  )
  @ZenMethod
  public static void addShapeless(
      @ZenDocNullable String name,
      IItemStack output,
      IIngredient[] ingredients
  ) {

    ZenWorktable.addShapeless(name, output, ingredients, null, 0, false, null, null);
  }

  @ZenDocMethod(
      order = 6,
      args = {
          @ZenDocArg(arg = "name"),
          @ZenDocArg(arg = "output"),
          @ZenDocArg(arg = "ingredients"),
          @ZenDocArg(arg = "tool"),
          @ZenDocArg(arg = "toolDamage"),
          @ZenDocArg(arg = "hidden"),
          @ZenDocArg(arg = "function"),
          @ZenDocArg(arg = "action")
      },
      description = {
          "If the `name` parameter is `null`, a name will be generated.",
          "If the `tool` parameter is `null`, the recipe will default to using",
          "the hammers provided in the config and will ignore the `toolDamage`",
          "parameter."
      }
  )
  @ZenMethod
  public static void addShapeless(
      @ZenDocNullable String name,
      IItemStack output,
      IIngredient[] ingredients,
      @ZenDocNullable IIngredient tool,
      int toolDamage,
      @Optional boolean hidden,
      @Optional IRecipeFunction function,
      @Optional IRecipeAction action
  ) {

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

    CraftTweaker.LATE_ACTIONS.add(new ActionAddShapelessRecipe(name, output, ingredients, function, action, hidden, tool, toolDamage));
  }

  @ZenDocMethod(
      order = 7,
      args = {
          @ZenDocArg(arg = "resourceLocations")
      }
  )
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

  @ZenDocMethod(
      order = 8,
      description = {
          "Blacklist all vanilla crafting recipes."
      }
  )
  @ZenMethod
  public static void blacklistAllVanillaRecipes() {

    WorktableRecipe.blacklistAll();
  }

  @ZenDocMethod(
      order = 9,
      args = {
          @ZenDocArg(arg = "resourceLocations")
      }
  )
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

  @ZenDocMethod(
      order = 10,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredient to match")
      },
      description = {
          "Removes pre-existing recipes, ie. recipes added by the mod."
      }
  )
  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenDocMethod(
      order = 9,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use this device."
      }
  )
  @ZenMethod
  public static void setGameStages(ZenStages stages) {

    ModuleTechBasicConfig.STAGES_WORKTABLE = stages.getStages();
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

    protected MCRecipeBase recipe;
    protected IItemStack output;
    protected boolean isShaped;
    protected String name;
    protected IIngredient tool;
    protected int toolDamage;

    private ActionBaseAddWorktableRecipe(MCRecipeBase recipe, IItemStack output, boolean isShaped, @Nullable IIngredient tool, int toolDamage) {

      this.recipe = recipe;
      this.output = output;
      this.isShaped = isShaped;
      this.tool = tool;
      this.toolDamage = toolDamage;

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
          this.name = this.calculateName();
          CraftTweakerAPI.logWarning("Recipe name [" + name + "] has duplicate uses, defaulting to calculated hash!");

        } else {
          this.name = proposedName;
        }

      } else {
        this.name = this.calculateName();
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
      WorktableRecipe recipe = new WorktableRecipe(this.recipe.setRegistryName(resourceLocation), CTInputHelper.toIngredient(this.tool), this.toolDamage).setRegistryName(resourceLocation);
      ModuleTechBasic.Registries.WORKTABLE_RECIPE.register(recipe);
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

      return this.recipe;
    }
  }

  private static class ActionAddShapedRecipe
      extends ActionBaseAddWorktableRecipe {

    public ActionAddShapedRecipe(@Nullable String name, IItemStack output, IIngredient[][] ingredients, @Nullable IRecipeFunction function, @Nullable IRecipeAction action, boolean mirrored, boolean hidden, @Nullable IIngredient tool, int toolDamage) {

      super(new MCRecipeShaped(ingredients, output, function, action, mirrored, hidden), output, true, tool, toolDamage);
      this.setName(name);
    }
  }

  private static class ActionAddShapelessRecipe
      extends ActionBaseAddWorktableRecipe {

    public ActionAddShapelessRecipe(@Nullable String name, IItemStack output, IIngredient[] ingredients, @Nullable IRecipeFunction function, @Nullable IRecipeAction action, boolean hidden, @Nullable IIngredient tool, int toolDamage) {

      super(new MCRecipeShapeless(ingredients, output, function, action, hidden), output, false, tool, toolDamage);
      this.setName(name);
    }
  }
}
