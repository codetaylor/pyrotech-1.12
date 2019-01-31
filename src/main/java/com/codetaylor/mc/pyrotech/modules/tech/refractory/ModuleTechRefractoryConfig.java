package com.codetaylor.mc.pyrotech.modules.tech.refractory;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "module.tech.Refractory")
public class ModuleTechRefractoryConfig {

  // ---------------------------------------------------------------------------
  // - Refractory
  // ---------------------------------------------------------------------------

  public static Refractory REFRACTORY = new Refractory();

  public static class Refractory {

    @Config.Comment({
        "Maximum chance for a recipe item to fail conversion.",
        "Recipe chances still apply, this is just a cap.",
        "Default: " + 0.95
    })
    public double MAX_FAILURE_CHANCE = 0.95;

    @Config.Comment({
        "Minimum chance for a recipe item to fail conversion.",
        "Recipe chances still apply, this is just a cap.",
        "Default: " + 0.05
    })
    public double MIN_FAILURE_CHANCE = 0.05;

    @Config.Comment({
        "The maximum fluid capacity of an active pile in mb.",
        "Recipes can be set to have more of a chance to fail the more fluid an",
        "active pile has inside. This encourages the use of the tar draining",
        "mechanics.",
        "Default: " + 500
    })
    public int ACTIVE_PILE_MAX_FLUID_CAPACITY = 500;

    @Config.Comment({
        "The duration in ticks that 1 mb of fluid will burn in the Tar Collector.",
        "Other fluids may be added here."
    })
    public Map<String, Integer> FLUID_BURN_TICKS = new HashMap<String, Integer>() {{
      this.put("wood_tar", 20);
      this.put("coal_tar", 40);
    }};

    @Config.Comment({
        "List of additional valid blocks for the refractory.",
        "String format: (domain):(path):(meta)",
        "If meta is * then it will match all meta.",
        "",
        "Note: these strings are for in-world blocks and the meta may be different",
        "than the itemStack version of the block."
    })
    public String[] REFRACTORY_BRICKS = new String[0];
  }

  // ---------------------------------------------------------------------------
  // - Tar Collector
  // ---------------------------------------------------------------------------

  public static TarCollector TAR_COLLECTOR = new TarCollector();

  public static class TarCollector {

    @Config.Comment({
        "How many smoke particles a burning collector will emit per tick.",
        "Remember, burning tar is supposed to be smokey and gross.",
        "Default: " + 10
    })
    public int SMOKE_PARTICLES_PER_TICK = 10;

    @Config.Comment({
        "Fluid capacity of the tar collector in mb.",
        "Default: " + 4000
    })
    public int TAR_COLLECTOR_CAPACITY = 4000;
  }

  // ---------------------------------------------------------------------------
  // - Tar Drain
  // ---------------------------------------------------------------------------

  public static TarDrain TAR_DRAIN = new TarDrain();

  public static class TarDrain {

    @Config.Comment({
        "Fluid capacity of the tar drain in mb.",
        "Default: " + 1000
    })
    public int TAR_DRAIN_CAPACITY = 1000;
  }

  // ---------------------------------------------------------------------------
  // - Fuel
  // ---------------------------------------------------------------------------

  public static Fuel FUEL = new Fuel();

  public static class Fuel {

    @Config.Comment({
        "Coal tar burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 6400
    })
    public int COAL_TAR_BURN_TIME_TICKS = 6400;

    @Config.Comment({
        "Wood tar burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 4800
    })
    public int WOOD_TAR_BURN_TIME_TICKS = 4800;
  }
}
