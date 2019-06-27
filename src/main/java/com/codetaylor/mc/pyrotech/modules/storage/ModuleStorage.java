package com.codetaylor.mc.pyrotech.modules.storage;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.storage.block.*;
import com.codetaylor.mc.pyrotech.modules.storage.event.EntityItemPickupEventHandler;
import com.codetaylor.mc.pyrotech.modules.storage.init.BlockInitializer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModuleStorage
    extends ModuleBase {

  public static final String MODULE_ID = "module.storage";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleStorage.class.getSimpleName());

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleStorage() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);

    String[] craftTweakerPlugins = {
        "ZenDurableRockBag",
        "ZenSimpleRockBag",
        "ZenStoneTank",
        "ZenBrickTank",
        "ZenWoodRack",
        "ZenCrate",
        "ZenDurableCrate",
        "ZenShelf",
        "ZenDurableShelf",
        "ZenStash",
        "ZenDurableStash"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.storage.plugin.crafttweaker." + plugin
      );
    }
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onPreInitializationEvent(event);

    FMLInterModComms.sendMessage(
        "waila",
        "register",
        "com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.PluginWaila.wailaCallback"
    );

    MinecraftForge.EVENT_BUS.register(new EntityItemPickupEventHandler());
  }

  @Override
  public void onRegister(Registry registry) {

    BlockInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    BlockInitializer.onClientRegister(registry);
  }

  @GameRegistry.ObjectHolder(ModuleStorage.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockShelf.NAME)
    public static final BlockShelf SHELF;

    @GameRegistry.ObjectHolder(BlockShelfStone.NAME)
    public static final BlockShelfStone SHELF_STONE;

    @GameRegistry.ObjectHolder(BlockStash.NAME)
    public static final BlockStash STASH;

    @GameRegistry.ObjectHolder(BlockStashStone.NAME)
    public static final BlockStashStone STASH_STONE;

    @GameRegistry.ObjectHolder(BlockCrate.NAME)
    public static final BlockCrate CRATE;

    @GameRegistry.ObjectHolder(BlockCrateStone.NAME)
    public static final BlockCrateStone CRATE_STONE;

    @GameRegistry.ObjectHolder(BlockWoodRack.NAME)
    public static final BlockWoodRack WOOD_RACK;

    @GameRegistry.ObjectHolder(BlockTank.NAME)
    public static final BlockTank TANK;

    @GameRegistry.ObjectHolder(BlockBagSimple.NAME)
    public static final BlockBagSimple BAG_SIMPLE;

    @GameRegistry.ObjectHolder(BlockBagDurable.NAME)
    public static final BlockBagDurable BAG_DURABLE;

    static {
      SHELF = null;
      STASH = null;
      CRATE = null;
      SHELF_STONE = null;
      STASH_STONE = null;
      CRATE_STONE = null;
      WOOD_RACK = null;
      TANK = null;
      BAG_SIMPLE = null;
      BAG_DURABLE = null;
    }
  }
}
