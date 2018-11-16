package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemIgniterBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileKilnBrick;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileKilnBrickTop;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class BlockKilnBrick
    extends Block {

  public static final String NAME = "kiln_brick";

  public static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(1.0 / 16.0, 0.0 / 16.0, 1.0 / 16.0, 15.0 / 16.0, 8.0 / 16.0, 15.0 / 16.0);

  public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

  public BlockKilnBrick() {

    super(Material.ROCK);
    this.setSoundType(SoundType.STONE);
    this.setHardness(2);
    this.setHarvestLevel("pickaxe", 0);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(FACING, EnumFacing.NORTH)
        .withProperty(TYPE, EnumType.Bottom)
    );
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    if (state.getValue(TYPE) == EnumType.BottomLit) {
      return 10;
    }

    return super.getLightValue(state, world, pos);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    if (this.isTop(state)) {
      return AABB_TOP;
    }

    return super.getBoundingBox(state, source, pos);
  }

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
    return this.getDefaultState().withProperty(FACING, opposite);
  }

  private boolean isTop(IBlockState state) {

    return state.getValue(TYPE) == EnumType.Top;
  }

  @Override
  public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {

    if (this.isTop(state)) {

      return false;

    } else {

      if (face == EnumFacing.UP
          || face == state.getValue(FACING)) {
        return false;
      }

      return this.isFullBlock(state);
    }
  }

  @Override
  public boolean isFullBlock(IBlockState state) {

    return !this.isTop(state);
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

  @Override
  public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {

    return false;
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, FACING, TYPE);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    // N, S, W, E
    // 0, 1, 2, 3 = facing, no top
    // 4, 5, 6, 7 = facing, top

    int type = ((meta >> 2) & 3);
    int facingIndex = (meta & 3) + 2;

    return this.getDefaultState()
        .withProperty(FACING, EnumFacing.VALUES[facingIndex])
        .withProperty(TYPE, EnumType.fromMeta(type));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    EnumFacing facing = state.getValue(FACING);
    EnumType type = state.getValue(TYPE);

    int meta = facing.getIndex() - 2;
    meta |= (type.getMeta() << 2);
    return meta;
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

    return 0;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    if (this.isTop(state)) {
      return new TileKilnBrickTop();

    } else {
      return new TileKilnBrick();
    }
  }

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
      return this.onBlockActivatedTop(world, pos, state, player, hand, facing, hitX, hitY, hitZ);

    } else {
      return this.onBlockActivatedBottom(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }
  }

  private boolean onBlockActivatedTop(
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

    TileEntity tileEntity = world.getTileEntity(pos.down());

    if (!(tileEntity instanceof TileKilnBrick)) {
      return false;
    }

    TileKilnBrick tileKiln = (TileKilnBrick) tileEntity;
    ItemStack heldItem = player.getHeldItemMainhand();

    if (facing != state.getValue(FACING)) {
      return false;
    }

    if (heldItem.isEmpty()) {

      // remove recipe items and recipe results

      if (!world.isRemote) {

        ItemStackHandler stackHandler = tileKiln.getStackHandler();
        ItemStack itemStack = stackHandler.extractItem(0, stackHandler.getStackInSlot(0).getCount(), false);

        if (!itemStack.isEmpty()) {
          StackHelper.addToInventoryOrSpawn(world, player, itemStack, pos);
        }

        stackHandler = tileKiln.getOutputStackHandler();

        for (int i = 0; i < stackHandler.getSlots(); i++) {
          itemStack = stackHandler.extractItem(i, stackHandler.getStackInSlot(i).getCount(), false);

          if (!itemStack.isEmpty()) {
            StackHelper.addToInventoryOrSpawn(world, player, itemStack, pos);
          }
        }
      }

      return true;

    } else {

      int count = heldItem.getCount();
      ItemStackHandler stackHandler = tileKiln.getStackHandler();
      ItemStack itemStack = stackHandler.insertItem(0, heldItem, world.isRemote);

      if (itemStack.getCount() != count) {

        if (!world.isRemote) {
          player.setHeldItem(hand, itemStack);
        }
        return true;
      }
    }

    return true;
  }

  private boolean onBlockActivatedBottom(
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

    TileEntity tileEntity = world.getTileEntity(pos);

    if (!(tileEntity instanceof TileKilnBrick)) {
      return false;
    }

    TileKilnBrick tileKiln = (TileKilnBrick) tileEntity;
    ItemStack heldItem = player.getHeldItemMainhand();

    if (heldItem.getItem() instanceof ItemIgniterBase) {
      return false;
    }

    if (facing != state.getValue(FACING)) {
      return false;
    }

    if (heldItem.isEmpty()) {

      // remove fuel

      ItemStackHandler fuelStackHandler = tileKiln.getFuelStackHandler();
      ItemStack itemStack = fuelStackHandler.extractItem(0, 64, world.isRemote);

      if (!world.isRemote) {

        if (!itemStack.isEmpty()) {
          StackHelper.addToInventoryOrSpawn(world, player, itemStack, pos);
        }

        BlockHelper.notifyBlockUpdate(world, pos);
      }

      return true;

    } else {

      if (heldItem.getItem() == Items.FLINT_AND_STEEL) {

        // Handle ignition with flint and steel item.

        if (!world.isRemote) {

          if (player.isCreative()) {
            heldItem.damageItem(1, player);
          }

          tileKiln.setActive(true);
          world.playSound(
              null,
              pos,
              SoundEvents.ITEM_FLINTANDSTEEL_USE,
              SoundCategory.BLOCKS,
              1.0F,
              Util.RANDOM.nextFloat() * 0.4F + 0.8F
          );
        }
        return true;
      }

      // Insert fuel.

      if (StackHelper.isFuel(heldItem)) {

        int count = heldItem.getCount();
        ItemStackHandler stackHandler = tileKiln.getFuelStackHandler();
        ItemStack itemStack = stackHandler.insertItem(0, heldItem, world.isRemote);

        if (count != itemStack.getCount()) {

          if (!world.isRemote) {
            player.setHeldItem(hand, itemStack);
            BlockHelper.notifyBlockUpdate(world, pos);
          }
          return true;
        }
      }
    }

    return true;
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
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

    if (!this.isTop(state)) {

      BlockPos up = pos.up();

      if (super.canPlaceBlockAt(world, up)) {
        EnumFacing facing = state.getValue(FACING);
        world.setBlockState(up, this.getDefaultState()
            .withProperty(FACING, facing)
            .withProperty(TYPE, EnumType.Top));
      }
    }
  }

  @Override
  public boolean canPlaceBlockAt(World world, BlockPos pos) {

    return super.canPlaceBlockAt(world, pos)
        && super.canPlaceBlockAt(world, pos.up());
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    if (this.isTop(state)) {

      BlockPos down = pos.down();

      if (world.getBlockState(down).getBlock() == this) {
        world.destroyBlock(down, true);
      }

    } else {

      BlockPos up = pos.up();

      if (world.getBlockState(up).getBlock() == this) {
        world.setBlockToAir(up);
      }

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileKilnBrick) {
        TileKilnBrick tileKiln = (TileKilnBrick) tileEntity;
        tileKiln.spawnAllItemsOnTop();
      }
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    if (this.isTop(state)) {

      TileEntity tileEntity = world.getTileEntity(pos.down());

      if (tileEntity instanceof TileKilnBrick
          && ((TileKilnBrick) tileEntity).isActive()
          && ((TileKilnBrick) tileEntity).isFiring()) {

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.25;
        double centerZ = pos.getZ() + 0.5;

        world.spawnParticle(
            EnumParticleTypes.SMOKE_LARGE,
            centerX + (Util.RANDOM.nextDouble() - 0.5),
            centerY,
            centerZ + (Util.RANDOM.nextDouble() - 0.5),
            0,
            0.05 + (Util.RANDOM.nextFloat() * 2 - 1) * 0.05,
            0
        );
      }

    } else {

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileKilnBrick
          && ((TileKilnBrick) tileEntity).isActive()) {

        EnumFacing enumfacing = state.getValue(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
        double d2 = (double) pos.getZ() + 0.5D;
        double d4 = rand.nextDouble() * 0.6D - 0.3D;

        if (rand.nextDouble() < 0.1D) {
          world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }

        double offset = 0.25;

        switch (enumfacing) {
          case WEST:
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - offset, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, d0 - offset, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            break;
          case EAST:
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + offset, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, d0 + offset, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            break;
          case NORTH:
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - offset, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - offset, 0.0D, 0.0D, 0.0D);
            break;
          case SOUTH:
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + offset, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + offset, 0.0D, 0.0D, 0.0D);
        }
      }
    }
  }

  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {

    if (state.getValue(TYPE) == EnumType.Bottom) {

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileKilnBrick
          && ((TileKilnBrick) tileEntity).getRemainingBurnTimeTicks() > 0) {

        return state.withProperty(TYPE, EnumType.BottomDormant);
      }
    }

    return state;
  }

  public enum EnumType
      implements IVariant {

    Top(0, "top"),
    Bottom(1, "bottom"),
    BottomLit(2, "bottom_lit"),
    BottomDormant(3, "bottom_dormant");

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
