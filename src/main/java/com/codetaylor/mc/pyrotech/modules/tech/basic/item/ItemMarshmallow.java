package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemMarshmallow
    extends ItemFood {

  public static final String NAME = "marshmallow";

  public ItemMarshmallow() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SATURATION, false);
    this.setAlwaysEdible();
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

  protected int getEffectDurationTicks() {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SPEED_DURATION_TICKS;
  }

  protected int getMaxEffectDurationTicks() {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MAX_MARSHMALLOW_SPEED_DURATION_TICKS;
  }

  @Nonnull
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World world, EntityLivingBase entityLiving) {

    ItemMarshmallow.applyMarshmallowEffects(this.getEffectDurationTicks(), this.getMaxEffectDurationTicks(), entityLiving, this.getEffect(), this.stackEffect());

    if (entityLiving instanceof EntityPlayer) {
      this.setCooldownOnMarshmallows((EntityPlayer) entityLiving);
    }

    return super.onItemUseFinish(stack, world, entityLiving);
  }

  protected Potion getEffect() {

    return MobEffects.SPEED;
  }

  protected boolean stackEffect() {

    return true;
  }

  protected void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK_EMPTY, 10);
    player.getCooldownTracker().setCooldown(this, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_ROASTED, 10);
  }

  public static void applyMarshmallowEffects(int durationTicks, int maxDurationTicks, EntityLivingBase entityLiving, Potion effect, boolean stackEffect) {

    if (durationTicks > 0) {

      int duration = durationTicks;

      PotionEffect activePotionEffect = entityLiving.getActivePotionEffect(effect);

      if (stackEffect && activePotionEffect != null) {
        duration = Math.min(maxDurationTicks, duration + activePotionEffect.getDuration());
      }

      entityLiving.addPotionEffect(new PotionEffect(effect, duration));
    }
  }
}
