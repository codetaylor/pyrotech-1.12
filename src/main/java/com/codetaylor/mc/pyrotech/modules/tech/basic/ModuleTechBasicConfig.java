package com.codetaylor.mc.pyrotech.modules.tech.basic;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Config(modid = ModuleTechBasic.MOD_ID, name = ModuleTechBasic.MOD_ID + "/" + "module.tech.Basic")
public class ModuleTechBasicConfig {

  @Config.Ignore
  public static final List<Ingredient> CAMPFIRE_FUEL_WHITELIST = new ArrayList<>(1);

  @Config.Ignore
  public static final List<Ingredient> CAMPFIRE_FUEL_BLACKLIST = new ArrayList<>(1);

  @Config.Ignore
  public static Stages STAGES_WORKTABLE = null;

  @Config.Ignore
  public static Stages STAGES_WORKTABLE_STONE = null;

  @Config.Ignore
  public static Stages STAGES_SOAKING_POT = null;

  @Config.Ignore
  public static Stages STAGES_PIT_KILN = null;

  @Config.Ignore
  public static Stages STAGES_DRYING_RACK_CRUDE = null;

  @Config.Ignore
  public static Stages STAGES_DRYING_RACK = null;

  @Config.Ignore
  public static Stages STAGES_COMPACTING_BIN = null;

  @Config.Ignore
  public static Stages STAGES_COMPOST_BIN = null;

  @Config.Ignore
  public static Stages STAGES_CHOPPING_BLOCK = null;

  @Config.Ignore
  public static Stages STAGES_CAMPFIRE = null;

  @Config.Ignore
  public static Stages STAGES_ANVIL_IRONCLAD = null;

  @Config.Ignore
  public static Stages STAGES_ANVIL_GRANITE = null;

  // ---------------------------------------------------------------------------
  // - Worktable Common
  // ---------------------------------------------------------------------------

  public static WorktableCommon WORKTABLE_COMMON = new WorktableCommon();

  public static class WorktableCommon {

    @Config.Comment({
        "Any recipe that you can complete in a vanilla crafting table can also",
        "be completed in this mod's worktables.",
        "",
        "If this list is not empty, only the recipes listed here will be allowed.",
        "",
        "The whitelist takes priority over the blacklist.",
        "",
        "String format is a recipe resource location: (domain):(path) or (domain):*"
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
        "String format is a recipe resource location: (domain):(path) or (domain):*"
    })
    public String[] RECIPE_BLACKLIST = new String[0];

    @Config.Comment({
        "If this is true, a player will be allowed to sneak + click using an",
        "empty hand to remove all items from the worktable's crafting grid.",
        "The removed items will be placed into the player's inventory or on top",
        "of the worktable if the player's inventory is full.",
        "Default: " + false
    })
    public boolean ALLOW_RECIPE_CLEAR = false;

    @Config.Comment({
        "If this is true, a player will be allowed to sneak + click using a",
        "hammer to automatically place items from their inventory into the",
        "worktable's crafting grid that match the ingredients for the last",
        "recipe completed. The hammer will be damaged, see RECIPE_REPEAT_TOOL_DAMAGE.",
        "Default: " + false
    })
    public boolean ALLOW_RECIPE_REPEAT = false;

