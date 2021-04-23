package com.codetaylor.mc.pyrotech.modules.hunting.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.client.LayerSpear;
import com.codetaylor.mc.pyrotech.modules.hunting.network.CSPacketCapabilitySyncSpearRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class ClientEntityJoinWorldEventHandler {

  private static MethodHandle renderLivingBase$layerRenderersGetter;

  static {

    try {
      renderLivingBase$layerRenderersGetter = MethodHandles.lookup().unreflectGetter(
          /*
          MC 1.12: net/minecraft/client/renderer/entity/RenderLivingBase.layerRenderers
          Name: h => field_177097_h => layerRenderers
          Comment: None
          Side: CLIENT
          AT: public net.minecraft.client.renderer.entity.RenderLivingBase field_177097_h # layerRenderers
           */
          ObfuscationReflectionHelper.findField(RenderLivingBase.class, "field_177097_h")
      );

    } catch (IllegalAccessException e) {
      ModuleCore.LOGGER.error("", e);
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void on(EntityJoinWorldEvent event) {

    if (renderLivingBase$layerRenderersGetter == null) {
      return;
    }

    Entity entity = event.getEntity();
    this.requestCapabilitySync(entity);
    this.addRenderLayer(entity);
  }

  private void requestCapabilitySync(Entity entity) {

    ModuleHunting.PACKET_SERVICE.sendToServer(new CSPacketCapabilitySyncSpearRequest(entity.getEntityId()));
  }

  private void addRenderLayer(Entity entity) {

    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
    Render<Entity> render = renderManager.getEntityRenderObject(entity);

    if (!(render instanceof RenderLivingBase)) {
      return;
    }

    RenderLivingBase<?> entityRender = (RenderLivingBase<?>) render;

    try {
      //noinspection unchecked
      List<LayerRenderer<?>> layerRenderers = (List<LayerRenderer<?>>) renderLivingBase$layerRenderersGetter.invokeExact(entityRender);

      for (LayerRenderer<?> layerRenderer : layerRenderers) {

        if (layerRenderer instanceof LayerSpear) {
          return;
        }
      }

    } catch (Throwable throwable) {
      ModuleCore.LOGGER.error("", throwable);
    }

    entityRender.addLayer(new LayerSpear(entityRender));
  }
}
