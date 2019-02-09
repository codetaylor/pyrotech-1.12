package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PopulateChunkEventHandler {

  @SubscribeEvent
  public static void on(PopulateChunkEvent.Post event) {

    if (!ModuleCoreConfig.TWEAKS.REMOVE_VANILLA_CRAFTING_TABLE
        && !ModuleCoreConfig.TWEAKS.REPLACE_VANILLA_FURNACE) {
      return;
    }

    World world = event.getWorld();
    int chunkX = event.getChunkX();
    int chunkZ = event.getChunkZ();
    int worldX = chunkX << 4;
    int worldZ = chunkZ << 4;
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    // long start = System.currentTimeMillis();

    for (int x = worldX; x < worldX + 16; x++) {
      for (int z = worldZ; z < worldZ + 16; z++) {

        for (int y = 0; y < 256; y++) {

          pos.setPos(x, y, z);
          IBlockState blockState = world.getBlockState(pos);
          Block block = blockState.getBlock();

          if (ModuleCoreConfig.TWEAKS.REMOVE_VANILLA_CRAFTING_TABLE) {

            if (block == Blocks.CRAFTING_TABLE) {
              world.setBlockToAir(pos);
            }
          }

          if (ModuleCoreConfig.TWEAKS.REPLACE_VANILLA_FURNACE) {

            if (block == Blocks.FURNACE) {
              world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
            }
          }
        }
      }
    }

    // System.out.println("Scan: " + (System.currentTimeMillis() - start));
  }

}