    @Config.Comment({
        "If ALLOW_RECIPE_REPEAT is enabled, this is the amount of damage that",
        "will be applied to the hammer. Set to zero to disable.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int RECIPE_REPEAT_TOOL_DAMAGE = 1;
  }

  // ---------------------------------------------------------------------------
  // - Worktable
  // ---------------------------------------------------------------------------

  public static Worktable WORKTABLE = new Worktable();

  public static class Worktable {

    @Config.Comment({
        "The number of hammer hits required to complete a craft.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1)
    public int HITS_PER_CRAFT = 4;

    @Config.Comment({
        "The amount of damage applied to the tool per craft.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int TOOL_DAMAGE_PER_CRAFT = 2;

    @Config.Comment({
        "The maximum stack size for each slot in the crafting grid.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 1, max = 64)
    public int GRID_MAX_STACK_SIZE = 1;

    @Config.Comment({
        "The maximum stack size for each slot in the shelf.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 1, max = 64)
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
        "Default: " + 64
    })
    @Config.RangeInt(min = 1)
    public int DURABILITY = 64;

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_HIT = 1;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Default: " + 0
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;
  }

  // ---------------------------------------------------------------------------
  // - Stone Worktable
  // ---------------------------------------------------------------------------

  public static StoneWorktable STONE_WORKTABLE = new StoneWorktable();

  public static class StoneWorktable {

    @Config.Comment({
        "The number of hammer hits required to complete a craft.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 1)
    public int HITS_PER_CRAFT = 2;

    @Config.Comment({
        "The amount of damage applied to the tool per craft.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int TOOL_DAMAGE_PER_CRAFT = 1;

    @Config.Comment({
        "The maximum stack size for each slot in the crafting grid.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 1, max = 64)
    public int GRID_MAX_STACK_SIZE = 32;

    @Config.Comment({
        "The maximum stack size for each slot in the shelf.",
        "Default: " + 64
    })
    @Config.RangeInt(min = 1, max = 64)
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
        "Default: " + (64 * 8)
    })
    @Config.RangeInt(min = 1)
    public int DURABILITY = 64 * 8;

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_HIT = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Default: " + 0
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;
  }

  // ---------------------------------------------------------------------------
  // - Compost Bin
  // ---------------------------------------------------------------------------

  public static CompostBin COMPOST_BIN = new CompostBin();

  public static class CompostBin {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "Set this to false to prevent the mod from automatically creating",
        "mulch recipes from food items.",
        "Default: " + true
    })
    public boolean AUTO_CREATE_RECIPES_FROM_FOOD = true;

    @Config.Comment({
        "Set this to false to prevent displaying the compost value in tooltips.",
        "Default: " + true
    })
    public boolean SHOW_COMPOST_VALUE_IN_TOOLTIPS = true;

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
        "The maximum number of output items the device can hold.",
        "This value also determines its maximum capacity.",
        "Default: " + 16
    })
    @Config.RequiresMcRestart
    public int MAXIMUM_OUTPUT_ITEM_CAPACITY = 16;

    @Config.Comment({
        "The required compost value to complete one recipe.",
        "Default: " + 16
    })
    public int COMPOST_VALUE_REQUIRED_PER_OUTPUT_ITEM = 16;

    @Config.Comment({
        "The range of compost values used when generating values for food.",
        "Default: [1, 8]"
    })
    public int[] GENERATED_FOOD_COMPOST_VALUE_RANGE = {1, 8};

    @Config.Comment({
        "How long does the process take in ticks.",
        "Default: " + (24000 * 4)
    })
    public int COMPOST_DURATION_TICKS = (24000 * 4);

    @Config.Comment({
        "A compost bin layer will compost faster for each actively composting",
        "layer above it. By default a layer will be 20% faster for each layer",
        "above it that is actively composting.",
        "Default: " + 0.2
    })
    public double ADDITIVE_PERCENTILE_SPEED_MODIFIER_PER_LAYER = 0.2;

    @Config.Comment({
        "The rate at which moisture will evaporate in mB / tick. The default",
        "is 1 mB every 48 ticks and will last two full Minecraft day/night cycles.",
        "Default: [1, 48]"
    })
    public int[] MOISTURE_EVAPORATION_RATE_MILLIBUCKETS_PER_TICK = {1, 48};
  }

  // ---------------------------------------------------------------------------
  // - Compacting Bin
  // ---------------------------------------------------------------------------

  public static CompactingBin COMPACTING_BIN = new CompactingBin();

  public static class CompactingBin {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

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
        "The number of output blocks the compacting bin can hold.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1)
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
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_HIT = 1;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Default: " + 0
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: 3"
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;
  }

  // ---------------------------------------------------------------------------
  // - Barrel
  // ---------------------------------------------------------------------------

  public static Barrel BARREL = new Barrel();

  public static class Barrel {

    @Config.Comment({
        "Multiplicative modifier applied to every recipe in this device.",
        "recipeDurationTicks = recipeDurationTicks * BASE_RECIPE_DURATION_MODIFIER",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double BASE_RECIPE_DURATION_MODIFIER = 1;

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;
  }

  // ---------------------------------------------------------------------------
  // - Soaking Pot
  // ---------------------------------------------------------------------------

  public static SoakingPot SOAKING_POT = new SoakingPot();

  public static class SoakingPot {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The maximum stack size that can be placed in the pot.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MAX_STACK_SIZE = 8;

    @Config.Comment({
        "The maximum fluid capacity in millibuckets.",
        "Default: " + 4000
    })
    @Config.RangeInt(min = 1)
    public int MAX_FLUID_CAPACITY = 4000;

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
    public boolean HOLDS_HOT_FLUIDS = false;

    @Config.Comment({
        "Multiplicative modifier applied to every recipe in this device.",
        "recipeDurationTicks = recipeDurationTicks * BASE_RECIPE_DURATION_MODIFIER",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double BASE_RECIPE_DURATION_MODIFIER = 1;
  }

  // ---------------------------------------------------------------------------
  // - Anvil Common
  // ---------------------------------------------------------------------------

  public static AnvilCommon ANVIL_COMMON = new AnvilCommon();

  public static class AnvilCommon {

    @Config.Comment({
        "These values are used to reduce the number of hits required to complete",
        "a recipe.",
        "",
        "The index into the array is the harvest level, the value at that index",
        "is the hit reduction. The array can be expanded as needed.",
        "If the harvest level of the tool used exceeds the array length, the",
        "last element in the array is used.",
        "",
        "ie. {wood, stone, iron, diamond}",
        "Default: {0, 1, 2, 3}"
    })
    public int[] HIT_REDUCTION_PER_HAMMER_HARVEST_LEVEL = {0, 1, 2, 3};

    /**
     * Returns the hammer hit reduction for the given hammer resource location,
     * or -1 if the given hammer isn't in the list.
     *
     * @param resourceLocation the hammer
     * @return the hammer hit reduction
     */
    public int getHammerHitReduction(ResourceLocation resourceLocation) {

      int hammerHarvestLevel = ModuleCoreConfig.HAMMERS.getHammerHarvestLevel(resourceLocation);

      if (hammerHarvestLevel > -1) {
        return ArrayHelper.getOrLast(this.HIT_REDUCTION_PER_HAMMER_HARVEST_LEVEL, hammerHarvestLevel);
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
  }

  // ---------------------------------------------------------------------------
  // - Granite Anvil
  // ---------------------------------------------------------------------------

  public static GraniteAnvil GRANITE_ANVIL = new GraniteAnvil();

  public static class GraniteAnvil {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The number of times the block can be hit before applying damage",
        "to the block. The block has a total of four damage stages. This number",
        "represents the number of hits for just one damage stage.",
        "Default: " + 64
    })
    @Config.RangeInt(min = 1)
    public int HITS_PER_DAMAGE = 64;

    @Config.Comment({
        "The amount of extra damage to apply to the anvil damage when hitting a bloom.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int BLOOM_EXTRA_DAMAGE_PER_HIT = 1;

    @Config.Comment({
        "The chance that extra damage will be applied when hitting a bloom.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double BLOOM_EXTRA_DAMAGE_CHANCE = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_HIT = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Default: " + 0
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;

    @Config.Comment({
        "Set to false to prevent the device from wearing out.",
        "Default: " + true
    })
    public boolean USE_DURABILITY = true;
  }

  // ---------------------------------------------------------------------------
  // - Ironclad Anvil
  // ---------------------------------------------------------------------------

  public static IroncladAnvil IRONCLAD_ANVIL = new IroncladAnvil();

  public static class IroncladAnvil {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The number of times the block can be hit before applying damage",
        "to the block. The block has a total of four damage stages. This number",
        "represents the number of hits for just one damage stage.",
        "Default: " + 256
    })
    @Config.RangeInt(min = 1)
    public int HITS_PER_DAMAGE = 256;

    @Config.Comment({
        "The amount of extra damage to apply to the anvil damage when hitting a bloom.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int BLOOM_EXTRA_DAMAGE_PER_HIT = 1;

    @Config.Comment({
        "The chance that extra damage will be applied when hitting a bloom.",
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double BLOOM_EXTRA_DAMAGE_CHANCE = 0.05;

    @Config.Comment({
        "How much exhaustion to apply per hit.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_HIT = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Default: " + 0
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;

    @Config.Comment({
        "If true, all the granite anvil recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_GRANITE_ANVIL_RECIPES = true;

    @Config.Comment({
        "Multiplicative modifier applied to the required hits for all inherited recipes.",
        "Default: " + 1.0
    })
    @Config.RangeDouble(min = 0)
    public double INHERITED_GRANITE_ANVIL_RECIPE_HIT_MODIFIER = 1.0;

    @Config.Comment({
        "Set to false to prevent the device from wearing out.",
        "Default: " + true
    })
    public boolean USE_DURABILITY = true;
  }

  // ---------------------------------------------------------------------------
  // - Chopping Block
  // ---------------------------------------------------------------------------

  public static ChoppingBlock CHOPPING_BLOCK = new ChoppingBlock();

  public static class ChoppingBlock {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

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
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double WOOD_CHIPS_CHANCE = 0.05;

    @Config.Comment({
        "The number of times the block can be chopped on before applying damage",
        "to the block. The block has a total of six damage stages. This number",
        "represents the number of chops for just one damage stage.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1)
    public int CHOPS_PER_DAMAGE = 16;

    @Config.Comment({
        "How much exhaustion to apply per axe chop.",
        "Default: " + 1.5
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_AXE_CHOP = 1.5;

    @Config.Comment({
        "How much exhaustion to apply per shovel scoop.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_SHOVEL_SCOOP = 0.5;

    @Config.Comment({
        "How much exhaustion to apply per completed craft.",
        "Default: " + 0
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_CRAFT_COMPLETE = 0;

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;

    @Config.Comment({
        "Set to false to prevent the device from wearing out.",
        "Default: " + true
    })
    public boolean USES_DURABILITY = true;
  }

  // ---------------------------------------------------------------------------
  // - Drying Rack Conditional Modifiers
  // ---------------------------------------------------------------------------

  public static class DryingRackConditionalModifiers {

    @Config.Comment({
        "The base speed if the device is being directly rained on.",
        "Default: " + (-1)
    })
    public double DIRECT_RAIN = -1;

    @Config.Comment({
        "The base speed if the it is raining, but not directly on the device, or",
        "the biome has high humidity.",
        "Default: " + 0.25
    })
    public double INDIRECT_RAIN = 0.25f;

    @Config.Comment({
        "The base speed if the device is in the Nether.",
        "Default: " + 2
    })
    public double NETHER = 2;

    @Config.Comment({
        "The base derived speed.",
        "Default: " + 1
    })
    public double BASE_DERIVED = 1;

    @Config.Comment({
        "Added to the base derived speed if the biome is hot.",
        "Default: " + 0.2
    })
    public double DERIVED_HOT = 0.2f;

    @Config.Comment({
        "Added to the base derived speed if the biome is dry.",
        "Default: " + 0.2
    })
    public double DERIVED_DRY = 0.2f;

    @Config.Comment({
        "Added to the base derived speed if the biome is cold.",
        "Default: " + (-0.2)
    })
    public double DERIVED_COLD = -0.2f;

    @Config.Comment({
        "Added to the base derived speed if the biome is wet.",
        "Default: " + (-0.2)
    })
    public double DERIVED_WET = -0.2f;

    @Config.Comment({
        "The device will gain a bonus for each fire source within this range.",
        "Default: " + 2
    })
    public int FIRE_SOURCE_BONUS_RANGE = 2;

    @Config.Comment({
        "Added to the base derived speed for each fire source block in range.",
        "Default: " + 0.2
    })
    public double FIRE_SOURCE_BONUS = 0.2f;

    @Config.Comment({
        "Added to the base derived speed if it isn't raining, the device has a",
        "direct line of sight to the sky, and it's daytime.",
        "Default: " + 0.2
    })
    public double DAYTIME = 0.2f;
  }

  // ---------------------------------------------------------------------------
  // - Crude Drying Rack
  // ---------------------------------------------------------------------------

  public static CrudeDryingRack CRUDE_DRYING_RACK = new CrudeDryingRack();

  public static class CrudeDryingRack {

    @Config.Ignore
    public static final Map<String, Float> BIOME_MODIFIERS = new HashMap<>();

    @Config.Comment({
        "speed = speed * SPEED_MODIFIER",
        "Default: " + 1.0
    })
    @Config.RangeDouble
    public double SPEED_MODIFIER = 1.0;

    @Config.Comment({
        "Multiplicative modifier applied to every recipe in this device.",
        "recipeDurationTicks = recipeDurationTicks * BASE_RECIPE_DURATION_MODIFIER",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double BASE_RECIPE_DURATION_MODIFIER = 1;

    public DryingRackConditionalModifiers CONDITIONAL_MODIFIERS = new DryingRackConditionalModifiers();
  }

  // ---------------------------------------------------------------------------
  // - Drying Rack
  // ---------------------------------------------------------------------------

  public static DryingRack DRYING_RACK = new DryingRack();

  public static class DryingRack {

    @Config.Ignore
    public static final Map<String, Float> BIOME_MODIFIERS = new HashMap<>();

    @Config.Comment({
        "speed = speed * SPEED_MODIFIER",
        "Default: " + 1.35
    })
    @Config.RangeDouble
    public double SPEED_MODIFIER = 1.35;

    @Config.Comment({
        "If true, all the crude drying rack recipes will also be available in this device.",
        "Default: " + true
    })
    public boolean INHERIT_CRUDE_DRYING_RACK_RECIPES = true;

    @Config.Comment({
        "Multiplicative modifier applied to the duration of all inherited recipes.",
        "Default: " + 1.0
    })
    @Config.RangeDouble(min = 0)
    public double INHERITED_CRUDE_DRYING_RACK_RECIPE_DURATION_MODIFIER = 1.0;

    @Config.Comment({
        "Multiplicative modifier applied to every recipe in this device.",
        "recipeDurationTicks = recipeDurationTicks * BASE_RECIPE_DURATION_MODIFIER",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double BASE_RECIPE_DURATION_MODIFIER = 1;

    @Config.Comment({
        "Set to true to allow the player to climb the sides of drying racks.",
        "Default: " + true
    })
    public boolean USE_AS_LADDER = true;

    @Config.Comment({
        "Set the up / down movement speed for climbing.",
        "Default: " + 0.1
    })
    public double CLIMB_SPEED = 0.1;

    public DryingRackConditionalModifiers CONDITIONAL_MODIFIERS = new DryingRackConditionalModifiers();
  }

  // ---------------------------------------------------------------------------
  // - Pit Kiln
  // ---------------------------------------------------------------------------

  public static PitKiln PIT_KILN = new PitKiln();

  public static class PitKiln {

    @Config.Comment({
        "The maximum stack size that can be placed in the pit kiln.",
        "Default: " + 8
    })
    @Config.RangeInt(min = 1, max = 64)
    public int MAX_STACK_SIZE = 8;

    @Config.Comment({
        "Multiplicative modifier applied to every recipe in this device.",
        "recipeDurationTicks = recipeDurationTicks * BASE_RECIPE_DURATION_MODIFIER",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double BASE_RECIPE_DURATION_MODIFIER = 1;

    @Config.Comment({
        "SPEED_SCALAR=(1-VARIABLE_SPEED_MODIFIER)PERCENTAGE_FULL+VARIABLE_SPEED_MODIFIER",
        "",
        "If set to 0.5, the Pit Kiln will complete 1 item in 50% of the time.",
        "For each item added after the first, the duration increases linearly",
        "until it is 100% when full.",
        "Setting the value to 0 is not recommended as it will cause one",
        "item to complete instantly.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double VARIABLE_SPEED_MODIFIER = 0.5;
  }

  // ---------------------------------------------------------------------------
  // - Campfire
  // ---------------------------------------------------------------------------

  public static Campfire CAMPFIRE = new Campfire();

  public static class Campfire {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The light level of the campfire when lit with max fuel.",
        "Default: " + 15
    })
    @Config.RangeInt(min = 0, max = 15)
    public int MAXIMUM_LIGHT_LEVEL = 11;

    @Config.Comment({
        "The light level of the campfire when lit with no fuel.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 15)
    public int MINIMUM_LIGHT_LEVEL = 3;

    @Config.Comment({
        "How many ticks to cook food on the campfire.",
        "Default: " + (90 * 20)
    })
    @Config.RangeInt(min = 1)
    public int COOK_TIME_TICKS = 90 * 20;

    @Config.Comment({
        "The amount of ticks of burn time added to the campfire",
        "for each log consumed.",
        "Default: " + (60 * 2 * 20)
    })
    @Config.RangeInt(min = 1)
    public int BURN_TIME_TICKS_PER_LOG = 60 * 2 * 20;

    @Config.Comment({
        "How many ticks does it take before the output food becomes burned food.",
        "Default: " + (30 * 20)
    })
    @Config.RangeInt(min = 1)
    public int BURNED_FOOD_TICKS = 30 * 20;

    @Config.Comment({
        "By default, the campfire will cook at a rate of 100% of the recipe's",
        "specified value with 4 logs, a rate of 200% with 8 logs, and a rate of",
        "0% with 0 logs.",
        "Keep in mind that an accelerated cooking speed will also reduce the",
        "time required for your food to burn.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 1, max = 8)
    public int FUEL_LEVEL_FOR_FULL_COOK_SPEED = 4;

    @Config.Comment({
        "Set to true if the campfire should be extinguished by rain.",
        "Default: " + true
    })
    public boolean EXTINGUISHED_BY_RAIN = true;

    @Config.Comment({
        "The number of ticks that the campfire can be exposed to rain before",
        "it is extinguished.",
        "Default: " + (10 * 20)
    })
    @Config.RangeInt(min = 1)
    public int TICKS_BEFORE_EXTINGUISHED = 10 * 20;

    @Config.Comment({
        "The chance that the campfire will produce ash when a fuel is consumed.",
        "Default: " + 0.25
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double ASH_CHANCE = 0.25;

    @Config.Comment({
        "The chance that the player will be damaged with fire when picking",
        "up a log while the campfire is lit.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double PLAYER_BURN_CHANCE = 0.5;

    @Config.Comment({
        "The amount of damage done to a player when picking up a log while the",
        "campfire is lit.",
        "Default: " + 1.0
    })
    @Config.RangeDouble(min = 0)
    public double PLAYER_LOG_BURN_DAMAGE = 1.0;

    @Config.Comment({
        "The amount of damage done to an entity when walking on top of a lit campfire.",
        "Set to zero to disable.",
        "Default: " + 1.0
    })
    @Config.RangeDouble(min = 0)
    public double ENTITY_WALK_BURN_DAMAGE = 1.0;

    @Config.Comment({
        "If true, all 'logWood' items will be valid fuel for the campfire.",
        "Default: " + true
    })
    public boolean USE_LOG_WOOD_OREDICT = true;

    public boolean isValidFuel(ItemStack fuel) {

      if (this.USE_LOG_WOOD_OREDICT
          && OreDictHelper.contains("logWood", fuel)
          && this.isNotBlacklistedFuel(fuel)) {
        return true;
      }

      // search custom additions
      for (Ingredient ingredient : ModuleTechBasicConfig.CAMPFIRE_FUEL_WHITELIST) {

        if (ingredient.apply(fuel)) {
          return this.isNotBlacklistedFuel(fuel);
        }
      }

      return false;
    }

    private boolean isNotBlacklistedFuel(ItemStack fuel) {

      for (Ingredient ingredient : ModuleTechBasicConfig.CAMPFIRE_FUEL_BLACKLIST) {

        if (ingredient.apply(fuel)) {
          return false;
        }
      }

      return true;
    }
  }

  public static CampfireMarshmallows CAMPFIRE_MARSHMALLOWS = new CampfireMarshmallows();

  public static class CampfireMarshmallows {

    @Config.Comment({
        "The maximum range in blocks at which a marshmallow roast will work.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int ROASTING_RANGE_BLOCKS = 2;

    @Config.Comment({
        "How many ticks until a marshmallow is roasted.",
        "Default: " + (5 * 20)
    })
    @Config.RangeInt(min = 0, max = 10 * 60 * 20)
    public int ROASTING_DURATION_TICKS = 5 * 20;

    @Config.Comment({
        "Percentage of random increase / decrease in roast duration per",
        "marshmallow.",
        "Default: " + 0.2
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double ROASTING_DURATION_VARIANCE_PERCENTAGE = 0.2;

    @Config.Comment({
        "How many ticks until a marshmallow is burned after it becomes roasted.",
        "Default: " + 20
    })
    @Config.RangeInt(min = 0, max = 10 * 60 * 20)
    public int ROASTING_BURN_DURATION_TICKS = 20;

    @Config.Comment({
        "The number of times marshmallows can be roasted before the stick breaks.",
        "Default: " + 8
    })
    public int MARSHMALLOW_STICK_DURABILITY = 8;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int MARSHMALLOW_HUNGER = 1;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0)
    public double MARSHMALLOW_SATURATION = 0.05;

    @Config.Comment({
        "The duration of the marshmallow's speed effect in ticks.",
        "Set to zero to disable.",
        "Default: " + (5 * 20)
    })
    public int MARSHMALLOW_SPEED_DURATION_TICKS = 5 * 20;

    @Config.Comment({
        "The max duration of the marshmallow's stacked speed effect in ticks.",
        "Set to zero to disable.",
        "Default: " + (5 * 20)
    })
    public int MAX_MARSHMALLOW_SPEED_DURATION_TICKS = 5 * 20;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 0)
    public int ROASTED_MARSHMALLOW_HUNGER = 2;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.1
    })
    @Config.RangeDouble(min = 0)
    public double ROASTED_MARSHMALLOW_SATURATION = 0.1;

    @Config.Comment({
        "The duration of the marshmallow's speed effect in ticks.",
        "Set to zero to disable.",
        "Default: " + (25 * 20)
    })
    public int ROASTED_MARSHMALLOW_SPEED_DURATION_TICKS = 25 * 20;

    @Config.Comment({
        "The max duration of the marshmallow's stacked speed effect in ticks.",
        "Set to zero to disable.",
        "Default: " + (5 * 60 * 20)
    })
    public int MAX_ROASTED_MARSHMALLOW_SPEED_DURATION_TICKS = 5 * 60 * 20;

    @Config.Comment({
        "How long before a roasted marshmallow loses potency. Potency affects",
        "the duration of the treat's speed effect.",
        "Default: " + (30 * 20)
    })
    @Config.RangeInt(min = 0)
    public int ROASTED_MARSHMALLOW_EFFECT_POTENCY_DURATION_TICKS = 30 * 20;

    @Config.Comment({
        "The amount of hunger healed by eating.",
        "For reference, the vanilla apple restores 4 hunger.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int BURNED_MARSHMALLOW_HUNGER = 1;

    @Config.Comment({
        "The amount of saturation healed by eating.",
        "For reference, the vanilla apple restores 0.3 saturation.",
        "Default: " + 0.01
    })
    @Config.RangeDouble(min = 0)
    public double BURNED_MARSHMALLOW_SATURATION = 0.01;

    @Config.Comment({
        "The duration of the marshmallow's slow effect in ticks.",
        "Set to zero to disable.",
        "Default: " + (5 * 20)
    })
    public int BURNED_MARSHMALLOW_SLOW_DURATION_TICKS = 5 * 20;

    @Config.Comment({
        "Set to false to disable the message broadcast to all players when a",
        "player burns a marshmallow.",
        "The message is located under the lang key:",
        "  gui.pyrotech.marshmallow.burned.broadcast.message"
    })
    public boolean ENABLE_BURNED_MARSHMALLOW_BROADCAST_MESSAGE = true;

    @Config.Comment({
        "Set to false to disable the message broadcast to all players when a",
        "player eats a burned marshmallow.",
        "The message is located under the lang key:",
        "  gui.pyrotech.marshmallow.burned.eat.broadcast.message"
    })
    public boolean ENABLE_BURNED_MARSHMALLOW_EAT_BROADCAST_MESSAGE = true;
  }

  // ---------------------------------------------------------------------------
  // - Campfire Effects
  // ---------------------------------------------------------------------------

  public static CampfireEffects CAMPFIRE_EFFECTS = new CampfireEffects();

  public static class CampfireEffects {

    @Config.Comment({
        "Set to true to enable debug messages.",
        "Default: " + false
    })
    public boolean DEBUG = false;

    @Config.Comment({
        "The time of day that the campfire effects should start working.",
        "If the current world time is larger than this value and less than",
        "the stop value, the effects will work.",
        "See: https://minecraft.gamepedia.com/Day-night_cycle#24-hour_Minecraft_day",
        "Default: " + 12000
    })
    @Config.RangeInt(min = 0, max = 24000)
    public int EFFECTS_START_TIME = 12000;

    @Config.Comment({
        "The time of day that the campfire effects should stop working.",
        "If the current world time is less than this value and larger than",
        "the start value, the effect will work.",
        "See: https://minecraft.gamepedia.com/Day-night_cycle#24-hour_Minecraft_day",
        "Default: " + 23000
    })
    @Config.RangeInt(min = 0, max = 24000)
    public int EFFECTS_STOP_TIME = 23000;

    @Config.Comment({
        "When a player is within range of a campfire, they will",
        "get the comfort effect.",
        "Set to false to disable.",
        "Default: " + true
    })
    public boolean COMFORT_EFFECT_ENABLED = true;

    @Config.Comment({
        "A percentile modifier for the amount of additional saturation restored",
        "when a player eats food with the comfort effect.",
        "Saturation restored = food saturation + food saturation * modifier",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0)
    public double COMFORT_SATURATION_MODIFIER = 0.5;

    @Config.Comment({
        "A percentile modifier for the amount of additional hunger restored",
        "when a player eats food with the comfort effect.",
        "Hunger restored = food hunger + food hunger * modifier",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0)
    public double COMFORT_HUNGER_MODIFIER = 0.5;

    @Config.Comment({
        "When a player is within range of a campfire, they will",
        "get the resting effect.",
        "Set to false to disable.",
        "Default: " + true
    })
    public boolean RESTING_EFFECT_ENABLED = true;

    @Config.Comment({
        "The number of ticks between the resting effect's health regen.",
        "For reference, the vanilla regen effect has an interval of 50 ticks.",
        "Default: " + 100
    })
    @Config.RangeInt(min = 1)
    public int RESTING_REGEN_INTERVAL_TICKS = 100;

    @Config.Comment({
        "The number of half-hearts regenerated per interval. For reference,",
        "the vanilla regen effect will restore 1 half-heart every 50 ticks.",
        "Set to zero to disable.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 0)
    public int RESTING_REGEN_HALF_HEARTS = 1;

    @Config.Comment({
        "The number of ticks before the resting effect levels up. The player",
        "must stand still for the level up to occur.",
        "Default: " + 200
    })
    @Config.RangeInt(min = 1)
    public int RESTING_LEVEL_UP_INTERVAL_TICKS = 200;

    @Config.Comment({
        "When a player eats with full saturation while under the Comfort effect,",
        "they will gain the Well Fed effect. Set to false to disable.",
        "Default: " + true
    })
    public boolean WELL_FED_EFFECT_ENABLED = true;

    @Config.Comment({
        "The duration of the Well Fed effect in ticks.",
        "Default: " + (5 * 60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int WELL_FED_DURATION_TICKS = 5 * 60 * 20;

    @Config.Comment({
        "Percentile exhaustion modifier for the Well Fed effect.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double WELL_FED_EXHAUSTION_MODIFIER = 0.5;

    @Config.Comment({
        "When a player stands still long enough with the Resting III effect,",
        "they will gain the Well Rested effect. Set to false to disable.",
        "Default: " + true
    })
    public boolean WELL_RESTED_EFFECT_ENABLED = true;

    @Config.Comment({
        "The duration of the Well Rested effect in ticks.",
        "Default: " + (5 * 60 * 20)
    })
    @Config.RangeInt(min = 0)
    public int WELL_RESTED_DURATION_TICKS = 5 * 60 * 20;

    @Config.Comment({
        "The number of half-hearts of absorption given. For reference,",
        "the vanilla absorption effect grants 4 half-hearts.",
        "Set to zero to disable.",
        "Default: " + 4
    })
    @Config.RangeInt(min = 0)
    public int WELL_RESTED_ABSORPTION_HALF_HEARTS = 4;

    @Config.Comment({
        "When a player stands still long enough with the Resting III effect,",
        "the Well Rested effect, the Comfort effect, and the Well Fed effect,",
        "they will gain the Focused effect. Set to false to disable.",
        "Default: " + true
    })
    public boolean FOCUSED_EFFECT_ENABLED = true;

    @Config.Comment({
        "Maximum XP bonus that a player can accumulate.",
        "Default: " + 1.5
    })
    @Config.RangeDouble(min = 0)
    public double FOCUSED_MAXIMUM_ACCUMULATED_BONUS = 1.5;

    @Config.Comment({
        "This defines how much XP bonus is accumulated per cycle. This effect",
        "cycles at the same rate as the Resting III effect.",
        "Default: " + 0.05
    })
    @Config.RangeDouble(min = 0)
    public double FOCUSED_ACCUMULATED_BONUS = 0.05;

    @Config.Comment({
        "Additional XP granted by the XP bonus on collection. The default is",
        "100% additional XP, so effectively double the XP collected.",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double FOCUSED_BONUS = 1;
  }
}