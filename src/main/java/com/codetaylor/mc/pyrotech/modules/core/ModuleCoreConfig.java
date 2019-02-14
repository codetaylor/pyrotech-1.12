package com.codetaylor.mc.pyrotech.modules.core;

import com.codetaylor.mc.pyrotech.modules.core.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + ".core")
public class ModuleCoreConfig {

  public static Tweaks TWEAKS = new Tweaks();

  public static class Tweaks {

    @Config.Comment({
        "If true, vanilla sheep won't drop vanilla wool."
    })
    public boolean PREVENT_WOOL_ON_SHEEP_DEATH = true;

    @Config.Comment({
        "When a vanilla crafting table spawns in the world, for example in a",
        "village, the table is removed."
    })
    public boolean REMOVE_VANILLA_CRAFTING_TABLE = true;

    @Config.Comment({
        "When a vanilla furnace spawns in the world, for example in a",
        "village, the furnace is replaced with cobblestone."
    })
    public boolean REPLACE_VANILLA_FURNACE = true;
  }

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
        ModuleCore.MOD_ID + ":" + ItemCrudeHammer.NAME + ";" + 0,
        ModuleCore.MOD_ID + ":" + ItemStoneHammer.NAME + ";" + 1,
        ModuleCore.MOD_ID + ":" + ItemBoneHammer.NAME + ";" + 1,
        ModuleCore.MOD_ID + ":" + ItemFlintHammer.NAME + ";" + 1,
        ModuleCore.MOD_ID + ":" + ItemIronHammer.NAME + ";" + 2,
        ModuleCore.MOD_ID + ":" + ItemDiamondHammer.NAME + ";" + 3
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

    @Config.Comment({
        "Durability of the crude hammer.",
        "Default: " + 32
    })
    public int CRUDE_HAMMER_DURABILITY = 32;

    @Config.Comment({
        "Durability of the stone hammer.",
        "Default: " + 150
    })
    public int STONE_HAMMER_DURABILITY = 150;

    @Config.Comment({
        "Durability of the bone hammer.",
        "Default: " + 150
    })
    public int BONE_HAMMER_DURABILITY = 150;

    @Config.Comment({
        "Durability of the flint hammer.",
        "Default: " + 150
    })
    public int FLINT_HAMMER_DURABILITY = 150;

    @Config.Comment({
        "Durability of the iron hammer.",
        "Default: " + 750
    })
    public int IRON_HAMMER_DURABILITY = 750;

