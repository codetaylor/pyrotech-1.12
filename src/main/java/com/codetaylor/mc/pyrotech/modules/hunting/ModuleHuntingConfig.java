package com.codetaylor.mc.pyrotech.modules.hunting;

import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleHunting.MOD_ID, name = ModuleHunting.MOD_ID + "/" + "module.Hunting")
public class ModuleHuntingConfig {

  public static Drops DROPS = new Drops();

  public static class Drops {

    @Config.Comment({
        "If true, leather drops will be removed from entities.",
        "Default: " + true
    })
    public boolean REMOVE_LEATHER_DROPS = true;

    @Config.Comment({
        "Drops added here will be added to the mob drops and auto-captured by",
        "the carcass. The key is the entity resource location and the value",
        "is the drop. The count is an integer and the chance is a float.",
        "Meta is optional and defaults to 0.",
        "",
        "String key format is (domain):(path)",
        "String value format is (domain):(path):(meta);(count);(chance)"
    })
    @Config.RequiresMcRestart
    public Map<String, String> DROP_MAP = new HashMap<String, String>() {{
      this.put("minecraft:pig", "pyrotech:hide_pig:0;1;0.85");
      this.put("minecraft:sheep", "pyrotech:hide_sheep_sheared:0;1;0.85");
      this.put("minecraft:cow", "pyrotech:pelt_cow:0;1;0.85");
      this.put("minecraft:mooshroom", "pyrotech:pelt_mooshroom:0;1;0.65");
      this.put("minecraft:polar_bear", "pyrotech:pelt_polar_bear:0;1;0.85");
      this.put("minecraft:bat", "pyrotech:pelt_bat:0;1;0.85");
      this.put("minecraft:horse", "pyrotech:pelt_horse:0;1;0.85");
      this.put("minecraft:donkey", "pyrotech:pelt_horse:0;1;0.85");
    }};

    @Config.Comment({
        "Items added to this list will be captured by the carcass.",
        "Meta is optional and defaults to 0.",
        "",
        "String format is (domain):(path):(meta)"
    })
    @Config.RequiresMcRestart
    public String[] DROP_CAPTURE_LIST = new String[] {
        "minecraft:beef",
        "minecraft:chicken",
        "minecraft:mutton",
        "minecraft:rabbit",
        "minecraft:rabbit_foot",
        "minecraft:porkchop"
    };
  }
}
