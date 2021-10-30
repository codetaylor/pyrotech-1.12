package com.codetaylor.mc.pyrotech.modules.core;

import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;

import java.util.Map;
import java.util.TreeMap;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "core")
public class ModuleCoreConfig {

  public static Tweaks TWEAKS = new Tweaks();

  public static class Tweaks {

    @Config.Comment({
        "If true, vanilla sheep won't drop wool when killed.",
        "Default: " + true
    })
    public boolean PREVENT_WOOL_ON_SHEEP_DEATH = true;

    @Config.Comment({
        "When a vanilla crafting table spawns in the world, for example in a",
        "village, the table is removed.",
        "Default: " + true
    })
    public boolean REMOVE_VANILLA_CRAFTING_TABLE = true;

    @Config.Comment({
        "When a vanilla furnace spawns in the world, for example in a",
        "village, the furnace is replaced with cobblestone.",
        "Default: " + true
    })
    public boolean REPLACE_VANILLA_FURNACE = true;

    @Config.Comment({
        "Set to false to disable dropping sticks from leaves.",
        "Default: " + true
    })
    public boolean DROP_STICKS_FROM_LEAVES = true;

    @Config.Comment({
        "Pyrotech will swap iron ingots for iron ore in loot tables by default.",
        "This feature may not play well with mods designed to modify loot tables.",
        "Set to false to disable.",
        "Default: " + true
    })
    public boolean REPLACE_IRON_INGOTS = true;

    @Config.Comment({
        "Pyrotech will swap iron ingots for iron ore in loot tables by default.",
        "This feature may not play well with mods designed to modify loot tables.",
        "Default: " + "minecraft:iron_ore"
    })
    public String REPLACE_IRON_INGOTS_WITH = "minecraft:iron_ore";

