package com.codetaylor.mc.pyrotech.modules.tech.machine;

import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Config;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
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
        "Durability of the gold sawmill blade.",
        "Default: " + 500
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int GOLD_DURABILITY = 32;

    @Config.Comment({
        "Durability of the diamond sawmill blade.",
        "Default: " + 1500
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int DIAMOND_DURABILITY = 1500;

    @Config.Comment({
        "Durability of the obsidian sawmill blade.",
        "Default: " + 1345
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int OBSIDIAN_DURABILITY = 1345;
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
        "Durability of the gold cog.",
        "Default: " + 33
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int GOLD_DURABILITY = 33;

    @Config.Comment({
        "Durability of the diamond cog.",
        "Default: " + (64 * 256)
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int DIAMOND_DURABILITY = (64 * 256);

    @Config.Comment({
        "Durability of the obsidian cog.",
        "Default: " + (int) ((64 * 256) * 0.8968)
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int OBSIDIAN_DURABILITY = (int) ((64 * 256) * 0.8968);
  }

  // ---------------------------------------------------------------------------
  // - Stone Hopper
  // ---------------------------------------------------------------------------

  public static StoneHopper STONE_HOPPER = new StoneHopper();

  public static class StoneHopper {

    @Config.Comment({
        "A list of valid cogs for the hopper.",
        "NOTE: Items provided here are assumed to have durability.",
        "String format is (domain):(path) for the item and (integer) for the",
        "number of items that the cog will transfer in one attempt."
    })
    public Map<String, Integer> COGS = new LinkedHashMap<String, Integer>() {{
      this.put("pyrotech:cog_wood", 1);
      this.put("pyrotech:cog_stone", 4);
      this.put("pyrotech:cog_flint", 8);
      this.put("pyrotech:cog_bone", 8);
      this.put("pyrotech:cog_iron", 16);
      this.put("pyrotech:cog_gold", 1);
      this.put("pyrotech:cog_obsidian", 32);
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
  // - Bellows
  // ---------------------------------------------------------------------------

  public static Bellows BELLOWS = new Bellows();

  public static class Bellows {

    @Config.Comment({
        "The number of ticks that the device takes to return to the up position.",
        "Default: " + 10
    })
    @Config.RangeInt(min = 1)
    public int TRAVEL_TIME_UP_TICKS = 10;

    @Config.Comment({
        "The number of ticks that the device takes to reach the down position.",
        "Default: " + (5 * 20)
    })
    @Config.RangeInt(min = 1)
    public int TRAVEL_TIME_DOWN_TICKS = 5 * 20;

    @Config.Comment({
        "The unmodulated, base airflow pushed per tick the device is active.",
        "Default: " + 0.1
    })
    public double BASE_AIRFLOW = 0.1;
  }

  // ---------------------------------------------------------------------------
  // - Mechanical Bellows
  // ---------------------------------------------------------------------------

  public static MechanicalBellows MECHANICAL_BELLOWS = new MechanicalBellows();

  public static class MechanicalBellows {

    @Config.Comment({
        "The number of ticks that the device takes to return to the up position.",
        "NOTE: Must be smaller than the travel time down.",
        "Default: " + 10
    })
    @Config.RangeInt(min = 1)
    public int TRAVEL_TIME_UP_TICKS = 10;

    @Config.Comment({
        "The number of ticks that the device takes to reach the down position.",
        "NOTE: Must be larger than the travel time up.",
        "Default: " + (5 * 20)
    })
    @Config.RangeInt(min = 1)
    public int TRAVEL_TIME_DOWN_TICKS = 5 * 20;

    @Config.Comment({
        "The unmodulated, base airflow pushed per tick the device is active.",
        "Default: " + 0.1
    })
    public double BASE_AIRFLOW = 0.1;

    @Config.Comment({
        "The amount of damage applied to a cog each time the machine pushes.",
        "Default: " + 8
    })
    public int COG_DAMAGE = 8;

    @Config.Comment({
        "A list of valid cogs for the device.",
        "NOTE: Items provided here are assumed to have durability.",
        "String format is (domain):(path)"
    })
    public String[] COGS = {
        "pyrotech:cog_wood",
        "pyrotech:cog_stone",
        "pyrotech:cog_flint",
        "pyrotech:cog_bone",
        "pyrotech:cog_iron",
        "pyrotech:cog_gold",
        "pyrotech:cog_obsidian",
        "pyrotech:cog_diamond"
    };

    public boolean isValidCog(@Nullable ResourceLocation resourceLocation) {

      if (resourceLocation != null) {
        return ArrayHelper.contains(COGS, resourceLocation.toString());
      }

      return false;
    }
  }

  // ---------------------------------------------------------------------------
  // - Mechanical Compacting Bin
  // ---------------------------------------------------------------------------

  public static MechanicalCompactingBin MECHANICAL_COMPACTING_BIN = new MechanicalCompactingBin();

  public static class MechanicalCompactingBin {

    @Config.Comment({
        "A list of valid cogs for the device.",
        "NOTE: Items provided here are assumed to have durability.",
        "String format is (domain):(path) for the item and (double) for the",
        "amount of recipe progress added per work cycle. The range is [0,1]."
    })
    public Map<String, Double> COGS = new LinkedHashMap<String, Double>() {{
      this.put("pyrotech:cog_wood", 0.10);
      this.put("pyrotech:cog_stone", 0.20);
      this.put("pyrotech:cog_flint", 0.25);
      this.put("pyrotech:cog_bone", 0.25);
      this.put("pyrotech:cog_iron", 0.35);
      this.put("pyrotech:cog_gold", 0.10);
      this.put("pyrotech:cog_obsidian", 0.35);
      this.put("pyrotech:cog_diamond", 0.50);
    }};

    /**
     * Returns the recipe progress for the given item resource location,
     * or -1 if it isn't in the list.
     */
    public double getCogRecipeProgress(@Nullable ResourceLocation resourceLocation) {

      if (resourceLocation != null) {
        Double result = this.COGS.get(resourceLocation.toString());

        if (result != null) {
          return result;
        }
      }

      return -1;
    }

    @Config.Comment({
        "If true, all the compacting bin recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_COMPACTING_BIN_RECIPES = true;

    @Config.Comment({
        "How many ticks before advancing the compacting recipe.",
        "Default: " + 40
    })
    public int WORK_INTERVAL_TICKS = 40;

    @Config.Comment({
        "The number of output blocks the compacting bin can hold.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1)
    public int INPUT_CAPACITY = 16;

    @Config.Comment({
        "The number of items the output slot can hold.",
        "Default: " + 64
    })
    public int OUTPUT_CAPACITY = 64;
  }

  // ---------------------------------------------------------------------------
  // - Mechanical Mulch Spreader
  // ---------------------------------------------------------------------------

  public static MechanicalMulchSpreader MECHANICAL_MULCH_SPREADER = new MechanicalMulchSpreader();

  public static class MechanicalMulchSpreader {

    @Config.Comment({
        "A list of valid cogs for the device.",
        "NOTE: Items provided here are assumed to have durability.",
        "String format is (domain):(path) for the item (range);(attempts) for the",
        "range of the mulch [0, 5] and the number of attempts per",
        "work cycle [1, 121]."
    })
    public Map<String, String> COGS = new LinkedHashMap<String, String>() {{
      this.put("pyrotech:cog_wood", "0;1");
      this.put("pyrotech:cog_stone", "1;1");
      this.put("pyrotech:cog_flint", "2;2");
      this.put("pyrotech:cog_bone", "2;2");
      this.put("pyrotech:cog_iron", "3;2");
      this.put("pyrotech:cog_gold", "0;1");
      this.put("pyrotech:cog_obsidian", "4;4");
      this.put("pyrotech:cog_diamond", "5;8");
    }};

    /**
     * Returns the recipe progress for the given item resource location,
     * or -1 if it isn't in the list.
     */
    public int[] getCogData(@Nullable ResourceLocation resourceLocation, int[] result) {

      if (resourceLocation != null) {
        String data = this.COGS.get(resourceLocation.toString());

        if (data != null) {

          try {
            String[] split = data.split(";");
            result[0] = MathHelper.clamp(Integer.valueOf(split[0]), 0, 5);
            result[1] = MathHelper.clamp(Integer.valueOf(split[1]), 1, 121);
            return result;

          } catch (Exception e) {
            ModuleTechMachine.LOGGER.error("Invalid mulch spreader cog data in config", e);
          }
        }
      }

      result[0] = -1;
      result[1] = -1;
      return result;
    }

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    public enum EnumCogDamageType {
      PerItem, PerOperation
    }

    @Config.Comment({
        "If set to PerItem, damage will be done to the cog for every block",
        "mulched. If set to PerOperation, damage will be done to the cog",
        "each time it does work, regardless of the number of blocks mulched.",
        "Default: " + "PerItem"
    })
    public EnumCogDamageType COG_DAMAGE_TYPE = EnumCogDamageType.PerItem;

    @Config.Comment({
        "How many ticks before making an attempt to apply mulch.",
        "Default: " + (10 * 20)
    })
    public int WORK_INTERVAL_TICKS = 10 * 20;

    @Config.Comment({
        "The number of stacks of mulch the device can hold.",
        "Default: " + 5
    })
    public int CAPACITY = 5;
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

    @Config.Comment({
        "Multiplicative modifier applied to the airflow from a block like the",
        "bellows.",
        "Default: " + 1.0
    })
    public double AIRFLOW_MODIFIER = 1.0;

    @Config.Comment({
        "Percentage of retained airflow lost per tick.",
        "Default: " + 0.02
    })
    public double AIRFLOW_DRAG_MODIFIER = 0.02;
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

    @Config.Comment({
        "Multiplicative modifier applied to the airflow from a block like the",
        "bellows.",
        "Default: " + 1.5
    })
    public double AIRFLOW_MODIFIER = 1.5;

    @Config.Comment({
        "Percentage of retained airflow lost per tick.",
        "Default: " + 0.02
    })
    public double AIRFLOW_DRAG_MODIFIER = 0.01;
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
        "Default: " + 0.25
    })
    public double INHERITED_DRYING_RACK_RECIPE_DURATION_MODIFIER = 0.25;

    @Config.Comment({
        "Multiplicative modifier applied to the airflow from a block like the",
        "bellows.",
        "Default: " + 1.0
    })
    public double AIRFLOW_MODIFIER = 1.0;

    @Config.Comment({
        "Percentage of retained airflow lost per tick.",
        "Default: " + 0.02
    })
    public double AIRFLOW_DRAG_MODIFIER = 0.02;
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

    @Config.Comment({
        "Multiplicative modifier applied to the airflow from a block like the",
        "bellows.",
        "Default: " + 1.5
    })
    public double AIRFLOW_MODIFIER = 1.5;

    @Config.Comment({
        "Percentage of retained airflow lost per tick.",
        "Default: " + 0.02
    })
    public double AIRFLOW_DRAG_MODIFIER = 0.01;
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
        "Valid sawmill blades.",
        "NOTE: Items listed here are assumed to have durability.",
        "Item string format is (domain):(path)"
    })
    public String[] SAWMILL_BLADES = {
        "pyrotech:sawmill_blade_stone",
        "pyrotech:sawmill_blade_flint",
        "pyrotech:sawmill_blade_bone"
    };

    @Config.Comment({
        "Multiplicative modifier applied to the airflow from a block like the",
        "bellows.",
        "Default: " + 1.0
    })
    public double AIRFLOW_MODIFIER = 1.0;

    @Config.Comment({
        "Percentage of retained airflow lost per tick.",
        "Default: " + 0.02
    })
    public double AIRFLOW_DRAG_MODIFIER = 0.02;

    @Config.Comment({
        "If true, all the chopping block recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_CHOPPING_BLOCK_RECIPES = true;

    @Config.Comment({
        "The recipe duration modifier for all inherited recipes.",
        "Default: " + 1.0
    })
    public double INHERITED_CHOPPING_BLOCK_RECIPE_DURATION_MODIFIER = 1.0;
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
        "Valid sawmill blades.",
        "NOTE: Items listed here are assumed to have durability.",
        "Item string format is (domain):(path)"
    })
    public String[] SAWMILL_BLADES = {
        "pyrotech:sawmill_blade_stone",
        "pyrotech:sawmill_blade_flint",
        "pyrotech:sawmill_blade_bone",
        "pyrotech:sawmill_blade_iron",
        "pyrotech:sawmill_blade_gold",
        "pyrotech:sawmill_blade_diamond",
        "pyrotech:sawmill_blade_obsidian"
    };

    @Config.Comment({
        "Multiplicative modifier applied to the airflow from a block like the",
        "bellows.",
        "Default: " + 1.5
    })
    public double AIRFLOW_MODIFIER = 1.5;

    @Config.Comment({
        "Percentage of retained airflow lost per tick.",
        "Default: " + 0.02
    })
    public double AIRFLOW_DRAG_MODIFIER = 0.01;
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
        "Default: " + true
    })
    public boolean ASYNCHRONOUS_OPERATION = true;

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
        "Multiplicative modifier applied to the airflow from a block like the",
        "bellows.",
        "Default: " + 1.0
    })
    public double AIRFLOW_MODIFIER = 1.0;

    @Config.Comment({
        "Percentage of retained airflow lost per tick.",
        "Default: " + 0.02
    })
    public double AIRFLOW_DRAG_MODIFIER = 0.02;
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
        "Default: " + true
    })
    public boolean ASYNCHRONOUS_OPERATION = true;

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
        "Multiplicative modifier applied to the airflow from a block like the",
        "bellows.",
        "Default: " + 1.5
    })
    public double AIRFLOW_MODIFIER = 1.5;

    @Config.Comment({
        "Percentage of retained airflow lost per tick.",
        "Default: " + 0.02
    })
    public double AIRFLOW_DRAG_MODIFIER = 0.01;
  }

}
