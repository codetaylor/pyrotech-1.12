package com.codetaylor.mc.pyrotech.modules.bloomery;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.bloomery.block.BlockBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.bloomery.init.BlockInitializer;
import com.codetaylor.mc.pyrotech.modules.bloomery.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.bloomery.init.recipe.BloomeryRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.bloomery.item.*;
import com.codetaylor.mc.pyrotech.modules.bloomery.recipe.BloomeryRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModuleBloomery
    extends ModuleBase {

  public static final String MODULE_ID = "module.bloomery";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  //public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleBloomery.class.getSimpleName());

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleBloomery() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);

    /*
    String[] craftTweakerPlugins = {
        "ZenKilnPit",
        "ZenKilnStone",
        "ZenBurn",
        "ZenDryingRack",
        "ZenDryingRackCrude",
        "ZenChoppingBlock",
        "ZenGraniteAnvil",
        "ZenMillStone",
        "ZenCrucibleStone",
        "ZenCompactingBin",
        "ZenCampfire",
        "ZenOvenStone",
        "ZenWorktable",
        "ZenSoakingPot"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker." + plugin
      );
    }
    */

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.bloomery.plugin.jei.PluginJEI"
    );
  }

  @SubscribeEvent
  public void onNewRegistryEvent(RegistryEvent.NewRegistry event) {

    new RegistryBuilder<BloomeryRecipe>()
        .setName(new ResourceLocation(ModuleBloomery.MOD_ID, "bloomery_recipe"))
        .setType(BloomeryRecipe.class)
        .allowModification()
        .create();
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onPreInitializationEvent(event);

    FMLInterModComms.sendMessage(
        "waila",
        "register",
        "com.codetaylor.mc.pyrotech.modules.bloomery.plugin.waila.PluginWaila.wailaCallback"
    );
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    BloomeryRecipesAdd.apply(ModPyrotechRegistries.BLOOMERY_RECIPE);
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
  public void onClientInitializationEvent(FMLInitializationEvent event) {

    super.onClientInitializationEvent(event);

    ItemInitializer.onClientInitialization();
  }

  @Override
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    super.onPostInitializationEvent(event);

    BloomeryRecipesAdd.applyBloomRecipes(
        ModPyrotechRegistries.BLOOMERY_RECIPE,
        ModPyrotechRegistries.GRANITE_ANVIL_RECIPE,
        ModPyrotechRegistries.COMPACTING_BIN_RECIPE
    );
  }

  @GameRegistry.ObjectHolder(ModuleBloomery.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockBloomery.NAME)
    public static final BlockBloomery BLOOMERY;

    @GameRegistry.ObjectHolder(BlockBloom.NAME)
    public static final BlockBloom BLOOM;

    @GameRegistry.ObjectHolder(BlockPileSlag.NAME)
    public static final BlockPileSlag PILE_SLAG;

    static {
      BLOOMERY = null;
      BLOOM = null;
      PILE_SLAG = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleBloomery.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(BlockBloom.NAME)
    public static final Item BLOOM;

    @GameRegistry.ObjectHolder(ItemSlag.NAME)
    public static final ItemSlag SLAG;

    @GameRegistry.ObjectHolder(ItemTongsEmptyStone.NAME)
    public static final ItemTongsEmptyBase TONGS_STONE;

    @GameRegistry.ObjectHolder(ItemTongsFullStone.NAME)
    public static final ItemTongsFullBase TONGS_STONE_FULL;

    @GameRegistry.ObjectHolder(ItemTongsEmptyFlint.NAME)
    public static final ItemTongsEmptyBase TONGS_FLINT;

    @GameRegistry.ObjectHolder(ItemTongsFullFlint.NAME)
    public static final ItemTongsFullBase TONGS_FLINT_FULL;

    @GameRegistry.ObjectHolder(ItemTongsEmptyBone.NAME)
    public static final ItemTongsEmptyBase TONGS_BONE;

    @GameRegistry.ObjectHolder(ItemTongsFullBone.NAME)
    public static final ItemTongsFullBase TONGS_BONE_FULL;

    @GameRegistry.ObjectHolder(ItemTongsEmptyIron.NAME)
    public static final ItemTongsEmptyBase TONGS_IRON;

    @GameRegistry.ObjectHolder(ItemTongsFullIron.NAME)
    public static final ItemTongsFullBase TONGS_IRON_FULL;

    @GameRegistry.ObjectHolder(ItemTongsEmptyDiamond.NAME)
    public static final ItemTongsEmptyBase TONGS_DIAMOND;

    @GameRegistry.ObjectHolder(ItemTongsFullDiamond.NAME)
    public static final ItemTongsFullBase TONGS_DIAMOND_FULL;

    static {
      BLOOM = null;
      SLAG = null;
      TONGS_STONE = null;
      TONGS_STONE_FULL = null;
      TONGS_BONE = null;
      TONGS_BONE_FULL = null;
      TONGS_FLINT = null;
      TONGS_FLINT_FULL = null;
      TONGS_IRON = null;
      TONGS_IRON_FULL = null;
      TONGS_DIAMOND = null;
      TONGS_DIAMOND_FULL = null;
    }
  }
}
