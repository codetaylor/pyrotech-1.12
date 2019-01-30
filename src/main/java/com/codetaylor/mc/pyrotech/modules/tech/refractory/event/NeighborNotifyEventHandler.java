package com.codetaylor.mc.pyrotech.modules.tech.refractory.event;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.util.RefractoryIgnitionHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NeighborNotifyEventHandler {

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event) {

    World world = event.getWorld();
    BlockPos pos = event.getPos();

    if (world.isRemote) {
      return;
    }

    Block fireBlock = world.getBlockState(pos).getBlock();

    if (event.isCanceled()
        || fireBlock != Blocks.FIRE) {
      return;
    }

    for (EnumFacing facing : event.getNotifiedSides()) {
      RefractoryIgnitionHelper.igniteBlocks(world, pos.offset(facing));
    }
  }
}
