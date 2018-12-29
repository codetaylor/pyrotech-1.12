package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.OvenStoneRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileOvenStone
    extends TileCombustionWorkerStoneItemInItemOutBase<OvenStoneRecipe> {

  @Override
  public OvenStoneRecipe getRecipe(ItemStack itemStack) {

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

  @Override
  protected boolean shouldKeepHeat() {

    return ModulePyrotechConfig.STONE_OVEN.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModulePyrotechConfig.STONE_OVEN.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModulePyrotechConfig.STONE_OVEN.FUEL_SLOT_SIZE;
  }
}
