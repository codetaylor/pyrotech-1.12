package com.codetaylor.mc.pyrotech.modules.worldgen;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import net.minecraftforge.common.config.Config;

@Config(modid = ModulePyrotech.MOD_ID, name = ModulePyrotech.MOD_ID + "/" + "module.WorldGen")
public class ModuleWorldGenConfig {

  // ---------------------------------------------------------------------------
  // - World Gen
  // ---------------------------------------------------------------------------

  public static WorldGenFossil FOSSIL = new WorldGenFossil();

  public static class WorldGenFossil {

    public boolean ENABLED = true;
    public int CHANCES_TO_SPAWN = 15;
    public int MIN_HEIGHT = 40;
    public int MAX_HEIGHT = 120;
    public int MIN_VEIN_SIZE = 10;
    public int MAX_VEIN_SIZE = 20;
  }

  public static WorldGenLimestone LIMESTONE = new WorldGenLimestone();

  public static class WorldGenLimestone {

    public boolean ENABLED = true;
    public int CHANCES_TO_SPAWN = 15;
    public int MIN_HEIGHT = 8;
    public int MAX_HEIGHT = 100;
    public int MIN_VEIN_SIZE = 10;
    public int MAX_VEIN_SIZE = 20;
  }
}
