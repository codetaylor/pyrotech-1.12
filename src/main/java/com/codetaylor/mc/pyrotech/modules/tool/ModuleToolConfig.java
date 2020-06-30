package com.codetaylor.mc.pyrotech.modules.tool;

import net.minecraftforge.common.config.Config;

import java.util.LinkedHashMap;
import java.util.Map;

@Config(modid = ModuleTool.MOD_ID, name = ModuleTool.MOD_ID + "/" + "module.Tool")
public class ModuleToolConfig {

  public static QuartzTools QUARTZ_TOOLS = new QuartzTools();

  public static class QuartzTools {

    @Config.Comment({
        "The speed multiplier that is applied when the tool is active.",
        "Default: " + 3
    })
    @Config.RangeDouble(min = 1)
    public double ACTIVE_HARVEST_SPEED_SCALAR = 3;

    @Config.Comment({
        "The damage multiplier that is applied when the sword is active.",
        "Default: " + 4
    })
    @Config.RangeDouble(min = 1)
    public double ACTIVE_SWORD_DAMAGE_SCALAR = 3;
  }

  public static RedstoneTools REDSTONE_TOOLS = new RedstoneTools();

  public static class RedstoneTools {

    @Config.Comment({
        "The duration of the redstone tools' special abilities in ticks.",
        "Default: " + (10 * 20)
    })
    public int ACTIVE_DURATION_TICKS = 10 * 20;

    @Config.Comment({
        "The chance that the tool will take damage when it is active.",
        "For example, if the value is 0.25, there will be a 75% chance",
        "that the tool will not take damage when damaged.",
        "Default: " + 0.125
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double ACTIVE_DAMAGE_CHANCE = 0.125;

    @Config.Comment({
        "The chance that the tool will activate when damaged while inactive.",
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double INACTIVE_ACTIVATION_CHANCE = 0.05;

    @Config.Comment({
        "The chance that the tool will activate when damaged while active.",
        "Default: " + 0.25
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double ACTIVE_ACTIVATION_CHANCE = 0.25;

    @Config.Comment({
        "The speed multiplier that is applied when the tool is active.",
        "Default: " + 2
    })
    @Config.RangeDouble(min = 1)
    public double ACTIVE_HARVEST_SPEED_SCALAR = 2;

    @Config.Comment({
        "The damage multiplier that is applied when the sword is active.",
        "Default: " + 2
    })
    @Config.RangeDouble(min = 1)
    public double ACTIVE_SWORD_DAMAGE_SCALAR = 2;

    @Config.Comment({
        "Chance to restore durability when near redstone ore and dense redstone ore.",
        "Default: " + 0.125
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double PROXIMITY_REPAIR_CHANCE = 0.125;

    @Config.Comment({
        "The amount of repair for each size variant: rocks, small, large.",
        "Default: [1, 2, 3]"
    })
    public int[] PROXIMITY_REPAIR_AMOUNTS = {1, 2, 3};
  }

  @Config.Comment({
      "The durability of the vanilla type tools, excluding hammers.",
      "To change the durability of hammers, see the hammers section in core.cfg."
  })
  public static Map<String, Integer> DURABILITY = new LinkedHashMap<>();

  static {
    DURABILITY.put("crude", 32);
    DURABILITY.put("bone", 150);
    DURABILITY.put("flint", 150);
    DURABILITY.put("redstone", 200);
    DURABILITY.put("quartz", 350);
    DURABILITY.put("obsidian", 1400);
  }

  @Config.Comment({
      "The harvest levels of each tool material, excluding hammers.",
      "To change the harvest level of hammers, see the hammers section in core.cfg."
  })
  public static Map<String, Integer> HARVEST_LEVEL = new LinkedHashMap<>();

  static {
    HARVEST_LEVEL.put("crude", 0);
    HARVEST_LEVEL.put("bone", 1);
    HARVEST_LEVEL.put("flint", 1);
    HARVEST_LEVEL.put("redstone", 1);
    HARVEST_LEVEL.put("quartz", 1);
    HARVEST_LEVEL.put("obsidian", 2);
  }

  /**
   * @param type the type
   * @return the harvest level or -1 if not in the map
   */
  public static int getHarvestLevel(String type) {

    return HARVEST_LEVEL.getOrDefault(type, -1);
  }

  @Config.Comment({
      "The durability of the shears.",
      "For reference, the durability of the vanilla shears is 238."
  })
  public static Map<String, Integer> SHEARS_DURABILITY = new LinkedHashMap<>();

  static {
    SHEARS_DURABILITY.put("clay", 60);
    SHEARS_DURABILITY.put("stone", 90);
    SHEARS_DURABILITY.put("bone", 120);
    SHEARS_DURABILITY.put("flint", 120);
    SHEARS_DURABILITY.put("gold", 30);
    SHEARS_DURABILITY.put("diamond", 952);
    SHEARS_DURABILITY.put("obsidian", 852);
  }

  @Config.Comment({
      "The durability of the crude fishing rod.",
      "Default: " + 16
  })
  @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
  public static int CRUDE_FISHING_ROD_DURABILITY = 16;

  @Config.Comment({
      "Chance that the crude fishing rod will break when damaged.",
      "The chance to break increases linearly as the rod takes damage.",
      "This is the maximum chance when the rod is at 100% damage.",
      "chance = (damage / maxDamage) * configChance",
      "Default: " + 0.65
  })
  public static double CRUDE_FISHING_ROD_BREAK_CHANCE = 0.65;
}
