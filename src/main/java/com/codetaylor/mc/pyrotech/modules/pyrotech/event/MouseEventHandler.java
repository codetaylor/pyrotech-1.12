package com.codetaylor.mc.pyrotech.modules.pyrotech.event;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.util.InteractionRayTraceData;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.CSPacketInteractionMouseWheel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class MouseEventHandler {

  @SubscribeEvent
  public static void on(MouseEvent event) {

    int wheelDelta = event.getDwheel();

    if (wheelDelta == 0) {
      return;
    }

    Minecraft minecraft = Minecraft.getMinecraft();
    EntityPlayerSP player = minecraft.player;

    if (!player.isSneaking()) {
      return;
    }

    RayTraceResult rayTraceResult = minecraft.objectMouseOver;

    if (rayTraceResult == null) {
      return;
    }

    if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
      return;
    }

    if (rayTraceResult.hitInfo instanceof InteractionRayTraceData.List) {

      CSPacketInteractionMouseWheel packet = new CSPacketInteractionMouseWheel(rayTraceResult.getBlockPos(), wheelDelta, rayTraceResult.sideHit, rayTraceResult.hitVec);
      ModulePyrotech.PACKET_SERVICE.sendToServer(packet);
      event.setCanceled(true);
    }
  }
}
