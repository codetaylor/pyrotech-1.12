package com.codetaylor.mc.pyrotech.modules.patreon;

import net.minecraftforge.common.config.Config;

@Config(modid = ModulePatreon.MOD_ID, name = ModulePatreon.MOD_ID + "/" + ".patreon")
public class ModulePatreonConfig {

  // ---------------------------------------------------------------------------
  // - Client
  // ---------------------------------------------------------------------------

  public static Client CLIENT = new Client();

  public static class Client {

    @Config.Comment({
        "If this is true, only your Patreon effects will be disabled on your client.",
        "Default: " + false
    })
    public boolean DISABLE_YOUR_PATREON_EFFECTS = false;

    @Config.Comment({
        "If this is true, all Patreon effects will be disabled on your client.",
        "Default: " + false
    })
    public boolean DISABLE_ALL_PATREON_EFFECTS = false;
  }

}
