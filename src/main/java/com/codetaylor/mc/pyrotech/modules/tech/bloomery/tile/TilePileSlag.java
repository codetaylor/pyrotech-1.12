package com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile;

import com.codetaylor.mc.athenaeum.inventory.LIFOStackHandler;
import com.codetaylor.mc.athenaeum.spi.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TilePileSlag
    extends TileEntityBase {

  private StackHandler stackHandler;
  private long lastMolten;

  public TilePileSlag() {

    this.stackHandler = new StackHandler();
    this.stackHandler.addObserver((handler, slot) -> this.markDirty());
  }

  public StackHandler getStackHandler() {

    return this.stackHandler;
  }

  public void setLastMolten(long timestamp) {

    this.lastMolten = timestamp;
  }

  public long getLastMolten() {

    return this.lastMolten;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("stackHandler", this.stackHandler.serializeNBT());
    compound.setLong("lastMolten", this.lastMolten);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.stackHandler.deserializeNBT(compound.getCompoundTag("stackHandler"));
    this.lastMolten = compound.getLong("lastMolten");
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
