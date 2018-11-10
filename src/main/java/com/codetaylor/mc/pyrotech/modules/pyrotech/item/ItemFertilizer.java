package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFertilizer
    extends Item {

  public static final String NAME = "fertilizer";

  @Override
  public EnumActionResult onItemUse(
      EntityPlayer player,
      World worldIn,
      BlockPos pos,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    ItemStack heldItem = player.getHeldItem(hand);

    if (!player.canPlayerEdit(pos.offset(facing), facing, heldItem)) {
      return EnumActionResult.FAIL;

    } else {

      if (ItemDye.applyBonemeal(heldItem, worldIn, pos, player, hand)) {

        if (!worldIn.isRemote) {
          worldIn.playEvent(2005, pos, 0);
        }

        return EnumActionResult.SUCCESS;
      }
    }

    return EnumActionResult.PASS;
  }
}
