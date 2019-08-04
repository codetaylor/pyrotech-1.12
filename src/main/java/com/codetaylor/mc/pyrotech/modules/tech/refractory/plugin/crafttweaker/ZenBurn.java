package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.tools.ZenDocAppend;
import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.library.crafttweaker.RemoveAllRecipesAction;
import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipeBuilder;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.Burn")
@ZenDocAppend({"docs/include/burn.example.md"})
@ZenClass("mods.pyrotech.Burn")
public class ZenBurn {

  @ZenDocMethod(
      order = 1,
      description = "Remove all recipes with the given recipe output.",
      args = {
          @ZenDocArg(arg = "output", info = "output ingredient to match")
      }
  )
  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenDocMethod(
      order = 2
  )
  @ZenMethod
  public static void removeAllRecipes() {

    CraftTweaker.LATE_ACTIONS.add(new RemoveAllRecipesAction<>(ModuleTechRefractory.Registries.BURN_RECIPE, "burn"));
  }

  private final String name;
  private final PitBurnRecipeBuilder builder;

  public ZenBurn(String name, PitBurnRecipeBuilder builder) {

    this.name = name;
    this.builder = builder;
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "blockString", info = "block string to match")
      }
  )
  @ZenMethod
  public static ZenBurn createBuilder(String name, IItemStack output, String blockString) {

    ItemStack itemStack = CTInputHelper.toStack(output);

    try {
      ParseResult result = RecipeItemParser.INSTANCE.parse(blockString);
      Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(result.getDomain(), result.getPath()));

      if (block == null) {
        CTLogHelper.logError("Unable to locate block for block-string: " + result);
        return null;
      }

      return new ZenBurn(name, new PitBurnRecipeBuilder(itemStack, new BlockMetaMatcher(block, result.getMeta())));

    } catch (MalformedRecipeItemException e) {
      CTLogHelper.logError("Unable to parse block-string: " + blockString, e);
    }

    return null;
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "burnStages")
      }
  )
  @ZenMethod
  public ZenBurn setBurnStages(int burnStages) {

    this.builder.setBurnStages(burnStages);
    return this;
  }

  @ZenDocMethod(
      order = 5,
      args = {
          @ZenDocArg(arg = "totalBurnTimeTicks")
      }
  )
  @ZenMethod
  public ZenBurn setTotalBurnTimeTicks(int totalBurnTimeTicks) {

    this.builder.setTotalBurnTimeTicks(totalBurnTimeTicks);
    return this;
  }

  @ZenDocMethod(
      order = 6,
      args = {
          @ZenDocArg(arg = "fluidProduced")
      }
  )
  @ZenMethod
  public ZenBurn setFluidProduced(ILiquidStack fluidProduced) {

    this.builder.setFluidProduced(CTInputHelper.toFluid(fluidProduced));
    return this;
  }

  @ZenDocMethod(
      order = 7,
      args = {
          @ZenDocArg(arg = "failureChance")
      }
  )
  @ZenMethod
  public ZenBurn setFailureChance(float failureChance) {

    this.builder.setFailureChance(failureChance);
    return this;
  }

  @ZenDocMethod(
      order = 8,
      args = {
          @ZenDocArg(arg = "failureItem")
      }
  )
  @ZenMethod
  public ZenBurn addFailureItem(IItemStack failureItem) {

    this.builder.addFailureItem(CTInputHelper.toStack(failureItem));
    return this;
  }

  @ZenDocMethod(
      order = 9,
      args = {
          @ZenDocArg(arg = "requiresRefractoryBlocks")
      }
  )
  @ZenMethod
  public ZenBurn setRequiresRefractoryBlocks(boolean requiresRefractoryBlocks) {

    this.builder.setRequiresRefractoryBlocks(requiresRefractoryBlocks);
    return this;
  }

  @ZenDocMethod(
      order = 10,
      args = {
          @ZenDocArg(arg = "fluidLevelAffectsFailureChance")
      }
  )
  @ZenMethod
  public ZenBurn setFluidLevelAffectsFailureChance(boolean fluidLevelAffectsFailureChance) {

    this.builder.setFluidLevelAffectsFailureChance(fluidLevelAffectsFailureChance);
    return this;
  }

  @ZenDocMethod(
      order = 11
  )
  @ZenMethod
  public void register() {

    PitBurnRecipe recipe = this.builder.create(new ResourceLocation("crafttweaker", this.name));
    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(recipe));
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      PitBurnRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing pit burn recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final PitBurnRecipe recipe;

    public AddRecipe(PitBurnRecipe recipe) {

      this.recipe = recipe;
    }

    @Override
    public void apply() {

      ModuleTechRefractory.Registries.BURN_RECIPE.register(this.recipe);
    }

    @Override
    public String describe() {

      return "Adding pit burn recipe for " + this.recipe.getOutput();
    }
  }

}
