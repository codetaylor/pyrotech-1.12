package com.codetaylor.mc.pyrotech.modules.hunting;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.hunting.block.BlockButchersBlock;
import com.codetaylor.mc.pyrotech.modules.hunting.block.BlockCarcass;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.ISpearEntityData;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.SpearEntityData;
import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntityMud;
import com.codetaylor.mc.pyrotech.modules.hunting.event.ClientEntityJoinWorldEventHandler;
import com.codetaylor.mc.pyrotech.modules.hunting.event.EntityAttachCapabilitiesEventHandler;
import com.codetaylor.mc.pyrotech.modules.hunting.event.LivingDeathEventHandler;
import com.codetaylor.mc.pyrotech.modules.hunting.init.*;
import com.codetaylor.mc.pyrotech.modules.hunting.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class ModuleHunting
    extends ModuleBase {

  public static final String MODULE_ID = "module.hunting";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;
  public static final Logger LOGGER = LogManager.getLogger(ModuleHunting.class);

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleHunting() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new EntityAttachCapabilitiesEventHandler());
    MinecraftForge.EVENT_BUS.register(new LivingDeathEventHandler());

    String[] craftTweakerPlugins = {
        "ZenCarcass",
        "ZenButchersBlock"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.hunting.plugin.crafttweaker." + plugin
      );
    }

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.hunting.plugin.jei.PluginJEI"
    );
  }

  @Override
  public void onRegister(Registry registry) {

    FluidInitializer.onRegister(registry);
    BlockInitializer.onRegister(registry);
    ItemInitializer.onRegister(registry);
    EntityInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    FluidInitializer.onClientRegister(registry);
    BlockInitializer.onClientRegister(registry);
    ItemInitializer.onClientRegister(registry);
    EntityInitializer.onClientRegister();
  }

  @Override
  public void onNetworkRegister(IPacketRegistry registry) {

    PacketInitializer.register(registry);
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onPreInitializationEvent(event);

    CapabilityManager.INSTANCE.register(ISpearEntityData.class, new SpearEntityData(), SpearEntityData::new);

    FMLInterModComms.sendFunctionMessage(
        "theoneprobe",
        "getTheOneProbe",
        "com.codetaylor.mc.pyrotech.modules.hunting.plugin.top.PluginTOP$Callback"
    );
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onClientPreInitializationEvent(event);

    FMLInterModComms.sendMessage(
        "waila",
        "register",
        "com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.PluginWaila.wailaCallback"
    );

    MinecraftForge.EVENT_BUS.register(new ClientEntityJoinWorldEventHandler());
  }

  @Override
  public void onInitializationEvent(FMLInitializationEvent event) {

    super.onInitializationEvent(event);

    EntityRegistry.addSpawn(
        EntityMud.class,
        ModuleHuntingConfig.ENTITY_MUD.SPAWN_WEIGHT,
        ModuleHuntingConfig.ENTITY_MUD.SPAWN_COUNT_MIN,
        ModuleHuntingConfig.ENTITY_MUD.SPAWN_COUNT_MAX,
        EnumCreatureType.MONSTER,
        Arrays.stream(ModuleHuntingConfig.ENTITY_MUD.SPAWN_BIOMES)
            .map(ResourceLocation::new)
            .map(ForgeRegistries.BIOMES::getValue)
            .toArray(Biome[]::new)
    );
  }

  @GameRegistry.ObjectHolder(ModuleHunting.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockCarcass.NAME)
    public static final BlockCarcass CARCASS;

    @GameRegistry.ObjectHolder(BlockButchersBlock.NAME)
    public static final BlockButchersBlock BUTCHERS_BLOCK;

    static {
      CARCASS = null;
      BUTCHERS_BLOCK = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleHunting.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(ItemPelt.NAME_RUINED)
    public static final ItemPelt PELT_RUINED;

    @GameRegistry.ObjectHolder(ItemHide.NAME_PIG)
    public static final ItemHide HIDE_PIG;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_SHEARED)
    public static final ItemHide HIDE_SHEEP_SHEARED;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_COW)
    public static final ItemPelt PELT_COW;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_MOOSHROOM)
    public static final ItemPelt PELT_MOOSHROOM;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_POLAR_BEAR)
    public static final ItemPelt PELT_POLAR_BEAR;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_BAT)
    public static final ItemPelt PELT_BAT;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_HORSE)
    public static final ItemPelt PELT_HORSE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_YELLOW)
    public static final ItemPelt PELT_SHEEP_YELLOW;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_WHITE)
    public static final ItemPelt PELT_SHEEP_WHITE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_GRAY_LIGHT)
    public static final ItemPelt PELT_SHEEP_GRAY_LIGHT;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_RED)
    public static final ItemPelt PELT_SHEEP_RED;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_PURPLE)
    public static final ItemPelt PELT_SHEEP_PURPLE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_PINK)
    public static final ItemPelt PELT_SHEEP_PINK;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_ORANGE)
    public static final ItemPelt PELT_SHEEP_ORANGE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_MAGENTA)
    public static final ItemPelt PELT_SHEEP_MAGENTA;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_LIME)
    public static final ItemPelt PELT_SHEEP_LIME;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_BLUE_LIGHT)
    public static final ItemPelt PELT_SHEEP_BLUE_LIGHT;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_GREEN)
    public static final ItemPelt PELT_SHEEP_GREEN;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_GRAY)
    public static final ItemPelt PELT_SHEEP_GRAY;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_CYAN)
    public static final ItemPelt PELT_SHEEP_CYAN;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_BROWN)
    public static final ItemPelt PELT_SHEEP_BROWN;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_BLUE)
    public static final ItemPelt PELT_SHEEP_BLUE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_BLACK)
    public static final ItemPelt PELT_SHEEP_BLACK;

    @GameRegistry.ObjectHolder(ItemHuntersKnife.BONE_NAME)
    public static final ItemHuntersKnife BONE_HUNTERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemHuntersKnife.FLINT_NAME)
    public static final ItemHuntersKnife FLINT_HUNTERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemHuntersKnife.STONE_NAME)
    public static final ItemHuntersKnife STONE_HUNTERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemHuntersKnife.GOLD_NAME)
    public static final ItemHuntersKnife GOLD_HUNTERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemHuntersKnife.IRON_NAME)
    public static final ItemHuntersKnife IRON_HUNTERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemHuntersKnife.DIAMOND_NAME)
    public static final ItemHuntersKnife DIAMOND_HUNTERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemHuntersKnife.OBSIDIAN_NAME)
    public static final ItemHuntersKnife OBSIDIAN_HUNTERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemButchersKnife.BONE_NAME)
    public static final ItemButchersKnife BONE_BUTCHERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemButchersKnife.FLINT_NAME)
    public static final ItemButchersKnife FLINT_BUTCHERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemButchersKnife.STONE_NAME)
    public static final ItemButchersKnife STONE_BUTCHERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemButchersKnife.GOLD_NAME)
    public static final ItemButchersKnife GOLD_BUTCHERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemButchersKnife.IRON_NAME)
    public static final ItemButchersKnife IRON_BUTCHERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemButchersKnife.DIAMOND_NAME)
    public static final ItemButchersKnife DIAMOND_BUTCHERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemButchersKnife.OBSIDIAN_NAME)
    public static final ItemButchersKnife OBSIDIAN_BUTCHERS_KNIFE;

    @GameRegistry.ObjectHolder(ItemHide.NAME_WASHED)
    public static final ItemHide HIDE_WASHED;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SMALL_WASHED)
    public static final ItemHide HIDE_SMALL_WASHED;

    @GameRegistry.ObjectHolder(ItemHideScraped.NAME)
    public static final ItemHideScraped HIDE_SCRAPED;

    @GameRegistry.ObjectHolder(ItemHideSmallScraped.NAME)
    public static final ItemHideSmallScraped HIDE_SMALL_SCRAPED;

    @GameRegistry.ObjectHolder(ItemLeatherDurableRepairKit.NAME)
    public static final ItemLeatherDurableRepairKit LEATHER_DURABLE_REPAIR_KIT;

    @GameRegistry.ObjectHolder(ItemLeatherDurableUpgradeKit.NAME)
    public static final ItemLeatherDurableUpgradeKit LEATHER_DURABLE_UPGRADE_KIT;

    @GameRegistry.ObjectHolder(ItemLeatherRepairKit.NAME)
    public static final ItemLeatherRepairKit LEATHER_REPAIR_KIT;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_LLAMA_WHITE)
    public static final ItemPelt PELT_LLAMA_WHITE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_LLAMA_CREAMY)
    public static final ItemPelt PELT_LLAMA_CREAMY;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_LLAMA_GRAY)
    public static final ItemPelt PELT_LLAMA_GRAY;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_LLAMA_BROWN)
    public static final ItemPelt PELT_LLAMA_BROWN;

    @GameRegistry.ObjectHolder(ItemHide.NAME_LLAMA)
    public static final ItemHide HIDE_LLAMA;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_WOLF)
    public static final ItemPelt PELT_WOLF;

    @GameRegistry.ObjectHolder(ItemHide.NAME_TANNED)
    public static final ItemHide HIDE_TANNED;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SMALL_TANNED)
    public static final ItemHide HIDE_SMALL_TANNED;

    @GameRegistry.ObjectHolder(ItemFlintArrow.NAME)
    public static final ItemFlintArrow FLINT_ARROW;

    @GameRegistry.ObjectHolder(ItemBoneArrow.NAME)
    public static final ItemBoneArrow BONE_ARROW;

    @GameRegistry.ObjectHolder(ItemFlintSpear.NAME)
    public static final ItemFlintSpear FLINT_SPEAR;

    @GameRegistry.ObjectHolder(ItemBoneSpear.NAME)
    public static final ItemBoneSpear BONE_SPEAR;

    @GameRegistry.ObjectHolder(ItemCrudeSpear.NAME)
    public static final ItemCrudeSpear CRUDE_SPEAR;

    static {
      PELT_RUINED = null;
      HIDE_PIG = null;
      HIDE_SHEEP_SHEARED = null;
      PELT_COW = null;
      PELT_MOOSHROOM = null;
      PELT_POLAR_BEAR = null;
      PELT_BAT = null;
      PELT_HORSE = null;
      PELT_SHEEP_YELLOW = null;
      PELT_SHEEP_WHITE = null;
      PELT_SHEEP_GRAY_LIGHT = null;
      PELT_SHEEP_RED = null;
      PELT_SHEEP_PURPLE = null;
      PELT_SHEEP_PINK = null;
      PELT_SHEEP_ORANGE = null;
      PELT_SHEEP_MAGENTA = null;
      PELT_SHEEP_LIME = null;
      PELT_SHEEP_BLUE_LIGHT = null;
      PELT_SHEEP_GREEN = null;
      PELT_SHEEP_GRAY = null;
      PELT_SHEEP_CYAN = null;
      PELT_SHEEP_BROWN = null;
      PELT_SHEEP_BLUE = null;
      PELT_SHEEP_BLACK = null;
      BONE_HUNTERS_KNIFE = null;
      FLINT_HUNTERS_KNIFE = null;
      STONE_HUNTERS_KNIFE = null;
      IRON_HUNTERS_KNIFE = null;
      GOLD_HUNTERS_KNIFE = null;
      DIAMOND_HUNTERS_KNIFE = null;
      OBSIDIAN_HUNTERS_KNIFE = null;
      BONE_BUTCHERS_KNIFE = null;
      FLINT_BUTCHERS_KNIFE = null;
      STONE_BUTCHERS_KNIFE = null;
      IRON_BUTCHERS_KNIFE = null;
      GOLD_BUTCHERS_KNIFE = null;
      DIAMOND_BUTCHERS_KNIFE = null;
      OBSIDIAN_BUTCHERS_KNIFE = null;
      HIDE_WASHED = null;
      HIDE_SMALL_WASHED = null;
      HIDE_SCRAPED = null;
      HIDE_SMALL_SCRAPED = null;
      LEATHER_DURABLE_REPAIR_KIT = null;
      LEATHER_DURABLE_UPGRADE_KIT = null;
      LEATHER_REPAIR_KIT = null;
      PELT_LLAMA_WHITE = null;
      PELT_LLAMA_CREAMY = null;
      PELT_LLAMA_GRAY = null;
      PELT_LLAMA_BROWN = null;
      HIDE_LLAMA = null;
      PELT_WOLF = null;
      HIDE_TANNED = null;
      HIDE_SMALL_TANNED = null;
      FLINT_ARROW = null;
      BONE_ARROW = null;
      FLINT_SPEAR = null;
      BONE_SPEAR = null;
      CRUDE_SPEAR = null;
    }
  }

  public static class Fluids {

    /*
    Fluids are injected from the fluid initializer.
     */

    public static final Fluid TANNIN;

    static {
      TANNIN = null;
    }
  }
}
