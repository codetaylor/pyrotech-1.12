package com.codetaylor.mc.pyrotech.modules.patreon.effect;

import com.codetaylor.mc.pyrotech.library.particle.ParticleEmitter;
import com.codetaylor.mc.pyrotech.library.particle.ParticleFactoryAdapter;
import com.codetaylor.mc.pyrotech.library.patreon.effect.EffectBase;
import com.codetaylor.mc.pyrotech.modules.patreon.ModulePatreonConfig;
import com.codetaylor.mc.pyrotech.modules.patreon.PlayerEntityTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class EffectHotfoot
    extends EffectBase {

  public EffectHotfoot(UUID uuid) {

    super(uuid);
  }

  @Override
  public boolean subscribeEvents() {

    return true;
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void on(TickEvent.ClientTickEvent event) {

    if (ModulePatreonConfig.CLIENT.DISABLE_ALL_PATREON_EFFECTS) {
      return;
    }

    if (event.phase == TickEvent.Phase.START) {

      Minecraft minecraft = Minecraft.getMinecraft();
      WorldClient world = minecraft.world;

      UUID uuid = this.getUuid();

      if (ModulePatreonConfig.CLIENT.DISABLE_YOUR_PATREON_EFFECTS) {
        EntityPlayerSP player = minecraft.player;

        if (player == null) {
          return;
        }

        UUID uniqueID = player.getUniqueID();

        if (uuid.equals(uniqueID)) {
          return;
        }
      }

      if (world != null) {

        if (world.getTotalWorldTime() % 4 != 0) {
          return;
        }

        EntityPlayer entity = PlayerEntityTracker.getEntityForPlayer(uuid);

        if (entity == null) {
          return;
        }

        if (entity.isInWater()) {
          minecraft.effectRenderer.addEffect(new ParticleEmitter(
              world,
              new Vec3d(entity.posX, entity.posY + 0.0625, entity.posZ),
              new Vec3d(0.25, 0.125, 0.25),
              15,
              20 * 2,
              new ParticleFactoryAdapter(new ParticleSmokeNormal.Factory())
          ));

        } else {
          minecraft.effectRenderer.addEffect(new ParticleEmitter(
              world,
              new Vec3d(entity.posX, entity.posY + 0.0625, entity.posZ),
              new Vec3d(0.25, 0.125, 0.25),
              entity.isSprinting() ? 2 : 15,
              entity.isSprinting() ? 20 * 4 : 20 * 2,
              new ParticleFactoryAdapter(new ParticleFlame.Factory())
          ));
        }
      }
    }
  }
}
