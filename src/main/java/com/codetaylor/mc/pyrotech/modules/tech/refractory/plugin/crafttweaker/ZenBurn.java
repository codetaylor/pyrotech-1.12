package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.Burn")
@ZenClass("mods.pyrotech.Burn")
public class ZenBurn {

  @ZenDocMethod(
      order = 1,
      description = {
          "|Parameter|Description|\n" +
              "|---------|-----------|\n" +
              "|name|the name of the recipe|\n" +
              "|output|the output for each completed burn stage|\n" +
              "|burnStages|the number of burn stages|\n" +
              "|totalBurnTimeTicks|the total number of ticks required to complete all burn stages|\n" +
              "|fluidProduced|the fluid produced for each completed burn stage|\n" +
              "|failureChance|the chance a failure item will be substituted for each burn stage result|\n" +
              "|failureItems|a list of items from which to pick a substitute for each failed burn stage result; items chosen randomly|\n" +
              "|requiresRefractoryBlocks|true if the recipe requires using refractory blocks|\n" +
              "|fluidLevelAffectsFailureChance|true if the build-up of fluid in burning blocks increases the failure chance of burn stages|"
      },
      args = {"name", "output", "blockString", "burnStages", "totalBurnTimeTicks", "fluidProduced", "failureChance", "failureItems", "requiresRefractoryBlocks", "fluidLevelAffectsFailureChance"}
  )
  @ZenMethod
  public static void addRecipe(
      String name,
      IItemStack output,
      String blockString,
      int burnStages,
      int totalBurnTimeTicks,
      ILiquidStack fluidProduced,
      float failureChance,
      IItemStack[] failureItems,
      boolean requiresRefractoryBlocks,
      boolean fluidLevelAffectsFailureChance
  ) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        blockString,
        burnStages,
        totalBurnTimeTicks,
        CraftTweakerMC.getLiquidStack(fluidProduced),
        failureChance,
        CraftTweakerMC.getItemStacks(failureItems),
        requiresRefractoryBlocks,
        fluidLevelAffectsFailureChance
    ));
  }

  @ZenDocMethod(
      order = 2,
      description = "Remove all recipes with the given recipe output.",
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

      PitBurnRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing pit burn recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final String blockString;
    private final ItemStack output;
    private final int burnStages;
    private final int totalBurnTimeTicks;
    private final FluidStack fluidProduced;
    private final float failureChance;
    private final ItemStack[] failureItems;
    private final boolean requiresRefractoryBlocks;
    private final boolean fluidLevelAffectsFailureChance;

    public AddRecipe(
        String name,
        ItemStack output,
        String blockString,
        int burnStages,
        int totalBurnTimeTicks,
        FluidStack fluidProduced,
        float failureChance,
        ItemStack[] failureItems,
        boolean requiresRefractoryBlocks,
        boolean fluidLevelAffectsFailureChance
    ) {

      this.name = name;
      this.output = output;
      this.blockString = blockString;
      this.burnStages = burnStages;
      this.totalBurnTimeTicks = totalBurnTimeTicks;
      this.fluidProduced = fluidProduced;
      this.failureChance = failureChance;
      this.failureItems = failureItems;
      this.requiresRefractoryBlocks = requiresRefractoryBlocks;
      this.fluidLevelAffectsFailureChance = fluidLevelAffectsFailureChance;
    }

    @Override
    public void apply() {

      try {
        BlockMetaMatcher blockMetaMatcher = Util.parseBlockStringWithWildcard(this.blockString, new RecipeItemParser());

        PitBurnRecipe recipe = new PitBurnRecipe(
            this.output,
            blockMetaMatcher,
            this.burnStages,
            this.totalBurnTimeTicks,
            this.fluidProduced,
            this.failureChance,
            this.failureItems,
            this.requiresRefractoryBlocks,
            this.fluidLevelAffectsFailureChance
        );
        ModuleTechRefractory.Registries.BURN_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));

      } catch (MalformedRecipeItemException e) {
        CraftTweakerAPI.logError("", e);
      }
    }

    @Override
    public String describe() {

      return "Adding burn recipe for " + this.output;
    }
  }

}
