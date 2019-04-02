package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper;

import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInFluidOutBase;
import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.JEIRecipeWrapperTimed;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperCrucible
    extends JEIRecipeWrapperTimed {

  private final ResourceLocation registryName;
  private final List<List<ItemStack>> inputs;
  private final FluidStack output;

  public JEIRecipeWrapperCrucible(MachineRecipeItemInFluidOutBase recipe) {

    super(recipe);

    this.registryName = recipe.getRegistryName();

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));
    this.output = recipe.getOutput();
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutput(VanillaTypes.FLUID, this.output);
  }

  @Override
  protected int getTimeDisplayY() {

    return super.getTimeDisplayY() - 8;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {

    return this.registryName;
  }
}
