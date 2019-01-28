package com.codetaylor.mc.pyrotech.modules.bloomery.item;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionItem;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.bloomery.util.BloomHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileAnvilBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public abstract class ItemTongsFullBase
    extends Item
    implements IInteractionItem {

  private final Supplier<ItemTongsEmptyBase> otherTongsSupplier;

  /* package */ ItemTongsFullBase(Supplier<ItemTongsEmptyBase> otherTongsSupplier, int durability) {

    this.otherTongsSupplier = otherTongsSupplier;

    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }

  public ItemTongsEmptyBase getItemTongsEmpty() {

    return this.otherTongsSupplier.get();
  }

  @Override
  public boolean isEnchantable(@Nonnull ItemStack stack) {

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

    return this.onItemRightClick(world, player, heldItem, target);
  }

  private ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, ItemStack heldItem, RayTraceResult target) {

    EnumFacing sideHit = target.sideHit;
    BlockPos pos = target.getBlockPos();
    BlockPos offset = pos.offset(sideHit);
    NBTTagCompound tagCompound = heldItem.getTagCompound();

    if (ModuleBloomery.Blocks.BLOOM.canPlaceBlockAt(world, offset)
        && tagCompound != null) {

      if (!world.isRemote) {
        world.setBlockState(offset, ModuleBloomery.Blocks.BLOOM.getDefaultState());
        TileBloom tile = (TileBloom) world.getTileEntity(offset);

        if (tile != null) {
          BlockPos tilePos = tile.getPos();
          tile.readFromNBT(tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG));
          tile.setPos(tilePos);
          BlockHelper.notifyBlockUpdate(world, offset);
        }
      }

      ItemStack itemStack = BloomHelper.createItemTongsEmpty(heldItem);

      if (itemStack.isEmpty()) {

        if (!world.isRemote) {
          world.playSound(
              null,
              player.getPosition(),
              SoundEvents.ENTITY_ITEM_BREAK,
              SoundCategory.BLOCKS,
              1.0F,
              Util.RANDOM.nextFloat() * 0.4F + 0.8F
          );
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, ItemStack.EMPTY);
      }

      return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }

    return ActionResult.newResult(EnumActionResult.PASS, heldItem);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (stack.getItem() != this) {
      return;
    }

    NBTTagCompound tagCompound = stack.getTagCompound();

    if (tagCompound != null
        && tagCompound.hasKey(StackHelper.BLOCK_ENTITY_TAG)) {

      NBTTagCompound teCompound = tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
      String langKey = teCompound.getString("langKey") + ".name";

      if (I18n.canTranslate(langKey)) {
        String translatedLangKey = I18n.translateToLocal(langKey);
        String translatedTooltip = I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".unique.name", translatedLangKey).trim();

        if (tooltip.size() > 1) {
          tooltip.add(1, TextFormatting.DARK_RED + translatedTooltip + TextFormatting.RESET);

        } else {
          tooltip.add(TextFormatting.GOLD + translatedTooltip + TextFormatting.RESET);
        }
      }
    }
  }

  @Override
  public boolean allowInteraction(TileEntity tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (tile instanceof TileAnvilBase) {
      return ((TileAnvilBase) tile).getStackHandler().getStackInSlot(0).isEmpty();
    }

    return false;
  }

  @Override
  public boolean doInteraction(TileEntity tile, World world, ItemStack heldItem, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

    if (tile instanceof TileAnvilBase) {
      NBTTagCompound tagCompound = heldItem.getTagCompound();

      if (tagCompound == null) {
        return false;
      }

      NBTTagCompound tileTag = tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
      ItemStack bloomStack = BloomHelper.createBloomAsItemStack(new ItemStack(ModuleBloomery.Blocks.BLOOM), tileTag);
      ((TileAnvilBase) tile).getStackHandler().insertItem(0, bloomStack, false);
      ItemStack emptyTongsStack = BloomHelper.createItemTongsEmpty(heldItem);
      heldItem.shrink(1);

      if (!emptyTongsStack.isEmpty()) {
        ItemHandlerHelper.giveItemToPlayer(player, emptyTongsStack, player.inventory.currentItem);
      }

      return true;
    }

    return false;
  }

  @Override
  public void applyItemDamage(ItemStack itemStack, EntityPlayer player) {

    // Damage is applied during the interaction.
  }
}
