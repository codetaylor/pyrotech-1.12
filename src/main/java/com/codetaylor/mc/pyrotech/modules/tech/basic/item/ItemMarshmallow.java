package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.item.ItemFood;

public class ItemMarshmallow
    extends ItemFood {

  public static final String NAME = "marshmallow";

  public ItemMarshmallow() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SATURATION, false);
  }
}
