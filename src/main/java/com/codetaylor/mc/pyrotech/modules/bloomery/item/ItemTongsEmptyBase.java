package com.codetaylor.mc.pyrotech.modules.bloomery.item;

import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionItem;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.util.BloomHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileGraniteAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class ItemTongsEmptyBase
    extends Item
    implements IInteractionItem {

  private final Supplier<ItemTongsFullBase> otherTongsSupplier;

  /* package */ ItemTongsEmptyBase(Supplier<ItemTongsFullBase> otherTongsSupplier, int durability) {

    this.otherTongsSupplier = otherTongsSupplier;

    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }

  public ItemTongsFullBase getItemTongsFull() {

    return this.otherTongsSupplier.get();
  }

  @Override
  public boolean isEnchantable(@Nonnull ItemStack stack) {

    return true;
  }

  @Override
  public boolean allowInteraction(TileEntity tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (tile instanceof TileGraniteAnvil) {
      return ((TileGraniteAnvil) tile).getStackHandler().getStackInSlot(0).getItem() == ModuleBloomery.Items.BLOOM;

    } else if (tile instanceof TileBloomery) {
      return ((TileBloomery) tile).getOutputStackHandler().getStackInSlot(0).getItem() == ModuleBloomery.Items.BLOOM;
    }

    return tile instanceof TileBloom;
  }

  @Override
  public boolean doInteraction(TileEntity tile, World world, ItemStack heldItem, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (tile instanceof TileGraniteAnvil) {
      ItemStack bloomStack = ((TileGraniteAnvil) tile).getStackHandler().extractItem(0, 1, false);
      ItemStack tongsFull = BloomHelper.createItemTongsFull(heldItem, bloomStack);
      heldItem.shrink(1);
      ItemHandlerHelper.giveItemToPlayer(player, tongsFull, player.inventory.currentItem);
      return true;

    } else if (tile instanceof TileBloomery) {

      ItemStack bloomStack = ((TileBloomery) tile).getOutputStackHandler().extractItem(0, 1, false);
      ItemStack tongsFull = BloomHelper.createItemTongsFull(heldItem, bloomStack);
      heldItem.shrink(1);
      ItemHandlerHelper.giveItemToPlayer(player, tongsFull, player.inventory.currentItem);
      return true;

    } else if (tile instanceof TileBloom) {

      ItemStack bloomStack = BloomHelper.toItemStack((TileBloom) tile, new ItemStack(ModuleBloomery.Blocks.BLOOM));
      ItemStack tongsFull = BloomHelper.createItemTongsFull(heldItem, bloomStack);

      if (!world.isRemote) {
        world.setBlockToAir(tile.getPos());
      }

      heldItem.shrink(1);
      ItemHandlerHelper.giveItemToPlayer(player, tongsFull, player.inventory.currentItem);
    }

    return false;
  }

  @Override
  public void applyItemDamage(ItemStack itemStack, EntityPlayer player) {

    // Only apply item damage when a bloom is placed.
  }
}
