package com.codetaylor.mc.pyrotech.modules.worldgen.world;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGenConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.Random;

public class WorldGenFossil
    implements IWorldGenFeature {

  private final WorldGenOre worldGenOre;

  public WorldGenFossil() {

    this.worldGenOre = new WorldGenOre(
        ModuleCore.Blocks.ORE_FOSSIL.getDefaultState(),
        random -> {
          int minVeinSize = ModuleWorldGenConfig.FOSSIL.MIN_VEIN_SIZE;
          int maxVeinSize = ModuleWorldGenConfig.FOSSIL.MAX_VEIN_SIZE;
          return (minVeinSize + random.nextInt(Math.max(1, maxVeinSize - minVeinSize + 1)));
        }
    );
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    final int chancesToSpawn = ModuleWorldGenConfig.FOSSIL.CHANCES_TO_SPAWN;
    final int minY = ModuleWorldGenConfig.FOSSIL.MIN_HEIGHT;
    final int maxY = ModuleWorldGenConfig.FOSSIL.MAX_HEIGHT;
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

    return ModuleWorldGenConfig.FOSSIL.ENABLED
        && ModuleWorldGenConfig.FOSSIL.CHANCES_TO_SPAWN > 0
        && this.isAllowedDimension(dimensionId, ModuleWorldGenConfig.FOSSIL.DIMENSION_WHITELIST, ModuleWorldGenConfig.FOSSIL.DIMENSION_BLACKLIST);
  }
}
