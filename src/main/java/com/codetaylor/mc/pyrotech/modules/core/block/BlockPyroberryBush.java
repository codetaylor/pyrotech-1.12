package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockShearable;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockBushBase;
import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleCombust;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockPyroberryBush
    extends BlockBushBase
    implements IBlockShearable {

  public static final String NAME = "pyroberry_bush";

  // ---------------------------------------------------------------------------
  // - Implementation
  // ---------------------------------------------------------------------------

  @Override
  public boolean isValidBlock(IBlockState blockState) {

    return (blockState.getMaterial() == Material.SAND);
  }

  @Override
  public Item getSeedItem() {

    return ModuleCore.Items.PYROBERRY_SEEDS;
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

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
}
