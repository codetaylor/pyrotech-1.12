package com.codetaylor.mc.pyrotech.modules.tech.bloomery;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockWitherForge;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.event.ItemTooltipEventHandler;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.BlockInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.EntityInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.SlagInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe.BloomeryRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe.CompactingBinRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.init.recipe.WitherForgeRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.*;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsFullBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.WitherForgeRecipe;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
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
import net.minecraftforge.registries.IForgeRegistryModifiable;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.IdentityHashMap;
import java.util.Map;

public class ModuleTechBloomery
    extends ModuleBase {

  public static final String MODULE_ID = "module.tech.bloomery";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleTechBloomery.class.getSimpleName());

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  private File modConfigurationDirectory;

  public ModuleTechBloomery() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);

    String[] craftTweakerPlugins = {
        "ZenBloomery"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.crafttweaker." + plugin
      );
    }

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.jei.PluginJEI"
    );
  }

  @SubscribeEvent
  public void onNewRegistryEvent(RegistryEvent.NewRegistry event) {

    // --- Creation

    new RegistryBuilder<BloomeryRecipe>()
        .setName(new ResourceLocation(ModuleTechBloomery.MOD_ID, "bloomery_recipe"))
        .setType(BloomeryRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<WitherForgeRecipe>()
        .setName(new ResourceLocation(ModuleTechBloomery.MOD_ID, "wither_forge_recipe"))
        .setType(WitherForgeRecipe.class)
        .allowModification()
        .create();

    // --- Injection

    Injector injector = new Injector();

    injector.inject(
        ModuleTechBloomery.Registries.class,
        "BLOOMERY_RECIPE",
        GameRegistry.findRegistry(BloomeryRecipe.class)
    );

    injector.inject(
        ModuleTechBloomery.Registries.class,
        "WITHER_FORGE_RECIPE",
        GameRegistry.findRegistry(WitherForgeRecipe.class)
    );
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    this.modConfigurationDirectory = event.getModConfigurationDirectory();

    super.onPreInitializationEvent(event);

    FMLInterModComms.sendMessage(
        "waila",
        "register",
        "com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.PluginWaila.wailaCallback"
    );

    MinecraftForge.EVENT_BUS.register(new ItemTooltipEventHandler());
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTextureStitchEvent(TextureStitchEvent event) {

    TextureMap map = event.getMap();
    map.registerSprite(new ResourceLocation("pyrotech:blocks/active_pile"));
    map.registerSprite(new ResourceLocation("pyrotech:blocks/ash_block"));
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    BloomeryRecipesAdd.apply(Registries.BLOOMERY_RECIPE);
    WitherForgeRecipesAdd.apply(Registries.WITHER_FORGE_RECIPE);

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)) {
      CompactingBinRecipesAdd.apply(ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE);
    }
  }

  @Override
  public void onRegister(Registry registry) {

    BlockInitializer.onRegister(registry);
    ItemInitializer.onRegister(registry);

    registry.registerItemRegistrationStrategy(forgeRegistry -> {
      SlagInitializer.initializeSlag(this.modConfigurationDirectory);
    });

    EntityInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    BlockInitializer.onClientRegister(registry);
    ItemInitializer.onClientRegister(registry);

    registry.registerClientModelRegistrationStrategy(SlagInitializer::initializeSlagModels);
  }

  @Override
  public void onClientInitializationEvent(FMLInitializationEvent event) {

    super.onClientInitializationEvent(event);

    SlagInitializer.initializeSlagColors();
  }

  @Override
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    super.onPostInitializationEvent(event);

    WitherForgeRecipesAdd.registerInheritedRecipes(
        Registries.BLOOMERY_RECIPE,
        Registries.WITHER_FORGE_RECIPE
    );

    if (ModPyrotech.INSTANCE.isModuleEnabled(ModuleTechBasic.class)) {
      WitherForgeRecipesAdd.registerBloomAnvilRecipes(
          Registries.WITHER_FORGE_RECIPE,
          ModuleTechBasic.Registries.ANVIL_RECIPE
      );
      BloomeryRecipesAdd.registerBloomAnvilRecipes(
          ModuleTechBloomery.Registries.BLOOMERY_RECIPE,
          ModuleTechBasic.Registries.ANVIL_RECIPE
      );
    }
  }

  @Override
  public void onClientPostInitializationEvent(FMLPostInitializationEvent event) {

    super.onClientPostInitializationEvent(event);

  }

  public static class Blocks {

    public static final Map<BlockPileSlag, BlockPileSlag.Properties> GENERATED_PILE_SLAG = new IdentityHashMap<>();

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + BlockPileSlag.NAME)
    public static final BlockPileSlag PILE_SLAG;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + BlockBloomery.NAME)
    public static final BlockBloomery BLOOMERY;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + BlockWitherForge.NAME)
    public static final BlockWitherForge WITHER_FORGE;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + BlockBloom.NAME)
    public static final BlockBloom BLOOM;

    static {
      PILE_SLAG = null;
      BLOOMERY = null;
      WITHER_FORGE = null;
      BLOOM = null;
    }
  }

  public static class Items {

    public static final Map<ItemSlag, ItemSlag.Properties> GENERATED_SLAG = new IdentityHashMap<>();

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemSlag.NAME)
    public static final Item SLAG;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + BlockBloom.NAME)
    public static final Item BLOOM;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsEmptyStone.NAME)
    public static final ItemTongsEmptyBase TONGS_STONE;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsFullStone.NAME)
    public static final ItemTongsFullBase TONGS_STONE_FULL;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsEmptyFlint.NAME)
    public static final ItemTongsEmptyBase TONGS_FLINT;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsFullFlint.NAME)
    public static final ItemTongsFullBase TONGS_FLINT_FULL;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsEmptyBone.NAME)
    public static final ItemTongsEmptyBase TONGS_BONE;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsFullBone.NAME)
    public static final ItemTongsFullBase TONGS_BONE_FULL;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsEmptyIron.NAME)
    public static final ItemTongsEmptyBase TONGS_IRON;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsFullIron.NAME)
    public static final ItemTongsFullBase TONGS_IRON_FULL;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsEmptyGold.NAME)
    public static final ItemTongsEmptyBase TONGS_GOLD;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsFullGold.NAME)
    public static final ItemTongsFullBase TONGS_GOLD_FULL;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsEmptyDiamond.NAME)
    public static final ItemTongsEmptyBase TONGS_DIAMOND;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsFullDiamond.NAME)
    public static final ItemTongsFullBase TONGS_DIAMOND_FULL;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsEmptyObsidian.NAME)
    public static final ItemTongsEmptyBase TONGS_OBSIDIAN;

    @GameRegistry.ObjectHolder(ModuleTechBloomery.MOD_ID + ":" + ItemTongsFullObsidian.NAME)
    public static final ItemTongsFullBase TONGS_OBSIDIAN_FULL;

    static {
      SLAG = null;
      BLOOM = null;
      TONGS_STONE = null;
      TONGS_STONE_FULL = null;
      TONGS_BONE = null;
      TONGS_BONE_FULL = null;
      TONGS_FLINT = null;
      TONGS_FLINT_FULL = null;
      TONGS_IRON = null;
      TONGS_IRON_FULL = null;
      TONGS_GOLD = null;
      TONGS_GOLD_FULL = null;
      TONGS_DIAMOND = null;
      TONGS_DIAMOND_FULL = null;
      TONGS_OBSIDIAN = null;
      TONGS_OBSIDIAN_FULL = null;
    }
  }

  public static class Registries {

    public static final IForgeRegistryModifiable<BloomeryRecipe> BLOOMERY_RECIPE;
    public static final IForgeRegistryModifiable<WitherForgeRecipe> WITHER_FORGE_RECIPE;

    static {
      BLOOMERY_RECIPE = null;
      WITHER_FORGE_RECIPE = null;
    }
  }

}
