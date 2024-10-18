package com.codetaylor.mc.pyrotech.modules.core.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerSleepInStrawBedEvent
    extends PlayerEvent {

  private final BlockPos pos;

  public PlayerSleepInStrawBedEvent(EntityPlayer player, BlockPos pos) {

    super(player);
    this.pos = pos;
  }

  public BlockPos getPos() {

    return this.pos;
  }
}