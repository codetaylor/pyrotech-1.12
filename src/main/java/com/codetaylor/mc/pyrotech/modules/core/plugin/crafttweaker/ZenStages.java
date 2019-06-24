package com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker;

import com.codetaylor.mc.pyrotech.library.Stages;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

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

  @ZenMethod
  public static ZenStages and(Object[] stages) {

    return new ZenStages(Stages.and(ZenStages.extractStages(stages)));
  }

  @ZenMethod
  public static ZenStages or(Object[] stages) {

    return new ZenStages(Stages.or(ZenStages.extractStages(stages)));
  }

  @ZenMethod
  public static ZenStages not(String stage) {

    return new ZenStages(Stages.not(stage));
  }

  @ZenMethod
  public static ZenStages not(ZenStages stages) {

    return new ZenStages(Stages.not(stages.getStages()));
  }
}
