package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntityMud;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockMudLayer
    extends BlockPartialBase {

  public static final String NAME = "mud_layer";

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
  private static final AxisAlignedBB COLLISION_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0, 1.0);

  public BlockMudLayer() {

    super(Material.PLANTS);
    this.setSoundType(SoundType.SLIME);
    this.setHardness(0.1f);
    this.setHarvestLevel("shovel", 0);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

    if (entity instanceof EntityMud) {
      entity.motionX *= 1.05;
      entity.motionZ *= 1.05;

    } else {
      entity.motionX *= 0.4;
      entity.motionZ *= 0.4;
    }
  }

  @SuppressWarnings("deprecation")
  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  @SuppressWarnings("deprecation")
  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {

    return COLLISION_AABB;
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean isPassable(IBlockAccess world, BlockPos pos) {

    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isTopSolid(@Nonnull IBlockState state) {

    return false;
  }

  @Nonnull
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess blockAccess, IBlockState state, BlockPos pos, EnumFacing facing) {

    return (facing == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }

  @Override
  public boolean canPlaceBlockAt(World world, BlockPos pos) {

    IBlockState state = world.getBlockState(pos.down());
    Block block = state.getBlock();

    if (block == this) {
      return false;
    }

    if (block == Blocks.BARRIER) {
      return false;
    }

    //noinspection deprecation
    if (!block.isFullBlock(state)
        || !block.isFullCube(state)) {
      return false;
    }

    if (world.isAirBlock(pos.down())) {
      return false;
    }

    BlockFaceShape blockfaceshape = state.getBlockFaceShape(world, pos.down(), EnumFacing.UP);

    return (blockfaceshape == BlockFaceShape.SOLID);
  }

  @SuppressWarnings("deprecation")
  @ParametersAreNonnullByDefault
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    if (!this.canPlaceBlockAt(world, pos)) {
      world.destroyBlock(pos, true);
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public void harvestBlock(
      World world,
      EntityPlayer player,
      BlockPos pos,
      IBlockState state,
      @Nullable TileEntity tile,
      ItemStack stack
  ) {

    super.harvestBlock(world, player, pos, state, tile, stack);
    world.setBlockToAir(pos);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    drops.add(new ItemStack(ModuleCore.Items.ROCK, 2, BlockRock.EnumType.MUD.getMeta()));
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean isReplaceable(IBlockAccess world, BlockPos pos) {

    return true;
  }
}
