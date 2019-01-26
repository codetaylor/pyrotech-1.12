package com.codetaylor.mc.pyrotech.library.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

// TODO: switch to tile data tank and remove this

/**
 * Derived from:
 * https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/slimeknights/tconstruct/library/fluid/FluidTankBase.java
 */
public class FluidTankBase
    extends FluidTank {

  protected IFluidTankUpdateListener fluidTankUpdateListener;

  public FluidTankBase(int capacity) {

    this(capacity, null);
  }

  public FluidTankBase(int capacity, @Nullable IFluidTankUpdateListener fluidTankUpdateListener) {

    super(capacity);
    this.fluidTankUpdateListener = fluidTankUpdateListener;
  }

  @Override
  public int fillInternal(FluidStack resource, boolean doFill) {

    int amount = super.fillInternal(resource, doFill);

    if (amount > 0 && doFill) {
      this.onContentsChanged(amount);
    }

    return amount;
  }

  @Nullable
  @Override
  public FluidStack drainInternal(int maxDrain, boolean doDrain) {

    FluidStack fluidStack = super.drainInternal(maxDrain, doDrain);

    if (fluidStack != null && doDrain) {
      this.onContentsChanged(-fluidStack.amount);
    }

    return fluidStack;
  }

  @Override
  public void setCapacity(int capacity) {

    this.capacity = capacity;

    if (this.fluid != null
        && this.fluid.amount > capacity) {
      this.drainInternal(this.fluid.amount - capacity, true);
    }
  }

  protected void onContentsChanged(int amount) {

    if (amount != 0
        && this.fluidTankUpdateListener != null) {
      this.fluidTankUpdateListener.onTankContentsChanged(this);
    }
  }
}
