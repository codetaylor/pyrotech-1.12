package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.OvenStoneRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileStoneOven
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

    return ModuleTechMachineConfig.STONE_OVEN.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.STONE_OVEN.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.STONE_OVEN.FUEL_SLOT_SIZE;
  }

}
