package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;

public class ItemBurnedFood
    extends ItemFood {

  public static final String NAME = "burned_food";

  public ItemBurnedFood() {

    super(ModulePyrotechConfig.FOOD.BURNED_FOOD_HUNGER, (float) ModulePyrotechConfig.FOOD.BURNED_FOOD_SATURATION, false);

    if (ModulePyrotechConfig.FOOD.BURNED_FOOD_HUNGER_EFFECT_DURATION_TICKS > 0) {
      this.setPotionEffect(new PotionEffect(MobEffects.HUNGER, ModulePyrotechConfig.FOOD.BURNED_FOOD_HUNGER_EFFECT_DURATION_TICKS, 0), 0.95f);
    }
  }
}
