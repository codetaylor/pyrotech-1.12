package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import com.codetaylor.mc.pyrotech.library.spi.plugin.jei.JEIRecipeWrapperTimed;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.SoakingPotRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperSoakingPot
    extends JEIRecipeWrapperTimed {

  private final ResourceLocation registryName;
  private final List<List<ItemStack>> inputs;
  private final FluidStack inputFluid;
  private final ItemStack output;

  public JEIRecipeWrapperSoakingPot(SoakingPotRecipe recipe) {

    super(recipe);

    this.registryName = recipe.getRegistryName();

    this.inputs = new ArrayList<>(2);
    this.inputs.add(Arrays.asList(recipe.getInputItem().getMatchingStacks()));

    if (recipe.isCampfireRequired()) {
      this.inputs.add(Collections.singletonList(new ItemStack(ModuleTechBasic.Blocks.CAMPFIRE)));
    }
    this.inputFluid = recipe.getInputFluid();

    this.output = recipe.getOutput();
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setInput(VanillaTypes.FLUID, this.inputFluid);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }

  public FluidStack getInputFluid() {

    return this.inputFluid;
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
