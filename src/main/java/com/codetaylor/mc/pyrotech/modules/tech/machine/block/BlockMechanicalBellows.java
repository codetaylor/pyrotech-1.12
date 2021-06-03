package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalBellows;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalBellowsTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.stream.Stream;

public class BlockMechanicalBellows
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final String NAME = "mechanical_bellows";

  public static final IProperty<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

  private static final AxisAlignedBB AABB_TOP = AABBHelper.create(0, 0, 0, 16, 10, 16);

  public BlockMechanicalBellows() {

    super(Material.ROCK);
    this.setHardness(2.0f);
    this.setResistance(0.2f);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @SuppressWarnings("deprecation")
  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (state.getBlock() == this
        && state.getValue(TYPE) == EnumType.TOP) {
      return AABB_TOP;
    }

    return super.getBoundingBox(state, source, pos);
  }

  @SuppressWarnings("deprecation")
  @Nullable
  @ParametersAreNonnullByDefault
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end) {

    RayTraceResult result = super.collisionRayTrace(blockState, world, pos, start, end);

    if (this.isTop(blockState)) {
      return this.interactionRayTrace(result, blockState, world, pos, start, end);
    }

    return result;
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(
      World world,
      BlockPos pos,
      IBlockState state,
      EntityPlayer player,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    if (this.isTop(state)) {
      return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
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

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    BlockPos posTop;
    BlockPos posBottom;

    boolean isTop = this.isTop(state);

    if (isTop) {
      posTop = pos;
      posBottom = pos.down();

    } else {
      posTop = pos.up();
      posBottom = pos;
    }

    TileEntity tileTop = world.getTileEntity(posTop);

    if (tileTop instanceof TileMechanicalBellowsTop) {
      TileCogWorkerBase.CogStackHandler cogStackHandler = ((TileMechanicalBellowsTop) tileTop).getCogStackHandler();
      StackHelper.spawnStackHandlerContentsOnTop(world, cogStackHandler, posBottom);
    }

    world.setBlockToAir(posTop);
    world.setBlockToAir(posBottom);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

    if (!this.isTop(state)) {

      BlockPos up = pos.up();

      if (super.canPlaceBlockAt(world, up)) {
        EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
        world.setBlockState(up, this.getDefaultState()
            .withProperty(Properties.FACING_HORIZONTAL, facing)
            .withProperty(TYPE, EnumType.TOP));
      }
    }

    super.onBlockPlacedBy(world, pos, state, placer, stack);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean canPlaceBlockAt(World world, BlockPos pos) {

    return super.canPlaceBlockAt(world, pos)
        && super.canPlaceBlockAt(world, pos.up());
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public boolean isTop(IBlockState blockState) {

    if (blockState.getBlock() == this) {
      return blockState.getValue(TYPE) == EnumType.TOP;
    }

    return false;
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(@Nonnull IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    if (state.getValue(TYPE) == EnumType.TOP) {
      return new TileMechanicalBellowsTop();
    }

    return new TileMechanicalBellows();
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, Properties.FACING_HORIZONTAL, TYPE);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    // 0-3 type 0
    // 4-7 type 1

    return this.getDefaultState()
        .withProperty(TYPE, EnumType.fromMeta((meta >> 2) & 1))
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.HORIZONTALS[meta & 3]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return (state.getValue(TYPE).getMeta() << 2) | state.getValue(Properties.FACING_HORIZONTAL).getHorizontalIndex();
  }

  public enum EnumType
      implements IVariant {

    BOTTOM(0, "bottom"),
    TOP(1, "top");

    private static final EnumType[] META_LOOKUP = Stream.of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);

    private final int meta;
    private final String name;

    EnumType(int meta, String name) {

      this.meta = meta;
      this.name = name;
    }

    @Override
    public int getMeta() {

      return this.meta;
    }

    @Nonnull
    @Override
    public String getName() {

      return this.name;
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

  }
}
