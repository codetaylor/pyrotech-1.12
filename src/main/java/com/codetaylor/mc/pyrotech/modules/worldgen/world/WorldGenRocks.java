package com.codetaylor.mc.pyrotech.modules.worldgen.world;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGenConfig;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.Random;

public class WorldGenRocks
    implements IWorldGenFeature {

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    final int blockXPos = chunkX << 4;
    final int blockZPos = chunkZ << 4;
    final double density = ModuleWorldGenConfig.ROCKS.DENSITY;

    for (int i = 0; i < ModuleWorldGenConfig.ROCKS.CHANCES_TO_SPAWN; i++) {

      int posX = blockXPos + random.nextInt(16) + 8;
      int posY = world.getHeight(blockXPos, blockZPos);
      int posZ = blockZPos + random.nextInt(16) + 8;

      BlockHelper.forBlocksInCube(world, new BlockPos(posX, posY, posZ), 4, 4, 4, (w, p, bs) -> {

        if (w.isAirBlock(p)
            && this.canSpawnOnTopOf(w, p.down(), w.getBlockState(p.down()))
            && random.nextFloat() < density) {
          world.setBlockState(p, ModuleCore.Blocks.ROCK.getDefaultState().withProperty(BlockRock.VARIANT, BlockRock.EnumType.STONE), 2 | 16);
        }

        return true; // keep processing
      });
    }

  }

  @Override
  public boolean isAllowed(int dimensionId) {

    return ModuleWorldGenConfig.ROCKS.ENABLED
        && ModuleWorldGenConfig.ROCKS.CHANCES_TO_SPAWN > 0
        && this.isAllowedDimension(dimensionId, ModuleWorldGenConfig.ROCKS.DIMENSION_WHITELIST, ModuleWorldGenConfig.ROCKS.DIMENSION_BLACKLIST);
  }

  private boolean canSpawnOnTopOf(World world, BlockPos pos, IBlockState blockState) {

    if (!blockState.isSideSolid(world, pos, EnumFacing.UP)) {
      return false;
    }

    Material material = blockState.getMaterial();

    return material == Material.GROUND
        || material == Material.GRASS
        || material == Material.ROCK;
  }
}
