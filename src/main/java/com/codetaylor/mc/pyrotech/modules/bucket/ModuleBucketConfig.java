package com.codetaylor.mc.pyrotech.modules.bucket;

import net.minecraftforge.common.config.Config;

import java.io.Serializable;

@Config(modid = ModuleBucket.MOD_ID, name = ModuleBucket.MOD_ID + "/" + "module.Bucket")
public class ModuleBucketConfig {

  // ---------------------------------------------------------------------------
  // - Bucket - Wood
  // ---------------------------------------------------------------------------

  public static BucketWood BUCKET_WOOD = new BucketWood();

  public static class BucketWood
      implements Serializable {

    @Config.Comment({
        "Board burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 200
    })
    @Config.RangeInt(min = 0)
    public int BURN_TIME_TICKS = 200;

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int MAX_DURABILITY = 8;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Default: " + 450
    })
    @Config.RangeInt
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 0)
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 8;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 1;

    @Config.Comment({
        "The maximum stack size for empty buckets.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MAX_STACK_SIZE = 1;

    @Config.Comment({
        "Set to false to disable milking a cow with the bucket.",
        "Default: " + true
    })
    public boolean ENABLE_COW_MILK = true;
  }

  // ---------------------------------------------------------------------------
  // - Bucket - Clay
  // ---------------------------------------------------------------------------

  public static BucketClay BUCKET_CLAY = new BucketClay();

  public static class BucketClay
      implements Serializable {

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Default: " + 12
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int MAX_DURABILITY = 12;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Default: " + 450
    })
    @Config.RangeInt
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 0)
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 4;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Default: " + 0
    })
    @Config.RangeInt(min = 0)
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 0;

    @Config.Comment({
        "The maximum stack size for empty buckets.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MAX_STACK_SIZE = 4;

    @Config.Comment({
        "Set to false to disable milking a cow with the bucket.",
        "Default: " + true
    })
    public boolean ENABLE_COW_MILK = true;
  }

  // ---------------------------------------------------------------------------
  // - Bucket - Stone
  // ---------------------------------------------------------------------------

  public static BucketStone BUCKET_STONE = new BucketStone();

  public static class BucketStone
      implements Serializable {

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int MAX_DURABILITY = 16;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Default: " + 450
    })
    @Config.RangeInt()
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 0)
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 4;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Default: " + 0
    })
    @Config.RangeInt(min = 0)
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 0;

    @Config.Comment({
        "The maximum stack size for empty buckets.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MAX_STACK_SIZE = 4;

    @Config.Comment({
        "Set to false to disable milking a cow with the bucket.",
        "Default: " + true
    })
    public boolean ENABLE_COW_MILK = true;
  }
}
