package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.jei;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper.JEIRecipeWrapperTimed;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperBloomery
    extends JEIRecipeWrapperTimed {

  private final List<List<ItemStack>> inputs;
  private final List<List<ItemStack>> outputs;

  public JEIRecipeWrapperBloomery(BloomeryRecipe recipe) {

    super(recipe);

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));

    this.outputs = new ArrayList<>();
    this.outputs.add(Collections.singletonList(recipe.getOutputBloom()));
    ItemStack copy = recipe.getSlagItemStack().copy();
    copy.setCount(recipe.getSlagCount());
    this.outputs.add(Collections.singletonList(copy));

  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutputLists(VanillaTypes.ITEM, this.outputs);
  }
}
