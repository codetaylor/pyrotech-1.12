package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

public class ItemBucketStone
    extends ItemBucketBase {

  public static final String NAME = "bucket_stone";

  @Override
  protected String getLangKey() {

    return "bucket.stone";
  }

  @Override
  protected int getBucketStackLimit() {

    return ModulePyrotechConfig.BUCKET_STONE.MAX_STACK_SIZE;
  }

  @Override
  protected boolean showAllBuckets() {

    return ModulePyrotechConfig.BUCKET_STONE.SHOW_ALL_BUCKETS;
  }

  @Override
  protected int getMaxDurability() {

    return ModulePyrotechConfig.BUCKET_STONE.MAX_DURABILITY;
  }

  @Override
  protected int getHotTemperature() {

    return ModulePyrotechConfig.BUCKET_STONE.HOT_TEMPERATURE;
  }

  @Override
  protected int getHotContainerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_STONE.HOT_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getHotPlayerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_STONE.HOT_PLAYER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getFullContainerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_STONE.FULL_CONTAINER_DAMAGE_PER_SECOND;
  }
}
