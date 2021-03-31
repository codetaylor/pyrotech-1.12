package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public final class SoundInitializer {

  public static void onRegister(IForgeRegistry<SoundEvent> registry) {

    Injector injector = new Injector();

    String[] resourcePaths = new String[]{
        "dense_redstone_ore_activate_00",
        "dense_redstone_ore_activate_01",
        "dense_redstone_ore_activate_02",
        "dense_redstone_ore_activate_03",
        "dense_redstone_ore_activate_04",
        "dense_redstone_ore_activate_05",
        "dense_redstone_ore_activate_06",
        "dense_redstone_ore_activate_07",

        "redstone_tool_activate_00",
        "redstone_tool_activate_01",
        "redstone_tool_activate_02",
        "redstone_tool_activate_03",
        "redstone_tool_activate_04",
        "redstone_tool_activate_05"
    };

    for (String resourcePath : resourcePaths) {
      SoundInitializer.register(registry, injector, resourcePath);
    }
  }

  private static void register(IForgeRegistry<SoundEvent> registry, Injector injector, String resourcePath) {

    ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, resourcePath);
    SoundEvent soundEvent = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
    registry.register(soundEvent);
    injector.inject(ModuleCore.Sounds.class, resourcePath.toUpperCase(), soundEvent);
  }
}