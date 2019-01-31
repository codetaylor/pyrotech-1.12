package com.codetaylor.mc.pyrotech.modules.ignition.item;

import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBowDrill
    extends ItemIgniterBase {

  public static final String NAME = "bow_drill";

  public ItemBowDrill() {

    this.setMaxDamage(ModuleIgnitionConfig.IGNITERS.BOW_DRILL_DURABILITY);
    this.setMaxStackSize(1);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return ModuleIgnitionConfig.IGNITERS.BOW_DRILL_USE_DURATION_TICKS;
  }

  @Override
  protected void damageItem(@Nonnull ItemStack stack, EntityLivingBase player) {

    if (player instanceof EntityPlayer
        && !((EntityPlayer) player).isCreative()) {
      stack.damageItem(1, player);
    }
  }
}
