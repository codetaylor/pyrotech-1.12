package com.codetaylor.mc.pyrotech.modules.pyrotech.event;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleFluids;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class FuelHandler {

  @SubscribeEvent
  public static void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {

    ItemStack fuel = event.getItemStack();

    if (fuel.getItem() == ModuleItems.MATERIAL
        && fuel.getMetadata() == ItemMaterial.EnumType.COAL_COKE.getMeta()) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.COAL_COKE_BURN_TIME_TICKS);

    } else if (fuel.getItem() == Item.getItemFromBlock(ModuleBlocks.COAL_COKE_BLOCK)) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.COAL_COKE_BLOCK_BURN_TIME_TICKS);

    } else if (Util.isFluidBucket(fuel, ModuleFluids.WOOD_TAR.getName())) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.WOOD_TAR_BURN_TIME_TICKS);

    } else if (Util.isFluidBucket(fuel, ModuleFluids.COAL_TAR.getName())) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.COAL_TAR_BURN_TIME_TICKS);

    } else if (fuel.getItem() == Item.getItemFromBlock(ModuleBlocks.THATCH)) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.THATCH_BURN_TIME_TICKS);

    } else if (fuel.getItem() == ModuleItems.MATERIAL) {

      if (fuel.getMetadata() == ItemMaterial.EnumType.STRAW.getMeta()) {
        event.setBurnTime(ModulePyrotechConfig.FUEL.STRAW_BURN_TIME_TICKS);

      } else if (fuel.getMetadata() == ItemMaterial.EnumType.CHARCOAL_FLAKES.getMeta()) {
        event.setBurnTime(ModulePyrotechConfig.FUEL.CHARCOAL_FLAKES_BURN_TIME_TICKS);
      }

    } else if (fuel.getItem() == ModuleItems.TINDER) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.TINDER_BURN_TIME_TICKS);

    } else if (fuel.getItem() == Item.getItemFromBlock(ModuleBlocks.LOG_PILE)) {
      event.setBurnTime(ModulePyrotechConfig.FUEL.LOG_PILE_BURN_TIME_TICKS);
    }
  }

}
