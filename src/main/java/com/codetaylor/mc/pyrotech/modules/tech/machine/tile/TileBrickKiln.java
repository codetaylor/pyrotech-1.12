package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileKilnBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TileBrickKiln
    extends TileKilnBase<StoneKilnRecipe> {

  @Override
  public StoneKilnRecipe getRecipe(ItemStack itemStack) {

    return StoneKilnRecipe.getRecipe(itemStack);
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.BRICK_KILN.ALLOW_AUTOMATION;
  }

  @Override
  protected double getFuelBurnTimeModifier(ItemStack fuel) {

    return ModuleTechMachineConfig.BRICK_KILN.FUEL_BURN_TIME_MODIFIER;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.BRICK_KILN.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.BRICK_KILN.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.BRICK_KILN.FUEL_SLOT_SIZE;
  }

  @Override
  protected double getAirflowModifier() {

    return ModuleTechMachineConfig.BRICK_KILN.AIRFLOW_MODIFIER;
  }

  @Override
  protected float getAirflowDragModifier() {

    return (float) ModuleTechMachineConfig.BRICK_KILN.AIRFLOW_DRAG_MODIFIER;
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechMachineConfig.STAGES_BRICK_KILN;
  }
}
