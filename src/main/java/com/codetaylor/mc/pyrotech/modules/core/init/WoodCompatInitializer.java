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

    data.mods.put("minecraft", WoodCompatInitializer.expand(

        // planks
        "log:0;planks:0",
        "log:1;planks:1",
        "log:2;planks:2",
        "log:3;planks:3",
        "log2:0;planks:4",
        "log2:1;planks:5",

        // slab
        "planks:0;wooden_slab:0",
        "planks:1;wooden_slab:1",
        "planks:2;wooden_slab:2",
        "planks:3;wooden_slab:3",
        "planks:4;wooden_slab:4",
        "planks:5;wooden_slab:5"
    ));

    data.mods.put("biomesoplenty", WoodCompatInitializer.expand(

        // planks
        "log_0:4;planks_0",
        "log_0:5;planks_0:1",
        "log_0:6;planks_0:2",
        "log_0:7;planks_0:3",
        "log_1:4;planks_0:4",
        "log_1:5;planks_0:5",
        "log_1:6;planks_0:6",
        "log_1:7;planks_0:7",
        "log_2:4;planks_0:8",
        "log_2:5;planks_0:9",
        "log_2:6;planks_0:10",
        "log_2:7;planks_0:11",
        "log_3:4;planks_0:12",
        "log_3:5;planks_0:13",
        "log_3:6;planks_0:14",
        "log_3:7;planks_0:15",

        // slabs
        "planks_0:0;wood_slab_0",
        "planks_0:1;wood_slab_0:1",
        "planks_0:2;wood_slab_0:2",
        "planks_0:3;wood_slab_0:3",
        "planks_0:4;wood_slab_0:4",
        "planks_0:5;wood_slab_0:5",
        "planks_0:6;wood_slab_0:6",
        "planks_0:7;wood_slab_0:7",
        "planks_0:8;wood_slab_1:0",
        "planks_0:9;wood_slab_1:1",
        "planks_0:10;wood_slab_1:2",
        "planks_0:11;wood_slab_1:3",
        "planks_0:12;wood_slab_1:4",
        "planks_0:13;wood_slab_1:5",
        "planks_0:14;wood_slab_1:6",
        "planks_0:15;wood_slab_1:7"
    ));

    data.mods.put("natura", WoodCompatInitializer.expand(

        // planks
        "overworld_logs;overworld_planks",
        "overworld_logs:1;overworld_planks:1",
        "overworld_logs:2;overworld_planks:2",
        "overworld_logs:3;overworld_planks:3",
        "overworld_logs2:0;overworld_planks:4",
        "overworld_logs2:1;overworld_planks:5",
        "overworld_logs2:2;overworld_planks:6",
        "overworld_logs2:3;overworld_planks:7",
        "redwood_logs:*;overworld_planks:8",

        "nether_logs:0;nether_planks:0",
        "nether_logs:2;nether_planks:2",
        "nether_logs:3;nether_planks:3",
        "nether_logs2:*;nether_planks:1",

        // slabs
        "overworld_planks;overworld_slab",
        "overworld_planks:1;overworld_slab:1",
        "overworld_planks:2;overworld_slab:2",
        "overworld_planks:3;overworld_slab:3",
        "overworld_planks:4;overworld_slab:4",
        "overworld_planks:5;overworld_slab2:0",
        "overworld_planks:6;overworld_slab2:1",
        "overworld_planks:7;overworld_slab2:2",
        "overworld_planks:8;overworld_slab2:3",

        "nether_planks:0;nether_slab:0",
        "nether_planks:1;nether_slab:1",
        "nether_planks:2;nether_slab:2",
        "nether_planks:3;nether_slab:3"
    ));

    // TODO: modded planks and slabs

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
        "Entries are in the format (input);(output)",
        "Entry item strings are in the format: (path):(meta)"
    };

    public Map<String, Map<String, String>> mods = new TreeMap<>();
  }

  public static Map<String, String> expand(String... strings) {

    Map<String, String> result = new TreeMap<>();

    for (String plank : strings) {
      String[] split = plank.split(";");
      result.put(split[0], split[1]);
    }

    return result;
  }

  private WoodCompatInitializer() {
    //
  }

}
