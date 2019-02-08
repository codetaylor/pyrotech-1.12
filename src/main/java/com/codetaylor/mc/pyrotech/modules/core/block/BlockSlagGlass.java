package com.codetaylor.mc.pyrotech.modules.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockSlagGlass
    extends Block {

  public static final String NAME = "slag_glass";

  public static final PropertyBool CONNECTED_DOWN = PropertyBool.create("connected_down");
  public static final PropertyBool CONNECTED_UP = PropertyBool.create("connected_up");
  public static final PropertyBool CONNECTED_NORTH = PropertyBool.create("connected_north");
  public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create("connected_south");
  public static final PropertyBool CONNECTED_WEST = PropertyBool.create("connected_west");
  public static final PropertyBool CONNECTED_EAST = PropertyBool.create("connected_east");

  public BlockSlagGlass() {

    super(Material.GLASS);

    this.setSoundType(SoundType.GLASS);
    this.setHardness(0.3f);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(CONNECTED_DOWN, Boolean.FALSE)
        .withProperty(CONNECTED_EAST, Boolean.FALSE)
        .withProperty(CONNECTED_NORTH, Boolean.FALSE)
        .withProperty(CONNECTED_SOUTH, Boolean.FALSE)
        .withProperty(CONNECTED_UP, Boolean.FALSE)
        .withProperty(CONNECTED_WEST, Boolean.FALSE));
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(
        this,
        CONNECTED_DOWN,
        CONNECTED_UP,
        CONNECTED_NORTH,
        CONNECTED_SOUTH,
        CONNECTED_WEST,
        CONNECTED_EAST
    );
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return 0;
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState();
  }

  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos position) {

    return state.withProperty(CONNECTED_DOWN, this.isConnected(world, position, EnumFacing.DOWN))
        .withProperty(CONNECTED_EAST, this.isConnected(world, position, EnumFacing.EAST))
        .withProperty(CONNECTED_NORTH, this.isConnected(world, position, EnumFacing.NORTH))
        .withProperty(CONNECTED_SOUTH, this.isConnected(world, position, EnumFacing.SOUTH))
        .withProperty(CONNECTED_UP, this.isConnected(world, position, EnumFacing.UP))
        .withProperty(CONNECTED_WEST, this.isConnected(world, position, EnumFacing.WEST));
  }

  private boolean isConnected(IBlockAccess world, BlockPos pos, EnumFacing side) {

    final IBlockState original = world.getBlockState(pos);
    final IBlockState connected = world.getBlockState(pos.offset(side));

    return this.canConnect(original, connected);
  }

  private boolean canConnect(IBlockState original, IBlockState connected) {

    return connected.getBlock() == original.getBlock();
  }

  @Nonnull
  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {

    IBlockState otherBlockState = blockAccess.getBlockState(pos.offset(side));
    Block block = otherBlockState.getBlock();

    if (block == this) {
      return false;
    }

    return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
  }
}
