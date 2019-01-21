package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileBloom;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemTongsFull
    extends ItemTongs {

  public static final String NAME = "tongs_full";

  public ItemTongsFull() {

    this.setMaxStackSize(1);
    this.setMaxDamage(EnumMaterial.IRON.getToolMaterial().getMaxUses());
  }

  @Override
  protected ActionResult<ItemStack> onItemRightClick(World world, ItemStack heldItem, RayTraceResult target) {

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

      // TODO: item break

      ItemStack itemStack = new ItemStack(ModuleItems.TONGS, 1, this.getMetadata(heldItem) + 1);
      return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }

    return ActionResult.newResult(EnumActionResult.PASS, heldItem);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {

    if (stack.getItem() != this) {
      return super.getItemStackDisplayName(stack);
    }

    NBTTagCompound tagCompound = stack.getTagCompound();

    if (tagCompound != null
        && tagCompound.hasKey(StackHelper.BLOCK_ENTITY_TAG)) {

      NBTTagCompound teCompound = tagCompound.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);
      String langKey = teCompound.getString("langKey") + ".name";

      if (I18n.canTranslate(langKey)) {
        String translatedLangKey = I18n.translateToLocal(langKey);
        return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".unique.name", translatedLangKey).trim();
      }
    }

    return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
  }
}
