package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.world.WorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModuleWorldGen {

  public static void register() {

    if (ModulePyrotechConfig.WORLD_GEN.ENABLED) {
      GameRegistry.registerWorldGenerator(new WorldGenerator(), 1);
    }
  }

  private ModuleWorldGen() {
    //
  }

}
