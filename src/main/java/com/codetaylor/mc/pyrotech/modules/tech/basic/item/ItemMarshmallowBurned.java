package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

public class ItemMarshmallowBurned
    extends ItemMarshmallow {

  public static final String NAME = "marshmallow_burned";

  public ItemMarshmallowBurned() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_SATURATION, false);
    this.setMaxStackSize(64);
  }

  @Override
  public int getHealAmount(ItemStack stack) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_HUNGER;
  }

  @Override
  public float getSaturationModifier(ItemStack stack) {

    return (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_SATURATION;
  }

  @Override
  protected int getEffectDurationTicks(World world, long roastedAtTimestamp) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_SLOW_DURATION_TICKS;
  }

  @Override
  protected int getMaxEffectDurationTicks() {

    // Not used since we return false from stackEffect().
    return 0;
  }

  @Override
  protected Potion getEffect() {

    return MobEffects.SLOWNESS;
  }

  @Override
  protected boolean stackEffect() {

    return false;
  }

  protected void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK_EMPTY, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW, 10);
    player.getCooldownTracker().setCooldown(this, 10);
  }
}
