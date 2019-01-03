package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class InteractionUseItemBase<T extends TileEntity & ITileInteractable>
    extends InteractionBase<T> {

  public InteractionUseItemBase(EnumFacing[] sides, AxisAlignedBB bounds) {

    super(sides, bounds);
  }

  @Override
  public boolean allowInteractionWithType(EnumType type) {

    return (type == EnumType.MouseClick);
  }

  @Override
  public boolean interact(EnumType type, T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (this.allowInteractionWithHand(hand)
        && this.allowInteractionWithType(type)
        && this.allowInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ)) {

      boolean complete = this.doInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);

      if (complete) {
        this.postInteraction(tile, world, hitPos, state, player, hand, hitSide, hitX, hitY, hitZ);
      }

      return complete;
    }

    return false;
  }

  protected void postInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (!world.isRemote
        && !player.isCreative()) {
      this.applyItemDamage(player.getHeldItem(hand), player);
    }
  }

  protected void applyItemDamage(ItemStack itemStack, EntityPlayer player) {

    itemStack.damageItem(this.getItemDamage(itemStack), player);
  }

  /**
   * The damage returned will be applied to the item used.
   * <p>
   * NOTE: Don't damage the item in this method, it will be damaged by the
   * calling code.
   *
   * @param itemStack the itemStack that will be damaged
   * @return the damage to be applied to the item used
   */
  protected int getItemDamage(ItemStack itemStack) {

    return 1;
  }

  protected abstract boolean allowInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ);

  protected abstract boolean doInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ);

}
