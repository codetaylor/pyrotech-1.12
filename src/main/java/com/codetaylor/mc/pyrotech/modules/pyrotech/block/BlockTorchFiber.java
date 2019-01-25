package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemIgniterBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileTorch;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
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
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class BlockTorchFiber
    extends BlockTorch
    implements IBlockInteractable {

  public static final String NAME = "torch";

  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

  protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(6.0 / 16.0, 0.0, 6.0 / 16.0, 10.0 / 16.0, 12.0 / 16.0, 10.0 / 16.0);
  protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 14.0 / 16.0, 1.0D);
  protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 14.0 / 16.0, 0.30000001192092896D);
  protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 14.0 / 16.0, 0.6499999761581421D);
  protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 14.0 / 16.0, 0.6499999761581421D);

  public BlockTorchFiber() {

    super();
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(FACING, EnumFacing.UP)
        .withProperty(TYPE, BlockTorchFiber.EnumType.UNLIT)
    );
  }

  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    switch (state.getValue(FACING)) {
      case EAST:
        return TORCH_EAST_AABB;
      case WEST:
        return TORCH_WEST_AABB;
      case SOUTH:
        return TORCH_SOUTH_AABB;
      case NORTH:
        return TORCH_NORTH_AABB;
      default:
        return STANDING_AABB;
    }
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    IBlockState actualState = this.getActualState(state, world, pos);
    EnumType type = actualState.getValue(TYPE);

    if (type == BlockTorchFiber.EnumType.LIT) {
      return 9;
    }

    return super.getLightValue(state, world, pos);
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    TileEntity tile = world.getTileEntity(pos);

    if (tile instanceof TileTorch) {
      ((TileTorch) tile).update();
    }
  }

  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    if (this.getActualState(state, world, pos).getValue(TYPE) == BlockTorchFiber.EnumType.LIT) {

      EnumFacing enumfacing = state.getValue(FACING);
      double x = (double) pos.getX() + 0.5D + (rand.nextDouble() * 2 - 1) * 0.1;
      double y = (double) pos.getY() + 0.7D + (2.0 / 16.0);
      double z = (double) pos.getZ() + 0.5D + (rand.nextDouble() * 2 - 1) * 0.1;

      if (enumfacing.getAxis().isHorizontal()) {
        EnumFacing enumfacing1 = enumfacing.getOpposite();
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.17D * (double) enumfacing1.getFrontOffsetX(), y + 0.22D, z + 0.17D * (double) enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D);
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.17D * (double) enumfacing1.getFrontOffsetX(), y + 0.22D, z + 0.17D * (double) enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D);

      } else {
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
        world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
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

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileTorch();
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Override
  public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState) {

    if (entity == null) {
      super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, null, isActualState);
    }
  }

  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {

    return this.getBoundingBox(blockState, world, pos);
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

    if (heldItem.getItem() instanceof ItemIgniterBase) {
      return false;
    }

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    IBlockState actualState = this.getActualState(state, world, pos);
    EnumType type = actualState.getValue(BlockTorchFiber.TYPE);

    if (type == BlockTorchFiber.EnumType.UNLIT) {
      super.getDrops(drops, world, pos, state, fortune);

    } else {

      if (Math.random() > 0.5) {
        drops.add(new ItemStack(Items.STICK));

      } else {
        drops.add(ItemMaterial.EnumType.PLANT_FIBERS_DRIED.asStack());
      }
    }
  }

  @Override
  public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {

    // Delay the destruction of the TE until after #getDrops is called. We need
    // access to the TE while creating the dropped item.
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
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, FACING, TYPE);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {

    TileEntity tile = world.getTileEntity(pos);

    if (tile instanceof TileTorch) {
      return state.withProperty(TYPE, ((TileTorch) tile).getType());
    }

    return state;
  }

  public enum EnumType
      implements IVariant {

    LIT(0, "lit"),
    UNLIT(1, "unlit"),
    DOUSED(2, "doused");

    private static final EnumType[] META_LOOKUP = Stream.of(BlockTorchFiber.EnumType.values())
        .sorted(Comparator.comparing(BlockTorchFiber.EnumType::getMeta))
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
