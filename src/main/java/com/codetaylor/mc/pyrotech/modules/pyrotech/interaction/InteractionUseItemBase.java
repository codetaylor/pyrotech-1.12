package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class InteractionUseItemBase<T extends TileEntity & ITileInteractable>
    extends InteractionBase<T> {

  public InteractionUseItemBase(EnumFacing[] sides, InteractionBounds bounds) {

    super(sides, bounds);
  }

  @Override
  public boolean interact(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = player.getHeldItemMainhand();

    if (!this.isItemStackValid(heldItem)) {
      return false;
    }

    if (!world.isRemote) {

      int itemDamage = this.getItemDamage(heldItem);

      if (itemDamage > 0
          && !player.isCreative()) {
        heldItem.damageItem(itemDamage, player);
      }
    }

    return this.doInteraction(tile, world, hitPos, player, hand, hitSide, hitX, hitY, hitZ);
  }

  protected int getItemDamage(ItemStack itemStack) {

    return 1;
  }

  protected abstract boolean doInteraction(T tile, World world, BlockPos hitPos, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ);

  protected abstract boolean isItemStackValid(ItemStack itemStack);

}
