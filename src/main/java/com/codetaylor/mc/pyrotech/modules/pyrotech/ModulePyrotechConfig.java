package com.codetaylor.mc.pyrotech.modules.pyrotech;

import com.codetaylor.mc.pyrotech.modules.pyrotech.block.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleFluids;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModulePyrotech.MOD_ID, name = ModulePyrotech.MOD_ID + "/" + ModulePyrotech.MOD_ID + ".module.Charcoal")
public class ModulePyrotechConfig {

  public static Client CLIENT = new Client();

  public static class Client {

    @Config.Comment({
        "How many smoke particles a burning collector will emit per tick.",
        "Default: 10"
    })
    public int BURNING_COLLECTOR_SMOKE_PARTICLES = 10;
  }

  public static BrickKiln BRICK_KILN = new BrickKiln();

  public static class BrickKiln {

    @Config.Comment({
        "Set to true to deactivate the kiln when a recipe completes.",
        "The kiln will need to be re-lit when it deactivates.",
        "Default: false"
    })
    public boolean KEEP_HEAT = false;
  }

  public static PitKiln PIT_KILN = new PitKiln();

  public static class PitKiln {

    @Config.Comment({
        "Reduce the duration of all recipes by this amount for each adjacent refractory block.",
        "Range: [0, 0.2]",
        "Default: 0.1"
    })
    public double REFRACTORY_BLOCK_TIME_BONUS = 0.1;

  }

  public static Refractory REFRACTORY = new Refractory();

  public static class Refractory {

    @Config.Comment({
        "Maximum chance for a recipe item to fail conversion.",
        "Default: 0.95"
    })
    public double MAX_FAILURE_CHANCE = 0.95;

    @Config.Comment({
        "Minimum chance for a recipe item to fail conversion.",
        "Default: 0.05"
    })
    public double MIN_FAILURE_CHANCE = 0.05;

    @Config.Comment({
        "The maximum fluid capacity of an active pile in mb.",
        "Default: 500"
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

  public static Fuel FUEL = new Fuel();

  public static class Fuel {

    @Config.Comment({
        "Tinder burn time in ticks.",
        "Default: 120"
    })
    public int TINDER_BURN_TIME_TICKS = 120;

    @Config.Comment({
        "Straw burn time in ticks.",
        "Default: 50"
    })
    public int STRAW_BURN_TIME_TICKS = 50;

    @Config.Comment({
        "Thatch burn time in ticks.",
        "Default: 200"
    })
    public int THATCH_BURN_TIME_TICKS = 200;

    @Config.Comment({
        "Coal tar burn time in ticks.",
        "Default: 6400"
    })
    public int COAL_TAR_BURN_TIME_TICKS = 6400;

    @Config.Comment({
        "Wood tar burn time in ticks.",
        "Default: 4800"
    })
    public int WOOD_TAR_BURN_TIME_TICKS = 4800;

    @Config.Comment({
        "Coal coke burn time in ticks.",
        "Default: 3200"
    })
    public int COAL_COKE_BURN_TIME_TICKS = 3200;

    @Config.Comment({
        "Coal coke block burn time in ticks.",
        "Default: 32000"
    })
    public int COAL_COKE_BLOCK_BURN_TIME_TICKS = 32000;

  }

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
        "Default: " + (60 * 20)
    })
    public int BURN_TIME_TICKS_PER_LOG = 60 * 20;

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
        "Default: " + 0.5f
    })
    public double ASH_CHANCE = 0.5f;
  }

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
        "Default: 16"
    })
    public int BOW_DRILL_DURABILITY = 16;

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
