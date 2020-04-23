package com.codetaylor.mc.pyrotech.modules.tech.basic.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class CapabilityFocused {

  @CapabilityInject(IFocusedPlayerData.class)
  public static Capability<IFocusedPlayerData> FOCUSED = null;

  public static IFocusedPlayerData get(EntityPlayer player) {

    return player.getCapability(FOCUSED, null);
  }
}
