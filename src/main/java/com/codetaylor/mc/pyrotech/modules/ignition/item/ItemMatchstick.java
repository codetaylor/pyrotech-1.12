package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemMatchstick
    extends ItemIgniterBase {

  public static final String NAME = "matchstick";

  public ItemMatchstick() {

    this.setMaxStackSize(ModulePyrotechConfig.GENERAL.MATCHSTICK_MAX_STACK_SIZE);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return ModulePyrotechConfig.GENERAL.MATCHSTICK_USE_DURATION_TICKS;
  }

  @Override
  protected void damageItem(@Nonnull ItemStack stack, EntityLivingBase player) {

    if (player instanceof EntityPlayer) {
      ((EntityPlayer) player).getCooldownTracker().setCooldown(this, ModulePyrotechConfig.GENERAL.MATCHSTICK_COOLDOWN_DURATION_TICKS);

      if (!((EntityPlayer) player).isCreative()) {
        stack.shrink(1);
      }
    }
  }
}
