package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.advancement.AdvancementTriggers;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModPyrotech.MOD_ID)
public class EntityItemPickupEventHandler {

  @SubscribeEvent
  public static void on(EntityItemPickupEvent event) {

    EntityPlayer entityPlayer = event.getEntityPlayer();

    if (!(entityPlayer instanceof EntityPlayerMP)) {
      return;
    }

    EntityItem entityItem = event.getItem();

    if (entityItem == null) {
      return;
    }

    ItemStack itemStack = entityItem.getItem();

    if (itemStack.isEmpty()) {
      return;
    }

    Item item = itemStack.getItem();
    ResourceLocation registryName = item.getRegistryName();

    if (registryName == null) {
      return;
    }

    String resourceDomain = registryName.getResourceDomain();

    if (ModuleCore.MOD_ID.equals(resourceDomain)) {
      AdvancementTriggers.MOD_ITEM_TRIGGER.trigger((EntityPlayerMP) entityPlayer);
    }
  }
}
