package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

public class ItemBucketClay
    extends ItemBucketBase {

  public static final String NAME = "bucket_clay";

  @Override
  protected String getLangKey() {

    return "bucket.clay";
  }

  @Override
  protected boolean showAllBuckets() {

    return ModulePyrotechConfig.BUCKET_CLAY.SHOW_ALL_BUCKETS;
  }

  @Override
  protected int getMaxDurability() {

    return ModulePyrotechConfig.BUCKET_CLAY.MAX_DURABILITY;
  }

  @Override
  protected int getHotTemperature() {

    return ModulePyrotechConfig.BUCKET_CLAY.HOT_TEMPERATURE;
  }

  @Override
  protected int getHotContainerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_CLAY.HOT_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getHotPlayerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_CLAY.HOT_PLAYER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getFullContainerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_CLAY.FULL_CONTAINER_DAMAGE_PER_SECOND;
  }
}