    @Config.Comment({
        "Set to false to allow all wood chips to be collected with any held item.",
        "Affects: ",
        "  - Pile of Wood Chips harvest tool",
        "  - Block of Wood Chips harvest tool",
        "  - Chopping Block Pile of Wood Chips harvest tool",
        "Default: " + true
    })
    public boolean REQUIRE_SHOVEL_TO_PICKUP_WOOD_CHIPS = true;
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
        ModuleCore.MOD_ID + ":" + ItemBoneHammerDurable.NAME + ";" + 1,
        ModuleCore.MOD_ID + ":" + ItemFlintHammer.NAME + ";" + 1,
        ModuleCore.MOD_ID + ":" + ItemFlintHammerDurable.NAME + ";" + 1,
        ModuleCore.MOD_ID + ":" + ItemIronHammer.NAME + ";" + 2,
        ModuleCore.MOD_ID + ":" + ItemGoldHammer.NAME + ";" + 1,
        ModuleCore.MOD_ID + ":" + ItemObsidianHammer.NAME + ";" + 2,
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
          return (split.length > 1) ? Integer.parseInt(split[1]) : 0;
        }
      }

      for (String entry : ModPyrotech.API_INTERNAL.HAMMERS) {
        String[] split = entry.split(";");
        String toMatch = split[0];

        if (resourceLocationString.equals(toMatch)) {
          return (split.length > 1) ? Integer.parseInt(split[1]) : 0;
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
        "Durability of the durable bone hammer.",
        "Default: " + 600
    })
    public int BONE_HAMMER_DURABLE_DURABILITY = 600;

    @Config.Comment({
        "Durability of the flint hammer.",
        "Default: " + 150
    })
    public int FLINT_HAMMER_DURABILITY = 150;

    @Config.Comment({
        "Durability of the durable flint hammer.",
        "Default: " + 600
    })
    public int FLINT_HAMMER_DURABLE_DURABILITY = 600;

    @Config.Comment({
        "Durability of the iron hammer.",
        "Default: " + 750
    })
    public int IRON_HAMMER_DURABILITY = 750;

    @Config.Comment({
        "Durability of the iron hammer.",
        "Default: " + 33
    })
    public int GOLD_HAMMER_DURABILITY = 33;

    @Config.Comment({
        "Durability of the diamond hammer.",
        "Default: " + 4500
    })
    public int DIAMOND_HAMMER_DURABILITY = 4500;

    @Config.Comment({
        "Durability of the obsidian hammer.",
        "Default: " + 4035
    })
    public int OBSIDIAN_HAMMER_DURABILITY = 4035;
  }

  // ---------------------------------------------------------------------------
  // - Gloamberry Bush
  // ---------------------------------------------------------------------------

  public static GloamberryBush GLOAMBERRY_BUSH = new GloamberryBush();

  public static class GloamberryBush {

    @Config.Comment({
        "The chance of advancing to the next growth stage when the block randomly ticks.",
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0)
    public double GROWTH_CHANCE = 0.05;

    @Config.Comment({
        "The chance of advancing to the last growth stage when the block randomly ticks.",
        "Default: " + 0.1
    })
    @Config.RangeDouble(min = 0)
    public double BERRY_GROWTH_CHANCE = 0.1;

    @Config.Comment({
        "The multiplicative modifier applied to the growth chance when the block can't see sky.",
        "chance = chance * modifier",
        "Default: " + 0.25
    })
    @Config.RangeDouble(min = 0)
    public double OBSTRUCTED_GROWTH_MULTIPLICATIVE_MODIFIER = 0.25;

    @Config.Comment({
        "The chance of losing it's berries during the day.",
        "Default: " + 0.75
    })
    @Config.RangeDouble(min = 0)
    public double DAYTIME_BERRY_LOSS_CHANCE = 0.75;
  }

  // ---------------------------------------------------------------------------
  // - Pyroberry Wine
  // ---------------------------------------------------------------------------

  public static GloamberryWine GLOAMBERRY_WINE = new GloamberryWine();

  public static class GloamberryWine {

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int HUNGER = 1;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.10
    })
    @Config.RangeDouble(min = 0)
    public double SATURATION = 0.10;

    @Config.Comment({
        "The tick duration of the effect.",
        "Default: " + (60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int EFFECT_DURATION_TICKS = 60 * 20;

    @Config.Comment({
        "The effect duration after which the player becomes sick.",
        "Default: " + (2 * 60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int SICK_THRESHOLD_TICKS = 2 * 60 * 20;
  }

  // ---------------------------------------------------------------------------
  // - Pyroberry Bush
  // ---------------------------------------------------------------------------

  public static PyroberryBush PYROBERRY_BUSH = new PyroberryBush();

  public static class PyroberryBush {

    @Config.Comment({
        "The chance of advancing to the next growth stage when the block randomly ticks.",
        "Default: " + 0.025
    })
    @Config.RangeDouble(min = 0)
    public double GROWTH_CHANCE = 0.025;

    @Config.Comment({
        "The chance of advancing to the last growth stage when the block randomly ticks.",
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0)
    public double BERRY_GROWTH_CHANCE = 0.05;

    @Config.Comment({
        "The chance of reverting to a previous growth stage when the block randomly ticks in the rain.",
        "Default: " + 1.0
    })
    @Config.RangeDouble(min = 0)
    public double RAIN_GROWTH_REVERT_CHANCE = 1.0;

    @Config.Comment({
        "The chance of reverting to a previous growth stage when the block randomly ticks and can't see sky.",
        "Default: " + 0.25
    })
    @Config.RangeDouble(min = 0)
    public double OBSTRUCTED_GROWTH_REVERT_CHANCE = 0.25;
  }

  // ---------------------------------------------------------------------------
  // - Pyroberry Wine
  // ---------------------------------------------------------------------------

  public static PyroberryWine PYROBERRY_WINE = new PyroberryWine();

  public static class PyroberryWine {

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int HUNGER = 1;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.10
    })
    @Config.RangeDouble(min = 0)
    public double SATURATION = 0.10;

    @Config.Comment({
        "The tick duration of the effect.",
        "Default: " + (60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int EFFECT_DURATION_TICKS = 60 * 20;

    @Config.Comment({
        "The effect duration after which the player becomes sick.",
        "Default: " + (2 * 60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int SICK_THRESHOLD_TICKS = 2 * 60 * 20;
  }

  // ---------------------------------------------------------------------------
  // - Pyroberry Cocktail
  // ---------------------------------------------------------------------------

  public static PyroberryCocktail PYROBERRY_COCKTAIL = new PyroberryCocktail();

  public static class PyroberryCocktail {

    @Config.Comment({
        "Defines the number of ticks a player must wait between throws.",
        "Default: " + 20
    })
    public int THROW_COOLDOWN_TICKS = 20;

    @Config.Comment({
        "Increase this number to decrease the thrown accuracy.",
        "Default: " + 5.0
    })
    @Config.RangeDouble(min = 1)
    public double INACCURACY = 5.0;

    @Config.Comment({
        "The velocity.",
        "Default: " + 0.75
    })
    @Config.RangeDouble(min = 0)
    public double VELOCITY = 0.75;

    @Config.Comment({
        "The pitch offset.",
        "Default: " + (-15.0)
    })
    public double PITCH = -15.0;

    @Config.Comment({
        "Defines the number of seconds for which a target entity will be set ablaze.",
        "Default: " + 20
    })
    public int ENTITY_FIRE_DURATION_SECONDS = 20;

    @Config.Comment({
        "Defines the range of blocks that will be set ablaze.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 0)
    public int FIRE_RANGE = 4;

    @Config.Comment({
        "The chance to light a block on fire in the given range.",
        "Default: " + 0.25
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double FIRE_CHANCE = 0.25;

  }

  // ---------------------------------------------------------------------------
  // - Rocks
  // ---------------------------------------------------------------------------

  public static Rocks ROCKS = new Rocks();

  public static class Rocks {

    @Config.Comment({
        "Defines the number of ticks a player must wait between throwing rocks / clumps.",
        "Default: " + 10
    })
    public int THROW_COOLDOWN_TICKS = 10;

    @Config.Comment({
        "Set to false to prevent players from throwing rocks and clumps.",
        "Default: " + true
    })
    public boolean THROW_ENABLED = true;

    @Config.Comment({
        "Netherrack rocks spread netherrack when placed. This defines how big",
        "their range is.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 16)
    public int NETHERRACK_SPREAD_RADIUS = 3;

    @Config.Comment({
        "Increase this number to decrease the thrown rock accuracy.",
        "Default: " + 5.0
    })
    @Config.RangeDouble(min = 1)
    public double INACCURACY = 5.0;

    @Config.Comment({
        "The velocity of a thrown rock.",
        "Default: " + 0.75
    })
    @Config.RangeDouble(min = 0)
    public double VELOCITY = 0.75;

    @Config.Comment({
        "The pitch offset of a thrown rock.",
        "Default: " + (-15.0)
    })
    public double PITCH = -15.0;

    @Config.Comment({
        "The damage of a thrown rock.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0)
    public double DAMAGE = 0.5;
  }

  // ---------------------------------------------------------------------------
  // - Food
  // ---------------------------------------------------------------------------

  public static Food FOOD = new Food();

  public static class Food {

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int PYROBERRIES_HUNGER = 2;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.1
    })
    @Config.RangeDouble(min = 0)
    public double PYROBERRIES_SATURATION = 0.1;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int GLOAMBERRIES_HUNGER = 2;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.1
    })
    @Config.RangeDouble(min = 0)
    public double GLOAMBERRIES_SATURATION = 0.1;

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
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 6
    })
    @Config.RangeInt(min = 0)
    public int STRANGE_TUBER_HUNGER = 2;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.2
    })
    @Config.RangeDouble(min = 0)
    public double STRANGE_TUBER_SATURATION = 0.2;

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
    public int BURNED_FOOD_HUNGER = 2;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.1
    })
    @Config.RangeDouble(min = 0)
    public double BURNED_FOOD_SATURATION = 0.1;

    @Config.Comment({
        "Eating the burned food applies the hunger effect like zombie flesh.",
        "Set to zero to disable",
        "Default: " + 600
    })
    @Config.RangeInt(min = 0)
    public int BURNED_FOOD_HUNGER_EFFECT_DURATION_TICKS = 600;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int TAINTED_MEAT_HUNGER = 1;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0)
    public double TAINTED_MEAT_SATURATION = 0.05;

    @Config.Comment({
        "Eating the burned food applies the poison effect.",
        "Set to zero to disable",
        "Default: " + 600
    })
    @Config.RangeInt(min = 0)
    public int TAINTED_MEAT_POISON_EFFECT_DURATION_TICKS = 600;

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
        "minecraft:iron_nugget",
        "minecraft:gold_nugget",
        "minecraft:iron_ingot_from_block",
        "minecraft:gold_ingot_from_block",
        "minecraft:fire_charge",
        "minecraft:leather",
        "minecraft:item_frame",
        "minecraft:book",
        "minecraft:lead",
        "minecraft:magma_cream",
        "minecraft:arrow",

        "minecraft:boat",
        "minecraft:spruce_boat",
        "minecraft:birch_boat",
        "minecraft:jungle_boat",
        "minecraft:acacia_boat",
        "minecraft:dark_oak_boat",

        "minecraft:leather_leggings",
        "minecraft:leather_helmet",
        "minecraft:leather_chestplate",
        "minecraft:leather_boots"
    };
  }

  // ---------------------------------------------------------------------------
  // - Client
  // ---------------------------------------------------------------------------

  public static Client CLIENT = new Client();

  public static class Client {

    @Config.Comment({
        "Set to false to hide the durability tooltip on tools."
    })
    public boolean SHOW_DURABILITY_TOOLTIPS = true;

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
        "Show an item's burn time in it's tooltip.",
        "Default: " + true
    })
    public boolean SHOW_BURN_TIME_IN_TOOLTIPS = true;

    @Config.Comment({
        "Some interactions will give off some green particles to indicate",
        "that the recipe / tool combination is valid and recipe progress",
        "has incremented.",
        "",
        "Set to false to disable these progression particles."
    })
    public boolean SHOW_RECIPE_PROGRESSION_PARTICLES = true;
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

    @Config.Comment({
        "Set to true to restrict the placement of mulch to moisturized farmland",
        "only. If set to false, mulch can be placed on wet or dry farmland.",
        "Default: " + false
    })
    public boolean RESTRICT_TO_MOISTURIZED_FARMLAND = false;
  }

  // ---------------------------------------------------------------------------
  // - Ore Compat
  // ---------------------------------------------------------------------------

  public static OreCompat ORE_COMPAT = new OreCompat();

  public static class OreCompat {

    @Config.Comment({
        "This map contains the oredict keys and hex color values that the ore",
        "compatibility system uses to auto generate content.",
        "Format: S:(oredict)=(hex_color)"
    })
    public Map<String, String> OREDICT_COLOR_MAP = new TreeMap<String, String>() {{
      this.put("oreAluminum", "e2e2e3");
      this.put("oreArdite", "e85e17");
      this.put("oreCobalt", "1c5bc1");
      this.put("oreCopper", "ffaa1c");
      this.put("oreGold", "fcee4b");
      this.put("oreIridium", "d5d4e7");
      this.put("oreIron", "d8af93");
      this.put("oreLead", "95afee");
      this.put("oreMithril", "9cf9ff");
      this.put("oreNickel", "e4e5d8");
      this.put("oreOctine", "ffaa22");
      this.put("oreOsmium", "9babc4");
      this.put("orePlatinum", "55f3ff");
      this.put("oreSilver", "e8f6fd");
      this.put("oreSyrmorite", "4455bb");
      this.put("oreTin", "c3e9ff");
      this.put("oreUranium", "5c734b");
    }};
  }

  // ---------------------------------------------------------------------------
  // - Fuel
  // ---------------------------------------------------------------------------

  public static Fuel FUEL = new Fuel();

  public static class Fuel {

    @Config.Comment({
        "Board burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 75
    })
    @Config.RangeInt(min = 0)
    public int BOARD_BURN_TIME_TICKS = 75;

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
        "Dried plant fibers burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 15
    })
    public int DRIED_PLANT_FIBERS = 15;

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
        "Default: " + 50
    })
    @Config.RangeInt(min = 0)
    public int WOOD_CHIPS_BURN_TIME_TICKS = 50;

    @Config.Comment({
        "Wood chips burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 400
    })
    @Config.RangeInt(min = 0)
    public int PILE_WOOD_CHIPS_BURN_TIME_TICKS = 400;

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

    @Config.Comment({
        "Pyroberries burn time in ticks.",
        "A burn time of 200 ticks will smelt one item in the vanilla furnace.",
        "Default: " + 400
    })
    @Config.RangeInt(min = 0)
    public int PYROBERRIES_BURN_TIME_TICKS = 400;
  }

  @Config.Comment({
      "A list of fluids that are valid for use in dousing extinguishable blocks."
  })
  public static String[] VALID_DOUSING_FLUIDS = {
      "water"
  };

}
