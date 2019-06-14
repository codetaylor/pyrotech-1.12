package com.codetaylor.mc.pyrotech.modules.core.advancement;

import com.codetaylor.mc.pyrotech.library.SimpleAdvancementTrigger;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.util.ResourceLocation;

public final class AdvancementTriggers {

  public static final SimpleAdvancementTrigger MOD_ITEM_TRIGGER = new SimpleAdvancementTrigger(new ResourceLocation(ModuleCore.MOD_ID, "pickup_mod_item"));

  private AdvancementTriggers() {
    //
  }

}
