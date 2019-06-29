package com.codetaylor.mc.pyrotech.modules.bucket.item;

import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;

public class ItemBucketClay
    extends ItemBucketBase {

  public static final String NAME = "bucket_clay";

  @Override
  protected String getLangKey() {

    return "bucket.clay";
  }

  @Override
  protected int getBucketStackLimit() {

    return ModuleBucketConfig.BUCKET_CLAY.MAX_STACK_SIZE;
  }

  @Override
  protected boolean showAllBuckets() {

    return ModuleBucketConfig.BUCKET_CLAY.SHOW_ALL_BUCKETS;
  }

  @Override
  protected int getMaxDurability() {

    return ModuleBucketConfig.BUCKET_CLAY.MAX_DURABILITY;
  }

  @Override
  protected int getHotTemperature() {

    return ModuleBucketConfig.BUCKET_CLAY.HOT_TEMPERATURE;
  }

  @Override
  protected int getHotContainerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_CLAY.HOT_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getHotPlayerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_CLAY.HOT_PLAYER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getFullContainerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_CLAY.FULL_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected boolean isCowMilkDisabled() {

    return !ModuleBucketConfig.BUCKET_CLAY.ENABLE_COW_MILK;
  }

  @Override
  protected boolean dropFluidSourceOnBreak() {

    return ModuleBucketConfig.BUCKET_CLAY.DROP_SOURCE_ON_BREAK;
  }
}
