package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;

public class ItemTaintedMeat
    extends ItemFood {

  public static final String NAME = "tainted_meat";

  public ItemTaintedMeat() {

    super(ModuleCoreConfig.FOOD.TAINTED_MEAT_HUNGER, (float) ModuleCoreConfig.FOOD.TAINTED_MEAT_SATURATION, false);

    if (ModuleCoreConfig.FOOD.BURNED_FOOD_HUNGER_EFFECT_DURATION_TICKS > 0) {
      this.setPotionEffect(new PotionEffect(MobEffects.POISON, ModuleCoreConfig.FOOD.TAINTED_MEAT_POISON_EFFECT_DURATION_TICKS, 0), 0.95f);
    }
  }
}
