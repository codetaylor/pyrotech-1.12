package com.codetaylor.mc.pyrotech.modules.storage.block.spi;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.block.item.ItemBlockBag;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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

public abstract class BlockBagBase
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

  public static final AxisAlignedBB AABB_NORTH = AABBHelper.create(3, 0, 5, 13, 10, 11);
  private static final AxisAlignedBB AABB_WEST = AABBHelper.rotateHorizontalCCW90Centered(AABB_NORTH);
  private static final AxisAlignedBB AABB_SOUTH = AABBHelper.rotateHorizontalCCW90Centered(AABB_WEST);
  private static final AxisAlignedBB AABB_EAST = AABBHelper.rotateHorizontalCCW90Centered(AABB_SOUTH);

  public BlockBagBase() {

    super(Material.CLOTH);
    this.setHardness(0.2f);
    this.setSoundType(SoundType.CLOTH);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.NORTH)
        .withProperty(TYPE, EnumType.CLOSED));
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public abstract int getItemCapacity();

  protected abstract String[] getItemWhitelist();

  protected abstract String[] getItemBlacklist();

  public abstract boolean allowAutoPickupMainhand();

  public abstract boolean allowAutoPickupOffhand();

  public abstract boolean allowAutoPickupHotbar();

  public abstract boolean allowAutoPickupInventory();

  public boolean isOpen(IBlockState blockState) {

    return (blockState.getValue(TYPE) == EnumType.OPEN);
  }

  public boolean isItemValidForInsertion(ItemStack itemStack) {

    ResourceLocation registryName = itemStack.getItem().getRegistryName();

    if (registryName == null) {
      return false;
    }

    String[] itemWhitelist = this.getItemWhitelist();
    String[] itemBlacklist = getItemBlacklist();

    if (itemWhitelist.length > 0) {

      for (String itemString : itemWhitelist) {

        try {
          ParseResult parseResult = RecipeItemParser.INSTANCE.parse(itemString);

          if (parseResult.matches(itemStack, true)) {
            return true;
          }

        } catch (MalformedRecipeItemException e) {
          ModuleStorage.LOGGER.error("Error parsing config string for valid bag item " + itemString, e);
        }
      }

      return false;

    } else if (itemBlacklist.length > 0) {

      for (String itemString : itemBlacklist) {

        try {
          ParseResult parseResult = RecipeItemParser.INSTANCE.parse(itemString);

          if (parseResult.matches(itemStack, true)) {
            return false;
          }

        } catch (MalformedRecipeItemException e) {
          ModuleStorage.LOGGER.error("Error parsing config string for valid bag item " + itemString, e);
        }
      }
    }

    return true;
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @SuppressWarnings("deprecation")
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
    ItemStack heldItem = placer.getHeldItem(hand);

    return this.getDefaultState()
        .withProperty(Properties.FACING_HORIZONTAL, opposite)
        .withProperty(TYPE, (heldItem.getMetadata() == 0) ? EnumType.CLOSED : EnumType.OPEN);
  }

  @Override
  public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {

    // Delay the destruction of the TE until after #getDrops is called. We need
    // access to the TE while creating the dropped item in order to serialize it.
    return willHarvest || super.removedByPlayer(state, world, pos, player, false);
  }

  @Override
  public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, ItemStack stack) {

    super.harvestBlock(world, player, pos, state, te, stack);

    if (!world.isRemote) {
      world.setBlockToAir(pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {

    // Called before #breakBlock
    TileEntity tileEntity = world.getTileEntity(pos);
    ItemStack itemStack = new ItemStack(this.getBlock(), 1, this.damageDropped(state));

    if (tileEntity instanceof TileBagBase) {

      TileBagBase.StackHandler tileStackHandler = ((TileBagBase) tileEntity).getStackHandler();

      for (int i = 0; i < tileStackHandler.getSlots(); i++) {
        ItemBlockBag.insertItem(itemStack, tileStackHandler.getStackInSlot(i), false);
      }
    }

    drops.add(itemStack);
  }

  protected abstract Block getBlock();

  // ---------------------------------------------------------------------------
  // - Bounds
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    switch (state.getValue(Properties.FACING_HORIZONTAL)) {

      case NORTH:
        return AABB_NORTH;
      case WEST:
        return AABB_WEST;
      case SOUTH:
        return AABB_SOUTH;
      case EAST:
        return AABB_EAST;
    }

    return super.getBoundingBox(state, source, pos);
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

    return this.createTileEntity();
  }

  protected abstract TileEntity createTileEntity();

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, TYPE, Properties.FACING_HORIZONTAL);
  }

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

  @Override
  public int damageDropped(IBlockState state) {

    return this.isOpen(state) ? 1 : 0;
  }

  public enum EnumType
      implements IVariant {

    CLOSED(0, "closed"),
    OPEN(1, "open");

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
