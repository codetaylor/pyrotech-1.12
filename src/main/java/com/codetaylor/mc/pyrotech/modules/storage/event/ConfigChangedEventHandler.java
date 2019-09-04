package com.codetaylor.mc.pyrotech.modules.storage.event;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.client.render.TESRFaucet;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModuleStorage.MOD_ID)
public class ConfigChangedEventHandler {

  @SubscribeEvent
  public void on(ConfigChangedEvent.OnConfigChangedEvent event) {

    if (event.getModID().equals(ModuleStorage.MOD_ID)) {
      TESRFaucet.updateBlockMatchersFromConfig();
    }
  }
}