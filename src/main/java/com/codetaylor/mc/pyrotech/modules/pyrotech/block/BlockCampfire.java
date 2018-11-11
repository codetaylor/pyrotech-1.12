package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.inventory.LIFOStackHandler;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCampfire;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class BlockCampfire
    extends Block {

  public static final String NAME = "campfire";

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  public static final PropertyInteger WOOD = PropertyInteger.create("wood", 0, 8);

  public static final AxisAlignedBB AABB_FULL = new AxisAlignedBB(0, 0, 0, 1, 6f / 16f, 1);
  public static final AxisAlignedBB AABB_TINDER = new AxisAlignedBB(4f / 16f, 0, 4f / 16f, 12f / 16f, 5f / 16f, 12f / 16f);
  public static final AxisAlignedBB AABB_ASH = new AxisAlignedBB(3f / 16f, 0, 3f / 16f, 13f / 16f, 1f / 16f, 13f / 16f);

  public BlockCampfire() {

    super(Material.WOOD);
  }

  @Nonnull
  @Override
  public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {

    IBlockState actualState = this.getActualState(state, world, pos);

    if (actualState.getValue(WOOD) > 0) {
      return SoundType.WOOD;

    } else if (actualState.getValue(VARIANT) == EnumType.ASH) {
      return SoundType.SAND;

    } else {
      return SoundType.PLANT;
    }
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    IBlockState actualState = this.getActualState(state, world, pos);

    if (actualState.getValue(VARIANT) == EnumType.LIT) {
      return 15;
    }

    return super.getLightValue(state, world, pos);
  }

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  @Override
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {

    return true;
  }

  @Nonnull
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {

    return BlockFaceShape.UNDEFINED;
  }

  @Override
  public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {

    return false;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    IBlockState actualState = this.getActualState(state, source, pos);

    if (actualState.getValue(WOOD) > 0) {
      return AABB_FULL;

    } else if (actualState.getValue(VARIANT) == EnumType.NORMAL
        || actualState.getValue(VARIANT) == EnumType.LIT) {
      return AABB_TINDER;

    } else if (actualState.getValue(VARIANT) == EnumType.ASH) {
      return AABB_ASH;
    }

    return super.getBoundingBox(state, source, pos);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (!(tileEntity instanceof TileCampfire)) {
      return false;
    }

    TileCampfire campfire = (TileCampfire) tileEntity;

    if (campfire.isDead()) {
      return false;
    }

    ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);

    if (heldItem.isEmpty()) {
      return this.handleInteraction_EmptyHand(world, pos, player, campfire);

    } else if (heldItem.getItem() == Items.FLINT_AND_STEEL) {
      return this.handleInteraction_FlintAndSteel(world, pos, player, campfire, heldItem);

    } else if (heldItem.getItem() instanceof ItemFood) {
      return this.handleInteraction_Food(world, campfire, heldItem);

    } else {
      return this.handleInteraction_Wood(world, pos, campfire, heldItem);
    }
  }

  private boolean handleInteraction_Wood(World world, BlockPos pos, TileCampfire campfire, ItemStack heldItem) {

    int logWood = OreDictionary.getOreID("logWood");
    int[] oreIDs = OreDictionary.getOreIDs(heldItem);

    for (int oreID : oreIDs) {

      if (oreID == logWood) {
        LIFOStackHandler fuelStackHandler = campfire.getFuelStackHandler();

        if (!world.isRemote) {
          int firstEmptyIndex = fuelStackHandler.getFirstEmptyIndex();

          if (firstEmptyIndex > -1) {

            if (!player.isCreative()) {
              heldItem.setCount(heldItem.getCount() - 1);
            }

            fuelStackHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1, heldItem.getMetadata()), false);
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
            BlockHelper.notifyBlockUpdate(world, pos);
          }
        }

        return true;
      }
    }

    return false;
  }

  private boolean handleInteraction_Food(World world, TileCampfire campfire, ItemStack heldItem) {

    ItemStack recipeResult = FurnaceRecipes.instance().getSmeltingResult(heldItem);

    if (recipeResult.isEmpty()) {
      return false;
    }

    ItemStack output = campfire.getOutputStackHandler().getStackInSlot(0);

    if (!output.isEmpty()) {
      // Only allow input if the output has been removed.
      return false;
    }

    ItemStackHandler inputHandler = campfire.getStackHandler();
    ItemStack result = inputHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1, heldItem.getMetadata()), world.isRemote);

    if (result.isEmpty()) {

      if (!world.isRemote) {
        heldItem.setCount(heldItem.getCount() - 1);
      }

      return true;
    }

    return false;
  }

  private boolean handleInteraction_FlintAndSteel(World world, BlockPos pos, EntityPlayer player, TileCampfire campfire, ItemStack heldItem) {

    if (!world.isRemote) {

      if (player.isCreative()) {
        heldItem.damageItem(1, player);
      }

      campfire.setActive(true);
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

  private boolean handleInteraction_EmptyHand(World world, BlockPos pos, EntityPlayer player, TileCampfire campfire) {

    //if (player.isSneaking()) {

      ItemStack itemStack = campfire.getStackHandler().extractItem(0, 64, world.isRemote);

      if (!itemStack.isEmpty()) {

        if (!world.isRemote) {
          StackHelper.spawnStackOnTop(world, itemStack, pos, -0.125);
        }

        return true;
      }

      itemStack = campfire.getOutputStackHandler().extractItem(0, 64, world.isRemote);

      if (!itemStack.isEmpty()) {

        if (!world.isRemote) {
          StackHelper.spawnStackOnTop(world, itemStack, pos, -0.125);
        }

        return true;
      }
    //}

    return false;
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    return 0;
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, WOOD, VARIANT);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState();
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return 0;
  }

  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCampfire) {

      TileCampfire tileCampfire = (TileCampfire) tileEntity;
      int fuelRemaining = tileCampfire.getFuelRemaining();
      EnumType type = tileCampfire.getState();

      return state
          .withProperty(WOOD, fuelRemaining)
          .withProperty(VARIANT, type);
    }

    return super.getActualState(state, world, pos);
  }

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    return world.isSideSolid(pos.down(), EnumFacing.UP)
        && super.canPlaceBlockAt(world, pos);
  }

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

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileCampfire();
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    if (!entity.isImmuneToFire()
        && entity instanceof EntityLivingBase
        && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entity)) {
      entity.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
    }

    super.onEntityWalk(world, pos, entity);
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCampfire) {
      ((TileCampfire) tileEntity).removeItems();
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCampfire
        && ((TileCampfire) tileEntity).isActive()) {

      double x = (double) pos.getX() + 0.5;
      double y = (double) pos.getY() + (4.0 / 16.0) + (rand.nextDouble() * 2.0 / 16.0);
      double z = (double) pos.getZ() + 0.5;

      if (rand.nextDouble() < 0.1) {
        world.playSound((double) pos.getX() + 0.5, (double) pos.getY(), (double) pos.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
      }

      for (int i = 0; i < 4; i++) {
        double offsetX = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
        double offsetZ = (rand.nextDouble() * 2.0 - 1.0) * 0.2;
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
      }
    }
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    if (world.isRemote) {
      return;
    }

    if (!world.isSideSolid(pos.down(), EnumFacing.UP)) {
      world.destroyBlock(pos, false);
    }
  }

  public enum EnumType
      implements IVariant {

    NORMAL(0, "normal"),
    LIT(1, "lit"),
    ASH(2, "ash");

    private static final BlockTarDrain.EnumType[] META_LOOKUP = Stream.of(BlockTarDrain.EnumType.values())
        .sorted(Comparator.comparing(BlockTarDrain.EnumType::getMeta))
        .toArray(BlockTarDrain.EnumType[]::new);

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

    public static BlockTarDrain.EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

  }
}
