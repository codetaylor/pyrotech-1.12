package com.codetaylor.mc.pyrotech.modules.tech.machine;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleTechMachine.MOD_ID, name = ModuleTechMachine.MOD_ID + "/" + "module.tech.Machine")
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
      this.put("pyrotech:cog_stone", 4);
      this.put("pyrotech:cog_flint", 8);
      this.put("pyrotech:cog_bone", 8);
      this.put("pyrotech:cog_iron", 16);
      this.put("pyrotech:cog_diamond", 64);
    }};

    public enum EnumCogDamageType {
      PerItem, PerOperation
    }

    @Config.Comment({
        "If set to PerItem, damage will be done to the cog for every item",
        "transferred. If set to PerOperation, damage will be done to the cog",
        "each time it makes a transfer, regardless of the size of the transfer.",
        "Default: " + "PerItem"
    })
    public EnumCogDamageType COG_DAMAGE_TYPE = EnumCogDamageType.PerItem;

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
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + false
    })
    public boolean KEEP_HEAT = false;

    @Config.Comment({
        "Modifies the burn time values of the fuel used.",
        "Default: " + 1.0
    })
    public double FUEL_BURN_TIME_MODIFIER = 1.0;

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
        "If true, all the pit kiln recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_PIT_KILN_RECIPES = true;

    @Config.Comment({
        "The recipe duration modifier for the inherited recipes.",
        "Default: " + 0.5
    })
    public double INHERITED_PIT_KILN_RECIPE_DURATION_MODIFIER = 0.5;

    @Config.Comment({
        "The failure chance modifier for the inherited recipes.",
        "Default: " + 0.25
    })
    public double INHERITED_PIT_KILN_RECIPE_FAILURE_CHANCE_MODIFIER = 0.25;
  }

  // ---------------------------------------------------------------------------
  // - Brick Kiln
  // ---------------------------------------------------------------------------

  public static BrickKiln BRICK_KILN = new BrickKiln();

  public static class BrickKiln {

    @Config.Comment({
        "If true, the device will use a skin that looks like parts of it are made",
        "from iron. Making the device from iron fits with the default mod progression,",
        "but may not fit with your modpack. Disabling this will use a different",
        "skin that looks like only the refractory bricks."
    })
    public boolean USE_IRON_SKIN = true;

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + true
    })
    public boolean KEEP_HEAT = true;

    @Config.Comment({
        "Modifies the burn time values of the fuel used.",
        "Default: " + 2.0
    })
    public double FUEL_BURN_TIME_MODIFIER = 2.0;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Default: " + 32
    })
    @Config.RangeInt(min = 1, max = 64)
    public int FUEL_SLOT_SIZE = 32;

    @Config.Comment({
        "If true, all the stone tier recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_STONE_TIER_RECIPES = true;

    @Config.Comment({
        "The recipe duration modifier for all inherited recipes.",
        "Default: " + 1.0
    })
    public double INHERITED_STONE_TIER_RECIPE_DURATION_MODIFIER = 1.0;

    @Config.Comment({
        "The failure chance modifier for all inherited recipes.",
        "Default: " + 0.25
    })
    public double INHERITED_STONE_TIER_RECIPE_FAILURE_CHANCE_MODIFIER = 0.25;
  }

  // ---------------------------------------------------------------------------
  // - Stone Oven
  // ---------------------------------------------------------------------------

  public static StoneOven STONE_OVEN = new StoneOven();

  public static class StoneOven {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + false
    })
    public boolean KEEP_HEAT = false;

    @Config.Comment({
        "Modifies the burn time values of the fuel used.",
        "Default: " + 1.0
    })
    public double FUEL_BURN_TIME_MODIFIER = 1.0;

    @Config.Comment({
        "Time to cook the entire input stack in ticks.",
        "Default: " + (2 * 60 * 20)
    })
    @Config.RangeInt(min = 1)
    public int COOK_TIME_TICKS = 2 * 60 * 20;

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
        "If true, all the drying rack recipes will also be available in the oven.",
        "Default: " + true
    })
    public boolean INHERIT_DRYING_RACK_RECIPES = true;

    @Config.Comment({
        "The recipe duration modifier for all inherited drying rack recipes.",
        "Default: " + 0.5
    })
    public double INHERITED_DRYING_RACK_RECIPE_DURATION_MODIFIER = 0.5;
  }

  // ---------------------------------------------------------------------------
  // - Brick Oven
  // ---------------------------------------------------------------------------

  public static BrickOven BRICK_OVEN = new BrickOven();

  public static class BrickOven {

    @Config.Comment({
        "If true, the device will use a skin that looks like parts of it are made",
        "from iron. Making the device from iron fits with the default mod progression,",
        "but may not fit with your modpack. Disabling this will use a different",
        "skin that looks like only the refractory bricks."
    })
    public boolean USE_IRON_SKIN = true;

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + true
    })
    public boolean KEEP_HEAT = true;

    @Config.Comment({
        "Modifies the burn time values of the fuel used.",
        "Default: " + 2.0
    })
    public double FUEL_BURN_TIME_MODIFIER = 2.0;

    @Config.Comment({
        "Time to cook the entire input stack in ticks.",
        "Default: " + (2 * 60 * 20)
    })
    @Config.RangeInt(min = 1)
    public int COOK_TIME_TICKS = 2 * 60 * 20;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Default: " + 32
    })
    @Config.RangeInt(min = 1, max = 64)
    public int FUEL_SLOT_SIZE = 32;

    @Config.Comment({
        "If true, all the stone tier recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_STONE_TIER_RECIPES = true;

    @Config.Comment({
        "The recipe duration modifier for all inherited recipes.",
        "Default: " + 1.0
    })
    public double INHERITED_STONE_TIER_RECIPE_DURATION_MODIFIER = 1.0;
  }

  // ---------------------------------------------------------------------------
  // - Stone Sawmill
  // ---------------------------------------------------------------------------

  public static StoneSawmill STONE_SAWMILL = new StoneSawmill();

  public static class StoneSawmill {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + false
    })
    public boolean KEEP_HEAT = false;

    @Config.Comment({
        "Modifies the burn time values of the fuel used.",
        "Default: " + 1.0
    })
    public double FUEL_BURN_TIME_MODIFIER = 1.0;

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
  // - Brick Sawmill
  // ---------------------------------------------------------------------------

  public static BrickSawmill BRICK_SAWMILL = new BrickSawmill();

  public static class BrickSawmill {

    @Config.Comment({
        "If true, the device will use a skin that looks like parts of it are made",
        "from iron. Making the device from iron fits with the default mod progression,",
        "but may not fit with your modpack. Disabling this will use a different",
        "skin that looks like only the refractory bricks."
    })
    public boolean USE_IRON_SKIN = true;

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + true
    })
    public boolean KEEP_HEAT = true;

    @Config.Comment({
        "Modifies the burn time values of the fuel used.",
        "Default: " + 2.0
    })
    public double FUEL_BURN_TIME_MODIFIER = 2.0;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Default: " + 32
    })
    @Config.RangeInt(min = 1, max = 64)
    public int FUEL_SLOT_SIZE = 32;

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

    @Config.Comment({
        "If true, all the stone tier recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_STONE_TIER_RECIPES = true;

    @Config.Comment({
        "The recipe duration modifier for all inherited recipes.",
        "Default: " + 1.0
    })
    public double INHERITED_STONE_TIER_RECIPE_DURATION_MODIFIER = 1.0;
  }

  // ---------------------------------------------------------------------------
  // - Stone Crucible
  // ---------------------------------------------------------------------------

  public static StoneCrucible STONE_CRUCIBLE = new StoneCrucible();

  public static class StoneCrucible {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + false
    })
    public boolean KEEP_HEAT = false;

    @Config.Comment({
        "Modifies the burn time values of the fuel used.",
        "Default: " + 1.0
    })
    public double FUEL_BURN_TIME_MODIFIER = 1.0;

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

  // ---------------------------------------------------------------------------
  // - Brick Crucible
  // ---------------------------------------------------------------------------

  public static BrickCrucible BRICK_CRUCIBLE = new BrickCrucible();

  public static class BrickCrucible {

    @Config.Comment({
        "If true, the device will use a skin that looks like parts of it are made",
        "from iron. Making the device from iron fits with the default mod progression,",
        "but may not fit with your modpack. Disabling this will use a different",
        "skin that looks like only the refractory bricks."
    })
    public boolean USE_IRON_SKIN = true;

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set to true to deactivate when a recipe completes.",
        "The worker will need to be re-lit when it deactivates.",
        "Default: " + true
    })
    public boolean KEEP_HEAT = true;

    @Config.Comment({
        "Modifies the burn time values of the fuel used.",
        "Default: " + 2.0
    })
    public double FUEL_BURN_TIME_MODIFIER = 2.0;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Default: " + 32
    })
    @Config.RangeInt(min = 1, max = 64)
    public int FUEL_SLOT_SIZE = 32;

    @Config.Comment({
        "Controls the size of the output tank in millibuckets.",
        "Default: " + 8000
    })
    @Config.RangeInt(min = 1)
    public int OUTPUT_TANK_SIZE = 8000;

    @Config.Comment({
        "If true, the machine will process all input items at the same time.",
        "If false, the machine will process one recipe at a time.",
        "Default: " + false
    })
    public boolean ASYNCHRONOUS_OPERATION = false;

    @Config.Comment({
        "If true, all the stone tier recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_STONE_TIER_RECIPES = true;

    @Config.Comment({
        "The recipe duration modifier for all inherited recipes.",
        "Default: " + 1.0
    })
    public double INHERITED_STONE_TIER_RECIPE_DURATION_MODIFIER = 1.0;
  }

}
