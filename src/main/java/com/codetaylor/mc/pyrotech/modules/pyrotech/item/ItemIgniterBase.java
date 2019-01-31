package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableWithIgniterItem;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.util.RefractoryIgnitionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class ItemIgniterBase
    extends Item {

  @Nonnull
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {

    return EnumAction.BOW;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    RayTraceResult rayTraceResult = this.rayTrace(world, player, false);
    ItemStack heldItem = player.getHeldItem(hand);

    // The ray trace result can be null
    //noinspection ConstantConditions
    if (rayTraceResult != null
        && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {

      player.setActiveHand(hand);
      return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);

    } else {
      return new ActionResult<>(EnumActionResult.FAIL, heldItem);
    }
  }

  @Override
  public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {

    if (!(player instanceof EntityPlayer)) {
      return;
    }

    World world = player.world;
    RayTraceResult rayTraceResult = this.rayTrace(world, (EntityPlayer) player, false);

    // The ray trace result can be null
    //noinspection ConstantConditions
    if (rayTraceResult == null
        || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {

      player.stopActiveHand();
      return;
    }

    if (world.isRemote) {

      Vec3d hit = rayTraceResult.hitVec;
      world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, hit.x, hit.y, hit.z, 0, 0, 0);
    }
  }

  @Nonnull
  @Override
  public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, EntityLivingBase player) {

    RayTraceResult rayTraceResult = this.rayTrace(world, (EntityPlayer) player, false);

    // The ray trace result can be null
    //noinspection ConstantConditions
    if (rayTraceResult == null
        || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {

      player.stopActiveHand();
      return stack;
    }

    BlockPos pos = rayTraceResult.getBlockPos();
    EnumFacing facing = rayTraceResult.sideHit;
    BlockPos offset = pos.offset(facing);
    IBlockState blockState = world.getBlockState(pos);
    Block block = blockState.getBlock();

    if (block instanceof IBlockIgnitableWithIgniterItem) {

      if (!world.isRemote) {
        ((IBlockIgnitableWithIgniterItem) block).igniteWithIgniterItem(world, pos, blockState, facing);
      }

      world.playSound(
          null,
          offset,
          SoundEvents.ITEM_FLINTANDSTEEL_USE,
          SoundCategory.BLOCKS,
          1.0F,
          Util.RANDOM.nextFloat() * 0.4F + 0.8F
      );

      this.damageItem(stack, player);

    } else if (Util.canSetFire(world, offset)) {

      if (!world.isRemote) {
        world.setBlockState(offset, Blocks.FIRE.getDefaultState(), 3);
      }

      world.playSound(
          null,
          offset,
          SoundEvents.ITEM_FLINTANDSTEEL_USE,
          SoundCategory.BLOCKS,
          1.0F,
          Util.RANDOM.nextFloat() * 0.4F + 0.8F
      );

      this.damageItem(stack, player);

    } else {

      if (!world.isRemote) {
        RefractoryIgnitionHelper.igniteBlocks(world, pos);
      }
    }

    return stack;
  }

  protected abstract void damageItem(@Nonnull ItemStack stack, EntityLivingBase player);
}
