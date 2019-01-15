package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;

public class ItemBucketWood
    extends ItemBucketBase {

  public static final String NAME = "bucket_wood";

  @Override
  protected String getLangKey() {

    return "bucket.wood";
  }

  @Override
  protected boolean showAllBuckets() {

    return ModulePyrotechConfig.BUCKET_WOOD.SHOW_ALL_BUCKETS;
  }

  @Override
  protected int getMaxDurability() {

    return ModulePyrotechConfig.BUCKET_WOOD.MAX_DURABILITY;
  }

  @Override
  protected int getHotTemperature() {

    return ModulePyrotechConfig.BUCKET_WOOD.HOT_TEMPERATURE;
  }

  @Override
  protected int getHotContainerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_WOOD.HOT_CONTAINER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getHotPlayerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_WOOD.HOT_PLAYER_DAMAGE_PER_SECOND;
  }

  @Override
  protected int getFullContainerDamagePerSecond() {

    return ModulePyrotechConfig.BUCKET_WOOD.FULL_CONTAINER_DAMAGE_PER_SECOND;
  }
}
