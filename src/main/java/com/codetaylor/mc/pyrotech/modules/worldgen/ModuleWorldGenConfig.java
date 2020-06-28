package com.codetaylor.mc.pyrotech.modules.worldgen;

import net.minecraftforge.common.config.Config;

@Config(modid = ModuleWorldGen.MOD_ID, name = ModuleWorldGen.MOD_ID + "/" + "module.WorldGen")
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
        "An int array of dimension id's that this is allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_WHITELIST = {
        0
    };

    @Config.Comment({
        "An int array of dimension id's that this is not allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_BLACKLIST = new int[0];

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
        "An int array of dimension id's that this is allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_WHITELIST = {
        0
    };

    @Config.Comment({
        "An int array of dimension id's that this is not allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_BLACKLIST = new int[0];

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
        "Default: " + 100
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

  public static WorldGenDenseCoalOre DENSE_COAL_ORE = new WorldGenDenseCoalOre();

  public static class WorldGenDenseCoalOre {

    @Config.Comment({
        "Set to false to disable this worldgen.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "An int array of dimension id's that this is allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_WHITELIST = {
        0
    };

    @Config.Comment({
        "An int array of dimension id's that this is not allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_BLACKLIST = new int[0];

    @Config.Comment({
        "The minimum world height at which this will gen.",
        "Must be larger than or equal to MAX_HEIGHT.",
        "Default: " + 0
    })
    @Config.RangeInt(min = 0, max = 255)
    public int MIN_HEIGHT = 0;

    @Config.Comment({
        "The maximum world height at which this will gen.",
        "Must be less than or equal to MIN_HEIGHT.",
        "Default: " + 32
    })
    @Config.RangeInt(min = 0, max = 255)
    public int MAX_HEIGHT = 32;

    @Config.Comment({
        "The minimum size of the cluster.",
        "Must be larger than or equal to MAX_VEIN_SIZE.",
        "NOTE: This worldgen is basic and does nothing to mitigate cascading",
        "world gen if the vein size is too large.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 1)
    public int MIN_VEIN_SIZE = 3;

    @Config.Comment({
        "The maximum size of the cluster.",
        "Must be less than or equal to MIN_VEIN_SIZE.",
        "NOTE: This worldgen is basic and does nothing to mitigate cascading",
        "world gen if the vein size is too large.",
        "Default: " + 6
    })
    @Config.RangeInt(min = 0)
    public int MAX_VEIN_SIZE = 6;
  }

  public static WorldGenDenseNetherCoalOre DENSE_NETHER_COAL_ORE = new WorldGenDenseNetherCoalOre();

  public static class WorldGenDenseNetherCoalOre {

    @Config.Comment({
        "Set to false to disable this worldgen.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "An int array of dimension id's that this is allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_WHITELIST = {
        -1
    };

    @Config.Comment({
        "An int array of dimension id's that this is not allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_BLACKLIST = new int[0];

    @Config.Comment({
        "How many times will the generator try to spawn this worldgen.",
        "Default: " + 30
    })
    @Config.RangeInt(min = 0)
    public int CHANCES_TO_SPAWN = 30;

    @Config.Comment({
        "The minimum world height at which this will gen.",
        "Must be larger than or equal to MAX_HEIGHT.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0, max = 255)
    public int MIN_HEIGHT = 1;

    @Config.Comment({
        "The maximum world height at which this will gen.",
        "Must be less than or equal to MIN_HEIGHT.",
        "Default: " + 127
    })
    @Config.RangeInt(min = 0, max = 255)
    public int MAX_HEIGHT = 127;

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
        "An int array of dimension id's that this is allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_WHITELIST = {
        0
    };

    @Config.Comment({
        "An int array of dimension id's that this is not allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_BLACKLIST = new int[0];

    @Config.Comment({
        "How many times will the generator try to spawn this worldgen.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 0)
    public int CHANCES_TO_SPAWN = 4;

    @Config.Comment({
        "The density of each successful spawn.",
        "Larger density means more are placed.",
        "Default: " + 0.0625
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double DENSITY = 0.0625;
  }

  public static WorldGenDenseRedstoneOre DENSE_REDSTONE_ORE = new WorldGenDenseRedstoneOre();

  public static class WorldGenDenseRedstoneOre {

    @Config.Comment({
        "Set to false to disable this worldgen.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "An int array of dimension id's that this is allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_WHITELIST = {
        0
    };

    @Config.Comment({
        "An int array of dimension id's that this is not allowed to generate in.",
        "Whitelist takes precedence over blacklist."
    })
    public int[] DIMENSION_BLACKLIST = new int[0];

    @Config.Comment({
        "Percent chance to attempt to spawn in a chunk.",
        "Default: " + 0.25
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double CHANCE_TO_SPAWN = 0.25;

    @Config.Comment({
        "Percent chance to spawn redstone ore in stone below the dense ore.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double CHANCE_TO_SPAWN_REDSTONE_ORE = 0.5;

    @Config.Comment({
        "An int array of the y value's [min, max] used for spawning.",
        "Default: [5, 25]"
    })
    public int[] VERTICAL_BOUNDS = {5, 25};
  }

}
