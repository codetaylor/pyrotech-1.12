package com.codetaylor.mc.pyrotech.modules.patreon.effect;

import com.codetaylor.mc.pyrotech.library.particle.ParticleEmitter;
import com.codetaylor.mc.pyrotech.library.particle.ParticleFactoryAdapter;
import com.codetaylor.mc.pyrotech.library.patreon.effect.EffectBase;
import com.codetaylor.mc.pyrotech.modules.patreon.ModulePatreonConfig;
import com.codetaylor.mc.pyrotech.modules.patreon.PlayerEntityTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EffectHothead
    extends EffectBase {

  public EffectHothead(UUID uuid) {

    super(uuid);
  }

  @Override
  public boolean subscribeEvents() {

    return true;
  }

  private static final Set<RenderPlayer> ATTACHED_RENDERERS = new HashSet<>();

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void on(RenderPlayerEvent.Pre event) {

    RenderPlayer renderer = event.getRenderer();

    if (!ATTACHED_RENDERERS.contains(renderer)) {
      renderer.addLayer(new HotheadLayer(renderer.getMainModel().bipedHead));
      ATTACHED_RENDERERS.add(renderer);
    }
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

        double offsetY = entity.isSneaking() ? 1.625 : 2.0;

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

    public HotheadLayer(ModelRenderer modelRenderer) {

      this.modelRenderer = modelRenderer;
    }

    public void doRenderLayer(@Nonnull EntityPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

      if (ModulePatreonConfig.CLIENT.DISABLE_ALL_PATREON_EFFECTS) {
        return;
      }

      UUID uuid = entity.getUniqueID();

      if (ModulePatreonConfig.CLIENT.DISABLE_YOUR_PATREON_EFFECTS) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (player == null) {
          return;
        }

        UUID uniqueID = player.getUniqueID();

        if (uuid.equals(uniqueID)) {
          return;
        }
      }

      GlStateManager.pushMatrix();

      if (entity.isSneaking()) {
        GlStateManager.translate(0.0F, 0.2F, 0.0F);
      }

      this.modelRenderer.postRender(0.0625F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

      float scalar = 0.95f;
      GlStateManager.scale(1.1875F * scalar, -1.1875F * scalar, -1.1875F * scalar);

      RenderLivingBase<EntityMagmaCube> render = (RenderLivingBase<EntityMagmaCube>) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntityMagmaCube.class);
      EntityMagmaCube magmaCube = new EntityMagmaCube(Minecraft.getMinecraft().world);
      render.doRender(magmaCube, 0.0, -0.025, 0.0, netHeadYaw, partialTicks);

      GlStateManager.popMatrix();
    }

    public boolean shouldCombineTextures() {

      return false;
    }
  }
}
