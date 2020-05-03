package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemMarshmallow
    extends ItemFood {

  public static final String NAME = "marshmallow";
  public static final String NAME_ROASTED = "marshmallow_roasted";
  public static final String NAME_BURNED = "marshmallow_burned";

  public ItemMarshmallow() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SATURATION, false);
  }

  public ItemMarshmallow(int amount, float saturation, boolean isWolfFood) {

    super(amount, saturation, isWolfFood);
  }

  @Override
  public int getHealAmount(ItemStack stack) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_HUNGER;
  }

  @Override
  public float getSaturationModifier(ItemStack stack) {

    return (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SATURATION;
  }

  protected int getSpeedDurationTicks() {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SPEED_DURATION_TICKS;
  }

  protected int getMaxSpeedDurationTicks() {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MAX_MARSHMALLOW_SPEED_DURATION_TICKS;
  }

  @Nonnull
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World world, EntityLivingBase entityLiving) {

    if (this.getSpeedDurationTicks() > 0) {

      int duration = this.getSpeedDurationTicks();

      PotionEffect activePotionEffect = entityLiving.getActivePotionEffect(MobEffects.SPEED);

      if (activePotionEffect != null) {
        duration = Math.min(this.getMaxSpeedDurationTicks(), duration + activePotionEffect.getDuration());
      }

      entityLiving.addPotionEffect(new PotionEffect(MobEffects.SPEED, duration));
    }

    if (entityLiving instanceof EntityPlayer) {
      this.setCooldownOnMarshmallows((EntityPlayer) entityLiving);
    }

    return super.onItemUseFinish(stack, world, entityLiving);
  }

  protected void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK, 10);
    player.getCooldownTracker().setCooldown(this, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_ROASTED, 10);
  }

}
