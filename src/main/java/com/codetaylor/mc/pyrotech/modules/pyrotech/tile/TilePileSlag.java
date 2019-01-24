package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.inventory.LIFOStackHandler;
import com.codetaylor.mc.athenaeum.spi.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TilePileSlag
    extends TileEntityBase {

  private StackHandler stackHandler;

  public TilePileSlag() {

    this.stackHandler = new StackHandler();
  }

  public StackHandler getStackHandler() {

    return this.stackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
  }

  public static class StackHandler
      extends LIFOStackHandler {

    /* package */ StackHandler() {

      super(8);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }
  }

}
