package com.codetaylor.mc.pyrotech.modules.worldgen.world;

import com.codetaylor.mc.pyrotech.library.util.FloodFill;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGenConfig;
import com.codetaylor.mc.pyrotech.modules.worldgen.world.spi.IWorldGenFeature;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.Random;

public class WorldGenDenseCoal
    implements IWorldGenFeature {

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    final int minY = ModuleWorldGenConfig.DENSE_COAL_ORE.MIN_HEIGHT;
    final int maxY = ModuleWorldGenConfig.DENSE_COAL_ORE.MAX_HEIGHT;
    final int minVeinSize = MathHelper.clamp(ModuleWorldGenConfig.DENSE_COAL_ORE.MIN_VEIN_SIZE, 0, ModuleWorldGenConfig.DENSE_COAL_ORE.MAX_VEIN_SIZE);
    final int maxVeinSize = Math.max(ModuleWorldGenConfig.DENSE_COAL_ORE.MAX_VEIN_SIZE, minVeinSize);
    final int blockXPos = chunkX << 4;
    final int blockZPos = chunkZ << 4;

    BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

    for (int x = 2; x < 16; x += 4) {
      for (int z = 2; z < 16; z += 4) {
        for (int y = minY; y < maxY; y++) {
          mutableBlockPos.setPos(blockXPos + x, y, blockZPos + z);

          if (world.getBlockState(mutableBlockPos).getBlock() == Blocks.COAL_ORE) {
            FloodFill.apply(
                world,
                mutableBlockPos,
                (w, p) -> (w.getBlockState(p).getBlock() == Blocks.COAL_ORE),
                (w, p) -> {
                  w.setBlockState(p, ModuleCore.Blocks.ORE_DENSE_COAL.getDefaultState());
                  return true;
                },
                random.nextInt(maxVeinSize - minVeinSize + 1) + minVeinSize
            );
            break;
          }
        }
      }
    }
  }

  @Override
  public boolean isAllowed(int dimensionId) {

    return ModuleWorldGenConfig.DENSE_COAL_ORE.ENABLED
        && this.isAllowedDimension(dimensionId, ModuleWorldGenConfig.DENSE_COAL_ORE.DIMENSION_WHITELIST, ModuleWorldGenConfig.DENSE_COAL_ORE.DIMENSION_BLACKLIST);
  }
}
