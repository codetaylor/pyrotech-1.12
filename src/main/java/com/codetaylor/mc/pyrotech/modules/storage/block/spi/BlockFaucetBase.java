package com.codetaylor.mc.pyrotech.modules.storage.block.spi;

import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileFaucetBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class BlockFaucetBase
    extends BlockPartialBase
    implements IBlockInteractable {

  private static final AxisAlignedBB AABB_NORTH = AABBHelper.create(4, 4, 10, 12, 10, 16);
  private static final AxisAlignedBB AABB_SOUTH = AABBHelper.create(4, 4, 0, 12, 10, 6);
  private static final AxisAlignedBB AABB_EAST = AABBHelper.create(0, 4, 4, 6, 10, 12);
  private static final AxisAlignedBB AABB_WEST = AABBHelper.create(10, 4, 4, 16, 10, 12);

  public BlockFaucetBase() {

    super(Material.ROCK);
    this.setResistance(2.5f);
    this.setHardness(1.5f);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.NORTH));
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 0);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  protected abstract boolean canTransferHotFluids();

  protected abstract int getTransferLimit();

  protected abstract int getTransferAmountPerTick();

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

  @Override
  public boolean canPlaceBlockOnSide(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {

    TileEntity tileEntity = world.getTileEntity(pos.offset(side.getOpposite()));

    if (tileEntity == null) {
      return false;
    }

    if (!tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
      return false;
    }

    return super.canPlaceBlockOnSide(world, pos, side);
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

    if (facing.getHorizontalIndex() > -1) {
      return this.getDefaultState()
          .withProperty(Properties.FACING_HORIZONTAL, facing);

    } else {
      return this.getDefaultState()
          .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.NORTH);
    }
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);

    switch (facing) {
      case NORTH:
        return AABB_NORTH;
      case SOUTH:
        return AABB_SOUTH;
      case EAST:
        return AABB_EAST;
      case WEST:
        return AABB_WEST;
      default:
        throw new IllegalStateException();
    }
  }

  // ---------------------------------------------------------------------------
  // - Redstone
  // ---------------------------------------------------------------------------

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {

    if (world.isRemote) {
      return;
    }

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);

    if (world.isAirBlock(pos.offset(facing.getOpposite()))) {
      world.destroyBlock(pos, true);
      return;
    }

    if (world.isBlockPowered(pos)) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileFaucetBase) {
        ((TileFaucetBase) tileEntity).setActive(true);
      }
    }
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
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return this.createTileEntity();
  }

  protected abstract TileEntity createTileEntity();

  // ---------------------------------------------------------------------------
  // - Tooltip
  // ---------------------------------------------------------------------------

  @Override
  public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag) {

    int rate = this.getTransferAmountPerTick();
    tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.fluid.transfer.rate", rate));

    int limit = this.getTransferLimit();

    if (limit > -1) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.fluid.transfer.limit", limit));
    }

    boolean hotFluids = this.canTransferHotFluids();
    tooltip.add((hotFluids ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.hot.fluids." + hotFluids));
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

    return state.getValue(Properties.FACING_HORIZONTAL).getHorizontalIndex();
  }

  @Override
  public int damageDropped(IBlockState state) {

    return 0;
  }
}
