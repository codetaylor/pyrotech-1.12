package com.codetaylor.mc.pyrotech.library.waila;

import net.minecraft.tileentity.TileEntity;

public abstract class ProviderDelegateBase<D, T extends TileEntity> {

  protected final D display;

  protected ProviderDelegateBase(D display) {

    this.display = display;
  }

  public abstract void display(T tile);
}
