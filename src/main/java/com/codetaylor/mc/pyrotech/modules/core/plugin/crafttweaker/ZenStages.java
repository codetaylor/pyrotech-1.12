package com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocAppend;
import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.library.Stages;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.Stages")
@ZenDocAppend({"docs/include/stages.example.md"})
@ZenClass("mods.pyrotech.Stages")
public class ZenStages {

  private final Stages stages;

  private ZenStages(Stages stages) {

    this.stages = stages;
  }

  public Stages getStages() {

    return this.stages;
  }

  private static Object[] extractStages(Object[] stages) {

    Object[] extractedStages = new Object[stages.length];

    for (int i = 0; i < stages.length; i++) {

      if (stages[i] instanceof ZenStages) {
        extractedStages[i] = ((ZenStages) stages[i]).getStages();

      } else {
        extractedStages[i] = stages[i];
      }
    }
    return extractedStages;
  }

  // --- ZenMethods ---

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "stages", info = "accepts an array of string or Stages objects")
      }
  )
  @ZenMethod
  public static ZenStages and(Object[] stages) {

    return new ZenStages(Stages.and(ZenStages.extractStages(stages)));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "stages", info = "accepts an array of string or Stages objects")
      }
  )
  @ZenMethod
  public static ZenStages or(Object[] stages) {

    return new ZenStages(Stages.or(ZenStages.extractStages(stages)));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "stage")
      }
  )
  @ZenMethod
  public static ZenStages not(String stage) {

    return new ZenStages(Stages.not(stage));
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "stages")
      }
  )
  @ZenMethod
  public static ZenStages not(ZenStages stages) {

    return new ZenStages(Stages.not(stages.getStages()));
  }
}
