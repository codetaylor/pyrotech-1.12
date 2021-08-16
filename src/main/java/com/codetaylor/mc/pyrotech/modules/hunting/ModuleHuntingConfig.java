package com.codetaylor.mc.pyrotech.modules.hunting;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemButchersKnife;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemHuntersKnife;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Config(modid = ModuleHunting.MOD_ID, name = ModuleHunting.MOD_ID + "/" + "module.Hunting")
public class ModuleHuntingConfig {

  @Config.Ignore
  public static Stages STAGES_CARCASS = null;

  @Config.Ignore
  public static Stages STAGES_BUTCHERS_BLOCK = null;

  public static Spear SPEAR = new Spear();

  public static class Spear {

    @Config.Comment({
        "Durability.",
        "(Integer)"
    })
    public Map<String, Integer> DURABILITY = new HashMap<String, Integer>() {{
      this.put("crude", 16);
      this.put("flint", 32);
      this.put("bone", 32);
    }};

    @Config.Comment({
        "Damage when thrown.",
        "For reference, the vanilla arrow does 2 damage.",
        "(Double)"
    })
    public Map<String, Double> THROWN_DAMAGE = new HashMap<String, Double>() {{
      this.put("crude", 2.0);
      this.put("flint", 4.0);
      this.put("bone", 4.0);
    }};

    @Config.Comment({
        "Increase this number to decrease the spear's accuracy.",
        "For reference, the vanilla arrow has an inaccuracy of 1.",
        "(Double)"
    })
    public Map<String, Double> INACCURACY = new HashMap<String, Double>() {{
      this.put("crude", 2.0);
      this.put("flint", 1.0);
      this.put("bone", 1.0);
    }};

    @Config.Comment({
        "The velocity scalar.",
        "For reference, the vanilla arrow has a velocity scalar of 3.",
        "(Double)"
    })
    public Map<String, Double> VELOCITY_SCALAR = new HashMap<String, Double>() {{
      this.put("crude", 1.0);
      this.put("flint", 2.0);
      this.put("bone", 2.0);
    }};
  }

  public static FlintArrow FLINT_ARROW = new FlintArrow();

  public static class FlintArrow {

