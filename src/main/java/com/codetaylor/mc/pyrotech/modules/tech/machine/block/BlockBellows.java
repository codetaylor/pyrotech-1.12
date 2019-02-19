package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBellows;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class BlockBellows
    extends BlockPartialBase {

  public static final String NAME = "bellows";

  public BlockBellows() {

    super(Material.ROCK);
    this.setHardness(2.0f);
    this.setResistance(0.2f);
  }

  @Override
  public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {

    /*TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileBellows) {
      float progress = ((TileBellows) tileEntity).getProgress();

      addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBHelper.create(0, 0, 0, 16, 3, 16));
      addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBHelper.create(1, 3, 1, 15, 6, 15));

      double offset = (-progress) * (8.0 / 16.0);
      addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBHelper.create(0, 14, 0, 16, 16, 16).offset(0, offset, 0));
      return;
    }*/

    super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entityIn, isActualState);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    TileEntity tileEntity = source.getTileEntity(pos);

    if (tileEntity instanceof TileBellows) {
      float progress = ((TileBellows) tileEntity).getProgress();

      if (MathHelper.epsilonEquals(progress, 0)) {
        return super.getBoundingBox(state, source, pos);
      }

      double offset = progress * 8.0;
      return AABBHelper.create(0, 0, 0, 16, 16 - offset, 16);

    } else {
      return super.getBoundingBox(state, source, pos);
    }
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

    EnumFacing opposite = placer.getHorizontalFacing();

    if (facing.getHorizontalIndex() > -1) {
      opposite = facing.getOpposite();
    }

    return this.getDefaultState().withProperty(Properties.FACING_HORIZONTAL, opposite);
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TileBellows();
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, Properties.FACING_HORIZONTAL);
  }

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

}
