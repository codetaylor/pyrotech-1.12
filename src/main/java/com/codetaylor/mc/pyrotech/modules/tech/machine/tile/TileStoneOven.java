package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileStoneOven
    extends TileCombustionWorkerStoneItemInItemOutBase<StoneOvenRecipe> {

  @Override
  public StoneOvenRecipe getRecipe(ItemStack itemStack) {

    return StoneOvenRecipe.getRecipe(itemStack);
  }

  @Override
  protected List<ItemStack> getRecipeOutput(StoneOvenRecipe recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

    ItemStack output = recipe.getOutput();
    ItemStack copy = output.copy();
    copy.setCount(input.getCount());
    outputItemStacks.add(copy);
    return outputItemStacks;
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.STONE_OVEN.ALLOW_AUTOMATION;
  }

  @Override
  protected double getFuelBurnTimeModifier(ItemStack fuel) {

    return ModuleTechMachineConfig.STONE_OVEN.FUEL_BURN_TIME_MODIFIER;
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

  @Override
  protected double getAirflowModifier() {

    return ModuleTechMachineConfig.STONE_OVEN.AIRFLOW_MODIFIER;
  }

  @Override
  protected float getAirflowDragModifier() {

    return (float) ModuleTechMachineConfig.STONE_OVEN.AIRFLOW_DRAG_MODIFIER;
  }
}
