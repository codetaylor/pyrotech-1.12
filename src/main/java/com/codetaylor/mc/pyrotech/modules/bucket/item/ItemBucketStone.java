package com.codetaylor.mc.pyrotech.modules.bucket.item;

import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucketConfig;

public class ItemBucketStone
    extends ItemBucketBase {

  public static final String NAME = "bucket_stone";

  @Override
  protected String getLangKey() {

    return "bucket.stone";
  }

  @Override
  protected int getBucketStackLimit() {

    return ModuleBucketConfig.BUCKET_STONE.MAX_STACK_SIZE;
  }

  @Override
  protected boolean showAllBuckets() {

    return ModuleBucketConfig.BUCKET_STONE.SHOW_ALL_BUCKETS;
  }

  @Override
  protected int getMaxDurability() {

    return ModuleBucketConfig.BUCKET_STONE.MAX_DURABILITY;
  }

  @Override
  protected int getHotTemperature() {

    return ModuleBucketConfig.BUCKET_STONE.HOT_TEMPERATURE;
  }

  @Override
  protected int getHotContainerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_STONE.HOT_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getHotPlayerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_STONE.HOT_PLAYER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getFullContainerDamagePerSecond() {

    return ModuleBucketConfig.BUCKET_STONE.FULL_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected boolean isCowMilkDisabled() {

    return !ModuleBucketConfig.BUCKET_STONE.ENABLE_COW_MILK;
  }

  @Override
  protected boolean dropFluidSourceOnBreak() {

    return ModuleBucketConfig.BUCKET_STONE.DROP_SOURCE_ON_BREAK;
  }
}
