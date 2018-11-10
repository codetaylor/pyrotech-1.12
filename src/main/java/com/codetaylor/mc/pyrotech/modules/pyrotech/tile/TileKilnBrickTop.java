package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileKilnBrickTop
    extends TileEntity {

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.down());

    if (tileEntity instanceof TileKilnBrick) {
      return tileEntity.hasCapability(capability, EnumFacing.UP);
    }

    return false;
  }

  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.down());

    if (tileEntity instanceof TileKilnBrick) {
      return tileEntity.getCapability(capability, EnumFacing.UP);
    }

    return null;
  }
}
