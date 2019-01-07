package com.codetaylor.mc.pyrotech.modules.pyrotech;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleFluids;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModulePyrotech.MOD_ID, name = ModulePyrotech.MOD_ID + "/" + ModulePyrotech.MOD_ID + ".module.Pyrotech")
public class ModulePyrotechConfig {

  // ---------------------------------------------------------------------------
  // - Recipes
  // ---------------------------------------------------------------------------

  public static Recipes RECIPES = new Recipes();

  public static class Recipes {

    @Config.Comment({
        "These recipes will be removed by resource name during the recipe",
        "registration event."
    })
    public String[] VANILLA_REMOVE = new String[]{
        "minecraft:wooden_axe",
        "minecraft:wooden_hoe",
        "minecraft:wooden_pickaxe",
        "minecraft:wooden_shovel",

        "minecraft:oak_planks",
        "minecraft:spruce_planks",
        "minecraft:birch_planks",
        "minecraft:jungle_planks",
        "minecraft:acacia_planks",
        "minecraft:dark_oak_planks",

        "minecraft:oak_wooden_slab",
        "minecraft:spruce_wooden_slab",
        "minecraft:birch_wooden_slab",
        "minecraft:jungle_wooden_slab",
        "minecraft:acacia_wooden_slab",
        "minecraft:dark_oak_wooden_slab",

        "minecraft:stone_axe",
        "minecraft:stone_pickaxe",
        "minecraft:stone_hoe",
        "minecraft:stone_shovel",
        "minecraft:stone_sword",

        "minecraft:bone_meal_from_bone",
        "minecraft:bone_meal_from_block",

        "minecraft:andesite",
        "minecraft:granite",
        "minecraft:diorite",

        "minecraft:stone_slab",
        "minecraft:sandstone_slab",
        "minecraft:cobblestone_slab",
        "minecraft:brick_slab",
        "minecraft:stone_brick_slab",
        "minecraft:nether_brick_slab",
        "minecraft:quartz_slab",
        "minecraft:red_sandstone_slab",
        "minecraft:purpur_slab",

        "minecraft:stick",
        "minecraft:clay",
        "minecraft:snow",
        "minecraft:bone_block"
    };
  }

  // ---------------------------------------------------------------------------
  // - Client
  // ---------------------------------------------------------------------------

  public static Client CLIENT = new Client();

  public static class Client {

    @Config.Comment({
        "How many smoke particles a burning collector will emit per tick.",
        "Default: " + 10
    })
    public int BURNING_COLLECTOR_SMOKE_PARTICLES = 10;

    @Config.Comment({
        "These items will be removed from JEI.",
        "String format is a resource location: (domain):(path)"
    })
    public String[] JEI_BLACKLIST = new String[]{
        "minecraft:wooden_axe",
        "minecraft:wooden_hoe",
        "minecraft:wooden_pickaxe",
        "minecraft:wooden_shovel"
    };

    @Config.Comment({
        "Set to true to render the interaction bounds for debugging."
    })
    public boolean SHOW_INTERACTION_BOUNDS = false;
  }

  // ---------------------------------------------------------------------------
  // - Mulched Farmland
  // ---------------------------------------------------------------------------

  public static MulchedFarmland MULCHED_FARMLAND = new MulchedFarmland();

  public static class MulchedFarmland {

    @Config.Comment({
        "The number of times the mulched farmland will apply bonemeal to a crop before",
        "reverting to normal moisturized farmland.",
        "Range: [1,+int]",
        "Default: " + 3
    })
    public int CHARGES = 3;

    @Config.Comment({
        "Set to true to ignore the charge count and allow the mulched farmland",
        "to exist indefinitely.",
        "Default: " + false
    })
    public boolean UNLIMITED_CHARGES = false;

    @Config.Comment({
        "Set to true to allow the farmland to be trampled and turned to dirt the",
        "same as vanilla farmland.",
        "Default: " + false
    })
    public boolean ALLOW_TRAMPLE = false;
  }

  // ---------------------------------------------------------------------------
  // - Torch
  // ---------------------------------------------------------------------------

  public static Torch TORCH = new Torch();

  public static class Torch {

