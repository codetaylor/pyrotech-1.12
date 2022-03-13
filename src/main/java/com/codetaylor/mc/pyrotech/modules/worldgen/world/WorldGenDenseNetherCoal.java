package com.codetaylor.mc.pyrotech.modules.worldgen.world;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGenConfig;
import com.codetaylor.mc.pyrotech.modules.worldgen.world.spi.IWorldGenFeature;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.Random;

public class WorldGenDenseNetherCoal
    implements IWorldGenFeature {

  private final WorldGenOre worldGenOre;

  public WorldGenDenseNetherCoal() {

    this.worldGenOre = new WorldGenOre(
        ModuleCore.Blocks.ORE_DENSE_NETHER_COAL.getDefaultState(),
        random -> {
          int minVeinSize = ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.MIN_VEIN_SIZE;
          int maxVeinSize = ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.MAX_VEIN_SIZE;
          return (minVeinSize + random.nextInt(Math.max(1, maxVeinSize - minVeinSize + 1)));
        },
        new WorldGenOre.BlockPredicate(Blocks.NETHERRACK)
    );
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    final int chancesToSpawn = ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.CHANCES_TO_SPAWN;
    final int minY = ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.MIN_HEIGHT;
    final int maxY = ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.MAX_HEIGHT;
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

    return ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.ENABLED
        && ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.CHANCES_TO_SPAWN > 0
        && this.isAllowedDimension(dimensionId, ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.DIMENSION_WHITELIST, ModuleWorldGenConfig.DENSE_NETHER_COAL_ORE.DIMENSION_BLACKLIST);
  }
}
