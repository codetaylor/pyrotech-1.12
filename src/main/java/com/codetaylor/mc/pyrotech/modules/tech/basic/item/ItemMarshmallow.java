package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemMarshmallow
    extends ItemFood {

  public static final String NAME = "marshmallow";

  public ItemMarshmallow() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SATURATION, false);
  }

  @Nonnull
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World world, EntityLivingBase entityLiving) {

    int durationTicks = Math.max(0, ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SPEED_DURATION_TICKS);

    if (durationTicks > 0) {
      entityLiving.addPotionEffect(new PotionEffect(MobEffects.SPEED, durationTicks));
    }

    return super.onItemUseFinish(stack, world, entityLiving);
  }
}
