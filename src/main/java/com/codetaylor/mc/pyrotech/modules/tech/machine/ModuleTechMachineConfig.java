package com.codetaylor.mc.pyrotech.modules.tech.machine;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "module.tech.Machine")
public class ModuleTechMachineConfig {

  // ---------------------------------------------------------------------------
  // - Sawmill Blades
  // ---------------------------------------------------------------------------

  public static SawmillBlades SAWMILL_BLADES = new SawmillBlades();

  public static class SawmillBlades {

    @Config.Comment({
        "Durability of the stone sawmill blade.",
        "Default: " + 32
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int STONE_DURABILITY = 32;

    @Config.Comment({
        "Durability of the flint sawmill blade.",
        "Default: " + 150
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int FLINT_DURABILITY = 150;

    @Config.Comment({
        "Durability of the bone sawmill blade.",
        "Default: " + 150
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int BONE_DURABILITY = 150;

    @Config.Comment({
        "Durability of the iron sawmill blade.",
        "Default: " + 500
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int IRON_DURABILITY = 500;

    @Config.Comment({
        "Durability of the diamond sawmill blade.",
        "Default: " + 1500
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int DIAMOND_DURABILITY = 1500;
  }

  // ---------------------------------------------------------------------------
  // - Stone Kiln
  // ---------------------------------------------------------------------------

  public static StoneKiln STONE_KILN = new StoneKiln();

  public static class StoneKiln {

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + false
    })
    public boolean KEEP_HEAT = false;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1, max = 64)
    public int FUEL_SLOT_SIZE = 16;
  }

  // ---------------------------------------------------------------------------
  // - Stone Oven
  // ---------------------------------------------------------------------------

  public static StoneOven STONE_OVEN = new StoneOven();

  public static class StoneOven {

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + false
    })
    public boolean KEEP_HEAT = false;

    @Config.Comment({
        "Time to cook the entire input stack in ticks.",
        "Default: " + (80 * 20)
    })
    @Config.RangeInt(min = 1)
    public int COOK_TIME_TICKS = 80 * 20;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1, max = 64)
    public int FUEL_SLOT_SIZE = 16;
  }

  // ---------------------------------------------------------------------------
  // - Stone Sawmill
  // ---------------------------------------------------------------------------

  public static StoneSawmill STONE_SAWMILL = new StoneSawmill();

  public static class StoneSawmill {

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + false
    })
    public boolean KEEP_HEAT = false;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1, max = 64)
    public int FUEL_SLOT_SIZE = 16;

    @Config.Comment({
        "Controls how much damage a spinning blade will do to an entity.",
        "Default: " + 3
    })
    @Config.RangeDouble(min = 0)
    public double ENTITY_DAMAGE_FROM_BLADE = 3;

    @Config.Comment({
        "Set to false to disable damaging blades.",
        "Default: " + true
    })
    public boolean DAMAGE_BLADES = true;

    @Config.Comment({
        "Chance of producing wood chips per second of operation.",
        "Default: " + 0.075
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double WOOD_CHIPS_CHANCE = 0.075;
  }

  // ---------------------------------------------------------------------------
  // - Stone Crucible
  // ---------------------------------------------------------------------------

  public static StoneCrucible STONE_CRUCIBLE = new StoneCrucible();

  public static class StoneCrucible {

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + false
    })
    public boolean KEEP_HEAT = false;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1, max = 64)
    public int FUEL_SLOT_SIZE = 16;

    @Config.Comment({
        "Controls the size of the output tank in millibuckets.",
        "Default: " + 4000
    })
    @Config.RangeInt(min = 1)
    public int OUTPUT_TANK_SIZE = 4000;

    @Config.Comment({
        "If true, the machine will process all input items at the same time.",
        "If false, the machine will process one recipe at a time.",
        "Default: " + false
    })
    public boolean ASYNCHRONOUS_OPERATION = false;
  }

}
