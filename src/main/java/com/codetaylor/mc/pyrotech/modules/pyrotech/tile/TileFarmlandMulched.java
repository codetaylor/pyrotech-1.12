package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

public class TileFarmlandMulched
    extends TileEntity {

  private int remainingCharges;

  public TileFarmlandMulched() {

    this.setRemainingCharges(ModulePyrotechConfig.MULCHED_FARMLAND.CHARGES);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public int getRemainingCharges() {

    return this.remainingCharges;
  }

  private void setRemainingCharges(int value) {

    this.remainingCharges = Math.max(1, value);
  }

  public void decrementRemainingCharges() {

    this.remainingCharges -= 1;
    this.markDirty();
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setInteger("remainingCharges", this.remainingCharges);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.setRemainingCharges(compound.getInteger("remainingCharges"));
  }
}
