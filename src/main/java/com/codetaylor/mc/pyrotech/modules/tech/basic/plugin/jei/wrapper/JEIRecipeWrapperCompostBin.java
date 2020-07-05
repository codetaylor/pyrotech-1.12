package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class does not implement {@link com.codetaylor.mc.pyrotech.library.spi.plugin.jei.IPyrotechRecipeWrapper}
 * because it does not supply a resource location per recipe. It instead is an aggregate of many different recipes.
 */
public class JEIRecipeWrapperCompostBin
    implements IRecipeWrapper {

  private final List<List<ItemStack>> inputs;
  private final ItemStack output;

  public JEIRecipeWrapperCompostBin(List<ItemStack> inputs, ItemStack output) {

    this.inputs = inputs.stream()
                        .map(ItemStack::copy)
                        .map(Collections::singletonList)
                        .collect(Collectors.toList());

    this.output = output;
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }

  public int getInputCount() {

    return this.inputs.size();
  }
}
