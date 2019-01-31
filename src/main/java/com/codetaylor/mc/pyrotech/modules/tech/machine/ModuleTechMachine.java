package com.codetaylor.mc.pyrotech.modules.tech.machine;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneCrucible;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneKiln;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneOven;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.BlockInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneCrucibleRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneKilnRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.init.recipe.StoneSawmillRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemMillBlade;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneSawmillRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneOvenRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import net.minecraftforge.registries.RegistryBuilder;
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

    new RegistryBuilder<StoneKilnRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "kiln_stone_recipe"))
        .setType(StoneKilnRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<StoneSawmillRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "mill_stone_recipe"))
        .setType(StoneSawmillRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<StoneCrucibleRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "crucible_stone_recipe"))
        .setType(StoneCrucibleRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<StoneOvenRecipe>()
        .setName(new ResourceLocation(ModuleTechMachine.MOD_ID, "oven_stone_recipe"))
        .setType(StoneOvenRecipe.class)
        .allowModification()
        .create();

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleTechMachine.Registries.class,
        "KILN_STONE_RECIPE",
        GameRegistry.findRegistry(StoneKilnRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "MILL_STONE_RECIPE",
        GameRegistry.findRegistry(StoneSawmillRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "OVEN_STONE_RECIPE",
        GameRegistry.findRegistry(StoneOvenRecipe.class)
    );
    injector.inject(
        ModuleTechMachine.Registries.class,
        "CRUCIBLE_STONE_RECIPE",
        GameRegistry.findRegistry(StoneCrucibleRecipe.class)
    );
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

    static {
      KILN_STONE = null;
      OVEN_STONE = null;
      MILL_STONE = null;
      CRUCIBLE_STONE = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleTechMachine.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder("sawmill_blade_stone")
    public static final ItemMillBlade STONE_MILL_BLADE;

    @GameRegistry.ObjectHolder("sawmill_blade_flint")
    public static final ItemMillBlade FLINT_MILL_BLADE;

    @GameRegistry.ObjectHolder("sawmill_blade_bone")
    public static final ItemMillBlade BONE_MILL_BLADE;

    @GameRegistry.ObjectHolder("sawmill_blade_iron")
    public static final ItemMillBlade IRON_MILL_BLADE;

    @GameRegistry.ObjectHolder("sawmill_blade_diamond")
    public static final ItemMillBlade DIAMOND_MILL_BLADE;

    static {
      STONE_MILL_BLADE = null;
      FLINT_MILL_BLADE = null;
      BONE_MILL_BLADE = null;
      IRON_MILL_BLADE = null;
      DIAMOND_MILL_BLADE = null;
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
