package com.codetaylor.mc.pyrotech;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.module.ModuleManager;
import com.codetaylor.mc.pyrotech.modules.bucket.ModuleBucket;
import com.codetaylor.mc.pyrotech.modules.plugin.dropt.ModulePluginDropt;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import java.util.HashSet;
import java.util.Set;

@Mod(
    modid = ModPyrotech.MOD_ID,
    version = ModPyrotech.VERSION,
    name = ModPyrotech.NAME
    //@@DEPENDENCIES@@
)
public class ModPyrotech {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final String VERSION = Reference.VERSION;
  public static final String NAME = Reference.NAME;

  @SuppressWarnings("unused")
  @Mod.Instance
  public static ModPyrotech INSTANCE;

  public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID) {

    @Override
    public ItemStack getTabIconItem() {

      if (INSTANCE.isModuleEnabled(ModuleTechBasic.class)) {
        return new ItemStack(ModuleTechBasic.Blocks.CAMPFIRE, 1, 0);

      } else {
        return new ItemStack(ModuleBlocks.LOG_PILE);
      }
    }
  };

  private final ModuleManager moduleManager;

  private Set<Class<? extends ModuleBase>> registeredModules = new HashSet<>();

  public ModPyrotech() {

    this.moduleManager = new ModuleManager(MOD_ID);
  }

  @Mod.EventHandler
  public void onConstructionEvent(FMLConstructionEvent event) {

    this.moduleManager.registerModules(
        ModulePyrotech.class
    );

    // --- MODULES ---

    if (ModPyrotechConfig.MODULES.get(ModuleBloomery.MODULE_ID)) {
      this.registerModule(ModuleBloomery.class);
    }

    if (ModPyrotechConfig.MODULES.get(ModuleBucket.MODULE_ID)) {
      this.registerModule(ModuleBucket.class);
    }

    if (ModPyrotechConfig.MODULES.get(ModuleStorage.MODULE_ID)) {
      this.registerModule(ModuleStorage.class);
    }

    if (ModPyrotechConfig.MODULES.get(ModuleTechBasic.MODULE_ID)) {
      this.registerModule(ModuleTechBasic.class);
    }

    if (ModPyrotechConfig.MODULES.get(ModuleTechMachine.MODULE_ID)) {
      this.registerModule(ModuleTechMachine.class);
    }

    if (ModPyrotechConfig.MODULES.get(ModuleWorldGen.MODULE_ID)) {
      this.registerModule(ModuleWorldGen.class);
    }

    // --- PLUGINS ---

    if (ModPyrotechConfig.MODULES.get(ModulePluginDropt.MODULE_ID)) {
      this.registerModule(ModulePluginDropt.class);
    }

    this.moduleManager.onConstructionEvent();
    this.moduleManager.routeFMLStateEvent(event);
  }

  private void registerModule(Class<? extends ModuleBase> moduleClass) {

    this.moduleManager.registerModules(moduleClass);
    this.registeredModules.add(moduleClass);
  }

  public boolean isModuleEnabled(Class<? extends ModuleBase> moduleClass) {

    return this.registeredModules.contains(moduleClass);
  }

  @Mod.EventHandler
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onInitializationEvent(FMLInitializationEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onLoadCompleteEvent(FMLLoadCompleteEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerAboutToStartEvent(FMLServerAboutToStartEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStartingEvent(FMLServerStartingEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStartedEvent(FMLServerStartedEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStoppingEvent(FMLServerStoppingEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStoppedEvent(FMLServerStoppedEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

}
