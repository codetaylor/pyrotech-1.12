package com.codetaylor.mc.pyrotech.modules.core.block.spi;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BlockBushBase
    extends BlockPartialBase {

  private static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

  private static final Random RANDOM = new Random();

  private static final AxisAlignedBB[] AABB = new AxisAlignedBB[]{
      AABBHelper.create(7, 0, 7, 9, 3, 9),
      AABBHelper.create(7, 0, 7, 9, 6, 9),
      AABBHelper.create(7, 0, 7, 9, 7, 9),
      AABBHelper.create(7, 0, 7, 9, 8, 9),
      AABBHelper.create(4, 6, 4, 12, 10, 12),
      AABBHelper.create(3, 5, 3, 13, 11, 13),
      AABBHelper.create(2, 4, 2, 14, 12, 14),
      AABBHelper.create(2, 4, 2, 14, 12, 14)
  };

  private static final List<Int2ObjectMap<AxisAlignedBB>> OFFSET_AABB = new ArrayList<>(8);

  static {
    // x = 0, 1, 2 = -1, 0, 1 = 2 bits
    // z = 0, 1, 2 = -1, 0, 1 = 2 bits

    for (AxisAlignedBB axisAlignedBB : AABB) {
      Int2ObjectOpenHashMap<AxisAlignedBB> map = new Int2ObjectOpenHashMap<>(9);
      OFFSET_AABB.add(map);

      for (int x = 0; x < 3; x++) {
        for (int z = 0; z < 3; z++) {
          map.put(BlockBushBase.encodeOffset(x, z), axisAlignedBB.offset((x - 1) * 0.0625, 0, (z - 1) * 0.0625));
        }
      }
    }
  }

  private static int encodeOffset(int x, int z) {

    return (x & 0b11) | ((z & 0b11) << 2);
  }

  public BlockBushBase() {

    super(Material.WOOD);
    this.setTickRandomly(true);
    this.setHardness(1);
    this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
  }

  // ---------------------------------------------------------------------------
  // - Contract
  // ---------------------------------------------------------------------------

  public abstract boolean isValidBlock(IBlockState blockState);

  public abstract Item getSeedItem();

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public int getMaxAge() {

    return 7;
  }

  public int getAge(IBlockState state) {

    return state.getValue(AGE);
  }

  public IBlockState withAge(int age) {

    return this.getDefaultState().withProperty(AGE, age);
  }

  public boolean isMaxAge(IBlockState state) {

    return state.getValue(AGE) >= this.getMaxAge();
  }

  @ParametersAreNonnullByDefault
  @Override
  public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {

    if (this.getAge(blockState) < 3) {
      return 0.1f;
    }

    return super.getBlockHardness(blockState, world, pos);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState blockState, EntityPlayer player) {

    return false;
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {

    long seed = MathHelper.getCoordinateRandom(pos.getX(), 0, pos.getZ());
    int value = (int) (seed >> 16 & 0b1111);
    int x = (value & 0b11) % 3;
    int z = ((value >> 2) & 0b11) % 3;
    return new Vec3d((x - 1) * 0.0625, 0, (z - 1) * 0.0625);
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public SoundType getSoundType(IBlockState blockState, World world, BlockPos pos, @Nullable Entity entity) {

    switch (this.getAge(blockState)) {
      case 0:
      case 1:
      case 2:
      case 3:
        return SoundType.PLANT;
      case 4:
      case 5:
      case 6:
      case 7:
      default:
        return SoundType.WOOD;
    }
  }

  @Nonnull
  @Override
  public Material getMaterial(@Nonnull IBlockState blockState) {

    switch (this.getAge(blockState)) {
      case 0:
      case 1:
      case 2:
      case 3:
        return Material.PLANTS;
      case 4:
      case 5:
      case 6:
      case 7:
      default:
        return Material.WOOD;
    }
  }

  @Nonnull
  @Override
  public EnumPushReaction getMobilityFlag(@Nonnull IBlockState blockState) {

    switch (this.getAge(blockState)) {
      case 0:
      case 1:
      case 2:
      case 3:
        return EnumPushReaction.DESTROY;
      case 4:
      case 5:
      case 6:
      case 7:
      default:
        return EnumPushReaction.BLOCK;
    }
  }

  @Override
  public boolean isReplaceable(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {

    return world.getBlockState(pos).getValue(AGE) < 4;
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {

    int age = blockState.getValue(AGE);

    if (age < 0 || age >= AABB.length) {
      return super.getBoundingBox(blockState, world, pos);
    }

    long seed = MathHelper.getCoordinateRandom(pos.getX(), 0, pos.getZ());
    int value = (int) (seed >> 16 & 0b1111);
    int x = (value & 0b11) % 3;
    int z = ((value >> 2) & 0b11) % 3;
    int encodedOffset = BlockBushBase.encodeOffset(x, z);

    return OFFSET_AABB.get(age).get(encodedOffset);
  }

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {

    switch (this.getAge(blockState)) {
      case 0:
      case 1:
      case 2:
      case 3:
        return Block.NULL_AABB;
      case 4:
      case 5:
      case 6:
      case 7:
        return this.getBoundingBox(blockState, world, pos);
      default:
        return super.getCollisionBoundingBox(blockState, world, pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Override
  public void neighborChanged(IBlockState blockState, World world, BlockPos pos, Block block, BlockPos fromPos) {

    this.checkAndDropBlock(world, pos);
  }

  protected void checkAndDropBlock(World world, BlockPos pos) {

    if (!this.isValidBlock(world.getBlockState(pos.down()))) {
      world.destroyBlock(pos, true);
    }
  }

  // ---------------------------------------------------------------------------
  // - Render
  // ---------------------------------------------------------------------------

  @Nonnull
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public Item getItemDropped(IBlockState blockState, Random rand, int fortune) {

    if (this.getAge(blockState) < 3) {
      return this.getSeedItem();
    }

    return Items.STICK;
  }

  @ParametersAreNonnullByDefault
  @Override
  public int quantityDropped(IBlockState blockState, int fortune, Random random) {

    if (this.getAge(blockState) < 3) {
      return 1;
    }

    return random.nextInt(3) + 1;
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.withAge(meta);
  }

  @Override
  public int getMetaFromState(@Nonnull IBlockState state) {

    return this.getAge(state);
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, AGE, Properties.FACING_HORIZONTAL);
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public IBlockState getActualState(IBlockState blockState, IBlockAccess world, BlockPos pos) {

    RANDOM.setSeed(MathHelper.getCoordinateRandom(pos.getX(), 0, pos.getZ()));
    EnumFacing facing = EnumFacing.HORIZONTALS[RANDOM.nextInt(4)];
    return blockState.withProperty(Properties.FACING_HORIZONTAL, facing);
  }
}
