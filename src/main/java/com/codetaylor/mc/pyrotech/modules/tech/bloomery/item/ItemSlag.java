package com.codetaylor.mc.pyrotech.modules.tech.bloomery.item;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemSlag
    extends Item {

  public static final String NAME = "slag";

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {

    if (stack.getItem() == this) {

      Properties properties = ModuleTechBloomery.Items.GENERATED_SLAG.get(this);

      if (properties != null
          && properties.langKey != null) {

        if (I18n.canTranslate(properties.langKey)) {
          String translatedLangKey = I18n.translateToLocal(properties.langKey);
          return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".unique.name", translatedLangKey).trim();
        }
      }
    }

    return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
  }

  public static class Properties {

    @Nullable
    public final String langKey;
    public final int color;

    public Properties(@Nullable String langKey, int color) {

      this.langKey = langKey;
      this.color = color;
    }
  }
}
