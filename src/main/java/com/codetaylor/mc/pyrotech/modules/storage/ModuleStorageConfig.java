package com.codetaylor.mc.pyrotech.modules.storage;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleCore.MOD_ID, name = ModuleCore.MOD_ID + "/" + "module.Storage")
public class ModuleStorageConfig {

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

}
