package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.entity.EntityRock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemRock
    extends ItemBlock {

  public ItemRock(Block block) {

    super(block);
    this.setHasSubtypes(true);
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return "tile." + ModulePyrotech.MOD_ID + "." + BlockRock.EnumType.fromMeta(stack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    ItemStack itemstack = player.getHeldItem(hand);

    if (!player.isCreative()) {
      itemstack.shrink(1);
    }

    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    if (!world.isRemote) {
      EntityRock entity = new EntityRock(world, player, itemstack.getMetadata());
      entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f);
      world.spawnEntity(entity);
    }

    return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
  }

  @Override
  public int getMetadata(int damage) {

    return damage;
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {

    if (stack.getItem() == this
        && stack.getMetadata() == BlockRock.EnumType.SLAG.getMeta()) {

      NBTTagCompound tagCompound = stack.getTagCompound();

      if (tagCompound != null
          && tagCompound.hasKey("langKey")) {

        String langKey = tagCompound.getString("langKey") + ".name";

        if (I18n.canTranslate(langKey)) {
          String translatedLangKey = I18n.translateToLocal(langKey);
          return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".unique.name", translatedLangKey).trim();
        }
      }
    }

    return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
  }
}
