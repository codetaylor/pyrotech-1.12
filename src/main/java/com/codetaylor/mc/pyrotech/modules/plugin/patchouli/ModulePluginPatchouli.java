package com.codetaylor.mc.pyrotech.modules.plugin.patchouli;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.packer.PackAPI;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModulePluginPatchouli
    extends ModuleBase {

  public static final String MODULE_ID = "plugin.patchouli";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public ModulePluginPatchouli() {

    super(0, MOD_ID);
  }

  @Override
  public void onClientPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onClientPreInitializationEvent(event);

    String resourcePath = "textures/gui/book/atlas/packed.json";
    ResourceLocation resourceLocation = new ResourceLocation(ModPyrotech.MOD_ID, resourcePath);
    PackAPI.register(resourceLocation);
  }
}
