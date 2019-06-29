package com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import crafttweaker.mc1120.events.ActionApplyEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrTEventHandler {

  private final ModuleCore moduleCore;

  /**
   * Ensure the events don't fire more than once
   */
  private boolean actionApplyEventFired;

  public CrTEventHandler(ModuleCore moduleCore) {

    this.moduleCore = moduleCore;
  }

  @SubscribeEvent
  public void on(ActionApplyEvent event) {

    if (!this.actionApplyEventFired) {
      this.actionApplyEventFired = true;
      this.moduleCore.onPostInitializationPreCrT();
    }
  }
}
