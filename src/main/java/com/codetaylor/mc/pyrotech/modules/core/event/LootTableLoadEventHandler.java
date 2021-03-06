package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ModPyrotech.MOD_ID)
public class LootTableLoadEventHandler {

  private static MethodHandle lootTable$poolsGetter;
  private static MethodHandle lootPool$entriesGetter;
  private static MethodHandle lootEntryItem$itemGetter;
  private static MethodHandle lootEntryItem$itemSetter;

  static {

    try {
      lootTable$poolsGetter = MethodHandles.lookup().unreflectGetter(
          /*
          MC 1.12: net/minecraft/world/storage/loot/LootTable.pools
          Name: c => field_186466_c => pools
          Comment: None
          Side: BOTH
          AT: public net.minecraft.world.storage.loot.LootTable field_186466_c # pools
           */
          ObfuscationReflectionHelper.findField(LootTable.class, "field_186466_c")
      );

      lootPool$entriesGetter = MethodHandles.lookup().unreflectGetter(
          /*
          MC 1.12: net/minecraft/world/storage/loot/LootPool.lootEntries
          Name: a => field_186453_a => lootEntries
          Comment: None
          Side: BOTH
          AT: public net.minecraft.world.storage.loot.LootPool field_186453_a # lootEntries
           */
          ObfuscationReflectionHelper.findField(LootPool.class, "field_186453_a")
      );

      lootEntryItem$itemGetter = MethodHandles.lookup().unreflectGetter(
          /*
          MC 1.12: net/minecraft/world/storage/loot/LootEntryItem.item
          Name: a => field_186368_a => item
          Comment: None
          Side: BOTH
          AT: public net.minecraft.world.storage.loot.LootEntryItem field_186368_a # item
           */
          ObfuscationReflectionHelper.findField(LootEntryItem.class, "field_186368_a")
      );

      lootEntryItem$itemSetter = MethodHandles.lookup().unreflectSetter(
          /*
          MC 1.12: net/minecraft/world/storage/loot/LootEntryItem.item
          Name: a => field_186368_a => item
          Comment: None
          Side: BOTH
          AT: public net.minecraft.world.storage.loot.LootEntryItem field_186368_a # item
           */
          ObfuscationReflectionHelper.findField(LootEntryItem.class, "field_186368_a")
      );

    } catch (IllegalAccessException e) {
      ModuleCore.LOGGER.error("", e);
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void on(LootTableLoadEvent event) {

    if (!ModuleCoreConfig.TWEAKS.REPLACE_IRON_INGOTS) {
      return;
    }

    ResourceLocation lootTableName = event.getName();

    if (lootTableName == null) {
      return;
    }

    if (lootTable$poolsGetter == null) {
      return;
    }

    LootTable table = event.getTable();

    if (table == null) {
      return;
    }

    Item replacementItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ModuleCoreConfig.TWEAKS.REPLACE_IRON_INGOTS_WITH));

    if (replacementItem == null) {
      ModuleCore.LOGGER.error("Unable to locate item for Iron Ingot replacement: " + ModuleCoreConfig.TWEAKS.REPLACE_IRON_INGOTS_WITH);
      return;
    }

    Map<Item, Item> replacementMap = new HashMap<Item, Item>() {{
      this.put(Items.IRON_INGOT, replacementItem);
    }};

    try {
      //noinspection unchecked
      List<LootPool> lootPoolList = (List<LootPool>) lootTable$poolsGetter.invokeExact(table);

      for (LootPool lootPool : lootPoolList) {
        //noinspection unchecked
        List<LootEntry> entryList = (List<LootEntry>) lootPool$entriesGetter.invokeExact(lootPool);

        for (LootEntry lootEntry : entryList) {

          if (lootEntry instanceof LootEntryItem) {
            Item item = (Item) lootEntryItem$itemGetter.invokeExact((LootEntryItem) lootEntry);

            for (Map.Entry<Item, Item> entry : replacementMap.entrySet()) {
              Item toReplace = entry.getKey();
              Item replaceWith = entry.getValue();

              if (item == toReplace) {
                lootEntryItem$itemSetter.invokeExact((LootEntryItem) lootEntry, replaceWith);
                ModuleCore.LOGGER.info("Replaced " + toReplace.getRegistryName() + " with " + replaceWith.getRegistryName() + " in " + lootTableName);
              }
            }
          }
        }
      }

    } catch (Throwable throwable) {
      ModuleCore.LOGGER.error("", throwable);
    }
  }
}