    @Config.Comment({
        "Chance to break when the arrow hits a block.",
        "This chance is modified by the speed of the arrow.",
        "Default: " + 0.75
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double BREAK_ON_HIT_CHANCE = 0.75;

    @Config.Comment({
        "Chance to drop materials when arrow breaks after hitting a block.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double MATERIAL_DROP_CHANCE = 0.5;

    @Config.Comment({
        "Stack size.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1, max = 64)
    public int STACK_SIZE = 16;

    @Config.Comment({
        "Damage.",
        "For reference, the vanilla arrow does 2.0 damage.",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double DAMAGE = 1;
  }

  public static BoneArrow BONE_ARROW = new BoneArrow();

  public static class BoneArrow {

    @Config.Comment({
        "Chance to break when the arrow hits a block.",
        "This chance is modified by the speed of the arrow.",
        "Default: " + 0.75
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double BREAK_ON_HIT_CHANCE = 0.75;

    @Config.Comment({
        "Chance to drop materials when arrow breaks after hitting a block.",
        "Default: " + 0.5
    })
    @Config.RangeDouble(min = 0, max = 1)
    public double MATERIAL_DROP_CHANCE = 0.5;

    @Config.Comment({
        "Stack size.",
        "Default: " + 16
    })
    @Config.RangeInt(min = 1, max = 64)
    public int STACK_SIZE = 16;

    @Config.Comment({
        "Damage.",
        "For reference, the vanilla arrow does 2.0 damage.",
        "Default: " + 1
    })
    @Config.RangeDouble(min = 0)
    public double DAMAGE = 1;
  }

  public static Carcass CARCASS = new Carcass();

  public static class Carcass {

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;

    @Config.Comment({
        "Use this to add items that you want to be valid knives.",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] ALLOWED_KNIVES = new String[]{
        "pyrotech:" + ItemHuntersKnife.BONE_NAME,
        "pyrotech:" + ItemHuntersKnife.FLINT_NAME,
        "pyrotech:" + ItemHuntersKnife.STONE_NAME,
        "pyrotech:" + ItemHuntersKnife.IRON_NAME,
        "pyrotech:" + ItemHuntersKnife.GOLD_NAME,
        "pyrotech:" + ItemHuntersKnife.DIAMOND_NAME,
        "pyrotech:" + ItemHuntersKnife.OBSIDIAN_NAME,
        "pyrotech:" + ItemButchersKnife.BONE_NAME,
        "pyrotech:" + ItemButchersKnife.FLINT_NAME,
        "pyrotech:" + ItemButchersKnife.STONE_NAME,
        "pyrotech:" + ItemButchersKnife.IRON_NAME,
        "pyrotech:" + ItemButchersKnife.GOLD_NAME,
        "pyrotech:" + ItemButchersKnife.DIAMOND_NAME,
        "pyrotech:" + ItemButchersKnife.OBSIDIAN_NAME
    };

    @Config.Comment({
        "How much exhaustion to apply per knife use.",
        "Default: " + 1.5
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_KNIFE_USE = 1.5;

    @Config.Comment({
        "Total progress required to drop item +/- 10%.",
        "Default: " + 100
    })
    @Config.RangeInt(min = 1)
    public int TOTAL_PROGRESS_REQUIRED = 100;

    @Config.Comment({
        "How fast the progress will be advanced per knife. This number is added",
        "to the progress until it reaches the total progress required.",
        "",
        "String key format is (domain):(path)",
        "Integer value format is (efficiency)"
    })
    @Config.RequiresMcRestart
    public Map<String, Integer> KNIFE_EFFICIENCY = new HashMap<String, Integer>() {{
      this.put("pyrotech:" + ItemHuntersKnife.BONE_NAME, 15);
      this.put("pyrotech:" + ItemHuntersKnife.FLINT_NAME, 15);
      this.put("pyrotech:" + ItemHuntersKnife.STONE_NAME, 10);
      this.put("pyrotech:" + ItemHuntersKnife.IRON_NAME, 20);
      this.put("pyrotech:" + ItemHuntersKnife.GOLD_NAME, 5);
      this.put("pyrotech:" + ItemHuntersKnife.DIAMOND_NAME, 35);
      this.put("pyrotech:" + ItemHuntersKnife.OBSIDIAN_NAME, 20);
      this.put("pyrotech:" + ItemButchersKnife.BONE_NAME, 15);
      this.put("pyrotech:" + ItemButchersKnife.FLINT_NAME, 15);
      this.put("pyrotech:" + ItemButchersKnife.STONE_NAME, 10);
      this.put("pyrotech:" + ItemButchersKnife.IRON_NAME, 20);
      this.put("pyrotech:" + ItemButchersKnife.GOLD_NAME, 5);
      this.put("pyrotech:" + ItemButchersKnife.DIAMOND_NAME, 35);
      this.put("pyrotech:" + ItemButchersKnife.OBSIDIAN_NAME, 20);
    }};
  }

  public static Drops DROPS = new Drops();

  public static class Drops {

    @Config.Comment({
        "If true, leather drops will be removed from entity drops.",
        "Default: " + true
    })
    public boolean REMOVE_LEATHER_DROPS = true;

    @Config.Comment({
        "Drops added here will be added to the mob drops and auto-captured by",
        "the carcass. The key is the entity resource location and the value",
        "is the drop. The count is an integer and the chance is a float.",
        "Meta is optional and defaults to 0.",
        "",
        "Sheep require special handling and aren't processed using the drop map.",
        "Llamas require special handling and aren't processed using the drop map.",
        "",
        "String key format is (domain):(path)",
        "String value format is (domain):(path):(meta);(count);(chance)",
        "Multiple values can be concatenated with &"
    })
    @Config.RequiresMcRestart
    public Map<String, String> DROP_MAP = new HashMap<String, String>() {{
      this.put("minecraft:pig", "pyrotech:hide_pig:0;1;0.85&pyrotech:material:11;2;0.50");
      this.put("minecraft:cow", "pyrotech:pelt_cow:0;1;0.85&pyrotech:material:11;2;0.50");
      this.put("minecraft:mooshroom", "pyrotech:pelt_mooshroom:0;1;0.65&pyrotech:material:11;2;0.50");
      this.put("minecraft:polar_bear", "pyrotech:pelt_polar_bear:0;1;0.85&pyrotech:material:11;2;0.50");
      this.put("minecraft:bat", "pyrotech:pelt_bat:0;1;0.65");
      this.put("minecraft:horse", "pyrotech:pelt_horse:0;1;0.85&pyrotech:material:11;2;0.50");
      this.put("minecraft:donkey", "pyrotech:pelt_horse:0;1;0.85&pyrotech:material:11;2;0.50");
      this.put("minecraft:rabbit", "minecraft:rabbit_hide:0;1;0.65&pyrotech:material:11;1;0.25");
      this.put("minecraft:wolf", "pyrotech:pelt_wolf:0;1;0.85&pyrotech:material:11;2;0.50");
    }};

    @Config.Comment({
        "Items added to this list will be captured by the carcass.",
        "Meta is optional and defaults to 0.",
        "",
        "String format is (domain):(path):(meta)"
    })
    @Config.RequiresMcRestart
    public String[] DROP_CAPTURE_LIST = new String[]{
        "minecraft:beef",
        "minecraft:chicken",
        "minecraft:mutton",
        "minecraft:rabbit",
        "minecraft:rabbit_foot",
        "minecraft:porkchop",
        "minecraft:red_mushroom"
    };

    @Config.Comment({
        "Sheep require special handling and aren't processed using the drop map.",
        "This is the chance that a sheep pelt will drop.",
        "Default: " + 0.85
    })
    public double SHEEP_PELT_CHANCE = 0.85;

    @Config.Comment({
        "Sheep require special handling and aren't processed using the drop map.",
        "This is the amount of sheep pelts that will drop.",
        "Default: " + 1
    })
    public int SHEEP_PELT_COUNT = 1;

    @Config.Comment({
        "Llamas require special handling and aren't processed using the drop map.",
        "This is the chance that a llama pelt will drop.",
        "Default: " + 0.85
    })
    public double LLAMA_PELT_CHANCE = 0.85;

    @Config.Comment({
        "Llama require special handling and aren't processed using the drop map.",
        "This is the amount of llama pelts that will drop.",
        "Default: " + 1
    })
    public int LLAMA_PELT_COUNT = 1;
  }

  public static LeatherKits LEATHER_KITS = new LeatherKits();

  public static class LeatherKits {

    @Config.Comment({
        "The number of uses per leather repair kit.",
        "Default: " + 4
    })
    public int LEATHER_REPAIR_KIT_USES = 4;

    @Config.Comment({
        "The number of uses per durable leather repair kit.",
        "Default: " + 8
    })
    public int LEATHER_DURABLE_REPAIR_KIT_USES = 8;

    @Config.Comment({
        "The damage applied to the hunters knife when repairing leather armor with a kit.",
        "Default: " + 4
    })
    public int HUNTERS_KNIFE_REPAIR_DAMAGE = 4;

    @Config.Comment({
        "The percentage of durability repaired.",
        "Default: " + 0.25
    })
    public double PERCENTAGE_DURABILITY_REPAIRED = 0.25;
  }

  public static ButchersBlock BUTCHERS_BLOCK = new ButchersBlock();

  public static class ButchersBlock {

    @Config.Comment({
        "Minimum amount of hunger the player needs to use.",
        "Default: " + 3
    })
    @Config.RangeInt(min = 0, max = 20)
    public int MINIMUM_HUNGER_TO_USE = 3;

    @Config.Comment({
        "Use this to add items that you want to be valid butcher's knives.",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] ALLOWED_BUTCHERS_KNIVES = new String[]{
        "pyrotech:" + ItemButchersKnife.BONE_NAME,
        "pyrotech:" + ItemButchersKnife.FLINT_NAME,
        "pyrotech:" + ItemButchersKnife.STONE_NAME,
        "pyrotech:" + ItemButchersKnife.IRON_NAME,
        "pyrotech:" + ItemButchersKnife.GOLD_NAME,
        "pyrotech:" + ItemButchersKnife.DIAMOND_NAME,
        "pyrotech:" + ItemButchersKnife.OBSIDIAN_NAME
    };

    @Config.Comment({
        "Use this to add items that you want to be valid hunter's knives.",
        "Items you add are assumed to have durability.",
        "",
        "String format is a resource location: (domain):(path)"
    })
    public String[] ALLOWED_HUNTERS_KNIVES = new String[]{
        "pyrotech:" + ItemHuntersKnife.BONE_NAME,
        "pyrotech:" + ItemHuntersKnife.FLINT_NAME,
        "pyrotech:" + ItemHuntersKnife.STONE_NAME,
        "pyrotech:" + ItemHuntersKnife.IRON_NAME,
        "pyrotech:" + ItemHuntersKnife.GOLD_NAME,
        "pyrotech:" + ItemHuntersKnife.DIAMOND_NAME,
        "pyrotech:" + ItemHuntersKnife.OBSIDIAN_NAME
    };

    @Config.Comment({
        "How much exhaustion to apply per knife use.",
        "Default: " + 1.5
    })
    @Config.RangeDouble(min = 0, max = 40)
    public double EXHAUSTION_COST_PER_KNIFE_USE = 0.75;

    @Config.Comment({
        "Total progress required to drop item +/- 10%.",
        "Default: " + 100
    })
    @Config.RangeInt(min = 1)
    public int TOTAL_PROGRESS_REQUIRED = 100;

    @Config.Comment({
        "How fast the progress will be advanced per knife. This number is added",
        "to the progress until it reaches the total progress required.",
        "",
        "String key format is (domain):(path)",
        "Integer value format is (efficiency)"
    })
    @Config.RequiresMcRestart
    public Map<String, Integer> KNIFE_EFFICIENCY = new HashMap<String, Integer>() {{
      // Hunter's Knife
      this.put("pyrotech:" + ItemHuntersKnife.BONE_NAME, 25);
      this.put("pyrotech:" + ItemHuntersKnife.FLINT_NAME, 25);
      this.put("pyrotech:" + ItemHuntersKnife.STONE_NAME, 17);
      this.put("pyrotech:" + ItemHuntersKnife.IRON_NAME, 34);
      this.put("pyrotech:" + ItemHuntersKnife.GOLD_NAME, 8);
      this.put("pyrotech:" + ItemHuntersKnife.DIAMOND_NAME, 58);
      this.put("pyrotech:" + ItemHuntersKnife.OBSIDIAN_NAME, 34);
      // Butcher's Knife
      this.put("pyrotech:" + ItemButchersKnife.BONE_NAME, 25);
      this.put("pyrotech:" + ItemButchersKnife.FLINT_NAME, 25);
      this.put("pyrotech:" + ItemButchersKnife.STONE_NAME, 17);
      this.put("pyrotech:" + ItemButchersKnife.IRON_NAME, 34);
      this.put("pyrotech:" + ItemButchersKnife.GOLD_NAME, 8);
      this.put("pyrotech:" + ItemButchersKnife.DIAMOND_NAME, 58);
      this.put("pyrotech:" + ItemButchersKnife.OBSIDIAN_NAME, 34);
    }};

    @Config.Comment({
        "This map is used when a Butcher's Knife is used at the table.",
        "",
        "Transform an item into another item when it comes out of the carcass.",
        "By default the Butcher's Knife will increase the chance of getting a",
        "Ruined Pelt instead of a good pelt, increase the chance of getting a",
        "Bone instead of a Bone Shard, and increase the chance of getting twice",
        "as much meat.",
        "",
        "String key format is (domain):(path):(meta)",
        "String value format is (domain):(path):(meta);(count);(chance)"
    })
    @Config.RequiresMcRestart
    public Map<String, String> BUTCHERS_KNIFE_OUTPUT_TRANSFORMERS = new HashMap<String, String>() {{
      // hides and pelts
      this.put("pyrotech:hide_pig:0", "pyrotech:pelt_ruined:0;1;0.85");
      this.put("pyrotech:pelt_cow:0", "pyrotech:pelt_ruined:0;1;0.85");
      this.put("pyrotech:pelt_mooshroom:0", "pyrotech:pelt_ruined:0;1;0.85");
      this.put("pyrotech:pelt_polar_bear:0", "pyrotech:pelt_ruined:0;1;0.85");
      this.put("pyrotech:pelt_bat:0", "pyrotech:pelt_ruined:0;1;0.85");
      this.put("pyrotech:pelt_horse:0", "pyrotech:pelt_ruined:0;1;0.85");
      this.put("minecraft:rabbit_hide:0", "pyrotech:pelt_ruined:0;1;0.85");
      this.put("pyrotech:pelt_wolf:0", "pyrotech:pelt_ruined:0;1;0.85");
      // bone shards
      this.put("pyrotech:material:11", "minecraft:bone:0;1;0.5");
      // meat
      this.put("minecraft:beef:0", "minecraft:beef:0;2;0.85");
      this.put("minecraft:chicken:0", "minecraft:chicken:0;2;0.85");
      this.put("minecraft:mutton:0", "minecraft:mutton:0;2;0.85");
      this.put("minecraft:rabbit:0", "minecraft:rabbit:0;2;0.85");
      this.put("minecraft:rabbit_foot:0", "minecraft:rabbit_foot:0;2;0.85");
      this.put("minecraft:porkchop:0", "minecraft:porkchop:0;2;0.85");
      this.put("minecraft:red_mushroom:0", "minecraft:red_mushroom:0;4;0.85");
    }};

    @Config.Comment({
        "This map is used when a Hunter's Knife is used at the table.",
        "",
        "Transform an item into another item when it comes out of the carcass.",
        "By default the Hunter's Knife will increase the chance of getting a",
        "Tainted Meat instead of a good meat, increase the chance of getting more",
        "Bone Shards, and increase the chance of getting twice as many hides.",
        "",
        "String key format is (domain):(path):(meta)",
        "String value format is (domain):(path):(meta);(count);(chance)"
    })
    @Config.RequiresMcRestart
    public Map<String, String> HUNTERS_KNIFE_OUTPUT_TRANSFORMERS = new HashMap<String, String>() {{
      // hides and pelts
      this.put("pyrotech:hide_pig:0", "pyrotech:hide_pig:0;2;0.85");
      this.put("pyrotech:pelt_cow:0", "pyrotech:pelt_cow:0;2;0.85");
      this.put("pyrotech:pelt_mooshroom:0", "pyrotech:pelt_mooshroom:0;2;0.85");
      this.put("pyrotech:pelt_polar_bear:0", "pyrotech:pelt_polar_bear:0;2;0.85");
      this.put("pyrotech:pelt_bat:0", "pyrotech:pelt_bat:0;2;0.85");
      this.put("pyrotech:pelt_horse:0", "pyrotech:pelt_horse:0;2;0.85");
      this.put("minecraft:rabbit_hide:0", "minecraft:rabbit_hide:0;2;0.85");
      this.put("pyrotech:pelt_wolf:0", "pyrotech:pelt_wolf:0;2;0.85");
      // bone shards
      this.put("pyrotech:material:11", "pyrotech:material:11;2;0.5");
      // meat
      this.put("minecraft:beef:0", "pyrotech:tainted_meat:0;1;0.85");
      this.put("minecraft:chicken:0", "pyrotech:tainted_meat:0;1;0.85");
      this.put("minecraft:mutton:0", "pyrotech:tainted_meat:0;1;0.85");
      this.put("minecraft:rabbit:0", "pyrotech:tainted_meat:0;1;0.85");
      this.put("minecraft:rabbit_foot:0", "minecraft:rabbit_foot:0;2;0.85");
      this.put("minecraft:porkchop:0", "pyrotech:tainted_meat:0;1;0.85");
      this.put("minecraft:red_mushroom:0", "minecraft:red_mushroom:0;4;0.85");
    }};
  }

  @Config.Comment({
      "The durability of the hunter's knives."
  })
  public static Map<String, Integer> HUNTERS_KNIFE_DURABILITY = new LinkedHashMap<>();

  static {
    HUNTERS_KNIFE_DURABILITY.put("bone", 150);
    HUNTERS_KNIFE_DURABILITY.put("flint", 150);
    HUNTERS_KNIFE_DURABILITY.put("stone", Item.ToolMaterial.STONE.getMaxUses());
    HUNTERS_KNIFE_DURABILITY.put("iron", Item.ToolMaterial.IRON.getMaxUses());
    HUNTERS_KNIFE_DURABILITY.put("gold", Item.ToolMaterial.GOLD.getMaxUses());
    HUNTERS_KNIFE_DURABILITY.put("diamond", Item.ToolMaterial.DIAMOND.getMaxUses());
    HUNTERS_KNIFE_DURABILITY.put("obsidian", 1400);
  }

  @Config.Comment({
      "Set to false to disable repair."
  })
  public static boolean ALLOW_HUNTERS_KNIFE_REPAIR = true;

  @Config.Comment({
      "The durability of the butcher's knives."
  })
  public static Map<String, Integer> BUTCHERS_KNIFE_DURABILITY = new LinkedHashMap<>();

  static {
    BUTCHERS_KNIFE_DURABILITY.put("bone", 150);
    BUTCHERS_KNIFE_DURABILITY.put("flint", 150);
    BUTCHERS_KNIFE_DURABILITY.put("stone", Item.ToolMaterial.STONE.getMaxUses());
    BUTCHERS_KNIFE_DURABILITY.put("iron", Item.ToolMaterial.IRON.getMaxUses());
    BUTCHERS_KNIFE_DURABILITY.put("gold", Item.ToolMaterial.GOLD.getMaxUses());
    BUTCHERS_KNIFE_DURABILITY.put("diamond", Item.ToolMaterial.DIAMOND.getMaxUses());
    BUTCHERS_KNIFE_DURABILITY.put("obsidian", 1400);
  }

  @Config.Comment({
      "Set to false to disable repair."
  })
  public static boolean ALLOW_BUTCHERS_KNIFE_REPAIR = true;

  @Config.Comment({
      "How much damage is applied to a Hunter's Knife when scraping a hide.",
      "Default: " + 2
  })
  public static int HIDE_SCRAPING_TOOL_DAMAGE = 2;

  @Config.Comment({
      "How many ticks are required to in-world soak a hide.",
      "Default: " + 20 * 60 * 10
  })
  public static int IN_WORLD_HIDE_SOAK_TICKS = 20 * 60 * 10;

}
