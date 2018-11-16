package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackCrudeRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileDryingRackBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileDryingRackCrude;
import net.minecraft.block.Block;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.stream.Stream;

public class BlockDryingRack
    extends Block
    implements IBlockVariant<BlockDryingRack.EnumType> {

  public static final String NAME = "drying_rack";

  public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0f / 16f, 0f / 16f, 0f / 16f, 16f / 16f, 12f / 16f, 16f / 16f);
  private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0f / 16f, 11f / 16f, 0f / 16f, 16f / 16f, 16f / 16f, 5f / 16f);
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
        .withProperty(VARIANT, EnumType.CRUDE));
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (state.getValue(VARIANT) == EnumType.NORMAL
        && facing != EnumFacing.UP) {
      return false;
    }

    int index = 0;

    if (state.getValue(VARIANT) == EnumType.NORMAL) {
      int x = (hitX < 0.5) ? 0 : 1;
      int y = (hitZ < 0.5) ? 0 : 1;
      index = x | (y << 1);
    }

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileDryingRackBase) {

      TileDryingRackBase dryingRack = (TileDryingRackBase) tileEntity;
      ItemStackHandler stackHandler = dryingRack.getStackHandler();
      ItemStackHandler outputStackHandler = dryingRack.getOutputStackHandler();
      ItemStack heldItemMainhand = player.getHeldItemMainhand();

      if (heldItemMainhand.isEmpty()) {

        // Remove input

        if (!stackHandler.getStackInSlot(index).isEmpty()) {

          ItemStack result = stackHandler.extractItem(index, 64, world.isRemote);

          if (!result.isEmpty()) {

            if (!world.isRemote) {
              StackHelper.addToInventoryOrSpawn(world, player, result, pos);
            }

            return true;
          }
        }

        // Remove output

        if (!outputStackHandler.getStackInSlot(index).isEmpty()) {

          ItemStack result = outputStackHandler.extractItem(index, 64, world.isRemote);

          if (!result.isEmpty()) {

            if (!world.isRemote) {
              StackHelper.addToInventoryOrSpawn(world, player, result, pos);
            }

            return true;
          }
        }

      } else {

        if (stackHandler.getStackInSlot(index).isEmpty()
            && outputStackHandler.getStackInSlot(index).isEmpty()) {

          // Insert item

          ItemStack itemStack = new ItemStack(heldItemMainhand.getItem(), 1, heldItemMainhand.getMetadata());

          boolean hasRecipe = this.hasRecipe(state, itemStack);

          if (hasRecipe) {

            // The item doesn't have a recipe, place it in the output slot.

            ItemStack result = outputStackHandler.insertItem(index, itemStack, world.isRemote);

            if (result.isEmpty()) {

              if (!world.isRemote) {
                heldItemMainhand.setCount(heldItemMainhand.getCount() - 1);
              }

              return true;
            }

          } else {

            // The item has a recipe, place it in the input slot.

            ItemStack result = stackHandler.insertItem(index, itemStack, world.isRemote);

            if (result.isEmpty()) {

              if (!world.isRemote) {
                heldItemMainhand.setCount(heldItemMainhand.getCount() - 1);
              }

              return true;
            }
          }
        }
      }

    }

    return true;
  }

  private boolean hasRecipe(IBlockState state, ItemStack itemStack) {

    Object recipe = null;

    if (state.getValue(VARIANT) == EnumType.CRUDE) {
      recipe = DryingRackCrudeRecipe.getRecipe(itemStack);

    } else if (state.getValue(VARIANT) == EnumType.NORMAL) {
      recipe = DryingRackRecipe.getRecipe(itemStack);
    }

    return (recipe == null);
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileDryingRackBase) {
      ((TileDryingRackBase) tileEntity).removeItems();
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

    return 150;
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
        && blockState.getValue(VARIANT) == EnumType.CRUDE) {
      horizontalFacing = blockState.getValue(BlockDryingRack.FACING);
    }

    return this.getDefaultState()
        .withProperty(VARIANT, EnumType.fromMeta(meta))
        .withProperty(FACING, horizontalFacing);
  }

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    return super.canPlaceBlockAt(world, pos);
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, BlockPos pos, EnumFacing side) {

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

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (state.getValue(VARIANT) == EnumType.CRUDE) {

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

    return (type == EnumType.CRUDE)
        || (type == EnumType.NORMAL);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    EnumType type = state.getValue(VARIANT);

    if (type == EnumType.CRUDE) {
      return new TileDryingRackCrude();

    } else if (type == EnumType.NORMAL) {
      return new TileDryingRack();
    }

    return null;
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

    return new BlockStateContainer(this, VARIANT, FACING);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    // 0-3 type 0
    // 4-7 type 1

    return this.getDefaultState()
        .withProperty(VARIANT, EnumType.fromMeta((meta >> 2) & 1))
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

    return EnumType.fromMeta(itemStack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  public enum EnumType
      implements IVariant {

    CRUDE(0, "crude"),
    NORMAL(1, "normal");

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
