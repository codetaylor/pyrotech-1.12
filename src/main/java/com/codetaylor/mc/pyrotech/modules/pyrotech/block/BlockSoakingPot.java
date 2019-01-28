package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockPartialBase;
import com.codetaylor.mc.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileAnvilBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileSoakingPot;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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

public class BlockSoakingPot
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final String NAME = "soaking_pot";

  public static final AxisAlignedBB AABB = new AxisAlignedBB(2.0 / 16.0, 0, 2.0 / 16.0, 14.0 / 16.0, 9.0 / 16.0, 14.0 / 16.0);

  public BlockSoakingPot() {

    super(Material.ROCK);
    this.setHardness(3.0F);
    this.setResistance(5.0F);
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 0);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {

    return this.interactionRayTrace(super.collisionRayTrace(blockState, world, pos, start, end), blockState, world, pos, start, end);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileAnvilBase) {
        StackHelper.spawnStackHandlerContentsOnTop(world, ((TileAnvilBase) tileEntity).getStackHandler(), pos);
      }
    }

    super.breakBlock(world, pos, state);
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

    EnumFacing opposite = placer.getHorizontalFacing().getOpposite();
    return this.getDefaultState().withProperty(Properties.FACING_HORIZONTAL, opposite);
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

    return new TileSoakingPot();
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
