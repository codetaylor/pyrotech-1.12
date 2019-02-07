package com.codetaylor.mc.pyrotech.modules.tech.machine;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.BlockInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.RegistryInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.*;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemCog;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemSawmillBlade;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModuleTechMachine
    extends ModuleBase {

  public static final String MODULE_ID = "module.tech.machine";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleTechMachine.class.getSimpleName());

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleTechMachine() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);

    String[] craftTweakerPlugins = {
        "ZenStoneKiln",
        "ZenStoneSawmill",
        "ZenStoneCrucible",
        "ZenStoneOven",
        "ZenBrickKiln",
        "ZenBrickSawmill",
        "ZenBrickCrucible",
        "ZenBrickOven"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.crafttweaker." + plugin
      );
    }

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.jei.PluginJEI"
    );
  }

  @SubscribeEvent
  public void onNewRegistryEvent(RegistryEvent.NewRegistry event) {

    RegistryInitializer.createRegistries(event);
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onPreInitializationEvent(event);

    FMLInterModComms.sendMessage(
        "waila",
        "register",
        "com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.PluginWaila.wailaCallback"
    );
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    StoneKilnRecipesAdd.apply(Registries.STONE_KILN_RECIPES);
    StoneSawmillRecipesAdd.apply(Registries.STONE_SAWMILL_RECIPES);
    StoneCrucibleRecipesAdd.apply(Registries.STONE_CRUCIBLE_RECIPES);
    StoneOvenRecipesAdd.apply(Registries.STONE_OVEN_RECIPES);

    BrickKilnRecipesAdd.apply(Registries.BRICK_KILN_RECIPES);
    BrickSawmillRecipesAdd.apply(Registries.BRICK_SAWMILL_RECIPES);
    BrickCrucibleRecipesAdd.apply(Registries.BRICK_CRUCIBLE_RECIPES);
    BrickOvenRecipesAdd.apply(Registries.BRICK_OVEN_RECIPES);
  }

  @Override
  public void onRegister(Registry registry) {

    BlockInitializer.onRegister(registry);
    ItemInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    BlockInitializer.onClientRegister(registry);
    ItemInitializer.onClientRegister(registry);
  }

  @Override
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    super.onPostInitializationEvent(event);

    StoneOvenRecipesAdd.registerInheritedDryingRackRecipes(ModuleTechBasic.Registries.DRYING_RACK_RECIPE, Registries.STONE_OVEN_RECIPES);

    BrickKilnRecipesAdd.registerInheritedRecipes(Registries.STONE_KILN_RECIPES, Registries.BRICK_KILN_RECIPES);
    BrickOvenRecipesAdd.registerInheritedRecipes(Registries.STONE_OVEN_RECIPES, Registries.BRICK_OVEN_RECIPES);
    BrickSawmillRecipesAdd.registerInheritedRecipes(Registries.STONE_SAWMILL_RECIPES, Registries.BRICK_SAWMILL_RECIPES);
    BrickCrucibleRecipesAdd.registerInheritedRecipes(Registries.STONE_CRUCIBLE_RECIPES, Registries.BRICK_CRUCIBLE_RECIPES);
  }

  @GameRegistry.ObjectHolder(ModuleTechMachine.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockStoneKiln.NAME)
    public static final BlockStoneKiln STONE_KILN;

    @GameRegistry.ObjectHolder(BlockBrickKiln.NAME)
    public static final BlockBrickKiln BRICK_KILN;

    @GameRegistry.ObjectHolder(BlockStoneOven.NAME)
    public static final BlockStoneOven STONE_OVEN;

    @GameRegistry.ObjectHolder(BlockBrickOven.NAME)
    public static final BlockBrickOven BRICK_OVEN;

    @GameRegistry.ObjectHolder(BlockStoneSawmill.NAME)
    public static final BlockStoneSawmill STONE_SAWMILL;

    @GameRegistry.ObjectHolder(BlockBrickSawmill.NAME)
    public static final BlockBrickSawmill BRICK_SAWMILL;

    @GameRegistry.ObjectHolder(BlockStoneCrucible.NAME)
    public static final BlockStoneCrucible STONE_CRUCIBLE;

    @GameRegistry.ObjectHolder(BlockBrickCrucible.NAME)
    public static final BlockBrickCrucible BRICK_CRUCIBLE;

    @GameRegistry.ObjectHolder(BlockStoneHopper.NAME)
    public static final BlockStoneHopper STONE_HOPPER;

    static {
      STONE_KILN = null;
      STONE_OVEN = null;
      STONE_SAWMILL = null;
      STONE_CRUCIBLE = null;

      BRICK_KILN = null;
      BRICK_OVEN = null;
      BRICK_SAWMILL = null;
      BRICK_CRUCIBLE = null;

      STONE_HOPPER = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleTechMachine.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder("sawmill_blade_stone")
    public static final ItemSawmillBlade STONE_MILL_BLADE;

    @GameRegistry.ObjectHolder("sawmill_blade_flint")
    public static final ItemSawmillBlade FLINT_MILL_BLADE;

    @GameRegistry.ObjectHolder("sawmill_blade_bone")
    public static final ItemSawmillBlade BONE_MILL_BLADE;

    @GameRegistry.ObjectHolder("sawmill_blade_iron")
    public static final ItemSawmillBlade IRON_MILL_BLADE;

    @GameRegistry.ObjectHolder("sawmill_blade_diamond")
    public static final ItemSawmillBlade DIAMOND_MILL_BLADE;

    @GameRegistry.ObjectHolder("cog_wood")
    public static final ItemCog WOOD_COG;

    @GameRegistry.ObjectHolder("cog_stone")
    public static final ItemCog STONE_COG;

    @GameRegistry.ObjectHolder("cog_flint")
    public static final ItemCog FLINT_COG;

    @GameRegistry.ObjectHolder("cog_bone")
    public static final ItemCog BONE_COG;

    @GameRegistry.ObjectHolder("cog_iron")
    public static final ItemCog IRON_COG;

    @GameRegistry.ObjectHolder("cog_diamond")
    public static final ItemCog DIAMOND_COG;

    static {
      STONE_MILL_BLADE = null;
      FLINT_MILL_BLADE = null;
      BONE_MILL_BLADE = null;
      IRON_MILL_BLADE = null;
      DIAMOND_MILL_BLADE = null;

      WOOD_COG = null;
      STONE_COG = null;
      FLINT_COG = null;
      BONE_COG = null;
      IRON_COG = null;
      DIAMOND_COG = null;
    }
  }

  public static class Registries {

    public static final IForgeRegistryModifiable<StoneKilnRecipe> STONE_KILN_RECIPES;
    public static final IForgeRegistryModifiable<StoneSawmillRecipe> STONE_SAWMILL_RECIPES;
    public static final IForgeRegistryModifiable<StoneOvenRecipe> STONE_OVEN_RECIPES;
    public static final IForgeRegistryModifiable<StoneCrucibleRecipe> STONE_CRUCIBLE_RECIPES;

    public static final IForgeRegistryModifiable<BrickKilnRecipe> BRICK_KILN_RECIPES;
    public static final IForgeRegistryModifiable<BrickSawmillRecipe> BRICK_SAWMILL_RECIPES;
    public static final IForgeRegistryModifiable<BrickOvenRecipe> BRICK_OVEN_RECIPES;
    public static final IForgeRegistryModifiable<BrickCrucibleRecipe> BRICK_CRUCIBLE_RECIPES;

    static {
      STONE_KILN_RECIPES = null;
      STONE_SAWMILL_RECIPES = null;
      STONE_OVEN_RECIPES = null;
      STONE_CRUCIBLE_RECIPES = null;

      BRICK_KILN_RECIPES = null;
      BRICK_SAWMILL_RECIPES = null;
      BRICK_OVEN_RECIPES = null;
      BRICK_CRUCIBLE_RECIPES = null;
    }
  }
}
