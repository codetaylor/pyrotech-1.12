package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.item.Item;

public class ItemLeatherRepairKit
    extends Item {

  public static final String NAME = "leather_repair_kit";

  public ItemLeatherRepairKit() {

    this.setMaxStackSize(1);
    this.setMaxDamage(ModuleHuntingConfig.LEATHER_KITS.LEATHER_REPAIR_KIT_USES);
  }
}