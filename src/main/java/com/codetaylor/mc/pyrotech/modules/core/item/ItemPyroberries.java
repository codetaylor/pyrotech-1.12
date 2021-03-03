package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemPyroberries
    extends ItemFood {

  public static final String NAME = "pyroberries";

  public ItemPyroberries() {

    super(ModuleCoreConfig.FOOD.PYROBERRIES_HUNGER, (float) ModuleCoreConfig.FOOD.PYROBERRIES_SATURATION, false);
    this.setAlwaysEdible();
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {

    entityLiving.setFire(5);
    return super.onItemUseFinish(stack, world, entityLiving);
  }
}
