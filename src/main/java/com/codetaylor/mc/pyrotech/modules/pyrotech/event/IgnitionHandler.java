package com.codetaylor.mc.pyrotech.modules.pyrotech.event;

import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableAdjacentFire;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class IgnitionHandler {

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event) {

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
      BlockPos offset = pos.offset(facing);
      IBlockState blockState = world.getBlockState(offset);
      Block block = blockState.getBlock();

      if (block instanceof IBlockIgnitableAdjacentFire) {
        ((IBlockIgnitableAdjacentFire) block).igniteWithAdjacentFire(world, offset, blockState, facing.getOpposite());
      }
    }
  }

}
