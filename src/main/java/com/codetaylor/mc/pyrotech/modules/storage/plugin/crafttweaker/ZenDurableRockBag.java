package com.codetaylor.mc.pyrotech.modules.storage.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.dropt.modules.dropt.compat.crafttweaker.export.ZenDocClass;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorageConfig;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.DurableRockBag")
@ZenClass("mods.pyrotech.DurableRockBag")
public class ZenDurableRockBag {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the device."
      }
  )
  @ZenMethod
  public static void setGameStages(ZenStages stages) {

    ModuleStorageConfig.STAGES_DURABLE_ROCK_BAG = stages.getStages();
  }
}
