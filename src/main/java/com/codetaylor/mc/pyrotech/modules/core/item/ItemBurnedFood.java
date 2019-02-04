package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class ItemBurnedFood
    extends ItemFood {

  public static final String NAME = "burned_food";

  public ItemBurnedFood() {

    super(ModuleCoreConfig.FOOD.BURNED_FOOD_HUNGER, (float) ModuleCoreConfig.FOOD.BURNED_FOOD_SATURATION, false);

    if (ModuleCoreConfig.FOOD.BURNED_FOOD_HUNGER_EFFECT_DURATION_TICKS > 0) {
      this.setPotionEffect(new PotionEffect(MobEffects.HUNGER, ModuleCoreConfig.FOOD.BURNED_FOOD_HUNGER_EFFECT_DURATION_TICKS, 0), 0.95f);
    }
  }

  @Override
  public int getItemBurnTime(ItemStack itemStack) {

    return ModuleCoreConfig.FUEL.BURNED_FOOD_BURN_TIME_TICKS;
  }
}
