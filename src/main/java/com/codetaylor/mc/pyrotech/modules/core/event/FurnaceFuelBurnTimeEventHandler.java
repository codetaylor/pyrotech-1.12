package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.block.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockCampfire;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockWorktableStone;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class FurnaceFuelBurnTimeEventHandler {

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockCoalCokeBlock.NAME)
  private static Item ITEM_COAL_COKE_BLOCK;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockCharcoalBlock.NAME)
  private static Item ITEM_CHARCOAL_BLOCK;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockThatch.NAME)
  private static Item ITEM_THATCH_BLOCK;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockLogPile.NAME)
  private static Item ITEM_LOG_PILE;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockPileWoodChips.NAME)
  private static Item ITEM_PILE_WOOD_CHIPS;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockWorktableStone.NAME)
  private static Item ITEM_STONE_WORKTABLE;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockRefractoryDoor.NAME)
  private static Item ITEM_REFRACTORY_DOOR;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockCampfire.NAME)
  private static Item ITEM_CAMPFIRE;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockPlanksTarred.NAME)
  private static Item ITEM_TARRED_PLANKS;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockWoolTarred.NAME)
  private static Item ITEM_TARRED_WOOL;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockRock.NAME)
  private static Item ITEM_ROCK;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockWoodTarBlock.NAME)
  private static Item ITEM_WOOD_TAR_BLOCK;

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID + ":" + BlockLivingTar.NAME)
  private static Item ITEM_LIVING_TAR;

  @SubscribeEvent
  public static void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {

    ItemStack itemStack = event.getItemStack();
    Item item = itemStack.getItem();

    if (item == ITEM_COAL_COKE_BLOCK) {
      event.setBurnTime(ModuleCoreConfig.FUEL.COAL_COKE_BLOCK_BURN_TIME_TICKS);

    } else if (item == ITEM_CHARCOAL_BLOCK) {
      event.setBurnTime(ModuleCoreConfig.FUEL.CHARCOAL_BLOCK_BURN_TIME_TICKS);

    } else if (item == ITEM_THATCH_BLOCK) {
      event.setBurnTime(ModuleCoreConfig.FUEL.STRAW_BALE_BURN_TIME_TICKS);

    } else if (item == ITEM_LOG_PILE) {
      event.setBurnTime(ModuleCoreConfig.FUEL.LOG_PILE_BURN_TIME_TICKS);

    } else if (item == ITEM_ROCK) {

      if (itemStack.getMetadata() == BlockRock.EnumType.WOOD_CHIPS.getMeta()) {
        event.setBurnTime(ModuleCoreConfig.FUEL.WOOD_CHIPS_BURN_TIME_TICKS);
      }

    } else if (item == ITEM_PILE_WOOD_CHIPS) {
      event.setBurnTime(ModuleCoreConfig.FUEL.PILE_WOOD_CHIPS_BURN_TIME_TICKS);

    } else if (item == ITEM_STONE_WORKTABLE
        || item == ITEM_REFRACTORY_DOOR
        || item == ITEM_CAMPFIRE) {
      event.setBurnTime(0);

    } else if (item == ITEM_TARRED_PLANKS) {
      event.setBurnTime(ModuleCoreConfig.FUEL.TARRED_PLANKS_BURN_TIME_TICKS);

    } else if (item == ITEM_TARRED_WOOL) {
      event.setBurnTime(ModuleCoreConfig.FUEL.TARRED_WOOL_BURN_TIME_TICKS);

    } else if (item == ITEM_WOOD_TAR_BLOCK) {
      event.setBurnTime(ModuleCoreConfig.FUEL.WOOD_TAR_BLOCK_BURN_TIME_TICKS);

    } else if (item == ITEM_LIVING_TAR) {
      event.setBurnTime(ModuleCoreConfig.FUEL.LIVING_TAR_BURN_TIME_TICKS);
    }
  }

}
