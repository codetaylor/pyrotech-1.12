package com.codetaylor.mc.pyrotech.modules.tech.refractory;

import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleTechRefractory.MOD_ID, name = ModuleTechRefractory.MOD_ID + "/" + "module.tech.Refractory")
public class ModuleTechRefractoryConfig {

  // ---------------------------------------------------------------------------
  // - Client
  // ---------------------------------------------------------------------------

  public static Client CLIENT = new Client();

  public static class Client {

    @Config.Comment({
        "Items listed here will have a tooltip applied indicating that they are",
        "valid for use as refractory structural blocks.",
        "String format: (domain):(path):(meta)",
        "If meta is * then it will match all meta.",
    })
    public String[] VALID_REFRACTORY_TOOLTIP = new String[0];
  }

  // ---------------------------------------------------------------------------
  // - General
  // ---------------------------------------------------------------------------

  public static General GENERAL = new General();

  public static class General {

    @Config.Comment({
        "The maximum number of blocks that can be lit in a pit burn or refractory burn.",
        "Default: " + 27
    })
    @Config.RangeInt(min = 1, max = 512)
    public int MAXIMUM_BURN_SIZE_BLOCKS = 27;
  }

  // ---------------------------------------------------------------------------
  // - Refractory
  // ---------------------------------------------------------------------------

  public static Refractory REFRACTORY = new Refractory();

  public static class Refractory {

    @Config.Comment({
        "Multiplicative modifier applied to the failure chance of recipes that",
        "don't require a refractory (pit burning recipes) when burned in an enclosure",
        "comprised completely of valid refractory blocks.",
        "Default: " + 0.1
    })
    @Config.RangeDouble(min = 0)
    public double REFRACTORY_FAILURE_MODIFIER = 0.1;

