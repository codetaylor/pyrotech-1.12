package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.ModPyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = ModPyrotech.MOD_ID)
public class EntityLivingDropsEventHandler {

  @GameRegistry.ObjectHolder("minecraft:wool")
  private static final Item ITEM_WOOL;

  static {
    ITEM_WOOL = null;
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void on(LivingDropsEvent event) {

    if (event.getEntityLiving() instanceof EntityPlayer) {
      return;
    }

    if (ModuleCoreConfig.TWEAKS.PREVENT_WOOL_ON_SHEEP_DEATH
        && event.getEntity() instanceof EntitySheep) {

      Iterator<EntityItem> iterator = event.getDrops().iterator();

      while (iterator.hasNext()) {
        EntityItem item = iterator.next();
        ItemStack itemStack = item.getItem();

        if (itemStack.getItem() == ITEM_WOOL) {
          iterator.remove();
        }
      }
    }

    // We call this here to ensure that it will always run after the sheep
    // wool directive has been processed.
    if (ModPyrotechConfig.MODULES.get(ModuleHunting.MODULE_ID)) {
      com.codetaylor.mc.pyrotech.modules.hunting.event.EntityLivingDropsEventHandler.on(event);
    }
  }

}
