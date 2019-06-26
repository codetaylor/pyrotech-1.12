package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileKilnBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TileStoneKiln
    extends TileKilnBase<StoneKilnRecipe> {

  @Override
  public StoneKilnRecipe getRecipe(ItemStack itemStack) {

    return StoneKilnRecipe.getRecipe(itemStack);
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.STONE_KILN.ALLOW_AUTOMATION;
  }

  @Override
  protected double getFuelBurnTimeModifier(ItemStack fuel) {

    return ModuleTechMachineConfig.STONE_KILN.FUEL_BURN_TIME_MODIFIER;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.STONE_KILN.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.STONE_KILN.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.STONE_KILN.FUEL_SLOT_SIZE;
  }

  @Override
  protected double getAirflowModifier() {

    return ModuleTechMachineConfig.STONE_KILN.AIRFLOW_MODIFIER;
  }

  @Override
  protected float getAirflowDragModifier() {

    return (float) ModuleTechMachineConfig.STONE_KILN.AIRFLOW_DRAG_MODIFIER;
  }

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechMachineConfig.STAGES_STONE_KILN;
  }
}
