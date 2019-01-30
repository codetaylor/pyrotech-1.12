package com.codetaylor.mc.pyrotech.modules.tech.refractory;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModulePackets;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.PitBurnRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockActivePile;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockPitAsh;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockTarCollector;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockTarDrain;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.event.FurnaceFuelBurnTimeEventHandler;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.event.NeighborNotifyEventHandler;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.event.RightClickBlockEventHandler;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.init.BlockInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.init.FluidInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.init.RegistryInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.init.recipe.BurnPitRecipesAdd;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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

import java.util.List;
import java.util.function.Predicate;

public class ModuleTechRefractory
    extends ModuleBase {

  public static final String MODULE_ID = "module.tech.refractory";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleTechRefractory.class.getSimpleName());

  static {
    FluidRegistry.enableUniversalBucket();
  }

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleTechRefractory() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new RightClickBlockEventHandler());
    MinecraftForge.EVENT_BUS.register(new NeighborNotifyEventHandler());
    MinecraftForge.EVENT_BUS.register(new FurnaceFuelBurnTimeEventHandler());

    String[] craftTweakerPlugins = {
        "ZenBurn"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.crafttweaker." + plugin
      );
    }

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.jei.PluginJEI"
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
        "com.codetaylor.mc.pyrotech.modules.pyrotech.plugin.waila.PluginWaila.wailaCallback"
    );
  }

  @Override
  public void onNetworkRegister(IPacketRegistry registry) {

    ModulePackets.register(registry);
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    BurnPitRecipesAdd.apply(ModuleTechRefractory.Registries.BURN_RECIPE);
  }

  @Override
  public void onRegister(Registry registry) {

    FluidInitializer.onRegister(registry);
    BlockInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    FluidInitializer.onClientRegister(registry);
    BlockInitializer.onClientRegister(registry);
  }

  @Override
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    super.onPostInitializationEvent(event);

    RegistryInitializer.initializeRefractoryBlocks();
  }

  public static class Fluids {

    /*
    Fluids are injected from the fluid initializer.
     */

    public static final Fluid WOOD_TAR;
    public static final Fluid COAL_TAR;

    static {
      WOOD_TAR = null;
      COAL_TAR = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleTechRefractory.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockTarCollector.NAME)
    public static final BlockTarCollector TAR_COLLECTOR;

    @GameRegistry.ObjectHolder(BlockTarDrain.NAME)
    public static final BlockTarDrain TAR_DRAIN;

    @GameRegistry.ObjectHolder(BlockPitAsh.NAME)
    public static final BlockPitAsh PIT_ASH_BLOCK;

    @GameRegistry.ObjectHolder(BlockActivePile.NAME)
    public static final BlockActivePile ACTIVE_PILE;

    static {
      TAR_COLLECTOR = null;
      TAR_DRAIN = null;
      PIT_ASH_BLOCK = null;
      ACTIVE_PILE = null;
    }
  }

  public static class Registries {

    public static final IForgeRegistryModifiable<PitBurnRecipe> BURN_RECIPE;

    public static final List<Predicate<IBlockState>> REFRACTORY_BLOCK_LIST;

    static {
      BURN_RECIPE = null;
      REFRACTORY_BLOCK_LIST = null;
    }
  }
}
