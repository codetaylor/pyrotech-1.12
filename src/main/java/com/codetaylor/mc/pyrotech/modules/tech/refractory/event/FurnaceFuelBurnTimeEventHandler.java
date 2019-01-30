package com.codetaylor.mc.pyrotech.modules.pyrotech.event;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockCoalCokeBlock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockLogPile;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.init.FluidInitializer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockCampfire;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockWorktableStone;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class FuelHandler {

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockCoalCokeBlock.NAME)
  private static Item ITEM_COAL_COKE_BLOCK;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockCharcoalBlock.NAME)
  private static Item ITEM_CHARCOAL_BLOCK;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockThatch.NAME)
  private static Item ITEM_THATCH_BLOCK;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockLogPile.NAME)
  private static Item ITEM_LOG_PILE;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockPileWoodChips.NAME)
  private static Item ITEM_PILE_WOOD_CHIPS;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockWorktableStone.NAME)
  private static Item ITEM_STONE_WORKTABLE;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockRefractoryDoor.NAME)
  private static Item ITEM_REFRACTORY_DOOR;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockCampfire.NAME)
  private static Item ITEM_CAMPFIRE;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockPlanksTarred.NAME)
  private static Item ITEM_TARRED_PLANKS;

  @GameRegistry.ObjectHolder(ModulePyrotech.MOD_ID + ":" + BlockWoolTarred.NAME)
  private static Item ITEM_TARRED_WOOL;

  @SubscribeEvent
  public static void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {

    ItemStack itemStack = event.getItemStack();
    Item item = itemStack.getItem();

    if (item == ITEM_COAL_COKE_BLOCK) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.COAL_COKE_BLOCK_BURN_TIME_TICKS);

    } else if (item == ITEM_CHARCOAL_BLOCK) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.CHARCOAL_BLOCK_BURN_TIME_TICKS);

    } else if (Util.isFluidBucket(itemStack, FluidInitializer.WOOD_TAR.getName())) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.WOOD_TAR_BURN_TIME_TICKS);

    } else if (Util.isFluidBucket(itemStack, FluidInitializer.COAL_TAR.getName())) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.COAL_TAR_BURN_TIME_TICKS);

    } else if (item == ITEM_THATCH_BLOCK) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.STRAW_BALE_BURN_TIME_TICKS);

    } else if (item == ITEM_LOG_PILE) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.LOG_PILE_BURN_TIME_TICKS);

    } else if (item == ModuleItems.ROCK) {

      if (itemStack.getMetadata() == BlockRock.EnumType.WOOD_CHIPS.getMeta()) {
        event.setBurnTime(ModulePyrotechConfig.FUEL.WOOD_CHIPS_BURN_TIME_TICKS);
      }

    } else if (item == ITEM_PILE_WOOD_CHIPS) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.PILE_WOOD_CHIPS_BURN_TIME_TICKS);

    } else if (item == ITEM_STONE_WORKTABLE
        || item == ITEM_REFRACTORY_DOOR
        || item == ITEM_CAMPFIRE) {
      event.setBurnTime(0);

    } else if (item == ITEM_TARRED_PLANKS) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.TARRED_PLANKS_BURN_TIME_TICKS);

    } else if (item == ITEM_TARRED_WOOL) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.TARRED_WOOL_BURN_TIME_TICKS);
    }
  }

}
