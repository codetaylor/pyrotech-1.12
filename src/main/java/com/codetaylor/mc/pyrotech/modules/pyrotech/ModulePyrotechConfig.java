package com.codetaylor.mc.pyrotech.modules.pyrotech;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleFluids;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModulePyrotech.MOD_ID, name = ModulePyrotech.MOD_ID + "/" + "module.Pyrotech")
public class ModulePyrotechConfig {

  public static Hammers HAMMERS = new Hammers();

  public static class Hammers {

    @Config.Comment({
        "Hammers are used on the worktables, anvils, and blooms.",
        "Use this to add items that you want to be valid for hammer recipes.",
        "The harvest level is used for hammer hit reduction with anvil recipes",
        "and for the bloom hammer power modifier.",
        "",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path);(harvest_level)"
    })
    public String[] HAMMER_LIST = new String[]{
        ModulePyrotech.MOD_ID + ":" + ItemCrudeHammer.NAME + ";" + 0,
        ModulePyrotech.MOD_ID + ":" + ItemStoneHammer.NAME + ";" + 1,
        ModulePyrotech.MOD_ID + ":" + ItemBoneHammer.NAME + ";" + 1,
        ModulePyrotech.MOD_ID + ":" + ItemFlintHammer.NAME + ";" + 1,
        ModulePyrotech.MOD_ID + ":" + ItemIronHammer.NAME + ";" + 2,
        ModulePyrotech.MOD_ID + ":" + ItemDiamondHammer.NAME + ";" + 3
    };

    /**
     * Returns the hammer harvest level for the given hammer resource location,
     * or -1 if the given hammer isn't in the list.
     *
     * @param resourceLocation the hammer
     * @return the hammer hit reduction
     */
    public int getHammerHarvestLevel(ResourceLocation resourceLocation) {

      String resourceLocationString = resourceLocation.toString();

      for (String entry : this.HAMMER_LIST) {
        String[] split = entry.split(";");
        String toMatch = split[0];

        if (resourceLocationString.equals(toMatch)) {
          return (split.length > 1) ? Integer.valueOf(split[1]) : 0;
        }
      }

      return -1;
    }
  }

  // ---------------------------------------------------------------------------
  // - Food
  // ---------------------------------------------------------------------------

  public static Food FOOD = new Food();

  public static class Food {

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "The vanilla apple restores 4 hunger.",
        "Default: " + 8
    })
    public int BAKED_APPLE_HUNGER = 8;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "The vanilla apple restores 0.3 saturation.",
        "Default: " + 0.6
    })
    public double BAKED_APPLE_SATURATION = 0.6;
  }

  // ---------------------------------------------------------------------------
  // - Recipes
  // ---------------------------------------------------------------------------

  public static Recipes RECIPES = new Recipes();

  public static class Recipes {

    @Config.Comment({
        "This is a list of vanilla furnace output recipe items that will have",
        "their recipe removed.",
        "Format is (domain):(path) or (domain):(path):(meta)"
    })
    public String[] VANILLA_FURNACE_REMOVE = new String[]{
        "minecraft:brick"
    };

    @Config.Comment({
        "These crafting recipes will be removed by resource name during the recipe",
        "registration event."
    })
    public String[] VANILLA_CRAFTING_REMOVE = new String[]{
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
        "minecraft:bone_block",
        "minecraft:paper",
        "minecraft:torch",
        "minecraft:coal_block",

        "minecraft:boat",
        "minecraft:spruce_boat",
        "minecraft:birch_boat",
        "minecraft:jungle_boat",
        "minecraft:acacia_boat",
        "minecraft:dark_oak_boat"
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
        "Default: " + 6
    })
    public int CHARGES = 6;

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
  // - Fiber Torch
  // ---------------------------------------------------------------------------

  public static FiberTorch FIBER_TORCH = new FiberTorch();

  public static class FiberTorch {

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
  // - Stone Torch
  // ---------------------------------------------------------------------------

  public static StoneTorch STONE_TORCH = new StoneTorch();

  public static class StoneTorch {

    @Config.Comment({
        "Set to true if the torch should be extinguished in the rain.",
        "Default: " + true
    })
    public boolean EXTINGUISHED_BY_RAIN = true;

    @Config.Comment({
        "Set to true if the torch should burn up after the set duration.",
        "Default: " + false
    })
    public boolean BURNS_UP = false;

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

    @Config.Comment({
        "Wood chips burn time in ticks.",
        "Default: " + (1600 / 8)
    })
    public int WOOD_CHIPS_BURN_TIME_TICKS = 1600 / 8;

    @Config.Comment({
        "Wood chips burn time in ticks.",
        "Default: " + (1600 / 8)
    })
    public int PILE_WOOD_CHIPS_BURN_TIME_TICKS = 1600;

    @Config.Comment({
        "Tarred planks burn time in ticks.",
        "Default: " + 800
    })
    public int TARRED_PLANKS_BURN_TIME_TICKS = 800;

    @Config.Comment({
        "Tarred wool burn time in ticks.",
        "Default: " + 800
    })
    public int TARRED_WOOL_BURN_TIME_TICKS = 800;

    @Config.Comment({
        "Charcoal block burn time in ticks.",
        "Default: " + 16000
    })
    public int CHARCOAL_BLOCK_BURN_TIME_TICKS = 16000;
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
        "Default: " + 32
    })
    public int BOW_DRILL_DURABILITY = 32;

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
