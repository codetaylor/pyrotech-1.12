package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.pyrotech.library.JsonInitializer;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class WoodCompatInitializer {

  public static void onPreInitialization(Path configurationPath) {

    JsonInitializer.create(
        configurationPath.resolve(ModuleCore.MOD_ID),
        ".core.Wood-Generated.json",
        ".core.Wood-Custom.json",
        () -> WoodCompatInitializer.createGeneratedData(new WoodCompatData()),
        ModuleCore.LOGGER
    );
  }

  public static WoodCompatData createGeneratedData(WoodCompatData data) {

    WoodCompatModData modData = new WoodCompatModData();

    // Planks
    modData.planks.put("log:0", "planks:0");
    modData.planks.put("log:1", "planks:1");
    modData.planks.put("log:2", "planks:2");
    modData.planks.put("log:3", "planks:3");
    modData.planks.put("log2:0", "planks:4");
    modData.planks.put("log2:1", "planks:5");

    // Slabs
    modData.slabs.put("planks:0", "wooden_slab:0");
    modData.slabs.put("planks:1", "wooden_slab:1");
    modData.slabs.put("planks:2", "wooden_slab:2");
    modData.slabs.put("planks:3", "wooden_slab:3");
    modData.slabs.put("planks:4", "wooden_slab:4");
    modData.slabs.put("planks:5", "wooden_slab:5");

    data.mods.put("minecraft", modData);

    // TODO: hook for modded planks and slabs

    return data;
  }

  public static class WoodCompatData {

    public Map<String, WoodCompatModData> mods = new HashMap<>();
  }

  public static class WoodCompatModData {

    public Map<String, String> planks = new HashMap<>();
    public Map<String, String> slabs = new HashMap<>();
  }

  private WoodCompatInitializer() {
    //
  }

}
