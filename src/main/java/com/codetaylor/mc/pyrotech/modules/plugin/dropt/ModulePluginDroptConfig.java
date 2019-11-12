package com.codetaylor.mc.pyrotech.modules.plugin.dropt;

import net.minecraftforge.common.config.Config;

import java.util.Map;
import java.util.TreeMap;

@Config(modid = ModulePluginDropt.MOD_ID, name = ModulePluginDropt.MOD_ID + "/" + "plugin.Dropt")
public class ModulePluginDroptConfig {

  public static final Map<String, Boolean> ENABLED_RULES = new TreeMap<>();

  static {
    String[] rules = {
        "grass_tall",
        "dirt",
        "grass",
        "sand",
        "sand_red",
        "gravel",
        "clay",
        "ore_coal",
        "ore_fossil",
        "sandstone",
        "limestone",
        "limestone_cobbled",
        "stone",
        "cobblestone",
        "diorite",
        "diorite_smooth",
        "diorite_cobbled",
        "andesite",
        "andesite_smooth",
        "andesite_cobbled",
        "granite",
        "granite_smooth",
        "granite_cobbled"
    };

    for (String rule : rules) {
      ENABLED_RULES.put(rule, true);
    }
  }

  @Config.Comment({
      "Set to true to significantly reduce the number of block drops from explosions.",
      "Can assist with lag during wither fights.",
      "",
      "Blocks affected: dirt, grass, sand, red sand, gravel, limestone, stone,",
      "cobblestone, diorite, smooth diorite, cobbled diorite, andesite, smooth",
      "andesite, cobbled andesite, granite, smooth granite, and cobbled granite."
  })
  public static boolean REDUCE_EXPLOSION_DROPS = false;

}
