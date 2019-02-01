package com.codetaylor.mc.pyrotech.modules.worldgen;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "module.WorldGen")
public class ModuleWorldGenConfig {

  // ---------------------------------------------------------------------------
  // - World Gen
  // ---------------------------------------------------------------------------

  public static WorldGenFossil FOSSIL = new WorldGenFossil();

  public static class WorldGenFossil {

    @Config.Comment({
        "Set to false to disable this worldgen.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "How many times will the generator try to spawn this worldgen.",
        "Default: " + 15
    })
    @Config.RangeInt(min = 0)
    public int CHANCES_TO_SPAWN = 15;

    @Config.Comment({
        "The minimum world height at which this will gen.",
        "Must be larger than or equal to MAX_HEIGHT.",
        "Default: " + 40
    })
    @Config.RangeInt(min = 0, max = 255)
    public int MIN_HEIGHT = 40;

    @Config.Comment({
        "The maximum world height at which this will gen.",
        "Must be less than or equal to MIN_HEIGHT.",
        "Default: " + 120
    })
    @Config.RangeInt(min = 0, max = 255)
    public int MAX_HEIGHT = 120;

    @Config.Comment({
        "The minimum size of the cluster.",
        "Must be larger than or equal to MAX_VEIN_SIZE.",
        "NOTE: This worldgen is basic and does nothing to mitigate cascading",
        "world gen if the vein size is too large.",
        "Default: " + 10
    })
    @Config.RangeInt(min = 0)
    public int MIN_VEIN_SIZE = 10;

    @Config.Comment({
        "The maximum size of the cluster.",
        "Must be less than or equal to MIN_VEIN_SIZE.",
        "NOTE: This worldgen is basic and does nothing to mitigate cascading",
        "world gen if the vein size is too large.",
        "Default: " + 15
    })
    @Config.RangeInt(min = 0)
    public int MAX_VEIN_SIZE = 15;
  }

  public static WorldGenLimestone LIMESTONE = new WorldGenLimestone();

  public static class WorldGenLimestone {

    @Config.Comment({
        "Set to false to disable this worldgen.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "How many times will the generator try to spawn this worldgen.",
        "Default: " + 15
    })
    @Config.RangeInt(min = 0)
    public int CHANCES_TO_SPAWN = 15;

    @Config.Comment({
        "The minimum world height at which this will gen.",
        "Must be larger than or equal to MAX_HEIGHT.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 0, max = 255)
    public int MIN_HEIGHT = 8;

    @Config.Comment({
        "The maximum world height at which this will gen.",
        "Must be less than or equal to MIN_HEIGHT.",
        "Default: " + 120
    })
    @Config.RangeInt(min = 0, max = 255)
    public int MAX_HEIGHT = 100;

    @Config.Comment({
        "The minimum size of the cluster.",
        "Must be larger than or equal to MAX_VEIN_SIZE.",
        "NOTE: This worldgen is basic and does nothing to mitigate cascading",
        "world gen if the vein size is too large.",
        "Default: " + 10
    })
    @Config.RangeInt(min = 1)
    public int MIN_VEIN_SIZE = 10;

    @Config.Comment({
        "The maximum size of the cluster.",
        "Must be less than or equal to MIN_VEIN_SIZE.",
        "NOTE: This worldgen is basic and does nothing to mitigate cascading",
        "world gen if the vein size is too large.",
        "Default: " + 20
    })
    @Config.RangeInt(min = 0)
    public int MAX_VEIN_SIZE = 20;
  }

  public static WorldGenRocks ROCKS = new WorldGenRocks();

  public static class WorldGenRocks {

    @Config.Comment({
        "Set to false to disable this worldgen.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "How many times will the generator try to spawn this worldgen.",
        "Default: " + 15
    })
    @Config.RangeInt(min = 0)
    public int CHANCES_TO_SPAWN = 4;

    @Config.Comment({
        "The density of each successful spawn.",
        "Larger density means more are placed.",
        "Default: " + 0.625
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double DENSITY = 0.0625;
  }

}
