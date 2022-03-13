package com.codetaylor.mc.pyrotech.modules.worldgen.feature;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGenConfig;
import com.codetaylor.mc.pyrotech.modules.worldgen.feature.spi.IWorldGenFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.Random;

public class WorldGenLimestone
    implements IWorldGenFeature {

  private final WorldGenOre worldGenOre;

  public WorldGenLimestone() {

    this.worldGenOre = new WorldGenOre(
        ModuleCore.Blocks.LIMESTONE.getDefaultState(),
        random -> {
          int minVeinSize = ModuleWorldGenConfig.LIMESTONE.MIN_VEIN_SIZE;
          int maxVeinSize = ModuleWorldGenConfig.LIMESTONE.MAX_VEIN_SIZE;
          return (minVeinSize + random.nextInt(Math.max(1, maxVeinSize - minVeinSize + 1)));
        }
    );
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    final int chancesToSpawn = ModuleWorldGenConfig.LIMESTONE.CHANCES_TO_SPAWN;
    final int minY = ModuleWorldGenConfig.LIMESTONE.MIN_HEIGHT;
    final int maxY = ModuleWorldGenConfig.LIMESTONE.MAX_HEIGHT;
    final int blockXPos = chunkX << 4;
    final int blockZPos = chunkZ << 4;

    for (int i = 0; i < chancesToSpawn; i++) {
      int posX = blockXPos + random.nextInt(16);
      int posY = minY + random.nextInt(Math.max(1, maxY - minY + 1));
      int posZ = blockZPos + random.nextInt(16);
      this.worldGenOre.generate(world, random, new BlockPos(posX, posY, posZ));
    }
  }

  @Override
  public boolean isAllowed(int dimensionId) {

    return ModuleWorldGenConfig.LIMESTONE.ENABLED
        && ModuleWorldGenConfig.LIMESTONE.CHANCES_TO_SPAWN > 0
        && this.isAllowedDimension(dimensionId, ModuleWorldGenConfig.LIMESTONE.DIMENSION_WHITELIST, ModuleWorldGenConfig.LIMESTONE.DIMENSION_BLACKLIST);
  }
}
