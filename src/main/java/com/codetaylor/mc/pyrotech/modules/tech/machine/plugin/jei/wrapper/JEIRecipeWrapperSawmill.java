package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.wrapper;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeBaseSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.wrapper.JEIRecipeWrapperTimed;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperSawmill
    extends JEIRecipeWrapperTimed {

  private final ResourceLocation registryName;
  private final List<List<ItemStack>> inputs;
  private final List<List<ItemStack>> output;

  public JEIRecipeWrapperSawmill(MachineRecipeBaseSawmill recipe) {

    super(recipe);

    this.registryName = recipe.getRegistryName();

    this.inputs = new ArrayList<>(2);
    this.inputs.add(Arrays.asList(recipe.getInput().getMatchingStacks()));
    this.inputs.add(Arrays.asList(recipe.getBlade().getMatchingStacks()));

    this.output = new ArrayList<>(2);
    this.output.add(Collections.singletonList(recipe.getOutput()));

    if (recipe.getWoodChips() > 0) {
      this.output.add(Collections.singletonList(new ItemStack(ModuleCore.Blocks.ROCK, recipe.getWoodChips(), BlockRock.EnumType.WOOD_CHIPS.getMeta())));
    }
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutputLists(VanillaTypes.ITEM, this.output);
  }

  @Override
  protected int getTimeDisplayY() {

    return super.getTimeDisplayY() - 2;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {

    return this.registryName;
  }
}
