package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

public class ItemSlag
    extends Item {

  public static final String NAME = "slag";

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {

    if (stack.getItem() == this) {

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