    @Config.Comment({
        "Set to true if the torch should be extinguished in the rain.",
        "Default: " + true
    })
    public boolean EXTINGUISHED_BY_RAIN = true;

    @Config.Comment({
        "Set to true if the torch should burn up after the set duration.",
        "Default: " + true
    })
    public boolean BURNS_UP = true;

    @Config.Comment({
        "The number of ticks before the torch burns up.",
        "Default: " + (14 * 60 * 20)
    })
    public int DURATION = 14 * 60 * 20;

    @Config.Comment({
        "Random range of ticks to -/+ from the duration.",
        "duration += rand[-variant, +variant]",
        "Default: " + (4 * 60 * 20)
    })
    public int DURATION_VARIANT = 4 * 60 * 20;
  }

  // ---------------------------------------------------------------------------
  // - Stash
  // ---------------------------------------------------------------------------

  public static Stash STASH = new Stash();

  public static class Stash {

    @Config.Comment({
        "The maximum number of stacks that can be stored in the stash.",
        "Default: " + 4
    })
    public int MAX_STACKS = 4;
  }

  // ---------------------------------------------------------------------------
  // - Worktable
  // ---------------------------------------------------------------------------

  public static Worktable WORKTABLE = new Worktable();

  public static class Worktable {

    @Config.Comment({
        "Use this to add items that you want to be valid for banging on the ",
        "worktable.",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] HAMMER_LIST = new String[]{
        ModulePyrotech.MOD_ID + ":" + ItemCrudeHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemStoneHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemBoneHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemFlintHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemIronHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemDiamondHammer.NAME
    };

    @Config.Comment({
        "The number of hammer hits required to complete a craft.",
        "Default: " + 4
    })
    public int HITS_PER_CRAFT = 4;

    @Config.Comment({
        "The maximum stack size for each slot in the crafting grid.",
        "Range: [1, 64]",
        "Default: " + 1
    })
    public int GRID_MAX_STACK_SIZE = 1;
  }

  // ---------------------------------------------------------------------------
  // - Compacting Bin
  // ---------------------------------------------------------------------------

  public static CompactingBin COMPACTING_BIN = new CompactingBin();

  public static class CompactingBin {

