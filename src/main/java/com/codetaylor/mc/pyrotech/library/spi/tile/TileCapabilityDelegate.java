package com.codetaylor.mc.pyrotech.library.spi.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileCapabilityDelegate
    extends TileEntity {

  private final EnumFacing delegateToOffset;
  private final EnumFacing delegateToFacing;

  protected TileCapabilityDelegate(EnumFacing delegateToOffset, EnumFacing delegateToFacing) {

    this.delegateToOffset = delegateToOffset;
    this.delegateToFacing = delegateToFacing;
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.offset(this.delegateToOffset));

    if (tileEntity != null) {
      return tileEntity.hasCapability(capability, this.delegateToFacing);
    }

    return false;
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.offset(this.delegateToOffset));

    if (tileEntity != null) {
      return tileEntity.getCapability(capability, this.delegateToFacing);
    }

    return null;
  }
}
