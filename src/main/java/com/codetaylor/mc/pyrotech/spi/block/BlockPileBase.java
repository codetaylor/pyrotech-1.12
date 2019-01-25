package com.codetaylor.mc.pyrotech.spi.block;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockPileBase
    extends BlockPartialBase {

  public static final PropertyInteger LEVEL = PropertyInteger.create("level", 1, 8);

  private static final AxisAlignedBB[] AABB_ARRAY = new AxisAlignedBB[]{
      new AxisAlignedBB(0, 0, 0, 1, 2.0 / 16.0, 1),
      new AxisAlignedBB(0, 0, 0, 1, 4.0 / 16.0, 1),
      new AxisAlignedBB(0, 0, 0, 1, 6.0 / 16.0, 1),
      new AxisAlignedBB(0, 0, 0, 1, 8.0 / 16.0, 1),
      new AxisAlignedBB(0, 0, 0, 1, 10.0 / 16.0, 1),
      new AxisAlignedBB(0, 0, 0, 1, 12.0 / 16.0, 1),
      new AxisAlignedBB(0, 0, 0, 1, 14.0 / 16.0, 1),
      FULL_BLOCK_AABB
  };

  public BlockPileBase(Material material) {

    super(material);
    this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 8));
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {

    return false;
  }

  @Override
  public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {

    return willHarvest || super.removedByPlayer(state, world, pos, player, false);
  }

  @Override
  public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, ItemStack stack) {

    super.harvestBlock(world, player, pos, state, te, stack);

    if (!world.isRemote) {
      int level = state.getValue(BlockPileBase.LEVEL);

      StackHelper.spawnStackOnTop(world, this.getDrop(world, pos, state), pos, (level * 2) / 16.0);

      if (level == 1) {
        world.setBlockToAir(pos);

      } else {
        world.setBlockState(pos, this.getDefaultState().withProperty(BlockPileBase.LEVEL, level - 1), 1 | 2);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
    //
  }

  protected abstract ItemStack getDrop(World world, BlockPos pos, IBlockState state);

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB_ARRAY[state.getValue(BlockPileBase.LEVEL) - 1];
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

    if (side == EnumFacing.DOWN) {
      return true;
    }

    return (state.getValue(BlockPileBase.LEVEL) == 8);
  }

  @Override
  public boolean isTopSolid(IBlockState state) {

    // This is checked by fire to see if it exists. Blocks will not catch fire
    // when lit for pit burning if this returns false.
    return (state.getValue(BlockPileBase.LEVEL) == 8);
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, BlockPileBase.LEVEL);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState()
        .withProperty(BlockPileBase.LEVEL, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(BlockPileBase.LEVEL);
  }

  @Nonnull
  @Override
  public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {

    return this.getDefaultState()
        .withProperty(BlockPileBase.LEVEL, 8);
  }
}