    @Config.Comment({
        "Any item with a tool class of shovel is automatically valid.",
        "Use this to add items that you want to be valid that don't have the",
        "shovel tool class. Items you add are assumed to have durability.",
        "",
        "The whitelist takes priority over the blacklist.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] SHOVEL_WHITELIST = new String[0];

    @Config.Comment({
        "Any item with a tool class of shovel is automatically valid.",
        "Use this to remove items that you don't want to be valid.",
        "",
        "The whitelist takes priority over the blacklist.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] SHOVEL_BLACKLIST = new String[0];

    @Config.Comment({
        "The number of output items the compacting bin can hold.",
        "Range: [1, +int)",
        "Default: 4"
    })
    public int MAX_CAPACITY = 4;

    @Config.Comment({
        "The shovel displayed in JEI recipes.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String JEI_DISPLAY_SHOVEL = "minecraft:stone_shovel";
  }

  // ---------------------------------------------------------------------------
  // - Granite Anvil
  // ---------------------------------------------------------------------------

  public static GraniteAnvil GRANITE_ANVIL = new GraniteAnvil();

  public static class GraniteAnvil {

    @Config.Comment({
        "The number of times the block can be hit before applying damage",
        "to the block. The block has a total of four damage stages. This number",
        "represents the number of hits for just one damage stage.",
        "Range: [1, +int]",
        "Default: " + 32
    })
    public int HITS_PER_DAMAGE = 32;

    @Config.Comment({
        "Use this to add items that you want to be valid for hammer recipes.",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] HAMMER_LIST = new String[]{
        ModulePyrotech.MOD_ID + ":" + ItemCrudeHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemStoneHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemBoneHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemFlintHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemIronHammer.NAME,
        ModulePyrotech.MOD_ID + ":" + ItemDiamondHammer.NAME
    };

    @Config.Comment({
        "Use this to add items that you want to be valid for pickaxe recipes.",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] PICKAXE_WHITELIST = new String[0];

    @Config.Comment({
        "Use this to add items that you want to be invalid for pickaxe recipes.",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] PICKAXE_BLACKLIST = new String[0];
  }

  // ---------------------------------------------------------------------------
  // - Chopping Block
  // ---------------------------------------------------------------------------

  public static ChoppingBlock CHOPPING_BLOCK = new ChoppingBlock();

  public static class ChoppingBlock {

    @Config.Comment({
        "Any item with a tool class of axe is automatically valid.",
        "Use this to add items that you want to be valid that don't have the",
        "axe tool class. Items you add are assumed to have durability.",
        "",
        "The whitelist takes priority over the blacklist.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] AXE_WHITELIST = new String[0];

    @Config.Comment({
        "Any item with a tool class of axe is automatically valid.",
        "Use this to remove items that you don't want to be valid.",
        "",
        "The whitelist takes priority over the blacklist.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] AXE_BLACKLIST = new String[0];

    @Config.Comment({
        "The number of chops required per harvest level of the axe used.",
        "The index into the array is the harvest level, the value at that index",
        "is the required number of chops. The array can be expanded as needed.",
        "If the harvest level of the tool used exceeds the array length, the",
        "last element in the array is used.",
        "",
        "ie. {wood, stone, iron, diamond}",
        "Valid values are in the range: [1,+int]",
        "Default: {6, 4, 2, 1}"
    })
    public int[] CHOPS_REQUIRED_PER_HARVEST_LEVEL = new int[]{6, 4, 2, 2};

    @Config.Comment({
        "The recipe result quantity given per harvest level of the axe used.",
        "The index into the array is the harvest level, the value at that index",
        "is the recipe result quantity. The array can be expanded as needed.",
        "If the harvest level of the tool used exceeds the array length, the",
        "last element in the array is used.",
        "",
        "ie. {wood, stone, iron, diamond}",
        "Valid values are in the range: [0,+int]",
        "Default: {1, 2, 3, 4}"
    })
    public int[] RECIPE_RESULT_QUANTITY_PER_HARVEST_LEVEL = new int[]{1, 2, 3, 4};

    @Config.Comment({
        "The item displayed to represent each harvest level in JEI.",
        "The index into the array is the harvest level, the value at that index",
        "is the displayed item. The array can be expanded as needed.",
        "If the harvest level of the tool used exceeds the array length, the",
        "last element in the array is used.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] JEI_HARVEST_LEVEL_ITEM = new String[]{
        "pyrotech:crude_axe",
        "minecraft:stone_axe",
        "minecraft:iron_axe",
        "minecraft:diamond_axe"
    };

    @Config.Comment({
        "The chance that a chop will spawn wood chips nearby.",
        "Range: [0, 1]",
        "Default: " + 0.05
    })
    public double WOOD_CHIPS_CHANCE = 0.05;

    @Config.Comment({
        "The number of times the block can be chopped on before applying damage",
        "to the block. The block has a total of six damage stages. This number",
        "represents the number of chops for just one damage stage.",
        "Range: [1, +int]",
        "Default: " + 16
    })
    public int CHOPS_PER_DAMAGE = 16;
  }

  // ---------------------------------------------------------------------------
  // - Crude Drying Rack
  // ---------------------------------------------------------------------------

  public static CrudeDryingRack CRUDE_DRYING_RACK = new CrudeDryingRack();

  public static class CrudeDryingRack {

    @Config.Comment({
        "speed = speed * SPEED_MODIFIER",
        "Default: " + 0.65
    })
    public double SPEED_MODIFIER = 0.65;
  }

  // ---------------------------------------------------------------------------
  // - Drying Rack
  // ---------------------------------------------------------------------------

  public static DryingRack DRYING_RACK = new DryingRack();

  public static class DryingRack {

    @Config.Comment({
        "speed = speed * SPEED_MODIFIER",
        "Default: " + 1.0
    })
    public double SPEED_MODIFIER = 1.0;
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
        "Range: [1, 64]",
        "Default: " + 8
    })
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Range: [1, 64]",
        "Default: " + 16
    })
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
    public int COOK_TIME_TICKS = 80 * 20;

    @Config.Comment({
        "Controls the number of recipe items that can be inserted.",
        "Range: [1, 64]",
        "Default: " + 8
    })
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Range: [1, 64]",
        "Default: " + 16
    })
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
        "Range: [1, 64]",
        "Default: " + 8
    })
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Range: [1, 64]",
        "Default: " + 16
    })
    public int FUEL_SLOT_SIZE = 16;

    @Config.Comment({
        "Controls how much damage a spinning blade will do to an entity.",
        "Default: " + 3
    })
    public int ENTITY_DAMAGE_FROM_BLADE = 3;

    @Config.Comment({
        "Set to false to disable damaging blades.",
        "Default: " + true
    })
    public boolean DAMAGE_BLADES = true;

    @Config.Comment({
        "Chance of producing wood chips when a recipe completes.",
        "Default: " + 0.8
    })
    public double WOOD_CHIPS_CHANCE = 0.8;
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
        "Range: [1, 64]",
        "Default: " + 8
    })
    public int INPUT_SLOT_SIZE = 8;

    @Config.Comment({
        "Controls the number of fuel items that can be inserted.",
        "Range: [1, 64]",
        "Default: " + 16
    })
    public int FUEL_SLOT_SIZE = 16;

    @Config.Comment({
        "Controls the size of the output tank in millibuckets.",
        "Range: [1, +int]",
        "Default: " + 4000
    })
    public int OUTPUT_TANK_SIZE = 4000;
  }

  // ---------------------------------------------------------------------------
  // - Pit Kiln
  // ---------------------------------------------------------------------------

  public static PitKiln PIT_KILN = new PitKiln();

  public static class PitKiln {

    @Config.Comment({
        "Reduce the duration of all recipes by this amount for each adjacent refractory block.",
        "Range: [0, 0.2]",
        "Default: " + 0.1
    })
    public double REFRACTORY_BLOCK_TIME_BONUS = 0.1;

  }

  // ---------------------------------------------------------------------------
  // - Refractory
  // ---------------------------------------------------------------------------

  public static Refractory REFRACTORY = new Refractory();

  public static class Refractory {

    @Config.Comment({
        "Maximum chance for a recipe item to fail conversion.",
        "Default: " + 0.95
    })
    public double MAX_FAILURE_CHANCE = 0.95;

    @Config.Comment({
        "Minimum chance for a recipe item to fail conversion.",
        "Default: " + 0.05
    })
    public double MIN_FAILURE_CHANCE = 0.05;

    @Config.Comment({
        "The maximum fluid capacity of an active pile in mb.",
        "Default: " + 500
    })
    public int ACTIVE_PILE_MAX_FLUID_CAPACITY = 500;

    @Config.Comment({
        "The duration in ticks that 1 mb of fluid will burn in the Tar Collector."
    })
    public Map<String, Integer> FLUID_BURN_TICKS = new HashMap<String, Integer>() {{
      this.put(ModuleFluids.WOOD_TAR.getName(), 20);
      this.put(ModuleFluids.COAL_TAR.getName(), 40);
    }};
  }

  // ---------------------------------------------------------------------------
  // - Fuel
  // ---------------------------------------------------------------------------

  public static Fuel FUEL = new Fuel();

  public static class Fuel {

    @Config.Comment({
        "Board burn time in ticks.",
        "Default: " + 120
    })
    public int BOARD_BURN_TIME_TICKS = 120;

    @Config.Comment({
        "Coal pieces burn time in ticks.",
        "Default: " + (1600 / 8)
    })
    public int COAL_PIECES_BURN_TIME_TICKS = 1600 / 8;

    @Config.Comment({
        "Charcoal flakes burn time in ticks.",
        "Default: " + (1600 / 8)
    })
    public int CHARCOAL_FLAKES_BURN_TIME_TICKS = 1600 / 8;

    @Config.Comment({
        "Tinder burn time in ticks.",
        "Default: " + 120
    })
    public int TINDER_BURN_TIME_TICKS = 120;

    @Config.Comment({
        "Straw burn time in ticks.",
        "Default: " + 50
    })
    public int STRAW_BURN_TIME_TICKS = 50;

    @Config.Comment({
        "Straw bale burn time in ticks.",
        "Default: " + 200
    })
    public int STRAW_BALE_BURN_TIME_TICKS = 200;

    @Config.Comment({
        "Coal tar burn time in ticks.",
        "Default: " + 6400
    })
    public int COAL_TAR_BURN_TIME_TICKS = 6400;

    @Config.Comment({
        "Wood tar burn time in ticks.",
        "Default: " + 4800
    })
    public int WOOD_TAR_BURN_TIME_TICKS = 4800;

    @Config.Comment({
        "Coal coke burn time in ticks.",
        "Default: " + 3200
    })
    public int COAL_COKE_BURN_TIME_TICKS = 3200;

    @Config.Comment({
        "Coal coke block burn time in ticks.",
        "Default: " + 32000
    })
    public int COAL_COKE_BLOCK_BURN_TIME_TICKS = 32000;

    @Config.Comment({
        "Log pile burn time in ticks.",
        "Default: " + (300 * 9)
    })
    public int LOG_PILE_BURN_TIME_TICKS = 300 * 9;

  }

  // ---------------------------------------------------------------------------
  // - Campfire
  // ---------------------------------------------------------------------------

  public static Campfire CAMPFIRE = new Campfire();

  public static class Campfire {

    @Config.Comment({
        "How many ticks to cook food on the campfire.",
        "Default: " + (20 * 20)
    })
    public int COOK_TIME_TICKS = 20 * 20;

    @Config.Comment({
        "The amount of ticks of burn time added to the campfire",
        "for each log consumed.",
        "Default: " + (60 * 2 * 20)
    })
    public int BURN_TIME_TICKS_PER_LOG = 60 * 2 * 20;

    @Config.Comment({
        "Set to true if the campfire should be extinguished by rain.",
        "Default: true"
    })
    public boolean EXTINGUISHED_BY_RAIN = true;

    @Config.Comment({
        "The number of ticks that the campfire can be exposed to rain before",
        "it is extinguished.",
        "Default: " + (10 * 20)
    })
    public int TICKS_BEFORE_EXTINGUISHED = 10 * 20;

    @Config.Comment({
        "The chance that the campfire will produce ash when a fuel is consumed.",
        "Range: [0, 1]",
        "Default: " + 0.25
    })
    public double ASH_CHANCE = 0.25;

    @Config.Comment({
        "The chance that the player will be damaged with fire when picking",
        "up a log while the campfire is lit.",
        "Default: " + 0.5
    })
    public double PLAYER_BURN_CHANCE = 0.5;

    @Config.Comment({
        "The amount of damage done to a player when picking up a log while the",
        "campfire is lit.",
        "Default: " + 1.0
    })
    public double PLAYER_BURN_DAMAGE = 1.0;
  }

  // ---------------------------------------------------------------------------
  // - Compat
  // ---------------------------------------------------------------------------

  public static CompatDropt COMPAT_DROPT = new CompatDropt();

  public static class CompatDropt {

    @Config.Comment({
        "This mod uses Dropt to replace certain block drops.",
        "To disable all of the block drop replacement rules, set this to false.",
        "",
        "The Dropt rule list for this mod uses a priority of zero, so",
        "if you want to override some of the Dropt rules, set your",
        "rule list priority to a value greater than zero.",
        "",
        "Changing this during runtime requires the '/dropt reload' command.",
        "Default: " + true
    })
    public boolean ENABLE = true;

  }

  // ---------------------------------------------------------------------------
  // - World Gen
  // ---------------------------------------------------------------------------

  public static WorldGen WORLD_GEN = new WorldGen();

  public static class WorldGen {

    @Config.Comment({
        "Set to false to disable all world gen from this mod."
    })
    public boolean ENABLED = true;

    public WorldGenFossil FOSSIL = new WorldGenFossil();

    public static class WorldGenFossil {

      public boolean ENABLED = true;
      public int CHANCES_TO_SPAWN = 15;
      public int MIN_HEIGHT = 40;
      public int MAX_HEIGHT = 120;
      public int MIN_VEIN_SIZE = 10;
      public int MAX_VEIN_SIZE = 20;
    }

    public WorldGenLimestone LIMESTONE = new WorldGenLimestone();

    public static class WorldGenLimestone {

      public boolean ENABLED = true;
      public int CHANCES_TO_SPAWN = 15;
      public int MIN_HEIGHT = 8;
      public int MAX_HEIGHT = 100;
      public int MIN_VEIN_SIZE = 10;
      public int MAX_VEIN_SIZE = 20;
    }
  }

  // ---------------------------------------------------------------------------
  // - General
  // ---------------------------------------------------------------------------

  public static General GENERAL = new General();

  public static class General {

    @Config.Comment({
        "List of valid refractory bricks used in the pit kiln and coke oven."
    })
    public String[] REFRACTORY_BRICKS = new String[]{
        ModulePyrotech.MOD_ID + ":" + BlockRefractoryBrick.NAME,
        ModulePyrotech.MOD_ID + ":" + BlockTarCollector.NAME + ":" + BlockTarCollector.EnumType.BRICK.getMeta(),
        ModulePyrotech.MOD_ID + ":" + BlockTarDrain.NAME + ":" + this.getTarDrainMeta(EnumFacing.NORTH),
        ModulePyrotech.MOD_ID + ":" + BlockTarDrain.NAME + ":" + this.getTarDrainMeta(EnumFacing.EAST),
        ModulePyrotech.MOD_ID + ":" + BlockTarDrain.NAME + ":" + this.getTarDrainMeta(EnumFacing.SOUTH),
        ModulePyrotech.MOD_ID + ":" + BlockTarDrain.NAME + ":" + this.getTarDrainMeta(EnumFacing.WEST),
        ModulePyrotech.MOD_ID + ":" + BlockIgniter.NAME + ":" + this.getIgniterMeta(EnumFacing.NORTH),
        ModulePyrotech.MOD_ID + ":" + BlockIgniter.NAME + ":" + this.getIgniterMeta(EnumFacing.EAST),
        ModulePyrotech.MOD_ID + ":" + BlockIgniter.NAME + ":" + this.getIgniterMeta(EnumFacing.SOUTH),
        ModulePyrotech.MOD_ID + ":" + BlockIgniter.NAME + ":" + this.getIgniterMeta(EnumFacing.WEST),
        ModulePyrotech.MOD_ID + ":" + BlockRefractoryGlass.NAME + ":*"
    };

    private int getTarDrainMeta(EnumFacing facing) {

      return ModuleBlocks.TAR_DRAIN.getMetaFromState(
          ModuleBlocks.TAR_DRAIN.getDefaultState()
              .withProperty(BlockTarDrain.VARIANT, BlockTarDrain.EnumType.BRICK)
              .withProperty(BlockTarDrain.FACING, facing)
      );
    }

    private int getIgniterMeta(EnumFacing facing) {

      return ModuleBlocks.IGNITER.getMetaFromState(
          ModuleBlocks.IGNITER.getDefaultState()
              .withProperty(BlockIgniter.VARIANT, BlockIgniter.EnumType.BRICK)
              .withProperty(BlockIgniter.FACING, facing)
      );
    }

    @Config.Comment({
        "Fluid capacity of the tar collector in mb.",
        "Default: 8000"
    })
    public int TAR_COLLECTOR_CAPACITY = 8000;

    @Config.Comment({
        "Fluid capacity of the tar drain in mb.",
        "Default: 1000"
    })
    public int TAR_DRAIN_CAPACITY = 1000;

    @Config.Comment({
        "The durability of the bow drill.",
        "Default: 16"
    })
    public int BOW_DRILL_DURABILITY = 16;

    @Config.Comment({
        "Defines how many ticks it takes to start a fire while using the bow drill.",
        "Default: " + (3 * 20)
    })
    public int BOW_DRILL_USE_DURATION = 3 * 20;

    @Config.Comment({
        "The durability of the flint and tinder.",
        "Default: 8"
    })
    public int FLINT_AND_TINDER_DURABILITY = 8;

    @Config.Comment({
        "Defines how many ticks it takes to start a fire while using the flint and tinder.",
        "Default: " + (5 * 20)
    })
    public int FLINT_AND_TINDER_USE_DURATION = 5 * 20;
  }

}