    @Config.Comment({
        "Maximum chance for a recipe item to fail conversion.",
        "Recipe chances still apply, this is just a cap.",
        "Default: " + 0.99
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double MAX_FAILURE_CHANCE = 0.99;

    @Config.Comment({
        "Minimum chance for a recipe item to fail conversion.",
        "Recipe chances still apply, this is just a cap.",
        "Default: " + 0.01
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double MIN_FAILURE_CHANCE = 0.01;

    @Config.Comment({
        "The maximum fluid capacity of an active pile in mB.",
        "Recipes can be set to have more of a chance to fail the more fluid an",
        "active pile has inside. This encourages the use of the tar draining",
        "mechanics.",
        "Default: " + 500
    })
    @Config.RangeInt(min = 1)
    public int ACTIVE_PILE_MAX_FLUID_CAPACITY = 500;

    @Config.Comment({
        "The duration in ticks that 1 mB of fluid will burn in the Tar Collector.",
        "Other fluids may be added here.",
        "Range: [1,+int]"
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
        "Note: These strings are for in-world blocks and the meta may be different",
        "than the itemStack version of the block.",
        "",
        "Note: Don't forget to add the item stack tooltip for any blocks you add",
        "here in the Client section."
    })
    public String[] REFRACTORY_BRICKS = new String[0];

    @Config.Comment({
        "Multiplicative modifier applied to every recipe in this device.",
        "recipeDurationTicks = recipeDurationTicks * BASE_RECIPE_DURATION_MODIFIER",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double BASE_RECIPE_DURATION_MODIFIER = 1;

    @Config.Comment({
        "Multiplicative modifier applied to every refractory recipe in this device.",
        "recipeDurationTicks = recipeDurationTicks * BASE_RECIPE_DURATION_MODIFIER * REFRACTORY_RECIPE_DURATION_MODIFIER",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double REFRACTORY_RECIPE_DURATION_MODIFIER = 1;
  }

  // ---------------------------------------------------------------------------
  // - Stone Tar Collector
  // ---------------------------------------------------------------------------

  public static StoneTarCollector STONE_TAR_COLLECTOR = new StoneTarCollector();

  public static class StoneTarCollector {

    @Config.Comment({
        "How many smoke particles a burning collector will emit per tick.",
        "Remember, burning tar is supposed to be smokey and gross.",
        "Default: " + 10
    })
    @Config.RangeInt(min = 0)
    public int SMOKE_PARTICLES_PER_TICK = 10;

    @Config.Comment({
        "Fluid capacity of the tar collector in mB.",
        "Default: " + 2000
    })
    @Config.RangeInt(min = 1)
    public int CAPACITY = 2000;

    @Config.Comment({
        "The temperature that the container considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Default: " + 450
    })
    @Config.RangeInt
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "If false, the container will break when a hot fluid is placed inside,",
        "and the fluid will spawn in the world where the tank was.",
        "Default: " + false
    })
    public boolean HOLDS_HOT_FLUIDS = false;
  }

  // ---------------------------------------------------------------------------
  // - Brick Tar Collector
  // ---------------------------------------------------------------------------

  public static BrickTarCollector BRICK_TAR_COLLECTOR = new BrickTarCollector();

  public static class BrickTarCollector {

    @Config.Comment({
        "How many smoke particles a burning collector will emit per tick.",
        "Remember, burning tar is supposed to be smokey and gross.",
        "Default: " + 10
    })
    @Config.RangeInt(min = 0)
    public int SMOKE_PARTICLES_PER_TICK = 10;

    @Config.Comment({
        "Fluid capacity of the tar collector in mB.",
        "Default: " + 4000
    })
    @Config.RangeInt(min = 1)
    public int CAPACITY = 4000;

    @Config.Comment({
        "The temperature that the container considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Default: " + 450
    })
    @Config.RangeInt
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "If false, the container will break when a hot fluid is placed inside,",
        "and the fluid will spawn in the world where the tank was.",
        "Default: " + true
    })
    public boolean HOLDS_HOT_FLUIDS = true;
  }

  // ---------------------------------------------------------------------------
  // - Stone Tar Drain
  // ---------------------------------------------------------------------------

  public static StoneTarDrain STONE_TAR_DRAIN = new StoneTarDrain();

  public static class StoneTarDrain {

    @Config.Comment({
        "Fluid capacity of the tar drain in mb.",
        "Default: " + 1000
    })
    @Config.RangeInt(min = 1)
    public int CAPACITY = 1000;

    @Config.Comment({
        "The temperature that the container considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Default: " + 450
    })
    @Config.RangeInt
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "If false, the container will break when a hot fluid is placed inside,",
        "and the fluid will spawn in the world where the tank was.",
        "Default: " + false
    })
    public boolean HOLDS_HOT_FLUIDS = false;

    @Config.Comment({
        "The range of this drain. Drains in a range equal to (RANGE * 2) + 1 out",
        "in front of the block. The front is the side with the big hole.",
        "Default: " + 1
    })
    public int RANGE = 1;

    @Config.Comment({
        "If true, the drain will collect fluid source blocks directly in front",
        "of the side with the big hole."
    })
    public boolean ALLOW_SOURCE_DRAIN = true;

    @Config.Comment({
        "If true, the drain will collect from all tile entities with fluid handler",
        "capabilities, not just the collectors."
    })
    public boolean ALLOW_TILE_DRAIN = true;
  }

  // ---------------------------------------------------------------------------
  // - Brick Tar Drain
  // ---------------------------------------------------------------------------

  public static BrickTarDrain BRICK_TAR_DRAIN = new BrickTarDrain();

  public static class BrickTarDrain {

    @Config.Comment({
        "Fluid capacity of the tar drain in mb.",
        "Default: " + 2000
    })
    @Config.RangeInt(min = 1)
    public int CAPACITY = 2000;

    @Config.Comment({
        "The temperature that the container considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Default: " + 450
    })
    @Config.RangeInt
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "If false, the container will break when a hot fluid is placed inside,",
        "and the fluid will spawn in the world where the tank was.",
        "Default: " + true
    })
    public boolean HOLDS_HOT_FLUIDS = true;

    @Config.Comment({
        "The range of this drain. Drains in a range equal to (RANGE * 2) + 1 out",
        "in front of the block. The front is the side with the big hole.",
        "Default: " + 2
    })
    public int RANGE = 2;

    @Config.Comment({
        "If true, the drain will collect fluid source blocks directly in front",
        "of the side with the big hole."
    })
    public boolean ALLOW_SOURCE_DRAIN = true;

    @Config.Comment({
        "If true, the drain will collect from all tile entities with fluid handler",
        "capabilities, not just the collectors."
    })
    public boolean ALLOW_TILE_DRAIN = true;
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
    @Config.RangeInt(min = 1)
    public int COAL_TAR_BURN_TIME_TICKS = 6400;

    @Config.Comment({
        "Wood tar burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 4800
    })
    @Config.RangeInt(min = 1)
    public int WOOD_TAR_BURN_TIME_TICKS = 4800;
  }
}
