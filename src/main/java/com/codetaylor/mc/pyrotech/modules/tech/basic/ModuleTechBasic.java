package com.codetaylor.mc.pyrotech.modules.tech.basic;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.init.BlockInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.init.PacketInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.init.RegistryInitializer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.ItemTinder;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.*;
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

public class ModuleTechBasic
    extends ModuleBase {

  public static final String MODULE_ID = "module.tech.basic";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleTechBasic.class.getSimpleName());

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleTechBasic() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);

    String[] craftTweakerPlugins = {
        "ZenKilnPit",
        "ZenCrudeDryingRack",
        "ZenDryingRack",
        "ZenChoppingBlock",
        "ZenAnvilGranite",
        "ZenAnvilIronclad",
        "ZenCompactingBin",
        "ZenCampfire",
        "ZenWorktable",
        "ZenSoakingPot"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker." + plugin
      );
    }

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.PluginJEI"
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
        "com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.PluginWaila.wailaCallback"
    );
  }

  @Override
  public void onNetworkRegister(IPacketRegistry registry) {

    PacketInitializer.register(registry);
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    PitKilnRecipesAdd.apply(ModuleTechBasic.Registries.KILN_PIT_RECIPE);
    CrudeDryingRackRecipesAdd.apply(ModuleTechBasic.Registries.CRUDE_DRYING_RACK_RECIPE);
    DryingRackRecipesAdd.apply(ModuleTechBasic.Registries.DRYING_RACK_RECIPE);
    AnvilGraniteRecipesAdd.apply(ModuleTechBasic.Registries.ANVIL_RECIPE);
    AnvilIroncladRecipesAdd.apply(ModuleTechBasic.Registries.ANVIL_RECIPE);
    ChoppingBlockRecipesAdd.apply(ModuleTechBasic.Registries.CHOPPING_BLOCK_RECIPE);
    CompactingBinRecipesAdd.apply(ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE);
    SoakingPotRecipesAdd.apply(ModuleTechBasic.Registries.SOAKING_POT_RECIPE);
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

    DryingRackRecipesAdd.registerInheritedRecipes(Registries.CRUDE_DRYING_RACK_RECIPE, Registries.DRYING_RACK_RECIPE);
    AnvilIroncladRecipesAdd.registerInheritedRecipes(Registries.ANVIL_RECIPE);
  }

  @GameRegistry.ObjectHolder(ModuleTechBasic.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockKilnPit.NAME)
    public static final BlockKilnPit KILN_PIT;

    @GameRegistry.ObjectHolder(BlockCampfire.NAME)
    public static final BlockCampfire CAMPFIRE;

    @GameRegistry.ObjectHolder(BlockDryingRack.NAME)
    public static final BlockDryingRack DRYING_RACK;

    @GameRegistry.ObjectHolder(BlockChoppingBlock.NAME)
    public static final BlockChoppingBlock CHOPPING_BLOCK;

    @GameRegistry.ObjectHolder(BlockAnvilGranite.NAME)
    public static final BlockAnvilGranite ANVIL_GRANITE;

    @GameRegistry.ObjectHolder(BlockAnvilIronPlated.NAME)
    public static final BlockAnvilIronPlated ANVIL_IRON_PLATED;

    @GameRegistry.ObjectHolder(BlockWorktable.NAME)
    public static final BlockWorktable WORKTABLE;

    @GameRegistry.ObjectHolder(BlockWorktableStone.NAME)
    public static final BlockWorktableStone WORKTABLE_STONE;

    @GameRegistry.ObjectHolder(BlockCompactingBin.NAME)
    public static final BlockCompactingBin COMPACTING_BIN;

    @GameRegistry.ObjectHolder(BlockSoakingPot.NAME)
    public static final BlockSoakingPot SOAKING_POT;

    static {
      KILN_PIT = null;
      CAMPFIRE = null;
      DRYING_RACK = null;
      CHOPPING_BLOCK = null;
      ANVIL_GRANITE = null;
      ANVIL_IRON_PLATED = null;
      WORKTABLE = null;
      WORKTABLE_STONE = null;
      COMPACTING_BIN = null;
      SOAKING_POT = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleTechBasic.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(ItemTinder.NAME)
    public static final ItemTinder TINDER;

    static {
      TINDER = null;
    }
  }

  public static class Registries {

    public static final IForgeRegistryModifiable<KilnPitRecipe> KILN_PIT_RECIPE;
    public static final IForgeRegistryModifiable<CrudeDryingRackRecipe> CRUDE_DRYING_RACK_RECIPE;
    public static final IForgeRegistryModifiable<DryingRackRecipe> DRYING_RACK_RECIPE;
    public static final IForgeRegistryModifiable<ChoppingBlockRecipe> CHOPPING_BLOCK_RECIPE;
    public static final IForgeRegistryModifiable<AnvilRecipe> ANVIL_RECIPE;
    public static final IForgeRegistryModifiable<CompactingBinRecipe> COMPACTING_BIN_RECIPE;
    public static final IForgeRegistryModifiable<CampfireRecipe> CAMPFIRE_RECIPE;
    public static final IForgeRegistryModifiable<WorktableRecipe> WORKTABLE_RECIPE;
    public static final IForgeRegistryModifiable<SoakingPotRecipe> SOAKING_POT_RECIPE;

    static {
      KILN_PIT_RECIPE = null;
      CRUDE_DRYING_RACK_RECIPE = null;
      DRYING_RACK_RECIPE = null;
      CHOPPING_BLOCK_RECIPE = null;
      ANVIL_RECIPE = null;
      COMPACTING_BIN_RECIPE = null;
      CAMPFIRE_RECIPE = null;
      WORKTABLE_RECIPE = null;
      SOAKING_POT_RECIPE = null;
    }
  }
}
