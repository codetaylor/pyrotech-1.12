package com.codetaylor.mc.pyrotech;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

@Config(modid = ModPyrotech.MOD_ID, name = ModPyrotech.MOD_ID + "/.modules")
public class ModPyrotechConfig {

  public static Map<String, Boolean> MODULES = new HashMap<>();

  static {
    MODULES.put("bloomery", true);
  }

  @SuppressWarnings("unused")
  public static class ConditionConfig
      implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {

      JsonArray modules = JsonUtils.getJsonArray(json, "modules");

      for (JsonElement element : modules) {
        String module = element.getAsString();

        if (!MODULES.containsKey(module)) {
          throw new JsonSyntaxException("Unknown module id: [" + module + "]");
        }

        if (!MODULES.get(module)) {
          return () -> false;
        }
      }

      return () -> true;
    }
  }
}
