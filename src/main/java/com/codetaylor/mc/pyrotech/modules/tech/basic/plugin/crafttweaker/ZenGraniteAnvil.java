package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
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

@ZenDocClass("mods.pyrotech.GraniteAnvil")
@ZenClass("mods.pyrotech.GraniteAnvil")
public class ZenGraniteAnvil {

  @ZenDocMethod(
      order = 1,
      args = {"name", "output", "input", "hits", "type"}
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, int hits, String type) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        hits,
        AnvilRecipe.EnumType.valueOf(type.toUpperCase())
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

      AnvilRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing granite anvil recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final ItemStack output;
    private final int hits;
    private final AnvilRecipe.EnumType type;
    private final String name;
    private final Ingredient input;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int hits,
        AnvilRecipe.EnumType type
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.hits = hits;
      this.type = type;
    }

    @Override
    public void apply() {

      AnvilRecipe recipe = new AnvilRecipe(
          this.output,
          this.input,
          this.hits,
          this.type
      );
      ModuleTechBasic.Registries.ANVIL_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding granite anvil recipe for " + this.output;
    }
  }

}
