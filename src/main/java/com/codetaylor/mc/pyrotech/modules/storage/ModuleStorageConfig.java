package com.codetaylor.mc.pyrotech.modules.storage;

import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRockGrass;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleStorage.MOD_ID, name = ModuleStorage.MOD_ID + "/" + "module.Storage")
public class ModuleStorageConfig {

  @Config.Ignore
  public static Stages STAGES_BRICK_TANK = null;

  @Config.Ignore
  public static Stages STAGES_STONE_TANK = null;

  // ---------------------------------------------------------------------------
  // - Simple Rock Bag
  // ---------------------------------------------------------------------------

  public static SimpleRockBag SIMPLE_ROCK_BAG = new SimpleRockBag();

  public static class SimpleRockBag {

    @Config.Comment({
        "If true, the bag will allow extraction of its contents from the bottom.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "If true, the bag will auto-collect items that it can carry when they",
        "are picked up and the bag is in the player's mainhand.",
        "Default: " + true
    })
    public boolean ALLOW_AUTO_PICKUP_MAINHAND = true;

    @Config.Comment({
        "If true, the bag will auto-collect items that it can carry when they",
        "are picked up and the bag is in the player's offhand.",
        "Default: " + true
    })
    public boolean ALLOW_AUTO_PICKUP_OFFHAND = true;

    @Config.Comment({
        "If true, the bag will auto-collect items that it can carry when they",
        "are picked up and the bag is in the player's hotbar.",
        "Default: " + true
    })
    public boolean ALLOW_AUTO_PICKUP_HOTBAR = true;

    @Config.Comment({
        "If true, the bag will auto-collect items that it can carry when they",
        "are picked up and the bag is in the player's main inventory.",
        "Default: " + false
    })
    public boolean ALLOW_AUTO_PICKUP_INVENTORY = false;

    @Config.Comment({
        "The maximum number of items the bag can carry.",
        "Default: " + 640
    })
    public int MAX_ITEM_CAPACITY = 640;

    @Config.Comment({
        "Items that are allowed in the bag.",
        "If the whitelist is not empty, only these items will be allowed.",
        "The whitelist takes precedence over the blacklist.",
        "Item string format is (domain):(path):(meta|*) where * matches any meta."
    })
    public String[] ITEM_WHITELIST = {
        ModuleStorage.MOD_ID + ":" + BlockRock.NAME + ":" + BlockRock.EnumType.STONE.getMeta(),
        ModuleStorage.MOD_ID + ":" + BlockRock.NAME + ":" + BlockRock.EnumType.GRANITE.getMeta(),
        ModuleStorage.MOD_ID + ":" + BlockRock.NAME + ":" + BlockRock.EnumType.DIORITE.getMeta(),
        ModuleStorage.MOD_ID + ":" + BlockRock.NAME + ":" + BlockRock.EnumType.ANDESITE.getMeta(),
        ModuleStorage.MOD_ID + ":" + BlockRock.NAME + ":" + BlockRock.EnumType.DIRT.getMeta(),
        ModuleStorage.MOD_ID + ":" + BlockRock.NAME + ":" + BlockRock.EnumType.SANDSTONE.getMeta(),
        ModuleStorage.MOD_ID + ":" + BlockRock.NAME + ":" + BlockRock.EnumType.LIMESTONE.getMeta(),
        ModuleStorage.MOD_ID + ":" + BlockRockGrass.NAME + ":0"
    };

    @Config.Comment({
        "Items that are not allowed in the bag.",
        "If the whitelist is empty, these items will be disallowed.",
        "The whitelist takes precedence over the blacklist.",
        "Item string format is (domain):(path):(meta|*) where * matches any meta."
    })
    public String[] ITEM_BLACKLIST = new String[0];
  }

  // ---------------------------------------------------------------------------
  // - Durable Rock Bag
  // ---------------------------------------------------------------------------

  public static DurableRockBag DURABLE_ROCK_BAG = new DurableRockBag();

  public static class DurableRockBag {

    @Config.Comment({
        "If true, the bag will allow extraction of its contents from the bottom.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "If true, the bag will auto-collect items that it can carry when they",
        "are picked up and the bag is in the player's mainhand.",
        "Default: " + true
    })
    public boolean ALLOW_AUTO_PICKUP_MAINHAND = true;

    @Config.Comment({
        "If true, the bag will auto-collect items that it can carry when they",
        "are picked up and the bag is in the player's offhand.",
        "Default: " + true
    })
    public boolean ALLOW_AUTO_PICKUP_OFFHAND = true;

    @Config.Comment({
        "If true, the bag will auto-collect items that it can carry when they",
        "are picked up and the bag is in the player's hotbar.",
        "Default: " + true
    })
    public boolean ALLOW_AUTO_PICKUP_HOTBAR = true;

