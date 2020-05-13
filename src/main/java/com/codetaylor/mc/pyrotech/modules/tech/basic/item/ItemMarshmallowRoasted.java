package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMarshmallowRoasted
    extends ItemMarshmallow {

  public static final String NAME = "marshmallow_roasted";

  public ItemMarshmallowRoasted() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.ROASTED_MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.ROASTED_MARSHMALLOW_SATURATION, false);
    this.setMaxStackSize(1);
  }

  @Override
  public int getHealAmount(ItemStack stack) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.ROASTED_MARSHMALLOW_HUNGER;
  }

  @Override
  public float getSaturationModifier(ItemStack stack) {

    return (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.ROASTED_MARSHMALLOW_SATURATION;
  }

  @Override
  protected int getEffectDurationTicks(World world, long roastedAtTimestamp) {

    return (int) (ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.ROASTED_MARSHMALLOW_SPEED_DURATION_TICKS * ItemMarshmallow.calculatePotency(world, roastedAtTimestamp));
  }

  @Override
  protected int getMaxEffectDurationTicks() {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MAX_ROASTED_MARSHMALLOW_SPEED_DURATION_TICKS;
  }

  @Override
  protected void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK_EMPTY, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW, 10);
    player.getCooldownTracker().setCooldown(this, 10);
  }
}
