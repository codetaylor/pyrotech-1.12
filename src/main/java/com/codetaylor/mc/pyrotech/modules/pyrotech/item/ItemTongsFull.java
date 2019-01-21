package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileBloom;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTongsFull
    extends ItemTongs {

  public static final String NAME = "tongs_full";

  @Override
  protected ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, ItemStack heldItem, RayTraceResult target) {

    EnumFacing sideHit = target.sideHit;
    BlockPos pos = target.getBlockPos();
    BlockPos offset = pos.offset(sideHit);
    NBTTagCompound tagCompound = heldItem.getTagCompound();

    if (ModuleBlocks.BLOOM.canPlaceBlockAt(world, offset)
        && tagCompound != null) {

      if (!world.isRemote) {
        world.setBlockState(offset, ModuleBlocks.BLOOM.getDefaultState());
        TileBloom tile = (TileBloom) world.getTileEntity(offset);

        if (tile != null) {
          BlockPos tilePos = tile.getPos();
          tile.readFromNBT(tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG));
          tile.setPos(tilePos);
          BlockHelper.notifyBlockUpdate(world, offset);
        }
      }

      ItemStack itemStack = new ItemStack(ModuleItems.TONGS, 1, this.getMetadata(heldItem));
      tagCompound.removeTag(StackHelper.BLOCK_ENTITY_TAG);
      itemStack.setTagCompound(tagCompound);

      if (itemStack.attemptDamageItem(ModulePyrotechConfig.GENERAL.TONGS_DAMAGE_PER_USE, RandomHelper.random(), null)) {

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
}
