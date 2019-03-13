package com.codetaylor.mc.pyrotech.modules.plugin.dropt;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import net.minecraft.creativetab.CreativeTabs;

public class ModulePluginDropt
    extends ModuleBase {

  public static final String MODULE_ID = "plugin.dropt";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public ModulePluginDropt() {

    super(0, MOD_ID);

    this.registerIntegrationPlugin(
        "dropt",
        "com.codetaylor.mc.pyrotech.modules.plugin.dropt.plugin.dropt.PluginDropt"
    );
  }
}
