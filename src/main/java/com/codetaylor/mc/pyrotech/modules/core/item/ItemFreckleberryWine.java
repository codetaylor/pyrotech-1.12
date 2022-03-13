package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemFreckleberryWine
    extends ItemFood {

  public static final String NAME = "freckleberry_wine";

  public ItemFreckleberryWine() {

    super(ModuleCoreConfig.FRECKLEBERRY_WINE.HUNGER, (float) ModuleCoreConfig.FRECKLEBERRY_WINE.SATURATION, false);
    this.setMaxStackSize(3);
    this.setAlwaysEdible();
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase entityLiving) {

    ItemStack resultItemStack = super.onItemUseFinish(itemStack, world, entityLiving);

    PotionEffect potionEffect = entityLiving.getActivePotionEffect(MobEffects.SPEED);

    int effectDuration = ModuleCoreConfig.FRECKLEBERRY_WINE.EFFECT_DURATION_TICKS;

    if (potionEffect != null) {
      effectDuration += potionEffect.getDuration();
    }

    if (effectDuration > ModuleCoreConfig.FRECKLEBERRY_WINE.SICK_THRESHOLD_TICKS) {
      int nauseaDuration = (effectDuration - ModuleCoreConfig.FRECKLEBERRY_WINE.SICK_THRESHOLD_TICKS) / 2;
      entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, nauseaDuration, 1));
    }

    entityLiving.addPotionEffect(new PotionEffect(MobEffects.SPEED, effectDuration));

    if (resultItemStack.isEmpty()) {
      return new ItemStack(Items.GLASS_BOTTLE);
    }

    if (entityLiving instanceof EntityPlayer) {
      ((EntityPlayer) entityLiving).addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
    }

    return resultItemStack;
  }
}