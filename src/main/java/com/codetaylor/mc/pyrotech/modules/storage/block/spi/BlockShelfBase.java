package com.codetaylor.mc.pyrotech.modules.storage.block.spi;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileShelf;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
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
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BlockShelfBase
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

  private static final Map<EnumFacing, Map<EnumType, AxisAlignedBB>> AABB;

  static {
    final AxisAlignedBB Z_POS = AABBHelper.create(0, 0, 10, 16, 16, 16);
    final AxisAlignedBB Z_NEG = AABBHelper.create(0, 0, 0, 16, 16, 6);
    final AxisAlignedBB X_POS = AABBHelper.create(0, 0, 0, 6, 16, 16);
    final AxisAlignedBB X_NEG = AABBHelper.create(10, 0, 0, 16, 16, 16);

    AABB = Stream.of(EnumFacing.HORIZONTALS)
        .collect(Collectors.toMap(facing -> facing, facing -> {

          Map<EnumType, AxisAlignedBB> map = new EnumMap<>(EnumType.class);

          if (facing == EnumFacing.NORTH) {
            map.put(EnumType.BACK, Z_POS);
            map.put(EnumType.FORWARD, Z_NEG);

          } else if (facing == EnumFacing.SOUTH) {
            map.put(EnumType.BACK, Z_NEG);
            map.put(EnumType.FORWARD, Z_POS);

          } else if (facing == EnumFacing.EAST) {
            map.put(EnumType.BACK, X_POS);
            map.put(EnumType.FORWARD, X_NEG);

          } else if (facing == EnumFacing.WEST) {
            map.put(EnumType.BACK, X_NEG);
            map.put(EnumType.FORWARD, X_POS);
          }

          return map;

        }, (u, v) -> {
          throw new IllegalStateException();
        }, () -> new EnumMap<>(EnumFacing.class)));

  }

  public BlockShelfBase(float hardness, float resistance) {

    super(Material.WOOD);
    this.setHardness(hardness);
    this.setResistance(resistance);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.NORTH)
        .withProperty(TYPE, EnumType.BACK));
    this.setSoundType(SoundType.WOOD);
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

  @Override
  public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileShelf) {
        ((TileShelf) tileEntity).dropContents();
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

    // if hit opposite facing, back
    // if hit any side facing, split facing forward / back
    EnumFacing playerFacing = placer.getHorizontalFacing();
    EnumFacing opposite = playerFacing.getOpposite();
    EnumType type = EnumType.BACK;

    if (facing != opposite
        && facing != playerFacing) {

      if (playerFacing == EnumFacing.SOUTH && hitZ < 0.5) {
        type = EnumType.FORWARD;

      } else if (playerFacing == EnumFacing.NORTH && hitZ > 0.5) {
        type = EnumType.FORWARD;

      } else if (playerFacing == EnumFacing.EAST && hitX < 0.5) {
        type = EnumType.FORWARD;

      } else if (playerFacing == EnumFacing.WEST && hitX > 0.5) {
        type = EnumType.FORWARD;
      }
    }

    return this.getDefaultState()
        .withProperty(Properties.FACING_HORIZONTAL, opposite)
        .withProperty(BlockShelfBase.TYPE, type);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
    EnumType type = state.getValue(BlockShelfBase.TYPE);
    return AABB.get(facing).get(type);
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

    return this.createTileEntity();
  }

  protected abstract TileEntity createTileEntity();

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

    return false;
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, Properties.FACING_HORIZONTAL, BlockShelfBase.TYPE);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    // 0-3 type 0
    // 4-7 type 1

    return this.getDefaultState()
        .withProperty(BlockShelfBase.TYPE, BlockShelfBase.EnumType.fromMeta((meta >> 2) & 1))
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.HORIZONTALS[meta & 3]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return (state.getValue(BlockShelfBase.TYPE).getMeta() << 2) | state.getValue(Properties.FACING_HORIZONTAL).getHorizontalIndex();
  }

  @Override
  public int damageDropped(IBlockState state) {

    return 0;
  }

  public enum EnumType
      implements IVariant {

    BACK(0, "back"),
    FORWARD(1, "forward");

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