    @Config.Comment({
        "If true, the bag will auto-collect items that it can carry when they",
        "are picked up and the bag is in the player's main inventory.",
        "Default: " + false
    })
    public boolean ALLOW_AUTO_PICKUP_INVENTORY = false;

    @Config.Comment({
        "The maximum number of items the bag can carry.",
        "Default: " + 1280
    })
    public int MAX_ITEM_CAPACITY = 1280;

    @Config.Comment({
        "Items that are allowed in the bag.",
        "If the whitelist is not empty, only these items will be allowed.",
        "The whitelist takes precedence over the blacklist.",
        "Item string format is (domain):(path):(meta|*) where * matches any meta."
    })
    public String[] ITEM_WHITELIST = ArrayHelper.combine(
        ModuleStorageConfig.SIMPLE_ROCK_BAG.ITEM_WHITELIST,
        new String[]{
            "minecraft:dirt:0",
            "minecraft:cobblestone",
            "minecraft:gravel",
            "minecraft:sandstone:0",
            "minecraft:red_sandstone:0",
            ModuleStorage.MOD_ID + ":" + BlockCobblestone.NAME + ":*"
        }
    );

    @Config.Comment({
        "Items that are not allowed in the bag.",
        "If the whitelist is empty, these items will be disallowed.",
        "The whitelist takes precedence over the blacklist.",
        "Item string format is (domain):(path):(meta|*) where * matches any meta."
    })
    public String[] ITEM_BLACKLIST = new String[0];
  }

  // ---------------------------------------------------------------------------
  // - Stone Tank
  // ---------------------------------------------------------------------------

  public static StoneTank STONE_TANK = new StoneTank();

  public static class StoneTank {

    @Config.Comment({
        "The amount of fluid this container can hold in mB.",
        "Default: " + 4000
    })
    public int CAPACITY = 4000;

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
        "True if the tank holds its contents when broken.",
        "Default: " + true
    })
    public boolean HOLDS_CONTENTS_WHEN_BROKEN = true;
  }

  // ---------------------------------------------------------------------------
  // - Brick Tank
  // ---------------------------------------------------------------------------

  public static BrickTank BRICK_TANK = new BrickTank();

  public static class BrickTank {

    @Config.Comment({
        "The amount of fluid this container can hold in mB.",
        "Default: " + 8000
    })
    public int CAPACITY = 8000;

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
        "True if the tank holds its contents when broken.",
        "Default: " + true
    })
    public boolean HOLDS_CONTENTS_WHEN_BROKEN = true;
  }

  // ---------------------------------------------------------------------------
  // - Crate
  // ---------------------------------------------------------------------------

  public static Crate CRATE = new Crate();

  public static class Crate {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The maximum number of stacks that can be stored in each slot.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 1)
    public int MAX_STACKS = 1;
  }

  // ---------------------------------------------------------------------------
  // - Durable Crate
  // ---------------------------------------------------------------------------

  public static DurableCrate DURABLE_CRATE = new DurableCrate();

  public static class DurableCrate {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The maximum number of items that can be stored in each slot.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 1)
    public int MAX_STACKS = 2;
  }

  // ---------------------------------------------------------------------------
  // - Shelf
  // ---------------------------------------------------------------------------

  public static Shelf SHELF = new Shelf();

  public static class Shelf {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The maximum number of stacks that can be stored in each slot.",
        "Default: " + 1
    })
    @Config.RangeInt(min = 1)
    public int MAX_STACKS = 1;
  }

  // ---------------------------------------------------------------------------
  // - Durable Shelf
  // ---------------------------------------------------------------------------

  public static DurableShelf DURABLE_SHELF = new DurableShelf();

  public static class DurableShelf {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The maximum number of items that can be stored in each slot.",
        "Default: " + 2
    })
    @Config.RangeInt(min = 1)
    public int MAX_STACKS = 2;
  }

  // ---------------------------------------------------------------------------
  // - Stash
  // ---------------------------------------------------------------------------

  public static Stash STASH = new Stash();

  public static class Stash {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The maximum number of stacks that can be stored in the stash.",
        "Default: " + 10
    })
    @Config.RangeInt(min = 1)
    public int MAX_STACKS = 10;
  }

  // ---------------------------------------------------------------------------
  // - Durable Stash
  // ---------------------------------------------------------------------------

  public static DurableStash DURABLE_STASH = new DurableStash();

  public static class DurableStash {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;

    @Config.Comment({
        "The maximum number of stacks that can be stored in the stash.",
        "Default: " + 20
    })
    @Config.RangeInt(min = 1)
    public int MAX_STACKS = 20;
  }

  // ---------------------------------------------------------------------------
  // - Wood Rack
  // ---------------------------------------------------------------------------

  public static WoodRack WOOD_RACK = new WoodRack();

  public static class WoodRack {

    @Config.Comment({
        "Set this to false to prevent piping contents in / out.",
        "Default: " + true
    })
    public boolean ALLOW_AUTOMATION = true;
  }
}
