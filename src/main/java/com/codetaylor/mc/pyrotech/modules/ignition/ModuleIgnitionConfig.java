package com.codetaylor.mc.pyrotech.modules.ignition;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleIgnition.MOD_ID, name = ModuleIgnition.MOD_ID + "/" + "module.Ignition")
public class ModuleIgnitionConfig {

  @Config.Ignore
  public static Stages STAGES_OIL_LAMP = null;

  // ---------------------------------------------------------------------------
  // - Fiber Torch
  // ---------------------------------------------------------------------------

  public static FiberTorch FIBER_TORCH = new FiberTorch();

  public static class FiberTorch {

    @Config.Comment({
        "The light value of the torch when lit.",
        "Default: " + 9
    })
    @Config.RangeInt(min = 0, max = 15)
    public int LIGHT_VALUE = 9;

    @Config.Comment({
        "Set to true if the torch should be extinguished in the rain.",
        "Default: " + true
    })
    public boolean EXTINGUISHED_BY_RAIN = true;

    @Config.Comment({
        "Set to true if the torch should burn up after the set duration.",
        "Default: " + true
    })
    public boolean BURNS_UP = true;

    @Config.Comment({
        "The number of ticks before the torch burns up.",
        "Default: " + (14 * 60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int DURATION = 14 * 60 * 20;

    @Config.Comment({
        "Random range of ticks to -/+ from the duration.",
        "duration += rand[-variant, +variant]",
        "Default: " + (4 * 60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int DURATION_VARIANT = 4 * 60 * 20;

    @Config.Comment({
        "The amount of fire damage done to an entity colliding with the torch.",
        "Set to zero to disable.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int FIRE_DAMAGE = 1;
  }

  // ---------------------------------------------------------------------------
  // - Stone Torch
  // ---------------------------------------------------------------------------

  public static StoneTorch STONE_TORCH = new StoneTorch();

  public static class StoneTorch {

    @Config.Comment({
        "The light value of the torch when lit.",
        "Default: " + 9
    })
    @Config.RangeInt(min = 0, max = 15)
    public int LIGHT_VALUE = 9;

    @Config.Comment({
        "Set to true if the torch should be extinguished in the rain.",
        "Default: " + true
    })
    public boolean EXTINGUISHED_BY_RAIN = true;

    @Config.Comment({
        "Set to true if the torch should burn up after the set duration.",
        "Default: " + false
    })
    public boolean BURNS_UP = false;

    @Config.Comment({
        "The number of ticks before the torch burns up.",
        "Default: " + (14 * 60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int DURATION = 14 * 60 * 20;

    @Config.Comment({
        "Random range of ticks to -/+ from the duration.",
        "duration += rand[-variant, +variant]",
        "Default: " + (4 * 60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int DURATION_VARIANT = 4 * 60 * 20;

    @Config.Comment({
        "The amount of fire damage done to an entity colliding with the torch.",
        "Set to zero to disable.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int FIRE_DAMAGE = 1;
  }

  // ---------------------------------------------------------------------------
  // - Oil Lamp
  // ---------------------------------------------------------------------------

  public static OilLamp OIL_LAMP = new OilLamp();

  public static class OilLamp {

    @Config.Comment({
        "The amount of fluid this container can hold in mB.",
        "Default: " + 2000
    })
    public int CAPACITY = 2000;

    @Config.Comment({
        "Maps valid fuel liquids to mB used per minute.",
        "Format: I:(liquid_name)=(int)"
    })
    public Map<String, Integer> ALLOWED_FUEL = new HashMap<String, Integer>() {{
      this.put("pyroberry_wine", 12);
      this.put("lamp_oil", 10);
    }};

    @Config.Comment({
        "The light value.",
        "Default: " + 12
    })
    public int LIGHT_VALUE = 12;
  }

  // ---------------------------------------------------------------------------
  // - Igniters
  // ---------------------------------------------------------------------------

  public static Igniters IGNITERS = new Igniters();

  public static class Igniters {

    @Config.Comment({
        "The durability of the bow drill.",
        "Default: " + 32
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int BOW_DRILL_DURABILITY = 32;

    @Config.Comment({
        "Defines how many ticks it takes to start a fire while using the bow drill.",
        "Default: " + (20)
    })
    @Config.RangeInt(min = 0)
    public int BOW_DRILL_USE_DURATION_TICKS = 20;

    @Config.Comment({
        "Defines how many cooldown ticks applied after using the bow drill.",
        "Default: " + (20)
    })
    @Config.RangeInt(min = 0)
    public int BOW_DRILL_COOLDOWN_TICKS = 20;

    @Config.Comment({
        "The durability of the durable bow drill.",
        "Default: " + 48
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int DURABLE_BOW_DRILL_DURABILITY = 48;

    @Config.Comment({
        "Defines how many ticks it takes to start a fire while using the durable bow drill.",
        "Default: " + (20)
    })
    @Config.RangeInt(min = 0)
    public int DURABLE_BOW_DRILL_USE_DURATION_TICKS = 20;

    @Config.Comment({
        "Defines how many cooldown ticks applied after using the durable bow drill.",
        "Default: " + (20)
    })
    @Config.RangeInt(min = 0)
    public int DURABLE_BOW_DRILL_COOLDOWN_TICKS = 20;

    @Config.Comment({
        "The durability of the flint and tinder.",
        "Default: 8"
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int FLINT_AND_TINDER_DURABILITY = 8;

    @Config.Comment({
        "Defines how many ticks it takes to start a fire while using the flint and tinder.",
        "Default: " + (4 * 20)
    })
    @Config.RangeInt(min = 0)
    public int FLINT_AND_TINDER_USE_DURATION_TICKS = 4 * 20;

    @Config.Comment({
        "Defines how many cooldown ticks applied after using the flint and tinder.",
        "Default: " + (20)
    })
    @Config.RangeInt(min = 0)
    public int FLINT_AND_TINDER_COOLDOWN_TICKS = 20;

    @Config.Comment({
        "The max stack size of the matchsticks.",
        "Default: " + 64
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MATCHSTICK_MAX_STACK_SIZE = 64;

    @Config.Comment({
        "Defines how many ticks it takes to start a fire while using the matchsticks.",
        "Default: " + 5
    })
    @Config.RangeInt(min = 0)
    public int MATCHSTICK_USE_DURATION_TICKS = 5;

    @Config.Comment({
        "How many ticks the player must wait before using a matchstick again.",
        "Default: " + (2 * 20)
    })
    @Config.RangeInt(min = 0)
    public int MATCHSTICK_COOLDOWN_DURATION_TICKS = 2 * 20;
  }

}
