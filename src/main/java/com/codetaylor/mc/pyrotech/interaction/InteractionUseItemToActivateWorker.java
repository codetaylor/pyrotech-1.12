package com.codetaylor.mc.pyrotech.interaction;

import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionUseItemBase;
import com.codetaylor.mc.pyrotech.library.spi.tile.ITileWorker;
import com.codetaylor.mc.pyrotech.library.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InteractionUseItemToActivateWorker<T extends TileEntity & ITileInteractable & ITileWorker>
    extends InteractionUseItemBase<T> {

  private final Item item;

  public InteractionUseItemToActivateWorker(Item item, EnumFacing[] sides, AxisAlignedBB bounds) {

    super(sides, bounds);
    this.item = item;
  }

  @Override
  protected boolean allowInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    return (player.getHeldItem(hand).getItem() == this.item);
  }

  @Override
  protected boolean doInteraction(T tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (!world.isRemote) {
      tile.workerSetActive(true);

      world.playSound(
          null,
          hitPos,
          SoundEvents.ITEM_FLINTANDSTEEL_USE,
          SoundCategory.BLOCKS,
          1.0F,
          Util.RANDOM.nextFloat() * 0.4F + 0.8F
      );
    }

    return true;
  }
}
