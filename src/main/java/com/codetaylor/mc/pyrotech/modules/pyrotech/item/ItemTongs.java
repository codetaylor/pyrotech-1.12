package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileBloom;
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

public class ItemTongs
    extends Item {

  public static final String NAME = "tongs";

  public ItemTongs() {

    this.setMaxStackSize(1);
    this.setMaxDamage(EnumMaterial.IRON.getToolMaterial().getMaxUses());
  }

  @Override
  public boolean isEnchantable(@Nonnull ItemStack stack) {

    // TODO: transfer enchantments
    return false;
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

    return this.onItemRightClick(world, heldItem, target);
  }

  protected ActionResult<ItemStack> onItemRightClick(World world, ItemStack heldItem, RayTraceResult target) {

    BlockPos pos = target.getBlockPos();
    IBlockState blockState = world.getBlockState(pos);
    Block block = blockState.getBlock();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (block == ModuleBlocks.BLOOM
        && tileEntity instanceof TileBloom) {

      TileBloom tile = (TileBloom) tileEntity;
      ItemStack itemStack = new ItemStack(ModuleItems.TONGS_FULL, 1, this.getMetadata(heldItem) + 1);
      TileBloom.toItemStack(tile, itemStack);

      if (!world.isRemote) {
        world.setBlockToAir(pos);
      }

      // TODO: item break

      return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }

    return ActionResult.newResult(EnumActionResult.PASS, heldItem);
  }
}
