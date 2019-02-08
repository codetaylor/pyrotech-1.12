package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class EntityLivingDeathEventHandler {

  @GameRegistry.ObjectHolder("minecraft:wool")
  private static final Item ITEM_WOOL;

  static {
    ITEM_WOOL = null;
  }

  @SubscribeEvent
  public static void on(LivingDropsEvent event) {

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
  }

}
