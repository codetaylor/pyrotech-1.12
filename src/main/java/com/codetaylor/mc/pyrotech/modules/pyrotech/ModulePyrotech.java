package com.codetaylor.mc.pyrotech.modules.pyrotech;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.fluid.CPacketFluidUpdate;
import com.codetaylor.mc.pyrotech.library.util.BlockMetaMatcher;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleEntities;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleRecipes;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnBrickRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.PitBurnRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModulePyrotech
    extends ModuleBase {

  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModulePyrotech.class.getSimpleName());

  static {
    FluidRegistry.enableUniversalBucket();
  }

  public static IPacketService PACKET_SERVICE;

  public ModulePyrotech() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();

    MinecraftForge.EVENT_BUS.register(this);

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker.ZenKilnPit"
    );

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker.ZenKilnBrick"
    );

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker.ZenBurn"
    );

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker.ZenDryingRack"
    );

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.PluginJEI"
    );

    this.registerIntegrationPlugin(
        "dropt",
        "com.codetaylor.mc.pyrotech.modules.pyrotech.compat.dropt.PluginDropt"
    );
  }

  @SubscribeEvent
  public void onNewRegistryEvent(RegistryEvent.NewRegistry event) {

    new RegistryBuilder<PitBurnRecipe>()
        .setName(new ResourceLocation(MOD_ID, "pit_recipe"))
        .setType(PitBurnRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<KilnPitRecipe>()
        .setName(new ResourceLocation(MOD_ID, "kiln_pit_recipe"))
        .setType(KilnPitRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<KilnBrickRecipe>()
        .setName(new ResourceLocation(MOD_ID, "kiln_brick_recipe"))
        .setType(KilnBrickRecipe.class)
        .allowModification()
        .create();

    new RegistryBuilder<DryingRackRecipe>()
        .setName(new ResourceLocation(MOD_ID, "drying_rack_recipe"))
        .setType(DryingRackRecipe.class)
        .allowModification()
        .create();
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onPreInitializationEvent(event);

    FMLInterModComms.sendMessage(
        "waila",
        "register",
        "com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar.wailaCallback"
    );
  }

  @Override
  public void onNetworkRegister(IPacketRegistry registry) {

    registry.register(CPacketFluidUpdate.class, CPacketFluidUpdate.class, Side.CLIENT);
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    ModuleRecipes.onRegisterRecipes(event.getRegistry());
    ModuleRecipes.onRegisterPitBurnRecipes(ModulePyrotechRegistries.BURN_RECIPE);
    ModuleRecipes.onRegisterKilnPitRecipes(ModulePyrotechRegistries.KILN_PIT_RECIPE);
    ModuleRecipes.onRegisterKilnBrickRecipe(ModulePyrotechRegistries.KILN_BRICK_RECIPE);
    ModuleRecipes.onRegisterDryingRackRecipes(ModulePyrotechRegistries.DRYING_RACK_RECIPE);
  }

  @Override
  public void onRegister(Registry registry) {

    ModuleBlocks.onRegister(registry);
    ModuleItems.onRegister(registry);
    ModuleEntities.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    ModuleBlocks.onClientRegister(registry);
    ModuleItems.onClientRegister(registry);
    ModuleEntities.onClientRegister();
  }

  @Override
  public void onClientInitializationEvent(FMLInitializationEvent event) {

    super.onClientInitializationEvent(event);

    ModuleBlocks.onClientInitialization();
  }

  @Override
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    super.onPostInitializationEvent(event);

    RecipeItemParser parser = new RecipeItemParser();

    // ------------------------------------------------------------------------
    // - Refractory Blocks
    // ------------------------------------------------------------------------

    for (String blockString : ModulePyrotechConfig.GENERAL.REFRACTORY_BRICKS) {
      try {
        ModulePyrotechRegistries.REFRACTORY_BLOCK_LIST.add(Util.parseBlockStringWithWildcard(blockString, parser));

      } catch (MalformedRecipeItemException e) {
        LOGGER.error("", e);
      }
    }

    // ------------------------------------------------------------------------
    // - Additional Valid Coke Oven Structure Blocks
    // ------------------------------------------------------------------------

    {
      ModulePyrotechRegistries.COKE_OVEN_VALID_STRUCTURE_BLOCK_LIST.add(new BlockMetaMatcher(
          ModuleBlocks.ACTIVE_PILE,
          0
      ));
      ModulePyrotechRegistries.COKE_OVEN_VALID_STRUCTURE_BLOCK_LIST.add(new BlockMetaMatcher(
          ModuleBlocks.PIT_ASH_BLOCK,
          0
      ));
    }
  }
}
