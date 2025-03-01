package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableAdjacentFire;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableAdjacentIgniterBlock;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.ignition.item.ItemIgniterBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileKilnPit;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class BlockKilnPit
    extends BlockPartialBase
    implements IBlockVariant<BlockKilnPit.EnumType>,
    IBlockInteractable,
    IBlockIgnitableAdjacentFire,
    IBlockIgnitableAdjacentIgniterBlock {

  public static final String NAME = "kiln_pit";

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  public static final AxisAlignedBB AABB_EMPTY = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
  public static final AxisAlignedBB AABB_THATCH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 10D / 16D, 1.0D);

  public BlockKilnPit() {

    super(Material.WOOD);
    this.setHardness(0.6f);
    this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockKilnPit.EnumType.EMPTY));
  }

  @Nullable
  @Override
  public String getHarvestTool(IBlockState state) {

    return "shovel";
  }

  @Nonnull
  @Override
  public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {

    switch (state.getValue(VARIANT)) {
      case EMPTY:
        return SoundType.GROUND;
      case THATCH:
        return SoundType.PLANT;
      case WOOD:
        return SoundType.WOOD;
      case ACTIVE:
        return SoundType.WOOD;
      case COMPLETE:
        return SoundType.SAND;
      default:
        return SoundType.WOOD;
    }
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (state.getValue(VARIANT) == BlockKilnPit.EnumType.EMPTY) {
      return AABB_EMPTY;
    }

    return FULL_BLOCK_AABB;
  }

  @Override
  public boolean isFullBlock(IBlockState state) {

    EnumType type = state.getValue(VARIANT);
    return type == BlockKilnPit.EnumType.WOOD || type == BlockKilnPit.EnumType.ACTIVE || type == BlockKilnPit.EnumType.COMPLETE;
  }

  @Override
  public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {

    EnumType type = state.getValue(VARIANT);

    if (face == EnumFacing.DOWN) {
      return !(type == BlockKilnPit.EnumType.EMPTY);

    } else {
      return this.isFullBlock(state);
    }
  }

  @Override
  public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {

    EnumType type = state.getValue(VARIANT);

    if (face == EnumFacing.DOWN) {
      return !(type == BlockKilnPit.EnumType.EMPTY);

    } else {
      return this.isFullBlock(state);
    }
  }

  @Override
  public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {

    return world.getBlockState(pos).getValue(VARIANT) == BlockKilnPit.EnumType.ACTIVE;
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, BlockKilnPit.EnumType.fromMeta(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(VARIANT).getMeta();
  }

  @Override
  public int damageDropped(IBlockState state) {

    return BlockKilnPit.EnumType.EMPTY.getMeta();
  }

  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {

    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof TileKilnPit) {
      ((TileKilnPit) tileEntity).setNeedStructureValidation();
    }
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

    return 0;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState) {

    if (state.getValue(VARIANT) == EnumType.THATCH) {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_THATCH);

    } else {
      super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity, isActualState);
    }
  }

  // ---------------------------------------------------------------------------
  // - Ignition
  // ---------------------------------------------------------------------------

  @Override
  public void igniteWithAdjacentFire(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    if (facing == EnumFacing.UP
        && blockState.getValue(BlockKilnPit.VARIANT) == BlockKilnPit.EnumType.WOOD) {

      world.setBlockState(pos, blockState.withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.ACTIVE));
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileKilnPit) {
        ((TileKilnPit) tileEntity).setActive(true);
      }
    }
  }

  @Override
  public void igniteWithAdjacentIgniterBlock(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    if (blockState.getValue(BlockKilnPit.VARIANT) == BlockKilnPit.EnumType.WOOD
        && Util.canSetFire(world, pos.up())) {

      world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState(), 3);
    }
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
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TileKilnPit();
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

    ItemStack heldItem = player.getHeldItemMainhand();

    Item item = heldItem.getItem();

    if (item instanceof ItemIgniterBase
        || item == Items.FLINT_AND_STEEL
        || item == Items.FIRE_CHARGE) {
      return false;
    }

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileKilnPit) {
      TileKilnPit tileKiln = (TileKilnPit) tileEntity;
      ItemStackHandler stackHandler = tileKiln.getStackHandler();
      StackHelper.spawnStackOnTop(world, stackHandler.getStackInSlot(0), pos);
      stackHandler = tileKiln.getOutputStackHandler();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        StackHelper.spawnStackOnTop(world, stackHandler.getStackInSlot(i), pos);
      }

      // Pop the used wood into the world.
      stackHandler = tileKiln.getLogStackHandler();
      StackHelper.spawnStackHandlerContentsOnTop(world, stackHandler, pos);
    }

    super.breakBlock(world, pos, state);
  }

  @Nonnull
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.SOLID;
  }

  @Override
  public boolean canSilkHarvest(
      World world, BlockPos pos, IBlockState state, EntityPlayer player
  ) {

    return false;
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    EnumType type = state.getValue(VARIANT);

    if (type == BlockKilnPit.EnumType.COMPLETE
        || type == BlockKilnPit.EnumType.ACTIVE) {
      return 0;
    }

    return super.quantityDropped(state, fortune, random);
  }

  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    // TODO: spawn directly in world?

    EnumType type = state.getValue(VARIANT);

    if (type == BlockKilnPit.EnumType.WOOD
        || type == BlockKilnPit.EnumType.THATCH) {
      drops.add(new ItemStack(ModuleCore.Blocks.THATCH, 1, 0));
    }

    super.getDrops(drops, world, pos, state, fortune);
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public String getModelName(ItemStack stack) {

    return BlockKilnPit.EnumType.fromMeta(stack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  public enum EnumType
      implements IVariant {

    EMPTY(0, "empty"),
    THATCH(1, "thatch"),
    WOOD(2, "wood"),
    ACTIVE(3, "active"),
    COMPLETE(4, "complete");

    private static final EnumType[] META_LOOKUP = Stream.of(BlockKilnPit.EnumType.values())
        .sorted(Comparator.comparing(BlockKilnPit.EnumType::getMeta))
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
