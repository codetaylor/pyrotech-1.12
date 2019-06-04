package com.codetaylor.mc.pyrotech.library.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IParticleFactory {

  Particle createParticle(World world, double x, double y, double z);
}
