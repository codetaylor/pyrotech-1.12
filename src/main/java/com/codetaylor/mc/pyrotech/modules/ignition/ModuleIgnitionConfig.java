package com.codetaylor.mc.pyrotech.modules.ignition;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "module.Ignition")
public class ModuleIgnitionConfig {

  // ---------------------------------------------------------------------------
  // - Fiber Torch
  // ---------------------------------------------------------------------------

  public static FiberTorch FIBER_TORCH = new FiberTorch();

  public static class FiberTorch {

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
    public int DURATION = 14 * 60 * 20;

    @Config.Comment({
        "Random range of ticks to -/+ from the duration.",
        "duration += rand[-variant, +variant]",
        "Default: " + (4 * 60 * 20)
    })
    public int DURATION_VARIANT = 4 * 60 * 20;
  }

  // ---------------------------------------------------------------------------
  // - Stone Torch
  // ---------------------------------------------------------------------------

  public static StoneTorch STONE_TORCH = new StoneTorch();

  public static class StoneTorch {

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
    public int DURATION = 14 * 60 * 20;

    @Config.Comment({
        "Random range of ticks to -/+ from the duration.",
        "duration += rand[-variant, +variant]",
        "Default: " + (4 * 60 * 20)
    })
    public int DURATION_VARIANT = 4 * 60 * 20;
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
    public int BOW_DRILL_DURABILITY = 32;

    @Config.Comment({
        "Defines how many ticks it takes to start a fire while using the bow drill.",
        "Default: " + (3 * 20)
    })
    public int BOW_DRILL_USE_DURATION_TICKS = 3 * 20;

    @Config.Comment({
        "The durability of the flint and tinder.",
        "Default: 8"
    })
    public int FLINT_AND_TINDER_DURABILITY = 8;

    @Config.Comment({
        "Defines how many ticks it takes to start a fire while using the flint and tinder.",
        "Default: " + (5 * 20)
    })
    public int FLINT_AND_TINDER_USE_DURATION_TICKS = 5 * 20;

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
    public int MATCHSTICK_USE_DURATION_TICKS = 5;

    @Config.Comment({
        "How many ticks the player must wait before using a matchstick again.",
        "Default: " + (2 * 20)
    })
    public int MATCHSTICK_COOLDOWN_DURATION_TICKS = 2 * 20;
  }

}
