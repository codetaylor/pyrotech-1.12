package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.JEIRecipeWrapperTimed;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.spi.DryingRackRecipeBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperDryingRack
    extends JEIRecipeWrapperTimed {

  private final ResourceLocation registryName;
  private final List<List<ItemStack>> inputs;
  private final ItemStack output;

  public JEIRecipeWrapperDryingRack(DryingRackRecipeBase recipe) {

    super(recipe);

    this.registryName = recipe.getRegistryName();
    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));
    this.output = recipe.getOutput();
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }

  @Override
  protected int getTimeDisplayY() {

    return super.getTimeDisplayY() - 14;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {

    return this.registryName;
  }

  @Override
  protected int getRecipeTime(IRecipeTimed recipe) {

    return (int) (super.getRecipeTime(recipe) * ModuleTechBasicConfig.DRYING_RACK.BASE_RECIPE_DURATION_MODIFIER);
  }
}
