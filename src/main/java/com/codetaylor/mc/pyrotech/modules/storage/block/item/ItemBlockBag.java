package com.codetaylor.mc.pyrotech.modules.storage.block.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockBagBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.List;

public class ItemBlockBag
    extends ItemBlock {

  private static final int COLOR_BAG_FULL = Color.decode("0xFF0000").getRGB();
  private static final int COLOR_BAG = Color.decode("0x70341e").getRGB();

  private static final String STACK_HANDLER_TAG = "contents";

  private final BlockBagBase blockBag;

  public ItemBlockBag(BlockBagBase blockBag) {

    super(blockBag);
    this.blockBag = blockBag;
    this.setMaxStackSize(1);
  }

  public boolean isOpen(ItemStack itemStack) {

    return (itemStack.getMetadata() == 1);
  }

  public boolean isItemValidForInsertion(ItemStack itemStack) {

    return this.blockBag.isItemValidForInsertion(itemStack);
  }

  public boolean allowAutoPickupMainhand() {

    return this.blockBag.allowAutoPickupMainhand();
  }

  public boolean allowAutoPickupOffhand() {

    return this.blockBag.allowAutoPickupOffhand();
  }

  public boolean allowAutoPickupHotbar() {

    return this.blockBag.allowAutoPickupHotbar();
  }

  public boolean allowAutoPickupInventory() {

    return this.blockBag.allowAutoPickupInventory();
  }

  private int getItemCapacity() {

    return this.blockBag.getItemCapacity();
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {

    return 1 - this.getCount(stack) / (double) this.getItemCapacity();
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {

    return this.getCount(stack) > 0;
  }

  @Override
  public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {

    if (this.getCount(stack) == this.getItemCapacity()) {
      return COLOR_BAG_FULL;
    }

    return COLOR_BAG;
  }

  public static ItemStack insertItem(ItemStack bag, ItemStack itemStack, boolean simulate) {

    TileBagBase.StackHandler stackHandler = ItemBlockBag.getStackHandler(bag);

    if (stackHandler == null) {
      return itemStack;
    }

    ItemStack result = stackHandler.insertItem(itemStack, simulate);

    if (!simulate) {

    }

    return result;
  }

  private static TileBagBase.StackHandler getStackHandler(ItemStack itemStack) {

    Item item = itemStack.getItem();

    if (item instanceof ItemBlockBag) {
      ItemBlockBag itemBlockBag = (ItemBlockBag) item;
      NBTTagCompound tagCompound = StackHelper.getTagSafe(itemStack);
      TileBagBase.StackHandler stackHandler = new TileBagBase.StackHandler(itemBlockBag.getItemCapacity(), itemBlockBag.blockBag::isItemValidForInsertion);
      stackHandler.addObserver((handler, slot) -> {
        itemBlockBag.setCount(itemStack, stackHandler);
        itemBlockBag.saveStackHandler(itemStack, stackHandler);
      });

      if (!tagCompound.hasKey(STACK_HANDLER_TAG)) {
        tagCompound.setTag(STACK_HANDLER_TAG, stackHandler.serializeNBT());

      } else {
        NBTTagCompound compound = tagCompound.getCompoundTag(STACK_HANDLER_TAG);
        stackHandler.deserializeNBT(compound);
      }

      itemBlockBag.setCount(itemStack, stackHandler);
      return stackHandler;
    }

    return null;
  }

  private void saveStackHandler(ItemStack itemStack, TileBagBase.StackHandler handler) {

    NBTTagCompound compound = StackHelper.getTagSafe(itemStack);
    compound.setTag(STACK_HANDLER_TAG, handler.serializeNBT());
  }

  public int getCount(ItemStack itemStack) {

    NBTTagCompound tagCompound = itemStack.getTagCompound();

    if (tagCompound == null) {
      return -1;
    }

    return tagCompound.getInteger("count");
  }

  private void setCount(ItemStack itemStack, TileBagBase.StackHandler handler) {

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);
    int count = handler.getTotalItemCount();
    tag.setInteger("count", count);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    super.addInformation(stack, world, tooltip, flag);
    int totalCount = this.getCount(stack);

    if (totalCount == -1) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.item.capacity.empty", this.getItemCapacity()));
      return;

    } else {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.item.capacity", totalCount, this.getItemCapacity()));
    }

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

      TileBagBase.StackHandler stackHandler = ItemBlockBag.getStackHandler(stack);

      if (stackHandler == null) {
        return;
      }

      int maxDigits = 0;

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        ItemStack stackInSlot = stackHandler.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          int count = stackInSlot.getCount();
          int digits = String.valueOf(count).length();

          if (digits > maxDigits) {
            maxDigits = digits;
          }
        }
      }

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        ItemStack stackInSlot = stackHandler.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          String stackCount = String.valueOf(stackInSlot.getCount());

          if (stackCount.length() < maxDigits) {
            stackCount = TextFormatting.DARK_GRAY + StringUtils.repeat("0", maxDigits - stackCount.length()).concat(TextFormatting.YELLOW + stackCount);

          } else {
            stackCount = TextFormatting.YELLOW + stackCount;
          }

          tooltip.add(" " + stackCount + " " + TextFormatting.GOLD + stackInSlot.getDisplayName());
        }
      }

    } else {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY));
    }
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

    if (player.isSneaking()) {
      ItemStack heldItem = player.getHeldItem(hand);

      // try inventory
      if (this.tryTransferItems(world, pos, side, heldItem)) {
        return EnumActionResult.SUCCESS;
      }

      // spill contents
      if (this.isOpen(heldItem)
          && this.trySpillContents(world, pos, side, heldItem)) {
        return EnumActionResult.SUCCESS;
      }
    }

    return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = player.getHeldItem(hand);
    TileBagBase.StackHandler stackHandler = ItemBlockBag.getStackHandler(heldItem);

    if (stackHandler == null) {
      return EnumActionResult.PASS;
    }

    IBlockState blockState = world.getBlockState(pos);
    Block block = blockState.getBlock();

    if (!block.isReplaceable(world, pos)) {
      pos = pos.offset(facing);
    }

    ItemStack copy = heldItem.copy();
    EnumActionResult result = super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);

    if (result == EnumActionResult.SUCCESS) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileBagBase) {
        IItemHandler itemHandler = ItemBlockBag.getStackHandler(copy);
        TileBagBase.StackHandler tileHandler = ((TileBagBase) tileEntity).getStackHandler();

        if (itemHandler != null
            && tileHandler != null) {
          for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(i);
            tileHandler.setStackInSlot(i, stackInSlot);
          }
        }
      }
    }

    return result;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    RayTraceResult rayTraceResult = this.rayTrace(world, player, false);
    ItemStack heldItem = player.getHeldItem(hand);

    if (player.isSneaking()) {

      //noinspection ConstantConditions
      if (rayTraceResult == null
          || rayTraceResult.typeOfHit == RayTraceResult.Type.MISS) {
        // cycle open closed
        heldItem.setItemDamage(heldItem.getItemDamage() == 0 ? 1 : 0);
        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
      }
    }

    return super.onItemRightClick(world, player, hand);
  }

  private boolean trySpillContents(World world, BlockPos pos, EnumFacing facing, ItemStack itemStack) {

    if (world.isAirBlock(pos.offset(facing))) {
      TileBagBase.StackHandler handler = ItemBlockBag.getStackHandler(itemStack);

      if (handler != null) {

        if (!world.isRemote) {

          for (int i = 0; i < handler.getSlots(); i++) {
            StackHelper.spawnStackHandlerContentsOnTop(world, handler, pos.offset(facing), 0);
          }
        }
        return true;
      }
    }

    return false;
  }

  private boolean tryTransferItems(World world, BlockPos pos, EnumFacing facing, ItemStack itemStack) {

    TileBagBase.StackHandler itemHandler = ItemBlockBag.getStackHandler(itemStack);

    if (itemHandler == null) {
      return false;
    }

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity == null) {
      return false;
    }

    IItemHandler otherHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

    if (otherHandler == null) {
      return false;
    }

    if (world.isRemote) {
      return true;
    }

    // Go through all the items in the bag
    for (int i = 0; i < itemHandler.getSlots(); i++) {
      ItemStack stackInSlot = itemHandler.getStackInSlot(i);

      if (stackInSlot.isEmpty()) {
        continue;
      }

      ItemStack remainingItems = stackInSlot.copy();

      // and try to put each stack into the targeted handler
      for (int j = 0; j < otherHandler.getSlots(); j++) {
        remainingItems = otherHandler.insertItem(j, remainingItems, false);

        if (remainingItems.isEmpty()) {
          break;
        }
      }

      if (stackInSlot.getCount() != remainingItems.getCount()) {
        itemHandler.setStackInSlot(i, remainingItems);
      }
    }

    return true;
  }
}
