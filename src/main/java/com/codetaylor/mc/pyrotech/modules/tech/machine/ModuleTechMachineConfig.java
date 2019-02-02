package com.codetaylor.mc.pyrotech.modules.tech.machine;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "module.tech.Machine")
public class ModuleTechMachineConfig {

  // ---------------------------------------------------------------------------
  // - General
  // ---------------------------------------------------------------------------

  public static General GENERAL = new General();

  public static class General {

    @Config.Comment({
        "The light level of stone machines when lit.",
        "Default: " + 9
    })
    @Config.RangeInt(min = 0, max = 15)
    public int STONE_MACHINE_LIGHT_LEVEL = 9;
  }

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
  // - Cogs
  // ---------------------------------------------------------------------------

  public static Cogs COGS = new Cogs();

  public static class Cogs {

    @Config.Comment({
        "Durability of the wooden cog.",
        "Default: " + 64
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int WOOD_DURABILITY = 64;

    @Config.Comment({
        "Durability of the stone cog.",
        "Default: " + 64 * 4
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int STONE_DURABILITY = (64 * 4);

    @Config.Comment({
        "Durability of the flint cog.",
        "Default: " + (64 * 16)
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int FLINT_DURABILITY = (64 * 16);

    @Config.Comment({
        "Durability of the bone cog.",
        "Default: " + (64 * 16)
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int BONE_DURABILITY = (64 * 16);

    @Config.Comment({
        "Durability of the iron cog.",
        "Default: " + (64 * 64)
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int IRON_DURABILITY = (64 * 64);

    @Config.Comment({
        "Durability of the diamond cog.",
        "Default: " + (64 * 256)
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int DIAMOND_DURABILITY = (64 * 256);
  }

  // ---------------------------------------------------------------------------
  // - Stone Hopper
  // ---------------------------------------------------------------------------

  public static StoneHopper STONE_HOPPER = new StoneHopper();

  public static class StoneHopper {

    @Config.Comment({
        "A list of valid cogs for the hopper.",
        "NOTE: Items provided here are assumed to have durability.",
        "Format is (domain):(path) for the item and (amount) for the number",
        "of items that the cog will transfer in one attempt."
    })
    public Map<String, Integer> COGS = new HashMap<String, Integer>() {{
      this.put("pyrotech:cog_wood", 1);
      this.put("pyrotech:cog_stone", 1);
      this.put("pyrotech:cog_flint", 4);
      this.put("pyrotech:cog_bone", 4);
      this.put("pyrotech:cog_iron", 16);
      this.put("pyrotech:cog_diamond", 64);
    }};

    /**
     * Returns the cog transfer amount for the given item resource location,
     * or -1 if it isn't in the list.
     */
    public int getCogTransferAmount(@Nullable ResourceLocation resourceLocation) {

      if (resourceLocation != null) {
        Integer result = this.COGS.get(resourceLocation.toString());

        if (result != null) {
          return result;
        }
      }

      return -1;
    }

    @Config.Comment({
        "How many ticks between transfer attempts.",
        "Default: " + 40
    })
    public int TRANSFER_INTERVAL_TICKS = 40;
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
