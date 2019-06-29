package com.codetaylor.mc.pyrotech;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.concurrent.Callable;

public interface IAirflowConsumerCapability {

  /**
   * @param airflow  the amount of airflow to consume
   * @param simulate true for simulation only
   * @return the amount of airflow that was not consumed, if any
   * @since API v1
   */
  float consumeAirflow(float airflow, boolean simulate);

  class Storage
      implements Capability.IStorage<IAirflowConsumerCapability> {

    @Override
    public NBTBase writeNBT(Capability<IAirflowConsumerCapability> capability, IAirflowConsumerCapability instance, EnumFacing side) {

      return new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<IAirflowConsumerCapability> capability, IAirflowConsumerCapability instance, EnumFacing side, NBTBase nbt) {
      //
    }
  }

  class Factory
      implements Callable<IAirflowConsumerCapability> {

    @Override
    public IAirflowConsumerCapability call() {

      return (airflow, simulate) -> 0;
    }
  }

}
