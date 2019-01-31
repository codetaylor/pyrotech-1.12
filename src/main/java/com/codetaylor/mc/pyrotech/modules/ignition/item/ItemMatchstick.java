package com.codetaylor.mc.pyrotech.modules.ignition.item;

import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemMatchstick
    extends ItemIgniterBase {

  public static final String NAME = "matchstick";

  public ItemMatchstick() {

    this.setMaxStackSize(ModuleIgnitionConfig.IGNITERS.MATCHSTICK_MAX_STACK_SIZE);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return ModuleIgnitionConfig.IGNITERS.MATCHSTICK_USE_DURATION_TICKS;
  }

  @Override
  protected void damageItem(@Nonnull ItemStack stack, EntityLivingBase player) {

    if (player instanceof EntityPlayer) {
      ((EntityPlayer) player).getCooldownTracker().setCooldown(this, ModuleIgnitionConfig.IGNITERS.MATCHSTICK_COOLDOWN_DURATION_TICKS);

      if (!((EntityPlayer) player).isCreative()) {
        stack.shrink(1);
      }
    }
  }
}
