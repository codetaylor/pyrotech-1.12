package com.codetaylor.mc.pyrotech.modules.hunting.capability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilitySpear {

  @CapabilityInject(ISpearEntityData.class)
  public static Capability<ISpearEntityData> INSTANCE = null;

  public static ISpearEntityData get(EntityLivingBase entity) {

    return entity.getCapability(INSTANCE, null);
  }

}
