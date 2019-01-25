package com.codetaylor.mc.pyrotech.modules.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.bloomery.util.BloomHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class ItemTongsEmptyBase
    extends Item {

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

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    ItemStack heldItem = player.getHeldItem(hand);

    if (hand != EnumHand.MAIN_HAND
        || heldItem.getItem() != this) {
      return ActionResult.newResult(EnumActionResult.FAIL, heldItem);
    }

    RayTraceResult target = this.rayTrace(world, player, false);

    if (target.typeOfHit != RayTraceResult.Type.BLOCK) {
      return ActionResult.newResult(EnumActionResult.FAIL, heldItem);
    }

    return this.onItemRightClick(world, player, heldItem, target);
  }

  protected ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, ItemStack heldItem, RayTraceResult target) {

    BlockPos pos = target.getBlockPos();
    IBlockState blockState = world.getBlockState(pos);
    Block block = blockState.getBlock();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (block == ModuleBloomery.Blocks.BLOOM
        && tileEntity instanceof TileBloom) {

      TileBloom tile = (TileBloom) tileEntity;
      ItemStack bloomStack = BloomHelper.toItemStack(tile, new ItemStack(ModuleBloomery.Blocks.BLOOM));
      ItemStack itemStack = BloomHelper.createItemTongsFull(heldItem, bloomStack);

      if (!world.isRemote) {
        world.setBlockToAir(pos);
      }

      return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }

    return ActionResult.newResult(EnumActionResult.PASS, heldItem);
  }
}
