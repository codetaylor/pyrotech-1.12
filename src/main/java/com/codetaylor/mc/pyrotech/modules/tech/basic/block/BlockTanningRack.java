package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileTanningRack;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockTanningRack
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final String NAME = "tanning_rack";
  public static final AxisAlignedBB AABB_NORTH_SOUTH = AABBHelper.create(0, 0, 4, 16, 16, 12);
  public static final AxisAlignedBB AABB_EAST_WEST = AABBHelper.create(4, 0, 0, 12, 16, 16);

  public BlockTanningRack() {

    super(Material.WOOD);
    this.setHardness(1.0f);
    this.setResistance(0.2f);
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------c

  @Override
  public boolean hasTileEntity(@Nonnull IBlockState state) {

    return true;
  }

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileTanningRack();
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------c

  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(@Nonnull IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {

    return this.interactionRayTrace(super.collisionRayTrace(blockState, world, pos, start, end), blockState, world, pos, start, end);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileTanningRack) {
        StackHelper.spawnStackHandlerContentsOnTop(world, ((TileTanningRack) tileEntity).getInputStackHandler(), pos);
        StackHelper.spawnStackHandlerContentsOnTop(world, ((TileTanningRack) tileEntity).getOutputStackHandler(), pos);
      }
    }

    super.breakBlock(world, pos, state);
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    switch (state.getValue(Properties.FACING_HORIZONTAL)) {
      case NORTH:
      case SOUTH:
        return AABB_NORTH_SOUTH;
      case EAST:
      case WEST:
        return AABB_EAST_WEST;
    }

    return super.getBoundingBox(state, source, pos);
  }

  // ---------------------------------------------------------------------------
  // - Render
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, Properties.FACING_HORIZONTAL);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.HORIZONTALS[meta]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(Properties.FACING_HORIZONTAL).getIndex() - 2;
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public IBlockState getStateForPlacement(
      World world,
      BlockPos pos,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      EntityLivingBase placer,
      EnumHand hand
  ) {

    EnumFacing placementFacing = placer.getHorizontalFacing().getOpposite();
    return this.getDefaultState().withProperty(Properties.FACING_HORIZONTAL, placementFacing);
  }
}
