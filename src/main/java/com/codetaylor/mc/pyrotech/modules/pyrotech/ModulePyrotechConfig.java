package com.codetaylor.mc.pyrotech.modules.pyrotech;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleFluids;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

@Config(modid = ModulePyrotech.MOD_ID, name = ModulePyrotech.MOD_ID + "/" + ModulePyrotech.MOD_ID + ".module.Pyrotech")
public class ModulePyrotechConfig {

  // ---------------------------------------------------------------------------
  // - Bucket - Wood
  // ---------------------------------------------------------------------------

  public static BucketWood BUCKET_WOOD = new BucketWood();

  public static class BucketWood {

    @Config.Comment({
        "Set to false to disable the bucket.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Range: [1, +int]",
        "Default: " + 8
    })
    public int MAX_DURABILITY = 8;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Range: [-int, +int]",
        "Default: " + 450
    })
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 8
    })
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 8;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 2
    })
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 1
    })
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 1;
  }

  // ---------------------------------------------------------------------------
  // - Bucket - Clay
  // ---------------------------------------------------------------------------

  public static BucketClay BUCKET_CLAY = new BucketClay();

  public static class BucketClay {

    @Config.Comment({
        "Set to false to disable the bucket.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Range: [1, +int]",
        "Default: " + 16
    })
    public int MAX_DURABILITY = 16;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Range: [-int, +int]",
        "Default: " + 450
    })
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 4
    })
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 4;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 2
    })
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 0
    })
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 0;
  }

  // ---------------------------------------------------------------------------
  // - Bucket - Stone
  // ---------------------------------------------------------------------------

  public static BucketStone BUCKET_STONE = new BucketStone();

  public static class BucketStone {

    @Config.Comment({
        "Set to false to disable the bucket.",
        "Default: " + true
    })
    public boolean ENABLED = true;

    @Config.Comment({
        "Set to true to show all bucket / fluid combinations.",
        "Default: " + false
    })
    public boolean SHOW_ALL_BUCKETS = false;

    @Config.Comment({
        "Set the number of times the bucket can be drained.",
        "Range: [1, +int]",
        "Default: " + 32
    })
    public int MAX_DURABILITY = 32;

    @Config.Comment({
        "The temperature that the bucket considers hot.",
        "The temperature of lava is 1300 and water is 300",
        "Range: [-int, +int]",
        "Default: " + 450
    })
    public int HOT_TEMPERATURE = 450;

    @Config.Comment({
        "The bucket will take damage if it is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 4
    })
    public int HOT_CONTAINER_DAMAGE_PER_SECOND = 4;

    @Config.Comment({
        "The player will take damage if the bucket is in the player's inventory",
        "and is filled with a hot fluid.",
        "This controls the amount of damage done to the player per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 2
    })
    public int HOT_PLAYER_DAMAGE_PER_SECOND = 2;

    @Config.Comment({
        "The container will take damage if the it is in the player's inventory",
        "and is filled.",
        "This controls the amount of damage done to the container per second.",
        "Set to zero to disable.",
        "Range: [0, +int]",
        "Default: " + 0
    })
    public int FULL_CONTAINER_DAMAGE_PER_SECOND = 0;
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
  // - Crate
  // ---------------------------------------------------------------------------

  public static Crate CRATE = new Crate();

  public static class Crate {

    @Config.Comment({
        "The maximum number of stacks that can be stored in each slot.",
        "Range: [1, (+int/stack_size)]",
        "Default: " + 1
    })
    public int MAX_STACKS = 1;
  }

  // ---------------------------------------------------------------------------
  // - Durable Crate
  // ---------------------------------------------------------------------------

  public static DurableCrate DURABLE_CRATE = new DurableCrate();

  public static class DurableCrate {

    @Config.Comment({
        "The maximum number of items that can be stored in each slot.",
        "Range: [1, (+int/stack_size)]",
        "Default: " + 4
    })
    public int MAX_STACKS = 4;
  }

  // ---------------------------------------------------------------------------
  // - Shelf
  // ---------------------------------------------------------------------------

  public static Shelf SHELF = new Shelf();

  public static class Shelf {

    @Config.Comment({
        "The maximum number of stacks that can be stored in each slot.",
        "Range: [1, (+int/stack_size)]",
        "Default: " + 1
    })
    public int MAX_STACKS = 1;
  }

  // ---------------------------------------------------------------------------
  // - Durable Shelf
  // ---------------------------------------------------------------------------

  public static DurableShelf DURABLE_SHELF = new DurableShelf();

  public static class DurableShelf {

    @Config.Comment({
        "The maximum number of items that can be stored in each slot.",
        "Range: [1, (+int/stack_size)]",
        "Default: " + 4
    })
    public int MAX_STACKS = 4;
  }

  // ---------------------------------------------------------------------------
  // - Stash
  // ---------------------------------------------------------------------------

  public static Stash STASH = new Stash();

  public static class Stash {

    @Config.Comment({
        "The maximum number of stacks that can be stored in the stash.",
        "Range: [1, (+int/stack_size)]",
        "Default: " + 4
    })
    public int MAX_STACKS = 4;
  }

  // ---------------------------------------------------------------------------
  // - Durable Stash
  // ---------------------------------------------------------------------------

  public static DurableStash DURABLE_STASH = new DurableStash();

  public static class DurableStash {

    @Config.Comment({
        "The maximum number of stacks that can be stored in the stash.",
        "Range: [1, (+int/stack_size)]",
        "Default: " + 16
    })
    public int MAX_STACKS = 16;
  }

  // ---------------------------------------------------------------------------
  // - Worktable Common
  // ---------------------------------------------------------------------------

  public static WorktableCommon WORKTABLE_COMMON = new WorktableCommon();

  public static class WorktableCommon {

    @Config.Comment({
        "Use this to add items that you want to be valid for banging on the ",
        "worktable. This list is shared between all worktables.",
        "",
        "NOTE: Items you add are assumed to have durability.",
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
        "Any recipe that you can complete in a vanilla crafting table can also",
        "be completed in this mod's worktables.",
        "",
        "If this list is not empty, only the recipes listed here will be allowed.",
        "",
        "The whitelist takes priority over the blacklist.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] RECIPE_WHITELIST = new String[0];

    @Config.Comment({
        "Any recipe that you can complete in a vanilla crafting table can also",
        "be completed in this mod's worktables.",
        "",
        "If this list is not empty, recipes listed here will be disallowed.",
        "",
        "The whitelist takes priority over the blacklist.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] RECIPE_BLACKLIST = {
        "minecraft:chest"
    };
  }

  // ---------------------------------------------------------------------------
  // - Worktable
  // ---------------------------------------------------------------------------

  public static Worktable WORKTABLE = new Worktable();

  public static class Worktable {

    @Config.Comment({
        "The number of hammer hits required to complete a craft.",
        "Range: [1, +int]",
        "Default: " + 4
    })
    public int HITS_PER_CRAFT = 4;

    @Config.Comment({
        "The amount of damage applied to the tool per craft.",
        "Range: [0, +int]",
        "Default: " + 2
    })
    public int TOOL_DAMAGE_PER_CRAFT = 2;

    @Config.Comment({
        "The maximum stack size for each slot in the crafting grid.",
        "Range: [1, 64]",
        "Default: " + 1
    })
    public int GRID_MAX_STACK_SIZE = 1;

    @Config.Comment({
        "The maximum stack size for each slot in the shelf.",
        "Range: [1, 64]",
        "Default: " + 1
    })
    public int SHELF_MAX_STACK_SIZE = 1;

    @Config.Comment({
        "If true, the worktable has durability and will break after the configured",
        "number of crafts completed.",
        "Default: " + true
    })
    public boolean USES_DURABILITY = true;

    @Config.Comment({
        "The number of crafts that the worktable can perform before it breaks.",
        "This is only relevant if the `USES_DURABILITY` flag is true.",
        "Range: [1, +int]",
        "Default: " + 64
    })
    public int DURABILITY = 64;

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Range: [0, 40]",
        "Default: " + 0.5
    })
    public double EXHAUSTION_COST_PER_HIT = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Range: [0, 40]",
        "Default: " + 0
    })
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Range: [0, 20]",
        "Default: 3"
    })
    public int MINIMUM_HUNGER_TO_USE = 3;
  }

  // ---------------------------------------------------------------------------
  // - Stone Worktable
  // ---------------------------------------------------------------------------

  public static StoneWorktable STONE_WORKTABLE = new StoneWorktable();

  public static class StoneWorktable {

    @Config.Comment({
        "The number of hammer hits required to complete a craft.",
        "Range: [1, +int]",
        "Default: " + 1
    })
    public int HITS_PER_CRAFT = 2;

    @Config.Comment({
        "The amount of damage applied to the tool per craft.",
        "Range: [0, +int]",
        "Default: " + 1
    })
    public int TOOL_DAMAGE_PER_CRAFT = 1;

    @Config.Comment({
        "The maximum stack size for each slot in the crafting grid.",
        "Range: [1, 64]",
        "Default: " + 1
    })
    public int GRID_MAX_STACK_SIZE = 32;

    @Config.Comment({
        "The maximum stack size for each slot in the shelf.",
        "Range: [1, 64]",
        "Default: " + 64
    })
    public int SHELF_MAX_STACK_SIZE = 64;

    @Config.Comment({
        "If true, the worktable has durability and will break after the configured",
        "number of crafts completed.",
        "Default: " + false
    })
    public boolean USES_DURABILITY = false;

    @Config.Comment({
        "The number of crafts that the worktable can perform before it breaks.",
        "This is only relevant if the `USES_DURABILITY` flag is true.",
        "Range: [1, +int]",
        "Default: " + (64 * 8)
    })
    public int DURABILITY = 64 * 8;

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Range: [0, 40]",
        "Default: " + 0.5
    })
    public double EXHAUSTION_COST_PER_HIT = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Range: [0, 40]",
        "Default: " + 0
    })
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Range: [0, 20]",
        "Default: 3"
    })
    public int MINIMUM_HUNGER_TO_USE = 3;
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
        "The item displayed to represent each harvest level in JEI.",
        "The index into the array is the harvest level, the value at that index",
        "is the displayed item. The array can be expanded as needed.",
        "If the harvest level of the tool used exceeds the array length, the",
        "last element in the array is used.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] JEI_HARVEST_LEVEL_ITEM = new String[]{
        "pyrotech:crude_shovel",
        "minecraft:stone_shovel",
        "minecraft:iron_shovel",
        "minecraft:diamond_shovel"
    };

    @Config.Comment({
        "The number of uses required per harvest level of the tool used.",
        "The index into the array is the harvest level, the value at that index",
        "is the required number of uses. The array can be expanded as needed.",
        "If the harvest level of the tool used exceeds the array length, the",
        "last element in the array is used.",
        "",
        "ie. {wood, stone, iron, diamond}",
        "Valid values are in the range: [1,+int]",
        "Default: {4, 3, 2, 1}"
    })
    public int[] TOOL_USES_REQUIRED_PER_HARVEST_LEVEL = new int[]{4, 3, 2, 1};

    @Config.Comment({
        "The amount of damage applied to the tool when a craft completes.",
        "Default: " + 1
    })
    public int TOOL_DAMAGE_PER_CRAFT = 1;

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Range: [0, 40]",
        "Default: " + 0.5
    })
    public double EXHAUSTION_COST_PER_HIT = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Range: [0, 40]",
        "Default: " + 0
    })
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Range: [0, 20]",
        "Default: 3"
    })
    public int MINIMUM_HUNGER_TO_USE = 3;
  }

  // ---------------------------------------------------------------------------
  // - Soaking Pot
  // ---------------------------------------------------------------------------

  public static SoakingPot SOAKING_POT = new SoakingPot();

  public static class SoakingPot {

    @Config.Comment({
        "The maximum stack size that can be placed in the pot.",
        "Range: [1, 64]",
        "Default: " + 1
    })
    public int MAX_STACK_SIZE = 1;

    @Config.Comment({
        "The maximum fluid capacity in millibuckets.",
        "Range: [1, +int]",
        "Default: " + 2000
    })
    public int MAX_FLUID_CAPACITY = 2000;
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
        "Default: " + 64
    })
    public int HITS_PER_DAMAGE = 64;

    @Config.Comment({
        "Use this to add items that you want to be valid for hammer recipes.",
        "The reduction parameter supplied here is used to reduce the number",
        "of hits required to complete a recipe. The reduction amount is subtracted",
        "from the recipe's number of hits.",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path);(hit_reduction)"
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
     * Returns the hammer hit reduction for the given hammer resource location,
     * or -1 if the given hammer isn't in the list.
     *
     * @param resourceLocation the hammer
     * @return the hammer hit reduction
     */
    public int getHammerHitReduction(ResourceLocation resourceLocation) {

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

    @Config.Comment({
        "The pickaxe displayed to represent each harvest level in JEI.",
        "The index into the array is the harvest level, the value at that index",
        "is the displayed item. The array can be expanded as needed.",
        "If the harvest level of the tool used exceeds the array length, the",
        "last element in the array is used.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] JEI_HARVEST_LEVEL_PICKAXE = new String[]{
        "pyrotech:crude_pickaxe",
        "minecraft:stone_pickaxe",
        "minecraft:iron_pickaxe",
        "minecraft:diamond_pickaxe"
    };

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Range: [0, 40]",
        "Default: " + 0.5
    })
    public double EXHAUSTION_COST_PER_HIT = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Range: [0, 40]",
        "Default: " + 0
    })
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Range: [0, 20]",
        "Default: 3"
    })
    public int MINIMUM_HUNGER_TO_USE = 3;
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
        "Default: {6, 4, 2, 2}"
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

    @Config.Comment({
        "How much exhaustion to apply per axe chop.",
        "Range: [0, 40]",
        "Default: " + 0.5
    })
    public double EXHAUSTION_COST_PER_AXE_CHOP = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per shovel scoop.",
        "Range: [0, 40]",
        "Default: " + 0.5
    })
    public double EXHAUSTION_COST_PER_SHOVEL_SCOOP = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Range: [0, 40]",
        "Default: " + 0
    })
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Range: [0, 20]",
        "Default: 3"
    })
    public int MINIMUM_HUNGER_TO_USE = 3;
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
        "Chance of producing wood chips per second of operation.",
        "Default: " + 0.15
    })
    public double WOOD_CHIPS_CHANCE = 0.15;
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

    @Config.Comment({
        "If true, the machine will process all input items at the same time.",
        "If false, the machine will process one recipe at a time.",
        "Default: " + false
    })
    public boolean ASYNCHRONOUS_OPERATION = false;
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

  @SuppressWarnings("unused")
  public static class ConditionConfig
      implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {

      String key = JsonUtils.getString(json, "key");

      switch (key) {
        case "bucket_wood":
          return () -> BUCKET_WOOD.ENABLED;
        case "bucket_clay":
          return () -> BUCKET_CLAY.ENABLED;
        case "bucket_stone":
          return () -> BUCKET_STONE.ENABLED;
        default:
          throw new JsonSyntaxException("Unknown config key [" + key + "]");
      }
    }
  }
}
