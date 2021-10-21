package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileDryingRack;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileDryingRackCrude;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileDryingRackBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.stream.Stream;

public class BlockDryingRack
    extends BlockPartialBase
    implements IBlockVariant<BlockDryingRack.EnumType>,
    IBlockInteractable {

  public static final String NAME = "drying_rack";

  public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  private static final AxisAlignedBB AABB = AABBHelper.create(1, 11, 1, 15, 12, 15);
  public static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0f / 16f, 11f / 16f, 0f / 16f, 16f / 16f, 16f / 16f, 5f / 16f);
  private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0f / 16f, 11f / 16f, 11f / 16f, 16f / 16f, 16f / 16f, 16f / 16f);
  private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(11f / 16f, 11f / 16f, 0f / 16f, 16f / 16f, 16f / 16f, 16f / 16f);
  private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0f / 16f, 11f / 16f, 0f / 16f, 5f / 16f, 16f / 16f, 16f / 16f);

  public BlockDryingRack() {

    super(Material.WOOD);
    this.setHardness(0.5f);
    this.setResistance(5.0f);
    this.setSoundType(SoundType.WOOD);
    this.setHarvestLevel("axe", 0);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(FACING, EnumFacing.SOUTH)
        .withProperty(VARIANT, BlockDryingRack.EnumType.CRUDE));
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

    return 150;
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
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileDryingRackBase) {
      ((TileDryingRackBase) tileEntity).dropContents();
    }

    super.breakBlock(world, pos, state);
  }

  // ---------------------------------------------------------------------------
  // - Placement
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {

    EnumFacing horizontalFacing = facing.getOpposite();

    if (horizontalFacing == EnumFacing.UP
        || horizontalFacing == EnumFacing.DOWN) {
      horizontalFacing = placer.getHorizontalFacing();
    }

    IBlockState blockState = world.getBlockState(pos.offset(facing.getOpposite()));

    if (blockState.getBlock() == this
        && blockState.getValue(VARIANT) == BlockDryingRack.EnumType.CRUDE) {
      horizontalFacing = blockState.getValue(BlockDryingRack.FACING);
    }

    return this.getDefaultState()
        .withProperty(VARIANT, BlockDryingRack.EnumType.fromMeta(meta))
        .withProperty(FACING, horizontalFacing);
  }

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    return super.canPlaceBlockAt(world, pos);
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (state.getValue(VARIANT) == BlockDryingRack.EnumType.CRUDE) {

      EnumFacing facing = state.getValue(FACING);

      switch (facing) {
        case NORTH:
          return AABB_NORTH;
        case SOUTH:
          return AABB_SOUTH;
        case EAST:
          return AABB_EAST;
        case WEST:
          return AABB_WEST;
      }

    }

    return AABB;
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    EnumType type = state.getValue(VARIANT);

    return (type == BlockDryingRack.EnumType.CRUDE)
        || (type == BlockDryingRack.EnumType.NORMAL);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    EnumType type = state.getValue(VARIANT);

    if (type == BlockDryingRack.EnumType.CRUDE) {
      return new TileDryingRackCrude();

    } else if (type == BlockDryingRack.EnumType.NORMAL) {
      return new TileDryingRack();
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {

    if (state.getBlock() == this
        && state.getValue(VARIANT) == EnumType.NORMAL) {
      IBlockState blockStateUp = world.getBlockState(pos.up());

      if (blockStateUp.getBlock() == this
          && blockStateUp.getValue(VARIANT) == EnumType.NORMAL) {
        return state.withProperty(VARIANT, EnumType.STACKED);
      }
    }

    return super.getActualState(state, world, pos);
  }

  @Override
  public void getSubBlocks(
      CreativeTabs itemIn,
      NonNullList<ItemStack> list
  ) {

    for (EnumType type : BlockDryingRack.EnumType.values()) {

      if (type != EnumType.STACKED) { // stacked is a visual state only
        list.add(new ItemStack(this, 1, type.getMeta()));
      }
    }
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT, FACING);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    // 0-3 type 0
    // 4-7 type 1

    return this.getDefaultState()
        .withProperty(VARIANT, BlockDryingRack.EnumType.fromMeta((meta >> 2) & 1))
        .withProperty(FACING, EnumFacing.HORIZONTALS[meta & 3]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return (state.getValue(VARIANT).getMeta() << 2) | state.getValue(FACING).getHorizontalIndex();
  }

  @Override
  public int damageDropped(IBlockState state) {

    return state.getValue(VARIANT).getMeta();
  }

  @Nonnull
  @Override
  public String getModelName(ItemStack itemStack) {

    return BlockDryingRack.EnumType.fromMeta(itemStack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  public enum EnumType
      implements IVariant {

    CRUDE(0, "crude"),
    NORMAL(1, "normal"),
    STACKED(2, "stacked");

    private static final EnumType[] META_LOOKUP = Stream.of(BlockDryingRack.EnumType.values())
        .sorted(Comparator.comparing(BlockDryingRack.EnumType::getMeta))
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
