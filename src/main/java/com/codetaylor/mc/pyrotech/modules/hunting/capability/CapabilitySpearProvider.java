package com.codetaylor.mc.pyrotech.modules.hunting.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilitySpearProvider
    implements ICapabilitySerializable<NBTTagCompound> {

  private final SpearEntityData data;

  public CapabilitySpearProvider() {

    this.data = new SpearEntityData();
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilitySpear.INSTANCE);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilitySpear.INSTANCE) {
      //noinspection unchecked
      return (T) this.data;
    }

    return null;
  }

  @Override
  public NBTTagCompound serializeNBT() {

    return (NBTTagCompound) this.data.writeNBT(CapabilitySpear.INSTANCE, this.data, null);
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {

    this.data.readNBT(CapabilitySpear.INSTANCE, this.data, null, nbt);
  }
}
