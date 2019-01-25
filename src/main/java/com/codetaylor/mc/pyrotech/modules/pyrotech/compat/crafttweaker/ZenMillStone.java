package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.MillStoneRecipe;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.StoneSawmill")
@ZenClass("mods.pyrotech.StoneSawmill")
public class ZenMillStone {

  @ZenDocMethod(
      order = 1,
      args = {"name", "output", "input", "burnTimeTicks", "blade", "createWoodChips"}
  )
  @ZenMethod
  public static void addRecipe(
      String name,
      IItemStack output,
      IIngredient input,
      int burnTimeTicks,
      IIngredient blade,
      @Optional(valueBoolean = true) boolean createWoodChips
  ) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        burnTimeTicks,
        CraftTweakerMC.getIngredient(blade),
        createWoodChips
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

      MillStoneRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing stone mill recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final ItemStack output;
    private final Ingredient input;
    private final int burnTimeTicks;
    private final Ingredient blade;
    private final boolean createWoodChips;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int burnTimeTicks,
        Ingredient blade,
        boolean createWoodChips
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.burnTimeTicks = burnTimeTicks;
      this.blade = blade;
      this.createWoodChips = createWoodChips;
    }

    @Override
    public void apply() {

      MillStoneRecipe recipe = new MillStoneRecipe(
          this.output,
          this.input,
          this.burnTimeTicks,
          this.blade,
          this.createWoodChips
      );
      ModPyrotechRegistries.MILL_STONE_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding stone mill recipe for " + this.output;
    }
  }

}
