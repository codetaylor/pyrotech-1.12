package com.codetaylor.mc.pyrotech.modules.ignition.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.athenaeum.tools.ZenDocPrepend;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.OilLamp")
@ZenDocPrepend({"docs/include/header.md"})
@ZenClass("mods.pyrotech.OilLamp")
public class ZenOilLamp {

  @ZenDocMethod(
      order = 0,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the device."
      }
  )
  @ZenMethod
  public static void setGameStages(ZenStages stages) {

    ModuleIgnitionConfig.STAGES_OIL_LAMP = stages.getStages();
  }
}
