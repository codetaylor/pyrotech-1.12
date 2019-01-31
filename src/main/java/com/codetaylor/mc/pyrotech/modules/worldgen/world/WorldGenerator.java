package com.codetaylor.mc.pyrotech.modules.worldgen.world;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockOre;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGenConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenerator
    implements IWorldGenerator {

  private WorldGenOre worldGenFossil;
  private WorldGenOre worldGenLimestone;

  public WorldGenerator() {

    this.worldGenFossil = new WorldGenOre(
        ModuleCore.Blocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, BlockOre.EnumType.FOSSIL_ORE),
        random -> {
          int minVeinSize = ModuleWorldGenConfig.FOSSIL.MIN_VEIN_SIZE;
          int maxVeinSize = ModuleWorldGenConfig.FOSSIL.MAX_VEIN_SIZE;
          return (minVeinSize + random.nextInt(maxVeinSize - minVeinSize));
        }
    );

    this.worldGenLimestone = new WorldGenOre(
        ModuleCore.Blocks.LIMESTONE.getDefaultState(),
        random -> {
          int minVeinSize = ModuleWorldGenConfig.LIMESTONE.MIN_VEIN_SIZE;
          int maxVeinSize = ModuleWorldGenConfig.LIMESTONE.MAX_VEIN_SIZE;
          return (minVeinSize + random.nextInt(maxVeinSize - minVeinSize));
        }
    );
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    // Fossil
    if (ModuleWorldGenConfig.FOSSIL.ENABLED
        && ModuleWorldGenConfig.FOSSIL.CHANCES_TO_SPAWN > 0) {
      final int chancesToSpawn = ModuleWorldGenConfig.FOSSIL.CHANCES_TO_SPAWN;
      final int minY = ModuleWorldGenConfig.FOSSIL.MIN_HEIGHT;
      final int maxY = ModuleWorldGenConfig.FOSSIL.MAX_HEIGHT;
      final int blockXPos = chunkX << 4;
      final int blockZPos = chunkZ << 4;

      for (int i = 0; i < chancesToSpawn; i++) {
        int posX = blockXPos + random.nextInt(16);
        int posY = minY + random.nextInt(maxY - minY);
        int posZ = blockZPos + random.nextInt(16);
        this.worldGenFossil.generate(world, random, new BlockPos(posX, posY, posZ));
      }
    }

    // Limestone
    if (ModuleWorldGenConfig.LIMESTONE.ENABLED
        && ModuleWorldGenConfig.LIMESTONE.CHANCES_TO_SPAWN > 0) {
      final int chancesToSpawn = ModuleWorldGenConfig.LIMESTONE.CHANCES_TO_SPAWN;
      final int minY = ModuleWorldGenConfig.LIMESTONE.MIN_HEIGHT;
      final int maxY = ModuleWorldGenConfig.LIMESTONE.MAX_HEIGHT;
      final int blockXPos = chunkX << 4;
      final int blockZPos = chunkZ << 4;

      for (int i = 0; i < chancesToSpawn; i++) {
        int posX = blockXPos + random.nextInt(16);
        int posY = minY + random.nextInt(maxY - minY);
        int posZ = blockZPos + random.nextInt(16);
        this.worldGenLimestone.generate(world, random, new BlockPos(posX, posY, posZ));
      }
    }

    // Stone Rocks
    {
      final int chancesToSpawn = 4;
      final int blockXPos = chunkX << 4;
      final int blockZPos = chunkZ << 4;
      final double density = ModuleWorldGenConfig.ROCKS.DENSITY;

      for (int i = 0; i < chancesToSpawn; i++) {

        int posX = blockXPos + random.nextInt(16) + 8;
        int posY = world.getHeight(blockXPos, blockZPos);
        int posZ = blockZPos + random.nextInt(16) + 8;

        BlockHelper.forBlocksInCube(world, new BlockPos(posX, posY, posZ), 4, 4, 4, (w, p, bs) -> {

          if (w.isAirBlock(p) && this.canSpawnOnTopOf(w, p.down(), w.getBlockState(p.down())) && random.nextFloat() < density) {
            world.setBlockState(p, ModuleCore.Blocks.ROCK.getDefaultState().withProperty(BlockRock.VARIANT, BlockRock.EnumType.STONE), 2 | 16);
          }

          return true; // keep processing
        });
      }
    }

  }

  private boolean canSpawnOnTopOf(World world, BlockPos pos, IBlockState blockState) {

    Block block = blockState.getBlock();
    return block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.STONE;
  }

}
