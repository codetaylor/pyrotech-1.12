package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockCobWet
    extends BlockFalling {

  public static final String NAME = "cob_wet";

  public static final PropertyInteger DRY = PropertyInteger.create("dry", 0, 3);

  private static final int FALL_CHECK_DELAY = 2;

  public BlockCobWet() {

    super(Material.GROUND);
    this.setSoundType(SoundType.SLIME);
    this.setHarvestLevel("shovel", 0);
    this.setHardness(0.4f);
    this.setTickRandomly(true);
    this.setDefaultState(this.blockState.getBaseState().withProperty(DRY, 0));
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {

    world.scheduleUpdate(pos, this, FALL_CHECK_DELAY);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    // Overriden to prevent update tick on neighbor change.
  }

  @ParametersAreNonnullByDefault
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (!world.isRemote) {

      if (this.checkFall(world, pos)) {
        return;
      }
    }

    if (!world.isRemote) {

      if (!world.isAreaLoaded(pos, 3)) {
        return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
      }

      BlockPos up = pos.up();

      if (world.isRainingAt(up)) {
        return;
      }

      if (world.getBlockState(up).getBlock() == ModuleCore.Blocks.COB_WET) {
        return;
      }

      int value = state.getValue(DRY);

      if (value >= 1) {
        world.setBlockState(pos, ModuleCore.Blocks.COB_DRY.getDefaultState());

      } else {
        world.setBlockState(pos, ModuleCore.Blocks.COB_WET.getDefaultState().withProperty(DRY, value + 1));
      }
    }
  }

  @Override
  public int tickRate(@Nonnull World world) {

    return 10;
  }

  /**
   * Checks and starts falling.
   *
   * @param world
   * @param pos
   * @return true if falling
   */
  private boolean checkFall(World world, BlockPos pos) {

    if ((world.isAirBlock(pos.down()) || canFallThrough(world.getBlockState(pos.down()))) && pos.getY() >= 0) {
      int range = 32;

      if (!fallInstantly && world.isAreaLoaded(pos.add(-range, -range, -range), pos.add(range, range, range))) {

        if (!world.isRemote) {
          EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, world.getBlockState(pos));
          this.onStartFalling(entityfallingblock);
          world.spawnEntity(entityfallingblock);
        }

      } else {
        IBlockState state = world.getBlockState(pos);
        world.setBlockToAir(pos);

        BlockPos blockpos = pos.down();

        while ((world.isAirBlock(blockpos) || canFallThrough(world.getBlockState(blockpos))) && blockpos.getY() > 0) {
          blockpos = blockpos.down();
        }

        if (blockpos.getY() > 0) {
          world.setBlockState(blockpos.up(), state); //Forge: Fix loss of state information during world gen.
        }
      }

      return true;
    }

    return false;
  }

  // ---------------------------------------------------------------------------
  // Variants
  // ---------------------------------------------------------------------------

  @Override
  public int damageDropped(@Nonnull IBlockState state) {

    return 0;
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(DRY, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(DRY);
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, DRY);
  }
}
