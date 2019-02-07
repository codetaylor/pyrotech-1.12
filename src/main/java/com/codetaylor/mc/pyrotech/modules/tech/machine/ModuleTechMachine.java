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
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneCrucibleRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneKilnRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneOvenRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneSawmillRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemCog;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemSawmillBlade;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
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
        "ZenKilnStone",
        "ZenMillStone",
        "ZenCrucibleStone",
        "ZenOvenStone"
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

    StoneKilnRecipesAdd.apply(ModuleTechMachine.Registries.KILN_STONE_RECIPE);
    StoneSawmillRecipesAdd.apply(ModuleTechMachine.Registries.MILL_STONE_RECIPE);
    StoneCrucibleRecipesAdd.apply(ModuleTechMachine.Registries.CRUCIBLE_STONE_RECIPE);
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

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)
        && ModuleTechMachineConfig.STONE_OVEN.INHERIT_DRYING_RACK_RECIPES) {
      StoneOvenRecipesAdd.registerInheritedDryingRackRecipes(
          ModuleTechBasic.Registries.DRYING_RACK_RECIPE,
          Registries.OVEN_STONE_RECIPE
      );
    }
  }

  @GameRegistry.ObjectHolder(ModuleTechMachine.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockStoneKiln.NAME)
    public static final BlockStoneKiln KILN_STONE;

    @GameRegistry.ObjectHolder(BlockStoneOven.NAME)
    public static final BlockStoneOven OVEN_STONE;

    @GameRegistry.ObjectHolder(BlockStoneSawmill.NAME)
    public static final BlockStoneSawmill MILL_STONE;

    @GameRegistry.ObjectHolder(BlockStoneCrucible.NAME)
    public static final BlockStoneCrucible CRUCIBLE_STONE;

    @GameRegistry.ObjectHolder(BlockStoneHopper.NAME)
    public static final BlockStoneHopper STONE_HOPPER;

    static {
      KILN_STONE = null;
      OVEN_STONE = null;
      MILL_STONE = null;
      CRUCIBLE_STONE = null;
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

    public static final IForgeRegistryModifiable<StoneKilnRecipe> KILN_STONE_RECIPE;
    public static final IForgeRegistryModifiable<StoneSawmillRecipe> MILL_STONE_RECIPE;
    public static final IForgeRegistryModifiable<StoneOvenRecipe> OVEN_STONE_RECIPE;
    public static final IForgeRegistryModifiable<StoneCrucibleRecipe> CRUCIBLE_STONE_RECIPE;

    static {
      KILN_STONE_RECIPE = null;
      MILL_STONE_RECIPE = null;
      OVEN_STONE_RECIPE = null;
      CRUCIBLE_STONE_RECIPE = null;
    }
  }
}
