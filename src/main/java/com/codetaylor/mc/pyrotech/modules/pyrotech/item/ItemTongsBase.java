package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileBloom;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class ItemTongsBase
    extends Item {

  private final Supplier<ItemTongsFullBase> otherTongsSupplier;

  /* package */ ItemTongsBase(Supplier<ItemTongsFullBase> otherTongsSupplier, int durability) {

    this.otherTongsSupplier = otherTongsSupplier;

    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }

  /**
   * Creates a new filled tongs item and merges NBT data from the empty tongs
   * and the bloom into the new item.
   * <p>
   * Does not apply damage.
   * Does not modify input stack.
   *
   * @param toFill the empty tongs
   * @param bloom  the bloom
   * @return a new filled tongs item stack
   */
  public static ItemStack getFilledItemStack(ItemStack toFill, ItemStack bloom) {

    Item item = toFill.getItem();

    if (!(item instanceof ItemTongsBase)) {
      return toFill;
    }

    ItemTongsBase tongs = (ItemTongsBase) item;

    ItemStack itemStack = new ItemStack(tongs.otherTongsSupplier.get(), 1, toFill.getMetadata());

    NBTTagCompound tag = new NBTTagCompound();

    if (bloom.getTagCompound() != null) {
      tag.merge(bloom.getTagCompound());
    }

    if (toFill.getTagCompound() != null) {
      tag.merge(toFill.getTagCompound());
    }

    itemStack.setTagCompound(tag);

    return itemStack;
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

    if (block == ModuleBlocks.BLOOM
        && tileEntity instanceof TileBloom) {

      TileBloom tile = (TileBloom) tileEntity;
      ItemStack bloomStack = TileBloom.toItemStack(tile, new ItemStack(ModuleBlocks.BLOOM));
      ItemStack itemStack = ItemTongsBase.getFilledItemStack(heldItem, bloomStack);

      if (!world.isRemote) {
        world.setBlockToAir(pos);
      }

      return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }

    return ActionResult.newResult(EnumActionResult.PASS, heldItem);
  }
}
