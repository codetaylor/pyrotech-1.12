package com.codetaylor.mc.pyrotech.modules.hunting.event;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.CapabilitySpearProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityAttachCapabilitiesEventHandler {

  @SubscribeEvent
  public void on(AttachCapabilitiesEvent<Entity> event) {

    Entity entity = event.getObject();

    if (!(entity instanceof FakePlayer)) {
      event.addCapability(new ResourceLocation(ModuleHunting.MOD_ID, "spear"), new CapabilitySpearProvider());
    }
  }

}
