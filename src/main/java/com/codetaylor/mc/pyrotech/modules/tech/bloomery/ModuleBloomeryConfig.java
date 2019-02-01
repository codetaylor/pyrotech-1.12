package com.codetaylor.mc.pyrotech.modules.tech.bloomery;

import net.minecraftforge.common.config.Config;

@Config(modid = ModuleBloomery.MOD_ID, name = ModuleBloomery.MOD_ID + "/" + "module.tech.Bloomery")
public class ModuleBloomeryConfig {

  // ---------------------------------------------------------------------------
  // - Slag
  // ---------------------------------------------------------------------------

  public static Slag SLAG = new Slag();

  public static class Slag {

    @Config.Comment({
        "Fire damage applied to entities from the molten slag.",
        "Default: " + 3
    })
    @Config.RangeDouble(min = 0)
    public double MOLTEN_WALK_DAMAGE = 3;

    @Config.Comment({
        "Chance that the player will catch fire when harvesting molten slag.",
        "Default: " + 0.125
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double HARVESTING_PLAYER_FIRE_CHANCE = 0.125;

    @Config.Comment({
        "How many seconds the fire will last when a player catches fire",
        "from harvesting molten slag.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int HARVESTING_PLAYER_FIRE_DURATION_SECONDS = 1;
  }

  // ---------------------------------------------------------------------------
  // - Bloomery
  // ---------------------------------------------------------------------------

  public static Bloomery BLOOMERY = new Bloomery();

  public static class Bloomery {

    @Config.Comment({
        "This is the total amount of fuel burn time required to operate the device",
        "at maximum speed. More fuel will increase the speed of the device with",
        "diminishing returns. If the speed cap flag is set to true, inserted ",
        "fuel items that cause the total burn time of all inserted fuel items",
        "to exceed this value will not be inserted.",
        "Default: " + (32000 * 4)
    })
    @Config.RangeInt(min = 1)
    public int FUEL_CAPACITY_BURN_TIME = 32000 * 4;

    @Config.Comment({
        "The total number of fuel items that the device can hold.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1)
    public int FUEL_CAPACITY_ITEMS = 16;

    @Config.Comment({
        "If true, inserted fuel items that cause the total burn time of all",
        "inserted fuel items to exceed the device's capacity will not be",
        "inserted.",
        "Default: " + false
    })
    public boolean HAS_SPEED_CAP = false;

    @Config.Comment({
        "The bloomery speed is based on the quality (burn time) of the fuel",
        "inserted: y = (scalar)(burnTime/maxBurnTime)^(1/2)",
        "For example, if set to 2, the max speed of the bloomery is 200% and",
        "25% of the total fuel capacity is required for a speed of 100%. If set",
        "to 3, the max speed is 300% and roughly 11% capacity is required for",
        "100% speed."
    })
    @Config.RangeDouble(min = 0)
    public double SPEED_SCALAR = 2;

    @Config.Comment({
        "The maximum amount of ash the device can hold.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1)
    public int MAX_ASH_CAPACITY = 16;

    @Config.Comment({
        "The chance that one fuel item will convert to one ash upon recipe",
        "completion.",
        "Default: " + 0.35
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double ASH_CONVERSION_CHANCE = 0.35;

    @Config.Comment({
        "The amount of damage done to an entity walking on top of the device",
        "when it is active.",
        "Default: " + 3
    })
    @Config.RangeDouble(min = 0)
    public double ENTITY_WALK_BURN_DAMAGE = 3;
  }

  // ---------------------------------------------------------------------------
  // - Bloom
  // ---------------------------------------------------------------------------

  public static Bloom BLOOM = new Bloom();

  public static class Bloom {

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_HIT = 1;

    @Config.Comment({
        "The number of hammer hits required to extract one item from the bloom's contents.",
        "This will be further reduced using the hammer hit reduction number provided",
        "for anvil hammers.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1)
    public int HAMMER_HITS_REQUIRED = 16;

    @Config.Comment({
        "The number of hammer hits required to extract one item from the bloom's contents",
        "when the bloom is placed on this mod's anvil.",
        "This will be further reduced using the hammer hit reduction number provided",
        "for anvil hammers.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1)
    public int HAMMER_HITS_IN_ANVIL_REQUIRED = 8;

    @Config.Comment({
        "If true, using the hammer on the bloom when the bloom is placed in the",
        "world will have a chance to break the block below the bloom when the bloom",
        "releases a stored item. This chance is based on the hardness of the block.",
        "",
        "Uses this to calculate the chance: 1 - (x/60)^(1/8), where 0 <= x <= 50",
        "This means that obsidian has roughly a 2.25% chance to break",
        "and average blocks with a hardness of 2 have roughly a 30% chance to break.",
        "Default: " + true
    })
    public boolean BREAKS_BLOCKS = true;

    @Config.Comment({
        "The amount of fire damage applied to a player when the bloom is in their",
        "inventory.",
        "Set to zero to disable.",
        "Default: " + 3
    })
    @Config.RangeDouble(min = 0)
    public double FIRE_DAMAGE_PER_SECOND = 3;

    @Config.Comment({
        "The amount of fire damage applied to a player when the bloom is in their",
        "inventory. Set to zero to disable.",
        "Default: " + 3
    })
    @Config.RangeDouble(min = 0)
    public double ENTITY_WALK_DAMAGE = 3;

    @Config.Comment({
        "The chance that hitting a raw bloom will spawn a nearby fire.",
        "Default: " + 0.1
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double FIRE_SPAWN_CHANCE_ON_HIT_RAW = 0.1;

    @Config.Comment({
        "The chance that hitting a bloom in an anvil will spawn a nearby fire.",
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double FIRE_SPAWN_CHANCE_ON_HIT_IN_ANVIL = 0.05;

    @Config.Comment({
        "The chance that a bloom will randomly spawn a nearby fire when the",
        "block randomly ticks.",
        "Default: " + 0.25
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double FIRE_SPAWN_CHANCE_RANDOM = 0.25;

    @Config.Comment({
        "A percentage modifier for hammer power per harvest level.",
        "",
        "The index into the array is the harvest level, the value at that index",
        "is the hammer power modifier. The array can be expanded as needed.",
        "If the harvest level of the tool used exceeds the array length, the",
        "last element in the array is used.",
        "",
        "ie. {wood, stone, iron, diamond}",
        "Default: {0.7, 0.85, 1.15, 1.3}"
    })
    public double[] HAMMER_POWER_MODIFIER_PER_HARVEST_LEVEL = {0.7, 0.85, 1.15, 1.3};
  }

  // ---------------------------------------------------------------------------
  // - Tongs
  // ---------------------------------------------------------------------------

  public static Tongs TONGS = new Tongs();

  public static class Tongs {

    @Config.Comment({
        "The durability of the stone tongs.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int STONE_TONGS_DURABILITY = 4;

    @Config.Comment({
        "The durability of the flint tongs.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int FLINT_TONGS_DURABILITY = 4;

    @Config.Comment({
        "The durability of the bone tongs.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int BONE_TONGS_DURABILITY = 4;

    @Config.Comment({
        "The durability of the iron tongs.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int IRON_TONGS_DURABILITY = 16;

    @Config.Comment({
        "The durability of the diamond tongs.",
        "Default: " + 64
    })
    @Config.RangeInt(min = 1, max = Short.MAX_VALUE)
    public int DIAMOND_TONGS_DURABILITY = 64;
  }
}
