package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class BlockRock //'n beats
    extends Block
    implements IBlockVariant<BlockRock.EnumType> {

  public static final String NAME = "rock";
  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);

  public BlockRock() {

    super(Material.GROUND);
    this.setHardness(0.1f);
    this.setSoundType(SoundType.STONE);
  }

  @Nonnull
  @Override
  public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {

    EnumType type = state.getValue(VARIANT);

    if (type == EnumType.SAND
        || type == EnumType.SAND_RED) {
      return SoundType.SAND;

    } else if (type == EnumType.DIRT
        || type == EnumType.WOOD_CHIPS) {
      return SoundType.GROUND;

    } else {
      return SoundType.STONE;
    }
  }

  @Override
  public boolean canHarvestBlock(IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {

    if (ModuleCoreConfig.TWEAKS.REQUIRE_SHOVEL_TO_PICKUP_WOOD_CHIPS) {
      IBlockState state = world.getBlockState(pos);

      if (state.getBlock() instanceof BlockRock
          && state.getValue(VARIANT) == EnumType.WOOD_CHIPS) {
        ItemStack itemMainhand = player.getHeldItemMainhand();
        Item item = itemMainhand.getItem();
        return item.getToolClasses(itemMainhand).contains("shovel");
      }
    }

    return super.canHarvestBlock(world, pos, player);
  }

  @Nullable
  @Override
  public String getHarvestTool(@Nonnull IBlockState state) {

    if (ModuleCoreConfig.TWEAKS.REQUIRE_SHOVEL_TO_PICKUP_WOOD_CHIPS
        && state.getBlock() instanceof BlockRock
        && state.getValue(VARIANT) == EnumType.WOOD_CHIPS) {
      return "shovel";
    }

    return super.getHarvestTool(state);
  }

  @Override
  public int getHarvestLevel(IBlockState state) {

    if (ModuleCoreConfig.TWEAKS.REQUIRE_SHOVEL_TO_PICKUP_WOOD_CHIPS
        && state.getBlock() instanceof BlockRock
        && state.getValue(VARIANT) == EnumType.WOOD_CHIPS) {
      return 0;
    }

    return super.getHarvestLevel(state);
  }

  // ---------------------------------------------------------------------------
  // - Placement
  // ---------------------------------------------------------------------------

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    return world.isSideSolid(pos.down(), EnumFacing.UP)
        && super.canPlaceBlockAt(world, pos);
  }

  @Override
  public boolean isReplaceable(IBlockAccess world, @Nonnull BlockPos pos) {

    return true;
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {

    if (!this.canPlaceBlockAt(world, pos)) {
      world.destroyBlock(pos, true);
    }
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

    return false;
  }

  @Override
  public boolean isFullBlock(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return this.isFullBlock(state);
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {

    return this.isFullBlock(state);
  }

  @Nonnull
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {

    return BlockFaceShape.UNDEFINED;
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {

    return NULL_AABB;
  }

  @Override
  public boolean isPassable(IBlockAccess world, BlockPos pos) {

    return true;
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Override
  public void getSubBlocks(
      CreativeTabs itemIn,
      NonNullList<ItemStack> list
  ) {

    for (EnumType type : EnumType.values()) {
      list.add(new ItemStack(this, 1, type.getMeta()));
    }
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, EnumType.fromMeta(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(VARIANT).getMeta();
  }

  @Override
  public int damageDropped(IBlockState state) {

    return this.getMetaFromState(state);
  }

  @Nonnull
  @Override
  public String getModelName(ItemStack itemStack) {

    return EnumType.fromMeta(itemStack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  public enum EnumType
      implements IVariant {

    STONE(0, NAME + "_stone"),
    GRANITE(1, NAME + "_granite"),
    DIORITE(2, NAME + "_diorite"),
    ANDESITE(3, NAME + "_andesite"),
    DIRT(4, NAME + "_dirt"),
    SAND(5, NAME + "_sand"),
    SANDSTONE(6, NAME + "_sandstone"),
    WOOD_CHIPS(7, NAME + "_wood_chips"),
    LIMESTONE(8, NAME + "_limestone"),
    SAND_RED(9, NAME + "_sand_red"),
    SANDSTONE_RED(10, NAME + "_sandstone_red");

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

    public ItemStack asStack() {

      return this.asStack(1);
    }

    public ItemStack asStack(int amount) {

      return new ItemStack(ModuleCore.Blocks.ROCK, amount, this.meta);
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

  }
}
