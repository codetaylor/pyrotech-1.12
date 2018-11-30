package com.codetaylor.mc.pyrotech.modules.pyrotech.event;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModulePyrotech.MOD_ID)
public class EventHandler {

  @SubscribeEvent
  public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {

    if (event.getModID().equals(ModulePyrotech.MOD_ID)) {
      ConfigManager.sync(ModulePyrotech.MOD_ID, Config.Type.INSTANCE);
    }
  }
}
