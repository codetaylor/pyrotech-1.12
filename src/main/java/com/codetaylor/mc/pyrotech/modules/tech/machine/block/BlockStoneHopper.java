package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneHopper;
import com.google.common.collect.ImmutableList;
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
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collision ray-trace code derived from RWTema's Diet Hopper code.
 * https://github.com/rwtema/DietHopper/blob/master/src/main/java/com/rwtema/diethopper/BlockDietHopper.java
 */
public class BlockStoneHopper
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final String NAME = "stone_hopper";

  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

  private static final Map<EnumType, Map<EnumFacing, List<AxisAlignedBB>>> RAYTRACE_COLLISION_BOUNDS;

  static {
    List<AxisAlignedBB> shared = ImmutableList.of(
        AABBHelper.create(0, 10, 0, 16, 16, 16),
        AABBHelper.create(4, 4, 4, 12, 10, 12)
    );

    RAYTRACE_COLLISION_BOUNDS = Stream.of(EnumType.values())
        .collect(Collectors.toMap(type -> type, type -> {

          Map<EnumFacing, List<AxisAlignedBB>> result;

          result = Stream.of(EnumFacing.values())
              .filter(t -> t != EnumFacing.UP)
              .collect(Collectors.toMap(aa -> aa, aa -> new ArrayList<>(shared), (u, v) -> {
                throw new IllegalStateException();
              }, () -> new EnumMap<>(EnumFacing.class)));

          if (type == EnumType.Down) {
            result.get(EnumFacing.NORTH).add(AABBHelper.create(6, 0, 6, 10, 4, 10));
            result.get(EnumFacing.SOUTH).add(AABBHelper.create(6, 0, 6, 10, 4, 10));
            result.get(EnumFacing.EAST).add(AABBHelper.create(6, 0, 6, 10, 4, 10));
            result.get(EnumFacing.WEST).add(AABBHelper.create(6, 0, 6, 10, 4, 10));

          } else {
            result.get(EnumFacing.SOUTH).add(AABBHelper.create(6, 4, 0, 10, 8, 4));
            result.get(EnumFacing.NORTH).add(AABBHelper.create(6, 4, 12, 10, 8, 16));
            result.get(EnumFacing.WEST).add(AABBHelper.create(12, 4, 6, 16, 8, 10));
            result.get(EnumFacing.EAST).add(AABBHelper.create(0, 4, 6, 4, 8, 10));
          }

          return result;

        }, (u, v) -> {
          throw new IllegalStateException();
        }, () -> new EnumMap<>(EnumType.class)));
  }

  public BlockStoneHopper() {

    super(Material.ROCK);
    this.setHardness(2);
    this.setHarvestLevel("pickaxe", 0);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.NORTH)
        .withProperty(TYPE, EnumType.Down));
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end) {

    return RAYTRACE_COLLISION_BOUNDS.get(blockState.getValue(TYPE))
        .get(blockState.getValue(Properties.FACING_HORIZONTAL))
        .stream()
        .map(bb -> rayTrace(pos, start, end, bb))
        .anyMatch(Objects::nonNull)
        ? this.interactionRayTrace(super.collisionRayTrace(blockState, world, pos, start, end), blockState, world, pos, start, end) : null;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileStoneHopper) {
      TileStoneHopper.CogStackHandler handler = ((TileStoneHopper) tileEntity).getCogStackHandler();
      StackHelper.spawnStackHandlerContentsOnTop(world, handler, pos);
    }

    super.breakBlock(world, pos, state);
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
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileStoneHopper();
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, Properties.FACING_HORIZONTAL, TYPE);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    // N, S, W, E
    // 0, 1, 2, 3 = facing, down
    // 4, 5, 6, 7 = facing, side

    int type = ((meta >> 2) & 3);
    int facingIndex = (meta & 3) + 2;

    return this.getDefaultState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.VALUES[facingIndex])
        .withProperty(TYPE, EnumType.fromMeta(type));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
    EnumType type = state.getValue(TYPE);

    int meta = facing.getIndex() - 2;
    meta |= (type.getMeta() << 2);
    return meta;
  }

  @Nonnull
  @Override
  public IBlockState getStateForPlacement(
      @Nonnull World world,
      @Nonnull BlockPos pos,
      @Nonnull EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      @Nonnull EntityLivingBase placer,
      EnumHand hand
  ) {

    EnumType type;

    if (facing.getHorizontalIndex() == -1) {
      type = EnumType.Down;
      facing = placer.getHorizontalFacing().getOpposite();

    } else {
      type = EnumType.Side;
    }

    return this.getDefaultState()
        .withProperty(Properties.FACING_HORIZONTAL, facing)
        .withProperty(TYPE, type);
  }

  public enum EnumType
      implements IVariant {

    Down(0, "down"),
    Side(1, "side");

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
