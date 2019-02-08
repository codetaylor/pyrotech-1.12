package com.codetaylor.mc.pyrotech.modules.storage;

import net.minecraftforge.common.config.Config;

@Config(modid = ModuleStorage.MOD_ID, name = ModuleStorage.MOD_ID + "/" + "module.Storage")
public class ModuleStorageConfig {

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
  // - Durable Stash
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
