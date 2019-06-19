package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.pyrotech.library.JsonInitializer;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

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

    // Minecraft
    data.mods.put("minecraft", new WoodCompatModData()
        .planks(
            "log:0;planks:0",
            "log:1;planks:1",
            "log:2;planks:2",
            "log:3;planks:3",
            "log2:0;planks:4",
            "log2:1;planks:5"
        )
        .slabs(
            "planks:0;wooden_slab:0",
            "planks:1;wooden_slab:1",
            "planks:2;wooden_slab:2",
            "planks:3;wooden_slab:3",
            "planks:4;wooden_slab:4",
            "planks:5;wooden_slab:5"
        )
    );

    // TODO: hook for modded planks and slabs

    return data;
  }

  public static class WoodCompatData {

    private String[] __comments = {
        "WARNING: All changes should be made to the file with the name Custom",
        "in the title. Changes made to the Generated file will be overwritten.",
        "",
        "This file defines input and output pairs for auto-generating recipes",
        "for the Chopping Block.",
        "",
        "Item strings for recipe inputs and outputs are in the format: (path):(meta)"
    };

    public Map<String, WoodCompatModData> mods = new TreeMap<>();
  }

  public static class WoodCompatModData {

    public Map<String, String> planks = new TreeMap<>();
    public Map<String, String> slabs = new TreeMap<>();

    public WoodCompatModData planks(String... strings) {

      for (String plank : strings) {
        String[] split = plank.split(";");
        this.planks.put(split[0], split[1]);
      }

      return this;
    }

    public WoodCompatModData slabs(String... strings) {

      for (String slab : strings) {
        String[] split = slab.split(";");
        this.planks.put(split[0], split[1]);
      }

      return this;
    }
  }

  private WoodCompatInitializer() {
    //
  }

}
