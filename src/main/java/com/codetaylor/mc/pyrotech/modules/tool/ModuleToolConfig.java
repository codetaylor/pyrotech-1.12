package com.codetaylor.mc.pyrotech.modules.tool;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModulePyrotech.MOD_ID, name = ModulePyrotech.MOD_ID + "/" + "module.Tool")
public class ModuleToolConfig {

  public static Map<String, Integer> DURABILITY = new HashMap<>();

  static {
    DURABILITY.put("crude", 32);
    DURABILITY.put("bone", 150);
    DURABILITY.put("flint", 150);
  }

}
