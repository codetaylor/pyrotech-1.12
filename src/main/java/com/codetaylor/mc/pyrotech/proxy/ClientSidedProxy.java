package com.codetaylor.mc.pyrotech.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ClientSidedProxy
    extends SidedProxy {

  @Override
  public boolean isRemote() {

    return true;
  }

  @Override
  public void playSound(SoundEvent soundEvent, SoundCategory soundCategory) {

    Minecraft minecraft = Minecraft.getMinecraft();
    WorldClient world = minecraft.world;
    EntityPlayerSP player = minecraft.player;

    // playSound(BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay)
    world.playSound(
        player.getPosition(),
        soundEvent,
        soundCategory,
        (0.5F + (float) Math.random() * 0.5F),
        0.9F + (float) Math.random() * 0.15F,
        false
    );
  }
}
