package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import com.codetaylor.mc.pyrotech.modules.hunting.item.spi.ItemSpearBase;

public class ItemBoneSpear
    extends ItemSpearBase {

  public static final String NAME = "bone_spear";

  private static final String CONFIG_KEY = "bone";

  @Override
  protected int getDurability() {

    return ModuleHuntingConfig.SPEAR.DURABILITY.get(CONFIG_KEY);
  }

  @Override
  protected double getVelocityScalar() {

    return ModuleHuntingConfig.SPEAR.VELOCITY_SCALAR.get(CONFIG_KEY);
  }

  @Override
  protected double getInaccuracy() {

    return ModuleHuntingConfig.SPEAR.INACCURACY.get(CONFIG_KEY);
  }

  @Override
  protected double getThrownDamage() {

    return ModuleHuntingConfig.SPEAR.THROWN_DAMAGE.get(CONFIG_KEY);
  }
}
