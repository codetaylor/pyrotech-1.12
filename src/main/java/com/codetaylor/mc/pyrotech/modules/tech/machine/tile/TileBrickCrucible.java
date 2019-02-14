package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.BrickCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCrucibleBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

public class TileBrickCrucible
    extends TileCrucibleBase<BrickCrucibleRecipe> {

  @Override
  public BrickCrucibleRecipe getRecipe(ItemStack itemStack) {

    return BrickCrucibleRecipe.getRecipe(itemStack);
  }

  @Override
  protected EnumFacing[] getInputInteractionSides() {

    return new EnumFacing[]{EnumFacing.UP};
  }

  @Override
  protected FluidStack getRecipeOutput(BrickCrucibleRecipe recipe, ItemStack input) {

    FluidStack fluidStack = recipe.getOutput();

    if (this.processAsynchronous()) {
      fluidStack.amount = fluidStack.amount * input.getCount();
    }

    return fluidStack;
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.ALLOW_AUTOMATION;
  }

  @Override
  protected double getFuelBurnTimeModifier(ItemStack fuel) {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.FUEL_BURN_TIME_MODIFIER;
  }

  @Override
  public boolean processAsynchronous() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.ASYNCHRONOUS_OPERATION;
  }

  @Override
  protected int getOutputFluidTankSize() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.OUTPUT_TANK_SIZE;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.FUEL_SLOT_SIZE;
  }

  @Override
  protected boolean canHoldHotFluids() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.HOLDS_HOT_FLUIDS;
  }

  @Override
  protected int getHotFluidTemperature() {

    return ModuleTechMachineConfig.BRICK_CRUCIBLE.HOT_TEMPERATURE;
  }

  @Override
  public boolean allowInsertInput(ItemStack stack, BrickCrucibleRecipe recipe) {

    return true;
  }

  @Override
  protected void reduceRecipeTime() {

    ItemStack input = this.getInputStackHandler().getStackInSlot(0);
    BrickCrucibleRecipe recipe = this.getRecipe(input);

    if (recipe == null) {
      return;
    }

    FluidStack output = recipe.getOutput();
    output.amount *= input.getCount();

    if (this.getOutputFluidTank().fill(output, false) == output.amount) {
      super.reduceRecipeTime();
    }
  }
}
