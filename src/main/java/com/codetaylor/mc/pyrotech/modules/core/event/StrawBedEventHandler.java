package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class StrawBedEventHandler {

  private final Int2ObjectMap<List<BlockPos>> usedBeds;

  public StrawBedEventHandler() {

    this.usedBeds = new Int2ObjectOpenHashMap<>();
    this.usedBeds.defaultReturnValue(null);
  }

  @SubscribeEvent
  public void on(TickEvent.WorldTickEvent event) {

    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    World world = event.world;

    if (world.isRemote) {
      return;
    }

    int dimensionId = world.provider.getDimension();
    List<BlockPos> blockPosList = this.usedBeds.get(dimensionId);

    if (blockPosList == null || blockPosList.isEmpty()) {
      return;
    }

    for (BlockPos blockPos : blockPosList) {

      IBlockState blockState = world.getBlockState(blockPos);
      Block block = blockState.getBlock();

      if (block == ModuleCore.Blocks.STRAW_BED && world.isDaytime()) {
        world.destroyBlock(blockPos, false);
      }
    }

    blockPosList.clear();
  }

  @SubscribeEvent
  public void on(PlayerSetSpawnEvent event) {

    EntityPlayer player = event.getEntityPlayer();
    World world = player.world;

    if (world.isRemote) {
      return;
    }

    IBlockState blockState = world.getBlockState(player.bedLocation);
    Block block = blockState.getBlock();

    if (block == ModuleCore.Blocks.STRAW_BED) {
      this.addUsedBed(world, player.bedLocation);
      event.setCanceled(true);
    }
  }

  private void addUsedBed(World world, BlockPos pos) {

    int dimensionId = world.provider.getDimension();
    List<BlockPos> blockPosList = this.usedBeds.get(dimensionId);

    if (blockPosList == null) {
      blockPosList = new ArrayList<>();
      this.usedBeds.put(dimensionId, blockPosList);
    }

    blockPosList.add(pos);
  }

}
