package com.codetaylor.mc.pyrotech.modules.plugin.patchouli;

import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleStorage.MOD_ID, name = ModuleStorage.MOD_ID + "/" + "plugin.Patchouli")
public class ModulePatchouliConfig {

  @Config.Comment({
      "If true, the player will be given the Pyrotech book when they start.",
      "Default: " + true
  })
  public static boolean GIVE_BOOK_ON_START = true;
}
