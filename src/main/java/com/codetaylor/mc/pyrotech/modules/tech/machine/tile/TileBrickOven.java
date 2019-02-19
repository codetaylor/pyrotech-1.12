package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickOvenRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileBrickOven
    extends TileCombustionWorkerStoneItemInItemOutBase<BrickOvenRecipe> {

  @Override
  public BrickOvenRecipe getRecipe(ItemStack itemStack) {

    return BrickOvenRecipe.getRecipe(itemStack);
  }

  @Override
  protected List<ItemStack> getRecipeOutput(BrickOvenRecipe recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

    ItemStack output = recipe.getOutput();
    ItemStack copy = output.copy();
    copy.setCount(input.getCount());
    outputItemStacks.add(copy);
    return outputItemStacks;
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.BRICK_OVEN.ALLOW_AUTOMATION;
  }

  @Override
  protected double getFuelBurnTimeModifier(ItemStack fuel) {

    return ModuleTechMachineConfig.BRICK_OVEN.FUEL_BURN_TIME_MODIFIER;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.BRICK_OVEN.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.BRICK_OVEN.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.BRICK_OVEN.FUEL_SLOT_SIZE;
  }

  @Override
  protected double getAirflowModifier() {

    return ModuleTechMachineConfig.BRICK_OVEN.AIRFLOW_MODIFIER;
  }

  @Override
  protected float getAirflowDragModifier() {

    return (float) ModuleTechMachineConfig.BRICK_OVEN.AIRFLOW_DRAG_MODIFIER;
  }
}
