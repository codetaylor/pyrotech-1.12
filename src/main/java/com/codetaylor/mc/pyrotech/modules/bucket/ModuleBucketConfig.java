package com.codetaylor.mc.pyrotech.modules.bucket;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "module.Bucket")
public class ModuleBucketConfig {

  // ---------------------------------------------------------------------------
  // - Bucket - Wood
  // ---------------------------------------------------------------------------

  public static BucketWood BUCKET_WOOD = new BucketWood();

  public static class BucketWood {

    @Config.Comment({
        "Set to false to disable the bucket.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Range: [1, +int]",
        "Default: " + 8
    })
    public int MAX_DURABILITY = 8;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Range: [-int, +int]",
        "Default: " + 450
    })
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 8
    })
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 8;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 2
    })
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 1
    })
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 1;

    @Config.Comment({
        "The maximum stack size for empty buckets.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MAX_STACK_SIZE = 1;
  }

  // ---------------------------------------------------------------------------
  // - Bucket - Clay
  // ---------------------------------------------------------------------------

  public static BucketClay BUCKET_CLAY = new BucketClay();

  public static class BucketClay {

    @Config.Comment({
        "Set to false to disable the bucket.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Range: [1, +int]",
        "Default: " + 16
    })
    public int MAX_DURABILITY = 16;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Range: [-int, +int]",
        "Default: " + 450
    })
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 4
    })
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 4;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 2
    })
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 0
    })
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 0;

    @Config.Comment({
        "The maximum stack size for empty buckets.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MAX_STACK_SIZE = 4;
  }

  // ---------------------------------------------------------------------------
  // - Bucket - Stone
  // ---------------------------------------------------------------------------

  public static BucketStone BUCKET_STONE = new BucketStone();

  public static class BucketStone {

    @Config.Comment({
        "Set to false to disable the bucket.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Range: [1, +int]",
        "Default: " + 32
    })
    public int MAX_DURABILITY = 32;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Range: [-int, +int]",
        "Default: " + 450
    })
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 4
    })
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 4;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 2
    })
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 0
    })
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 0;

    @Config.Comment({
        "The maximum stack size for empty buckets.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MAX_STACK_SIZE = 4;
  }

  @SuppressWarnings("unused")
  public static class ConditionConfig
      implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {

      String key = JsonUtils.getString(json, "key");

      switch (key) {
        case "wood":
          return () -> BUCKET_WOOD.ENABLED;
        case "clay":
          return () -> BUCKET_CLAY.ENABLED;
        case "stone":
          return () -> BUCKET_STONE.ENABLED;
        default:
          throw new JsonSyntaxException("Unknown config key [" + key + "]");
      }
    }
  }
}
