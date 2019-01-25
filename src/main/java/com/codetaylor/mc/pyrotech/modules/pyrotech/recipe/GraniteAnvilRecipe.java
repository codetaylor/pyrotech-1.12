package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class GraniteAnvilRecipe
    extends IForgeRegistryEntry.Impl<GraniteAnvilRecipe>
    implements IRecipeSingleOutput {

  public enum EnumType {
    HAMMER, PICKAXE
  }

  @Nullable
  public static GraniteAnvilRecipe getRecipe(ItemStack input) {

    for (GraniteAnvilRecipe recipe : ModPyrotechRegistries.GRANITE_ANVIL_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModPyrotechRegistries.GRANITE_ANVIL_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int hits;
  private final EnumType type;

  public GraniteAnvilRecipe(
      ItemStack output,
      Ingredient input,
      int hits,
      EnumType type
  ) {

    this.input = input;
    this.output = output;
    this.hits = hits;
    this.type = type;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getHits() {

    return this.hits;
  }

  public EnumType getType() {

    return this.type;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
