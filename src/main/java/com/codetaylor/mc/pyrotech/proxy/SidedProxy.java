package com.codetaylor.mc.pyrotech.proxy;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class SidedProxy {

  public boolean isRemote() {

    return false;
  }

  public void playSound(SoundEvent soundEvent, SoundCategory soundCategory) {

    //
  }

  public void playSound(SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch, boolean distanceDelay) {
    //
  }

  public void playSound(BlockPos pos, SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch, boolean distanceDelay) {

    //
  }
}
