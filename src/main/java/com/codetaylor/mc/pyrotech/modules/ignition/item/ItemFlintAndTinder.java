package com.codetaylor.mc.pyrotech.modules.ignition.item;

import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemFlintAndTinder
    extends ItemIgniterBase {

  public static final String NAME = "flint_and_tinder";

  public ItemFlintAndTinder() {

    this.setMaxDamage(ModuleIgnitionConfig.IGNITERS.FLINT_AND_TINDER_DURABILITY);
    this.setMaxStackSize(1);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return ModuleIgnitionConfig.IGNITERS.FLINT_AND_TINDER_USE_DURATION_TICKS;
  }

  @Override
  protected void damageItem(@Nonnull ItemStack stack, EntityLivingBase player) {

    if (player instanceof EntityPlayer
        && !((EntityPlayer) player).isCreative()) {
      stack.damageItem(1, player);
    }
  }
}
