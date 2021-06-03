package com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileSawmillBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public abstract class BlockSawmillBase
    extends BlockCombustionWorkerStoneBase {

  private static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(1.0 / 16.0, 0.0 / 16.0, 1.0 / 16.0, 15.0 / 16.0, 4.0 / 16.0, 15.0 / 16.0);

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (this.isTop(state)) {
      return AABB_TOP;
    }

    return super.getBoundingBox(state, source, pos);
  }

  @Override
  public void onEntityWalk(World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {

    if (this.isTop(world.getBlockState(pos))) {
      TileEntity tile = world.getTileEntity(pos.down());

      if (tile instanceof TileSawmillBase) {
        TileSawmillBase<?> tileSawmillBase = (TileSawmillBase<?>) tile;

        if (tileSawmillBase.workerIsActive()
            && tileSawmillBase.getEntityDamageFromBlade() > 0) {
          entity.attackEntityFrom(DamageSource.GENERIC, (float) tileSawmillBase.getEntityDamageFromBlade());
        }
      }
    }

    super.onEntityWalk(world, pos, entity);
  }

  @Override
  protected void randomDisplayTickActiveTop(IBlockState state, World world, BlockPos pos, Random rand) {

    double centerX = pos.getX();
    double centerY = pos.getY() - 0.2;
    double centerZ = pos.getZ();

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);

    switch (facing) {

      case NORTH:
        centerX += 8.0 / 16.0;
        centerZ += 18.0 / 16.0;
        break;

      case SOUTH:
        centerX += 8.0 / 16.0;
        centerZ += -2.0 / 16.0;
        break;

      case EAST:
        centerX += -2.0 / 16.0;
        centerZ += 8.0 / 16.0;
        break;

      case WEST:
        centerX += 18.0 / 16.0;
        centerZ += 8.0 / 16.0;
        break;

    }

    world.spawnParticle(
        EnumParticleTypes.SMOKE_LARGE,
        centerX/* + (Util.RANDOM.nextDouble() - 0.5) * 0.25*/,
        centerY,
        centerZ/* + (Util.RANDOM.nextDouble() - 0.5) * 0.25*/,
        0,
        0.05 + (Util.RANDOM.nextFloat() * 2 - 1) * 0.05,
        0
    );
  }

  @Override
  protected void randomDisplayTickWorkingTop(IBlockState state, World world, BlockPos pos, Random rand) {

    TileEntity tileEntity = world.getTileEntity(pos.down());

    if (tileEntity instanceof TileSawmillBase) {
      ItemStackHandler inputStackHandler = ((TileSawmillBase<?>) tileEntity).getInputStackHandler();
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(0);
      Block blockFromItem = Block.getBlockFromItem(stackInSlot.getItem());

      if (blockFromItem != Blocks.AIR) {
        //noinspection deprecation
        IBlockState blockState = blockFromItem.getStateFromMeta(stackInSlot.getMetadata());
        int stateId = Block.getStateId(blockState);

        for (int i = 0; i < 8; ++i) {
          world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D, stateId);
        }

      } else {

        for (int i = 0; i < 8; ++i) {
          int itemId = Item.getIdFromItem(stackInSlot.getItem());
          int metadata = stackInSlot.getMetadata();
          world.spawnParticle(EnumParticleTypes.ITEM_CRACK, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D, itemId, metadata);
        }
      }
    }
  }
}
