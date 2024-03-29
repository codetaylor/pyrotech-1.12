package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.item.spi.ItemHideScrapedBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemHideScraped
    extends ItemHideScrapedBase {

  public static final String NAME = "hide_scraped";

  @Override
  public boolean hasCustomEntity(@Nonnull ItemStack stack) {

    return true;
  }

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public Entity createEntity(World world, Entity location, ItemStack itemStack) {

    EntityItemHideScraped entityItem = new EntityItemHideScraped(world, location.posX, location.posY, location.posZ, itemStack, new ItemStack(ModuleHunting.Items.HIDE_WASHED));

    entityItem.motionX = location.motionX;
    entityItem.motionY = location.motionY;
    entityItem.motionZ = location.motionZ;

    entityItem.setPickupDelay(40);

    return entityItem;
  }
}