package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileChoppingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockChoppingBlock
    extends BlockPartialBase {

  public static final String NAME = "chopping_block";

  public static final IProperty<Integer> DAMAGE = PropertyInteger.create("damage", 0, 5);
  public static final IProperty<Integer> SAWDUST = PropertyInteger.create("sawdust", 0, 5);

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 6f / 16f, 1);

  public BlockChoppingBlock() {

    super(Material.WOOD);
    this.setHardness(0.5f);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (!(tileEntity instanceof TileChoppingBlock)) {
      return false;
    }

    TileChoppingBlock choppingBlock = (TileChoppingBlock) tileEntity;

    choppingBlock.interact(choppingBlock, world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);

    return true;
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileChoppingBlock();
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, DAMAGE, SAWDUST);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState()
        .withProperty(DAMAGE, meta)
        .withProperty(SAWDUST, 0);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(DAMAGE);
  }

  @Override
  public int damageDropped(IBlockState state) {

    return state.getValue(DAMAGE);
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

    return (side == EnumFacing.DOWN);
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

}
