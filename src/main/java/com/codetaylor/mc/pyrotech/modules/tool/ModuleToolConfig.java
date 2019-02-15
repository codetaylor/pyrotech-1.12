package com.codetaylor.mc.pyrotech.modules.tool;

import net.minecraftforge.common.config.Config;

import java.util.LinkedHashMap;
import java.util.Map;

@Config(modid = ModuleTool.MOD_ID, name = ModuleTool.MOD_ID + "/" + "module.Tool")
public class ModuleToolConfig {

  @Config.Comment({
      "The durability of the vanilla type tools, excluding hammers."
  })
  public static Map<String, Integer> DURABILITY = new LinkedHashMap<>();

  static {
    DURABILITY.put("crude", 32);
    DURABILITY.put("bone", 150);
    DURABILITY.put("flint", 150);
    DURABILITY.put("obsidian", 1400);
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
