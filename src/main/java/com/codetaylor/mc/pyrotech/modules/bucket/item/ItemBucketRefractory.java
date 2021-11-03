package com.codetaylor.mc.pyrotech.modules.bucket.item;

import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;

public class ItemBucketRefractory
    extends ItemBucketBase {

  public static final String NAME = "bucket_refractory";

  @Override
  protected String getLangKey() {

    return "bucket.refractory";
  }

  @Override
  protected int getBucketStackLimit() {

    return ModuleBucketConfig.BUCKET_REFRACTORY.MAX_STACK_SIZE;
  }

  @Override
  protected boolean showAllBuckets() {

    return ModuleBucketConfig.BUCKET_REFRACTORY.SHOW_ALL_BUCKETS;
  }

  @Override
  protected int getMaxDurability() {

    return ModuleBucketConfig.BUCKET_REFRACTORY.MAX_DURABILITY;
  }

  @Override
  protected int getHotTemperature() {

    return ModuleBucketConfig.BUCKET_REFRACTORY.HOT_TEMPERATURE;
  }

  @Override
  protected int getHotContainerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_REFRACTORY.HOT_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getHotPlayerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_REFRACTORY.HOT_PLAYER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getFullContainerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_REFRACTORY.FULL_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected boolean isCowMilkDisabled() {

    return !ModuleBucketConfig.BUCKET_REFRACTORY.ENABLE_COW_MILK;
  }

  @Override
  protected boolean dropFluidSourceOnBreak() {

    return ModuleBucketConfig.BUCKET_REFRACTORY.DROP_SOURCE_ON_BREAK;
  }
}
