package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class TanningRackRecipe
    extends IForgeRegistryEntry.Impl<TanningRackRecipe>
    implements IRecipeSingleOutput,
    IRecipeTimed {

  @Nullable
  public static TanningRackRecipe getRecipe(ItemStack inputItem) {

    if (inputItem.isEmpty()) {
      return null;
    }

    for (TanningRackRecipe recipe : ModuleTechBasic.Registries.TANNING_RACK_RECIPE) {

      if (recipe.matches(inputItem)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.TANNING_RACK_RECIPE, output);
  }

  private final ItemStack output;
  private final Ingredient inputItem;
  private final ItemStack rainFailureItem;
  private final int timeTicks;

  public TanningRackRecipe(
      ItemStack output,
      Ingredient inputItem,
      ItemStack rainFailureItem,
      int timeTicks
  ) {

    this.output = output;
    this.inputItem = inputItem;
    this.rainFailureItem = rainFailureItem;
    this.timeTicks = timeTicks;
  }

  public Ingredient getInputItem() {

    return this.inputItem;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public ItemStack getRainFailureItem() {

    return this.rainFailureItem.copy();
  }

  @Override
  public int getTimeTicks() {

    return (int) Math.max(1, this.timeTicks * ModuleTechBasicConfig.TANNING_RACK.BASE_RECIPE_DURATION_MODIFIER);
  }

  public boolean matches(ItemStack inputItem) {

    return this.inputItem.apply(inputItem);
  }
}
