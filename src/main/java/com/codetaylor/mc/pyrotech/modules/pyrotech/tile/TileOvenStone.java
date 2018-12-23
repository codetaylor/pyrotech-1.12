package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.OvenStoneRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileCombustionWorkerStoneBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileOvenStone
    extends TileCombustionWorkerStoneBase<OvenStoneRecipe> {

  @Override
  protected OvenStoneRecipe getRecipe(ItemStack itemStack) {

    return OvenStoneRecipe.getRecipe(itemStack);
  }

  @Override
  protected List<ItemStack> getRecipeOutput(OvenStoneRecipe recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

    ItemStack output = recipe.getOutput();
    ItemStack copy = output.copy();
    copy.setCount(input.getCount());
    outputItemStacks.add(copy);
    return outputItemStacks;
  }
}
