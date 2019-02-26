package com.codetaylor.mc.pyrotech.modules.bucket.item;

import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;
import net.minecraft.item.ItemStack;

public class ItemBucketWood
    extends ItemBucketBase {

  public static final String NAME = "bucket_wood";

  @Override
  protected String getLangKey() {

    return "bucket.wood";
  }

  @Override
  protected int getBucketStackLimit() {

    return ModuleBucketConfig.BUCKET_WOOD.MAX_STACK_SIZE;
  }

  @Override
  protected boolean showAllBuckets() {

    return ModuleBucketConfig.BUCKET_WOOD.SHOW_ALL_BUCKETS;
  }

  @Override
  protected int getMaxDurability() {

    return ModuleBucketConfig.BUCKET_WOOD.MAX_DURABILITY;
  }

  @Override
  protected int getHotTemperature() {

    return ModuleBucketConfig.BUCKET_WOOD.HOT_TEMPERATURE;
  }

  @Override
  protected int getHotContainerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_WOOD.HOT_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getHotPlayerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_WOOD.HOT_PLAYER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getFullContainerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_WOOD.FULL_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  public int getItemBurnTime(ItemStack stack) {

    return ModuleBucketConfig.BUCKET_WOOD.BURN_TIME_TICKS;
  }
}
