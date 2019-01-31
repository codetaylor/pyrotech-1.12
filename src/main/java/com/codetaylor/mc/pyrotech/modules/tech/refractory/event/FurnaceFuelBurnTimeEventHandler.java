package com.codetaylor.mc.pyrotech.modules.tech.refractory.event;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FurnaceFuelBurnTimeEventHandler {

  @SubscribeEvent
  public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {

    ItemStack itemStack = event.getItemStack();

    if (Util.isFluidBucket(itemStack, ModuleTechRefractory.Fluids.WOOD_TAR.getName())) {
      event.setBurnTime(ModuleTechRefractoryConfig.FUEL.WOOD_TAR_BURN_TIME_TICKS);

    } else if (Util.isFluidBucket(itemStack, ModuleTechRefractory.Fluids.COAL_TAR.getName())) {
      event.setBurnTime(ModuleTechRefractoryConfig.FUEL.COAL_TAR_BURN_TIME_TICKS);
    }
  }

}
