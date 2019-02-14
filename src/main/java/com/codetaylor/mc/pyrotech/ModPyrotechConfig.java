package com.codetaylor.mc.pyrotech;

import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnition;
import com.codetaylor.mc.pyrotech.modules.plugin.dropt.ModulePluginDropt;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleTool;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGen;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BooleanSupplier;

@Config(modid = ModPyrotech.MOD_ID, name = ModPyrotech.MOD_ID + "/.modules")
public class ModPyrotechConfig {

  public static Map<String, Boolean> MODULES = new TreeMap<>();

  static {
    MODULES.put(ModuleTechBloomery.MODULE_ID, true);
    MODULES.put(ModuleBucket.MODULE_ID, true);
    MODULES.put(ModuleIgnition.MODULE_ID, true);
    MODULES.put(ModuleStorage.MODULE_ID, true);
    MODULES.put(ModuleTechBasic.MODULE_ID, true);
    MODULES.put(ModuleTechMachine.MODULE_ID, true);
    MODULES.put(ModuleTechRefractory.MODULE_ID, true);
    MODULES.put(ModuleTool.MODULE_ID, true);
    MODULES.put(ModuleWorldGen.MODULE_ID, true);

    MODULES.put(ModulePluginDropt.MODULE_ID, true);
  }

  @SuppressWarnings("unused")
  public static class ConditionConfig
      implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {

      if (json.has("include")) {
        JsonArray include = JsonUtils.getJsonArray(json, "include");

        for (JsonElement element : include) {
          String module = element.getAsString();

          if (!MODULES.containsKey(module)) {
            throw new JsonSyntaxException("Unknown module id: [" + module + "]");
          }

          if (!MODULES.get(module)) {
            return () -> false;
          }
        }
      }

      if (json.has("exclude")) {
        JsonArray exclude = JsonUtils.getJsonArray(json, "exclude");

        for (JsonElement element : exclude) {
          String module = element.getAsString();

          if (!MODULES.containsKey(module)) {
            throw new JsonSyntaxException("Unknown module id: [" + module + "]");
          }

          if (MODULES.get(module)) {
            return () -> false;
          }
        }
      }

      return () -> true;
    }
  }
}
