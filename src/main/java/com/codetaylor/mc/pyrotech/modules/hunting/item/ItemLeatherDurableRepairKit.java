package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.item.Item;

public class ItemLeatherDurableRepairKit
    extends Item {

  public static final String NAME = "leather_durable_repair_kit";

  public ItemLeatherDurableRepairKit() {

    this.setMaxStackSize(1);
    this.setMaxDamage(ModuleHuntingConfig.LEATHER_KITS.LEATHER_DURABLE_REPAIR_KIT_USES);
  }
}