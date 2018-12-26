package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileCombustionWorkerStoneBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileStoneTop
    extends TileEntity {

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.down());

    if (tileEntity instanceof TileCombustionWorkerStoneBase) {
      return tileEntity.hasCapability(capability, EnumFacing.UP);
    }

    return false;
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.down());

    if (tileEntity instanceof TileCombustionWorkerStoneBase) {
      return tileEntity.getCapability(capability, EnumFacing.UP);
    }

    return null;
  }

  public boolean isCustom() {

    return false;
  }
}
