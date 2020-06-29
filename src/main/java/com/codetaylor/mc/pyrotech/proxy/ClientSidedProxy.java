package com.codetaylor.mc.pyrotech.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ClientSidedProxy
    extends SidedProxy {

  @Override
  public boolean isRemote() {

    return true;
  }

  @Override
  public void playSound(SoundEvent soundEvent, SoundCategory soundCategory) {

    Minecraft minecraft = Minecraft.getMinecraft();
    EntityPlayerSP player = minecraft.player;

    if (player == null) {
      return;
    }

    this.playSound(
        player.getPosition(),
        soundEvent,
        soundCategory,
        (0.5F + (float) Math.random() * 0.5F),
        0.9F + (float) Math.random() * 0.15F,
        false
    );
  }

  @Override
  public void playSound(SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch, boolean distanceDelay) {

    Minecraft minecraft = Minecraft.getMinecraft();
    WorldClient world = minecraft.world;
    EntityPlayerSP player = minecraft.player;

    if (player == null) {
      return;
    }

    if (world != null) {
      world.playSound(player.getPosition(), soundEvent, soundCategory, volume, pitch, distanceDelay);
    }
  }

  @Override
  public void playSound(BlockPos pos, SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch, boolean distanceDelay) {

    Minecraft minecraft = Minecraft.getMinecraft();
    WorldClient world = minecraft.world;

    if (world != null) {
      world.playSound(pos, soundEvent, soundCategory, volume, pitch, distanceDelay);
    }
  }
}
