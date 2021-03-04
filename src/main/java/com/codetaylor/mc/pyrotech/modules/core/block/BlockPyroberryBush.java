package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockShearable;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleCombust;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockPyroberryBush
    extends BlockPartialBase
    implements IBlockShearable {

  public static final String NAME = "pyroberry_bush";

  private static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

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

  public BlockPyroberryBush() {

    super(Material.WOOD);
    this.setTickRandomly(true);
    this.setHardness(1);
    this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
  }

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

  public static boolean isValidBlock(IBlockState blockState) {

    return (blockState.getBlock() == Blocks.SAND);
  }

  @ParametersAreNonnullByDefault
  @Override
  public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {

    if (this.getAge(blockState) < 3) {
      return 0.1f;
    }

    return super.getBlockHardness(blockState, world, pos);
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

    return AABB[age];
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
        return AABB[4];
      case 5:
        return AABB[5];
      case 6:
        return AABB[6];
      case 7:
        return AABB[7];
      default:
        return super.getCollisionBoundingBox(blockState, world, pos);
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState blockState) {

    super.breakBlock(world, pos, blockState);

    if (!world.isRemote) {

      switch (this.getAge(blockState)) {
        case 0:
        case 1:
        case 2:
        case 3:
          break;
        case 4:
        case 5:
        case 6:
        case 7:
        default:
          this.spawnFireParticles(pos, world.provider.getDimension());
          world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1, 1);
      }

    }

    if (this.isMaxAge(blockState)) {
      world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.5f, true);
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (hand == EnumHand.MAIN_HAND) {
      ItemStack itemStack = player.getHeldItemMainhand();
      Item item = itemStack.getItem();

      if (item == Items.BLAZE_POWDER) {
        int age = this.getAge(blockState);

        if (age < this.getMaxAge()
            && ForgeHooks.onCropsGrowPre(world, pos, blockState, true)) {

          if (!player.isCreative() && !player.isSpectator()) {
            itemStack.setCount(itemStack.getCount() - 1);
          }

          world.setBlockState(pos, this.withAge(age + 1), 2);
          world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1, 1);
          this.spawnFireParticles(pos, world.provider.getDimension());
          ForgeHooks.onCropsGrowPost(world, pos, blockState, world.getBlockState(pos));

        } else if (this.isMaxAge(blockState)) {
          this.spawnFireParticles(pos, world.provider.getDimension());
          world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.5f, true);
        }

        return true;

      } else if (item == Items.AIR && this.isMaxAge(blockState)) {
        this.spawnFireParticles(pos, world.provider.getDimension());
        world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.5f, true);
        return true;
      }
    }

    return super.onBlockActivated(world, pos, blockState, player, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {

    super.onBlockClicked(world, pos, player);

    if (this.getAge(world.getBlockState(pos)) == this.getMaxAge()) {
      this.spawnFireParticles(pos, world.provider.getDimension());
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    super.onEntityWalk(world, pos, entity);

    if (RandomHelper.random().nextFloat() < 0.05
        && this.getAge(world.getBlockState(pos)) == this.getMaxAge()) {
      this.spawnFireParticles(pos, world.provider.getDimension());
    }
  }

  @Override
  public void onSheared(World world, BlockPos pos, IBlockState blockState, ItemStack itemStack, EntityPlayer player) {

    if (this.isMaxAge(blockState)) {
      world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1, 1);
      world.setBlockState(pos, this.withAge(this.getMaxAge() - 1), 3);
      itemStack.damageItem(1, player);
      this.spawnFireParticles(pos, player.dimension);
      world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1, 1);
      StackHelper.spawnStackOnTop(world, new ItemStack(ModuleCore.Items.PYROBERRIES, RandomHelper.random().nextInt(3) + 1), pos, 0.5);
    }
  }

  private void spawnFireParticles(BlockPos pos, int dimension) {

    ModuleCore.PACKET_SERVICE.sendToAllAround(new SCPacketParticleCombust(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.5, 0.5, 0.5), dimension, pos);
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Override
  public void neighborChanged(IBlockState blockState, World world, BlockPos pos, Block block, BlockPos fromPos) {

    this.checkAndDropBlock(world, pos);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState blockState, Random rand) {

    this.checkAndDropBlock(world, pos);

    if (world.canSeeSky(pos)) {

      if (world.isRainingAt(pos.up())) {
        // Reduce age if raining.
        int age = this.getAge(blockState);

        if (age > 4
            && ForgeHooks.onCropsGrowPre(world, pos, blockState, rand.nextFloat() < ModuleCoreConfig.PYROBERRY_BUSH.RAIN_GROWTH_REVERT_CHANCE)) {
          world.setBlockState(pos, this.withAge(age - 1), 2);
          ForgeHooks.onCropsGrowPost(world, pos, blockState, world.getBlockState(pos));
        }

        if (!world.isRemote) {

          for (int i = 0; i < 8; i++) {
            double offsetX = (RandomHelper.random().nextDouble() * 2.0 - 1.0) * 0.4;
            double offsetY = (RandomHelper.random().nextDouble() * 2.0 - 1.0) * 0.4;
            double offsetZ = (RandomHelper.random().nextDouble() * 2.0 - 1.0) * 0.4;
            double x = pos.getX() + 0.5 + offsetX;
            double y = pos.getY() + 0.6 + offsetY;
            double z = pos.getZ() + 0.5 + offsetZ;
            ((WorldServer) world).spawnParticle(RandomHelper.random().nextFloat() < 0.25 ? EnumParticleTypes.SMOKE_LARGE : EnumParticleTypes.SMOKE_NORMAL, x, y, z, 4, 0.0, 0.0, 0.0, 0.0);
          }

          world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 1);
        }

      } else if (world.isDaytime()) {
        // Increase age if is daytime.
        int age = this.getAge(blockState);
        boolean grew = false;

        if (age < this.getMaxAge()) {
          double chance = (age == this.getMaxAge() - 1) ? ModuleCoreConfig.PYROBERRY_BUSH.PYROBERRY_GROWTH_CHANCE : ModuleCoreConfig.PYROBERRY_BUSH.GROWTH_CHANCE;

          if (ForgeHooks.onCropsGrowPre(world, pos, blockState, rand.nextFloat() < chance)) {
            world.setBlockState(pos, this.withAge(age + 1), 2);
            grew = true;
            ForgeHooks.onCropsGrowPost(world, pos, blockState, world.getBlockState(pos));
          }
        }

        if ((age > 2 && grew) || (this.isMaxAge(blockState) && rand.nextFloat() < 0.5)) {
          this.spawnFireParticles(pos, world.provider.getDimension());
          world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1, 1);
        }
      }

    } else {
      // Reduce age if the plant can't see sky.
      int age = this.getAge(blockState);

      if (age > 4) {

        if (ForgeHooks.onCropsGrowPre(world, pos, blockState, rand.nextFloat() < ModuleCoreConfig.PYROBERRY_BUSH.OBSTRUCTED_GROWTH_REVERT_CHANCE)) {
          world.setBlockState(pos, this.withAge(age - 1), 2);
          ForgeHooks.onCropsGrowPost(world, pos, blockState, world.getBlockState(pos));
        }
      }
    }
  }

  protected void checkAndDropBlock(World world, BlockPos pos) {

    if (!BlockPyroberryBush.isValidBlock(world.getBlockState(pos.down()))) {
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
      return ModuleCore.Items.PYROBERRY_SEEDS;
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

    return new BlockStateContainer(this, AGE);
  }
}
