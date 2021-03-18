package com.codetaylor.mc.pyrotech.modules.core.item.spi;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class ItemBushSeedsBase
    extends Item {

  protected abstract boolean isValidBlock(IBlockState blockState);

  protected abstract IBlockState getPlacementState();

  @Nonnull
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack itemStack = player.getHeldItem(hand);
    IBlockState blockState = world.getBlockState(pos);

    if (facing == EnumFacing.UP
        && player.canPlayerEdit(pos.offset(facing), facing, itemStack)
        && this.isValidBlock(blockState)
        && world.isAirBlock(pos.up())) {

      world.setBlockState(pos.up(), this.getPlacementState());

      if (player instanceof EntityPlayerMP) {
        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos.up(), itemStack);
      }

      itemStack.shrink(1);
      return EnumActionResult.SUCCESS;

    } else {
      return EnumActionResult.FAIL;
    }
  }
}
