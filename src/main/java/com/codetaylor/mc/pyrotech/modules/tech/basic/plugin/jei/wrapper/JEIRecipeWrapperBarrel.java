package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.JEIRecipeWrapperTimed;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.BarrelRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JEIRecipeWrapperBarrel
    extends JEIRecipeWrapperTimed {

  private final ResourceLocation registryName;
  private final List<List<ItemStack>> inputs;
  private final FluidStack inputFluid;
  private final FluidStack outputFluid;

  public JEIRecipeWrapperBarrel(BarrelRecipe recipe) {

    super(recipe);

    this.registryName = recipe.getRegistryName();

    Ingredient[] inputItems = recipe.getInputItems();
    this.inputs = Arrays.stream(inputItems)
        .map(ingredient -> Arrays.asList(ingredient.getMatchingStacks()))
        .collect(Collectors.toList());

    this.inputFluid = recipe.getInputFluid();
    this.outputFluid = recipe.getOutput();
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setInput(VanillaTypes.FLUID, this.inputFluid);
    ingredients.setOutput(VanillaTypes.FLUID, this.outputFluid);
  }

  public FluidStack getInputFluid() {

    return this.inputFluid;
  }

  @Override
  protected int getTimeDisplayX(int stringWidth) {

    return 54 - stringWidth / 2;
  }

  @Override
  protected int getTimeDisplayY() {

    return super.getTimeDisplayY() - 2 + 3;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {

    return this.registryName;
  }
}
