package com.codetaylor.mc.pyrotech.modules.worldgen.world;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGenConfig;
import com.codetaylor.mc.pyrotech.modules.worldgen.world.spi.IWorldGenFeature;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class WorldGenGloamberryBush
    implements IWorldGenFeature {

  private final BlockPos.MutableBlockPos pos;

  private Set<Biome> allowedBiomeSet;

  public WorldGenGloamberryBush() {

    this.pos = new BlockPos.MutableBlockPos();
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    final int blockXPos = chunkX << 4;
    final int blockZPos = chunkZ << 4;

    Biome biome = world.getBiome(this.pos.setPos(blockXPos, 0, blockZPos));

    if (!this.isAllowed(biome)) {
      return;
    }

    if (random.nextFloat() > ModuleWorldGenConfig.GLOAMBERRY_BUSH.CLUSTER_FREQUENCY) {
      return;
    }

    final double density = ModuleWorldGenConfig.GLOAMBERRY_BUSH.DENSITY;

    for (int i = 0; i < ModuleWorldGenConfig.GLOAMBERRY_BUSH.CHANCES_TO_SPAWN; i++) {

      int posX = blockXPos + random.nextInt(16) + 8;
      int posY = world.getHeight(blockXPos, blockZPos);
      int posZ = blockZPos + random.nextInt(16) + 8;

      BlockHelper.forBlocksInCube(world, new BlockPos(posX, posY, posZ), 4, 4, 4, (w, p, bs) -> {

        if (w.isAirBlock(p)
            && random.nextFloat() < density
            && this.canSpawnOnTopOf(w, p.down(), w.getBlockState(p.down()))) {
          world.setBlockState(p, ModuleCore.Blocks.GLOAMBERRY_BUSH.withAge(random.nextInt(3) + 4), 2 | 16);
        }

        return true; // keep processing
      });
    }

  }

  @Override
  public boolean isAllowed(int dimensionId) {

    return ModuleWorldGenConfig.GLOAMBERRY_BUSH.ENABLED
        && ModuleWorldGenConfig.GLOAMBERRY_BUSH.CHANCES_TO_SPAWN > 0
        && ModuleWorldGenConfig.GLOAMBERRY_BUSH.CLUSTER_FREQUENCY > 0
        && this.isAllowedDimension(dimensionId, ModuleWorldGenConfig.GLOAMBERRY_BUSH.DIMENSION_WHITELIST, ModuleWorldGenConfig.GLOAMBERRY_BUSH.DIMENSION_BLACKLIST);
  }

  private boolean isAllowed(Biome biome) {

    if (this.allowedBiomeSet == null) {
      String[] allowedBiomes = ModuleWorldGenConfig.GLOAMBERRY_BUSH.ALLOWED_BIOMES;
      this.allowedBiomeSet = new HashSet<>(allowedBiomes.length);

      for (String allowedBiome : allowedBiomes) {
        Biome biomeLookup = ForgeRegistries.BIOMES.getValue(new ResourceLocation(allowedBiome));

        if (biomeLookup != null) {
          this.allowedBiomeSet.add(biomeLookup);

        } else {
          ModuleCore.LOGGER.error("Missing biome registration for " + allowedBiome + " in Pyroberry Bush config");
        }
      }
    }

    return this.allowedBiomeSet.contains(biome);
  }

  private boolean canSpawnOnTopOf(World world, BlockPos pos, IBlockState blockState) {

    if (!blockState.isSideSolid(world, pos, EnumFacing.UP)) {
      return false;
    }

    return ModuleCore.Blocks.GLOAMBERRY_BUSH.isValidBlock(blockState);
  }
}
