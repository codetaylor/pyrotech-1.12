package com.codetaylor.mc.pyrotech.modules.tech.refractory.event;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.util.RefractoryIgnitionHelper;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RightClickBlockEventHandler {

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {

    ItemStack itemStack = event.getItemStack();
    BlockPos pos = event.getPos();
    World world = event.getWorld();

    Item item = itemStack.getItem();

    if (item == Items.FLINT_AND_STEEL) {

      if (RefractoryIgnitionHelper.igniteBlocks(world, pos)) {
        world.playSound(
            null,
            pos,
            SoundEvents.ITEM_FLINTANDSTEEL_USE,
            SoundCategory.BLOCKS,
            1.0F,
            Util.RANDOM.nextFloat() * 0.4F + 0.8F
        );

        event.setUseItem(Event.Result.ALLOW);
      }
    }
  }

}
