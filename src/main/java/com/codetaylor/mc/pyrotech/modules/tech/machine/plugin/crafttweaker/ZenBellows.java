package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.athenaeum.tools.ZenDocPrepend;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.Bellows")
@ZenClass("mods.pyrotech.Bellows")
@ZenDocPrepend({"docs/include/header.md"})
public class ZenBellows {

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

    ModuleTechMachineConfig.STAGES_BELLOWS = stages.getStages();
  }
}
