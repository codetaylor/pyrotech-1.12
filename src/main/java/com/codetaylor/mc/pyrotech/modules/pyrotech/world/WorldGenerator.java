package com.codetaylor.mc.pyrotech.modules.pyrotech.world;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockOre;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenerator
    implements IWorldGenerator {

  private WorldGenOre worldGenFossil;

  public WorldGenerator() {

    this.worldGenFossil = new WorldGenOre(
        ModuleBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, BlockOre.EnumType.FOSSIL_ORE),
        rand -> {
          int minVeinSize = ModulePyrotechConfig.WORLD_GEN.FOSSIL.MIN_VEIN_SIZE;
          int maxVeinSize = ModulePyrotechConfig.WORLD_GEN.FOSSIL.MAX_VEIN_SIZE;
          return (minVeinSize + rand.nextInt(maxVeinSize - minVeinSize));
        }
    );
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    // Fossil
    if (ModulePyrotechConfig.WORLD_GEN.FOSSIL.ENABLED) {
      final int chancesToSpawn = ModulePyrotechConfig.WORLD_GEN.FOSSIL.CHANCES_TO_SPAWN;
      final int minY = ModulePyrotechConfig.WORLD_GEN.FOSSIL.MIN_HEIGHT;
      final int maxY = ModulePyrotechConfig.WORLD_GEN.FOSSIL.MAX_HEIGHT;
      final int blockXPos = chunkX << 4;
      final int blockZPos = chunkZ << 4;

      for (int i = 0; i < chancesToSpawn; i++) {
        int posX = blockXPos + random.nextInt(16);
        int posY = minY + random.nextInt(maxY - minY);
        int posZ = blockZPos + random.nextInt(16);
        this.worldGenFossil.generate(world, random, new BlockPos(posX, posY, posZ));
      }
    }
  }

}
