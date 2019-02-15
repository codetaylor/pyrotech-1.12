package com.codetaylor.mc.pyrotech.modules.worldgen.world;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenOre
    extends WorldGenerator {

  private final IBlockState oreBlock;
  private final IRandomIntSupplier countSupplier;
  private final Predicate<IBlockState> predicate;

  public WorldGenOre(IBlockState state, IRandomIntSupplier countSupplier) {

    this(state, countSupplier, new StonePredicate());
  }

  public WorldGenOre(IBlockState state, IRandomIntSupplier countSupplier, Predicate<IBlockState> predicate) {

    this.oreBlock = state;
    this.countSupplier = countSupplier;
    this.predicate = predicate;
  }

  @Override
  public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos position) {

    int numberOfBlocks = this.countSupplier.get(rand);

    float f = rand.nextFloat() * (float) Math.PI;
    double d0 = (double) ((float) (position.getX() + 8) + MathHelper.sin(f) * (float) numberOfBlocks / 8.0F);
    double d1 = (double) ((float) (position.getX() + 8) - MathHelper.sin(f) * (float) numberOfBlocks / 8.0F);
    double d2 = (double) ((float) (position.getZ() + 8) + MathHelper.cos(f) * (float) numberOfBlocks / 8.0F);
    double d3 = (double) ((float) (position.getZ() + 8) - MathHelper.cos(f) * (float) numberOfBlocks / 8.0F);
    double d4 = (double) (position.getY() + rand.nextInt(3) - 2);
    double d5 = (double) (position.getY() + rand.nextInt(3) - 2);

    for (int i = 0; i < numberOfBlocks; ++i) {
      float f1 = (float) i / (float) numberOfBlocks;
      double d6 = d0 + (d1 - d0) * (double) f1;
      double d7 = d4 + (d5 - d4) * (double) f1;
      double d8 = d2 + (d3 - d2) * (double) f1;
      double d9 = rand.nextDouble() * (double) numberOfBlocks / 16.0D;
      double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
      double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
      int j = MathHelper.floor(d6 - d10 / 2.0D);
      int k = MathHelper.floor(d7 - d11 / 2.0D);
      int l = MathHelper.floor(d8 - d10 / 2.0D);
      int i1 = MathHelper.floor(d6 + d10 / 2.0D);
      int j1 = MathHelper.floor(d7 + d11 / 2.0D);
      int k1 = MathHelper.floor(d8 + d10 / 2.0D);

      for (int l1 = j; l1 <= i1; ++l1) {
        double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);

        if (d12 * d12 < 1.0D) {
          for (int i2 = k; i2 <= j1; ++i2) {
            double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);

            if (d12 * d12 + d13 * d13 < 1.0D) {
              for (int j2 = l; j2 <= k1; ++j2) {
                double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);

                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                  BlockPos blockpos = new BlockPos(l1, i2, j2);

                  IBlockState state = world.getBlockState(blockpos);
                  if (state.getBlock().isReplaceableOreGen(state, world, blockpos, this.predicate)) {
                    world.setBlockState(blockpos, this.oreBlock, 2);
                  }
                }
              }
            }
          }
        }
      }
    }

    return true;
  }

  static class StonePredicate
      implements Predicate<IBlockState> {

    private StonePredicate() {

    }

    public boolean apply(IBlockState blockState) {

      if (blockState != null && blockState.getBlock() == Blocks.STONE) {
        BlockStone.EnumType type = blockState.getValue(BlockStone.VARIANT);
        return type.isNatural();

      } else {
        return false;
      }
    }
  }

  public static class BlockPredicate
      implements Predicate<IBlockState> {

    private Block toReplace;

    public BlockPredicate(Block toReplace) {

      this.toReplace = toReplace;
    }

    public boolean apply(IBlockState blockState) {

      return blockState != null
          && blockState.getBlock() == this.toReplace;
    }
  }
}