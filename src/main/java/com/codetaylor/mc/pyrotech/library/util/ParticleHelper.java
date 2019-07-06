package com.codetaylor.mc.pyrotech.library.util;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public final class ParticleHelper {

  @SideOnly(Side.CLIENT)
  public static void spawnProgressParticlesClient(int amount, double x, double y, double z, double rangeX, double rangeY, double rangeZ) {

    WorldClient world = Minecraft.getMinecraft().world;
    Random random = RandomHelper.random();

    for (int i = 0; i < amount; ++i) {
      double d0 = random.nextGaussian() * 0.02D;
      double d1 = random.nextGaussian() * 0.02D;
      double d2 = random.nextGaussian() * 0.02D;
      world.spawnParticle(
          EnumParticleTypes.VILLAGER_HAPPY,
          x + (random.nextFloat() * 2 - 1) * rangeX,
          y + (random.nextFloat() * 2 - 1) * rangeY,
          z + (random.nextFloat() * 2 - 1) * rangeZ,
          d0, d1, d2
      );
    }
  }

  private ParticleHelper() {
    //
  }

}
