package com.codetaylor.mc.pyrotech.modules.tool;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "module.Tool")
public class ModuleToolConfig {

  public static Map<String, Integer> DURABILITY = new HashMap<>();

  static {
    DURABILITY.put("crude", 32);
    DURABILITY.put("bone", 150);
    DURABILITY.put("flint", 150);
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
