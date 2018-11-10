package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei;

import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnBrickRecipe;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperKilnBrick
    extends JEIRecipeWrapperTimed {

  private final List<List<ItemStack>> inputs;
  private final ItemStack output;

  public JEIRecipeWrapperKilnBrick(KilnBrickRecipe recipe) {

    super(recipe);

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));
    this.output = recipe.getOutput();
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(ItemStack.class, this.inputs);
    ingredients.setOutput(ItemStack.class, this.output);
  }
}