    @Config.Comment({
        "Durability of the diamond hammer.",
        "Default: " + 4500
    })
    public int DIAMOND_HAMMER_DURABILITY = 4500;
  }

  // ---------------------------------------------------------------------------
  // - Food
  // ---------------------------------------------------------------------------

  public static Food FOOD = new Food();

  public static class Food {

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 6
    })
    @Config.RangeInt(min = 0)
    public int BAKED_APPLE_HUNGER = 6;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.45
    })
    @Config.RangeDouble(min = 0)
    public double BAKED_APPLE_SATURATION = 0.45;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla beetroot restores 1 hunger.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int ROASTED_BEETROOT_HUNGER = 2;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla beetroot restores 0.6 saturation.",
        "Default: " + 0.9
    })
    @Config.RangeDouble(min = 0)
    public double ROASTED_BEETROOT_SATURATION = 0.9;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla carrot restores 3 hunger.",
        "Default: " + 5
    })
    @Config.RangeInt(min = 0)
    public int ROASTED_CARROT_HUNGER = 5;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla carrot restores 0.6 saturation.",
        "Default: " + 0.9
    })
    @Config.RangeDouble(min = 0)
    public double ROASTED_CARROT_SATURATION = 0.9;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 6
    })
    @Config.RangeInt(min = 0)
    public int ROASTED_EGG_HUNGER = 6;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.6
    })
    @Config.RangeDouble(min = 0)
    public double ROASTED_EGG_SATURATION = 0.6;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 5
    })
    @Config.RangeInt(min = 0)
    public int ROASTED_BROWN_MUSHROOM_HUNGER = 5;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.6
    })
    @Config.RangeDouble(min = 0)
    public double ROASTED_BROWN_MUSHROOM_SATURATION = 0.6;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 5
    })
    @Config.RangeInt(min = 0)
    public int ROASTED_RED_MUSHROOM_HUNGER = 5;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.6
    })
    @Config.RangeDouble(min = 0)
    public double ROASTED_RED_MUSHROOM_SATURATION = 0.6;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int BURNED_FOOD_HUNGER;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.1
    })
    @Config.RangeDouble(min = 0)
    public double BURNED_FOOD_SATURATION;

    @Config.Comment({
        "Eating the burned food applies the hunger effect like zombie flesh.",
        "Set to zero to disable",
        "Default: " + 600
    })
    @Config.RangeInt(min = 0)
    public int BURNED_FOOD_HUNGER_EFFECT_DURATION_TICKS = 600;
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
        "minecraft:coal",
        "minecraft:chest",
        "minecraft:furnace",
        "minecraft:crafting_table",
        "minecraft:redstone_block",
        "minecraft:redstone",
        "minecraft:lapis_block",
        "minecraft:lapis_lazuli",

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
        "These items will be removed from JEI.",
        "Note: This should only be used to remove vanilla items.",
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
        "Default: " + 6
    })
    @Config.RangeInt(min = 1)
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
  // - Fuel
  // ---------------------------------------------------------------------------

  public static Fuel FUEL = new Fuel();

  public static class Fuel {

    @Config.Comment({
        "Board burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 200
    })
    @Config.RangeInt(min = 0)
    public int BOARD_BURN_TIME_TICKS = 200;

    @Config.Comment({
        "Burned food burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 200
    })
    @Config.RangeInt(min = 0)
    public int BURNED_FOOD_BURN_TIME_TICKS = 200;

    @Config.Comment({
        "Coal pieces burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + (1600 / 8)
    })
    @Config.RangeInt(min = 0)
    public int COAL_PIECES_BURN_TIME_TICKS = 1600 / 8;

    @Config.Comment({
        "Charcoal flakes burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + (1600 / 8)
    })
    @Config.RangeInt(min = 0)
    public int CHARCOAL_FLAKES_BURN_TIME_TICKS = 1600 / 8;

    @Config.Comment({
        "Tinder burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 120
    })
    @Config.RangeInt(min = 0)
    public int TINDER_BURN_TIME_TICKS = 120;

    @Config.Comment({
        "Straw burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 50
    })
    @Config.RangeInt(min = 0)
    public int STRAW_BURN_TIME_TICKS = 50;

    @Config.Comment({
        "Straw bale burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 200
    })
    @Config.RangeInt(min = 0)
    public int STRAW_BALE_BURN_TIME_TICKS = 200;

    @Config.Comment({
        "Coal coke burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 3200
    })
    @Config.RangeInt(min = 0)
    public int COAL_COKE_BURN_TIME_TICKS = 3200;

    @Config.Comment({
        "Coal coke block burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 32000
    })
    @Config.RangeInt(min = 0)
    public int COAL_COKE_BLOCK_BURN_TIME_TICKS = 32000;

    @Config.Comment({
        "Living tar burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 32000
    })
    @Config.RangeInt(min = 0)
    public int LIVING_TAR_BURN_TIME_TICKS = 32000;

    @Config.Comment({
        "Log pile burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + (300 * 10)
    })
    @Config.RangeInt(min = 0)
    public int LOG_PILE_BURN_TIME_TICKS = 300 * 10;

    @Config.Comment({
        "Wood chips burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 100
    })
    @Config.RangeInt(min = 0)
    public int WOOD_CHIPS_BURN_TIME_TICKS = 100;

    @Config.Comment({
        "Wood chips burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 800
    })
    @Config.RangeInt(min = 0)
    public int PILE_WOOD_CHIPS_BURN_TIME_TICKS = 800;

    @Config.Comment({
        "Charcoal block burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 16000
    })
    @Config.RangeInt(min = 0)
    public int CHARCOAL_BLOCK_BURN_TIME_TICKS = 16000;

    @Config.Comment({
        "Tarred planks burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 800
    })
    @Config.RangeInt(min = 0)
    public int TARRED_PLANKS_BURN_TIME_TICKS = 800;

    @Config.Comment({
        "Tarred wool burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 800
    })
    @Config.RangeInt(min = 0)
    public int TARRED_WOOL_BURN_TIME_TICKS = 800;

    @Config.Comment({
        "Tarred board burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 400
    })
    @Config.RangeInt(min = 0)
    public int TARRED_BOARD_BURN_TIME_TICKS = 400;

    @Config.Comment({
        "Kindling burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 800
    })
    @Config.RangeInt(min = 0)
    public int KINDLING_BURN_TIME_TICKS = 800;

    @Config.Comment({
        "Tarred kindling burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 1600
    })
    @Config.RangeInt(min = 0)
    public int TARRED_KINDLING_BURN_TIME_TICKS = 1600;

    @Config.Comment({
        "Wood tar block burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 8000
    })
    @Config.RangeInt(min = 0)
    public int WOOD_TAR_BLOCK_BURN_TIME_TICKS = 8000;
  }

}
