package com.codetaylor.mc.pyrotech.modules.patreon.effect;

import com.codetaylor.mc.pyrotech.library.particle.ParticleEmitter;
import com.codetaylor.mc.pyrotech.library.particle.ParticleFactoryAdapter;
import com.codetaylor.mc.pyrotech.library.patreon.effect.EffectBase;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.patreon.ModulePatreonConfig;
import com.codetaylor.mc.pyrotech.modules.patreon.PlayerEntityTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffectHothead
    extends EffectBase {

  private static MethodHandle renderPlayer$smallArmsGetter;

  public EffectHothead(UUID uuid) {

    super(uuid);
  }

  @Override
  public boolean subscribeEvents() {

    return true;
  }

  private static final Map<UUID, RenderPlayer> RENDERERS = new HashMap<>();

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void on(RenderPlayerEvent.Pre event) {

    if (renderPlayer$smallArmsGetter == null) {

      try {
        renderPlayer$smallArmsGetter = MethodHandles.lookup().unreflectGetter(
          /*
          MC 1.12: net/minecraft/client/renderer/entity/RenderPlayer.smallArms
          Name: a => field_177140_a => smallArms
          Comment: this field is used to indicate the 3-pixel wide arms
          Side: CLIENT
          AT: public net.minecraft.client.renderer.entity.RenderPlayer field_177140_a # smallArms
           */
            ObfuscationReflectionHelper.findField(RenderPlayer.class, "field_177140_a")
        );

      } catch (Exception e) {
        ModuleCore.LOGGER.error("", e);
      }
    }

    if (event.getRenderer() instanceof RenderPlayerHothead) {
      return;
    }

    if (renderPlayer$smallArmsGetter == null) {
      return;
    }

    UUID uuid = event.getEntityPlayer().getUniqueID();

    if (uuid.equals(this.getUuid())) {
      EntityPlayer entityForPlayer = PlayerEntityTracker.getEntityForPlayer(uuid);

      if (entityForPlayer != null) {

        try {
          RenderPlayer renderPlayer = RENDERERS.get(uuid);

          if (renderPlayer == null) {
            boolean smallArms = (boolean) renderPlayer$smallArmsGetter.invokeExact(event.getRenderer());
            renderPlayer = new RenderPlayerHothead(Minecraft.getMinecraft().getRenderManager(), smallArms);
            RENDERERS.put(uuid, renderPlayer);
          }

          renderPlayer.doRender((AbstractClientPlayer) event.getEntityPlayer(), event.getX(), event.getY(), event.getZ(), event.getEntity().rotationYaw, event.getPartialRenderTick());
          event.setCanceled(true);

        } catch (Throwable throwable) {
          ModuleCore.LOGGER.error("", throwable);
        }
      }
    }
  }

  private static class RenderPlayerHothead
      extends RenderPlayer {

    public RenderPlayerHothead(RenderManager renderManager, boolean smallArms) {

      super(renderManager, smallArms);
      this.addLayer(new HotheadLayer(this.getMainModel().bipedHead));
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void on(TickEvent.ClientTickEvent event) {

    if (renderPlayer$smallArmsGetter == null) {
      return;
    }

    if (ModulePatreonConfig.CLIENT.DISABLE_ALL_PATREON_EFFECTS) {
      return;
    }

    if (event.phase == TickEvent.Phase.START) {

      Minecraft minecraft = Minecraft.getMinecraft();
      WorldClient world = minecraft.world;

      if (minecraft.isGamePaused()) {
        return;
      }

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

        double offsetY = 2.0;

        if (entity.isPlayerSleeping()) {
          offsetY = 0;//-0.0625;

        } else if (entity.isSneaking()) {
          offsetY = 1.625;
        }

        if (entity.isInWater()) {
          minecraft.effectRenderer.addEffect(new ParticleEmitter(
              world,
              new Vec3d(entity.posX, entity.posY + offsetY, entity.posZ),
              new Vec3d(0.25, 0.0125, 0.25),
              15,
              20 * 2,
              new ParticleFactoryAdapter(new ParticleSmokeNormal.Factory())
          ));

        } else {
          minecraft.effectRenderer.addEffect(new ParticleEmitter(
              world,
              new Vec3d(entity.posX, entity.posY + offsetY, entity.posZ),
              new Vec3d(0.25, 0.0125, 0.25),
              entity.isSprinting() ? 2 : 15,
              entity.isSprinting() ? 20 * 4 : 20 * 2,
              new ParticleFactoryAdapter(new ParticleFlame.Factory())
          ));
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  public static class HotheadLayer
      implements LayerRenderer<EntityPlayer> {

    private final ModelRenderer modelRenderer;
    private World world;
    private EntityMagmaCube entityMagmaCube;

    public HotheadLayer(ModelRenderer modelRenderer) {

      this.modelRenderer = modelRenderer;
    }

    public void doRenderLayer(@Nonnull EntityPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

      if (ModulePatreonConfig.CLIENT.DISABLE_ALL_PATREON_EFFECTS) {
        return;
      }

      UUID uuid = entity.getUniqueID();
      Minecraft minecraft = Minecraft.getMinecraft();

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

      if (this.world == null) {
        this.world = entity.world;
      }

      if (this.entityMagmaCube == null || this.world != entity.world) {
        this.world = entity.world;
        this.entityMagmaCube = new EntityMagmaCube(this.world);
      }

      GlStateManager.pushMatrix();

      if (entity.isSneaking()) {
        GlStateManager.translate(0.0F, 0.2F, 0.0F);
      }

      this.modelRenderer.postRender(0.0625F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

      float scalar = 0.95f;
      GlStateManager.scale(1.1875F * scalar, -1.1875F * scalar, -1.1875F * scalar);

      RenderLivingBase<EntityMagmaCube> render = (RenderLivingBase<EntityMagmaCube>) minecraft.getRenderManager().entityRenderMap.get(EntityMagmaCube.class);
      render.doRender(this.entityMagmaCube, 0.0, -0.025, 0.0, netHeadYaw, partialTicks);

      GlStateManager.popMatrix();
    }

    public boolean shouldCombineTextures() {

      return false;
    }
  }
}
