package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ComfortEffectFoodBonus {

  public static class LivingEntityUseItemEventHandler {

    @SubscribeEvent
    public void on(LivingEntityUseItemEvent.Finish event) {

      EntityLivingBase entityLiving = event.getEntityLiving();

      if (entityLiving.getEntityWorld().isRemote) {
        return;
      }

      if (entityLiving instanceof EntityPlayer) {
        ItemStack itemStack = event.getItem();
        Item item = itemStack.getItem();

        if (item instanceof ItemFood) {
          ItemFood itemFood = (ItemFood) item;
          int healAmount = (int) Math.max(0, itemFood.getHealAmount(itemStack) * ModuleTechBasicConfig.CAMPFIRE.COMFORT_HUNGER_MODIFIER);
          float saturationModifier = (float) Math.max(0, itemFood.getSaturationModifier(itemStack) * ModuleTechBasicConfig.CAMPFIRE.COMFORT_SATURATION_MODIFIER);
          FoodStats foodStats = ((EntityPlayer) entityLiving).getFoodStats();
          foodStats.addStats(healAmount, saturationModifier);
        }
      }
    }
  }
}
