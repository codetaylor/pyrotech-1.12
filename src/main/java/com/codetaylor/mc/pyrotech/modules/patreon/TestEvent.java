package com.codetaylor.mc.pyrotech.modules.patreon;

import com.codetaylor.mc.pyrotech.library.particle.ParticleEmitter;
import com.codetaylor.mc.pyrotech.library.particle.ParticleFactoryAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(Side.CLIENT)
public class TestEvent {

  private static final Set<RenderPlayer> ATTACHED_RENDERERS = new HashSet<>();

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void on(RenderPlayerEvent.Pre event) {

    RenderPlayer renderer = event.getRenderer();

    if (!ATTACHED_RENDERERS.contains(renderer)) {
      renderer.addLayer(new TestLayer(renderer.getMainModel().bipedHead));
      ATTACHED_RENDERERS.add(renderer);
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void on(TickEvent.ClientTickEvent event) {

    if (ModulePatreonConfig.CLIENT.DISABLE_ALL_PATREON_EFFECTS) {
      return;
    }

    if (event.phase == TickEvent.Phase.START) {

      Minecraft minecraft = Minecraft.getMinecraft();
      WorldClient world = minecraft.world;

      if (minecraft.isGamePaused()) {
        return;
      }

//      UUID uuid = this.getUuid();
      //UUID uuid = UUID.fromString("46562dd7-ada9-4af8-b88c-3a0f2d3e8860");

      if (ModulePatreonConfig.CLIENT.DISABLE_YOUR_PATREON_EFFECTS) {
        EntityPlayerSP player = minecraft.player;

        if (player == null) {
          return;
        }

        UUID uniqueID = player.getUniqueID();

//        if (uuid.equals(uniqueID)) {
//          return;
//        }
      }

      if (world != null) {

        if (world.getTotalWorldTime() % 4 != 0) {
          return;
        }

//        EntityPlayer entity = PlayerEntityTracker.getEntityForPlayer(uuid);

        EntityPlayer entity = minecraft.player;

        if (entity == null) {
          return;
        }

        if (entity.isInWater()) {
          minecraft.effectRenderer.addEffect(new ParticleEmitter(
              world,
              new Vec3d(entity.posX, entity.posY + 2.0, entity.posZ),
              new Vec3d(0.25, 0.0125, 0.25),
              15,
              20 * 2,
              new ParticleFactoryAdapter(new ParticleSmokeNormal.Factory())
          ));

        } else {
          minecraft.effectRenderer.addEffect(new ParticleEmitter(
              world,
              new Vec3d(entity.posX, entity.posY + 2.0, entity.posZ),
              new Vec3d(0.25, 0.0125, 0.25),
              entity.isSprinting() ? 2 : 15,
              entity.isSprinting() ? 20 * 4 : 20 * 2,
              new ParticleFactoryAdapter(new ParticleFlame.Factory())
          ));
        }
      }
    }
  }
}
