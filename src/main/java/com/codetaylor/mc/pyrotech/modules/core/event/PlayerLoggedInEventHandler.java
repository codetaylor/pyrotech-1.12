package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketModuleListRequest;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketRestartRequired;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = ModPyrotech.MOD_ID)
public class PlayerLoggedInEventHandler {

  @SubscribeEvent
  public static void on(PlayerEvent.PlayerLoggedInEvent event) {

    EntityPlayerMP player = (EntityPlayerMP) event.player;

    // Request enabled module list
    ModuleCore.PACKET_SERVICE.sendTo(new SCPacketModuleListRequest(), player);

    // The client will respond to this packet with its module config and
    // if the response doesn't match the server, the player will be kicked.

    if (ModuleCore.RESTART_REQUIRED) {
      ModuleCore.PACKET_SERVICE.sendTo(new SCPacketRestartRequired(), player);
    }
  }
}