package com.codetaylor.mc.pyrotech.modules.core;

import com.codetaylor.mc.athenaeum.interaction.event.InteractionMouseScrollEventHandler;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.BulkRenderItemSupplier;
import com.codetaylor.mc.pyrotech.IAirflowConsumerCapability;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.blockrenderer.BlockRenderer;
import com.codetaylor.mc.pyrotech.library.blockrenderer.RenderTickEventHandler;
import com.codetaylor.mc.pyrotech.modules.core.advancement.AdvancementTriggers;
import com.codetaylor.mc.pyrotech.modules.core.block.*;
import com.codetaylor.mc.pyrotech.modules.core.command.ClientCommandExport;
import com.codetaylor.mc.pyrotech.modules.core.command.ClientCommandLang;
import com.codetaylor.mc.pyrotech.modules.core.event.HarvestDropsEventHandler;
import com.codetaylor.mc.pyrotech.modules.core.event.StrawBedEventHandler;
import com.codetaylor.mc.pyrotech.modules.core.event.TooltipEventHandler;
import com.codetaylor.mc.pyrotech.modules.core.init.*;
import com.codetaylor.mc.pyrotech.modules.core.init.recipe.VanillaCraftingRecipesRemove;
import com.codetaylor.mc.pyrotech.modules.core.init.recipe.VanillaFurnaceRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.core.init.recipe.VanillaFurnaceRecipesRemove;
import com.codetaylor.mc.pyrotech.modules.core.item.*;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.CrTEventHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Collections;

public class ModuleCore
    extends ModuleBase {

  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleCore.class.getSimpleName());

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public static boolean MISSING_WOOD_COMPAT = true;
  public static boolean MISSING_ORE_COMPAT = true;

  @CapabilityInject(IAirflowConsumerCapability.class)
  public static final Capability<IAirflowConsumerCapability> CAPABILITY_AIRFLOW_CONSUMER;

  static {
    CAPABILITY_AIRFLOW_CONSUMER = null;
  }

  public ModuleCore() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new InteractionMouseScrollEventHandler(PACKET_SERVICE));
    MinecraftForge.EVENT_BUS.register(new StrawBedEventHandler());

    String[] craftTweakerPlugins = {
        "ZenStages"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker." + plugin
      );
    }

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.core.plugin.jei.PluginJEI"
    );
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onPreInitializationEvent(event);

    CapabilityManager.INSTANCE.register(IAirflowConsumerCapability.class, new IAirflowConsumerCapability.Storage(), new IAirflowConsumerCapability.Factory());

    CompatInitializerWood.WoodCompatData woodCompatData = CompatInitializerWood.read(this.getConfigurationDirectory().toPath());

    if (woodCompatData != null) {
      MISSING_WOOD_COMPAT = false;
    }

    CompatInitializerOre.OreCompatData oreCompatData = CompatInitializerOre.read(this.getConfigurationDirectory().toPath());

    if (oreCompatData != null) {
      MISSING_ORE_COMPAT = false;
    }

    if (Loader.isModLoaded("crafttweaker")) {
      MinecraftForge.EVENT_BUS.register(new CrTEventHandler(this));
    }

    if (ModuleCoreConfig.TWEAKS.DROP_STICKS_FROM_LEAVES) {
      MinecraftForge.EVENT_BUS.register(new HarvestDropsEventHandler.Sticks());
    }

    FMLInterModComms.sendFunctionMessage(
        "theoneprobe",
        "getTheOneProbe",
        "com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP$Callback"
    );
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onClientPreInitializationEvent(event);

    if (ModuleCoreConfig.CLIENT.SHOW_BURN_TIME_IN_TOOLTIPS) {
      MinecraftForge.EVENT_BUS.register(new TooltipEventHandler.BurnTime());
    }
  }

  @Override
  public void onInitializationEvent(FMLInitializationEvent event) {

    super.onInitializationEvent(event);

    CriteriaTriggers.register(AdvancementTriggers.MOD_ITEM_TRIGGER);
  }

  @Override
  public void onNetworkRegister(IPacketRegistry registry) {

    PacketInitializer.register(registry);
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    // furnace recipes must be removed before new recipes are added or we
    // end up removing explicitly added recipes
    VanillaFurnaceRecipesRemove.apply();
    VanillaFurnaceRecipesAdd.apply();
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
  public void onClientInitializationEvent(FMLInitializationEvent event) {

    super.onClientInitializationEvent(event);

    BlockInitializer.onClientInitialization();

    BlockRenderer blockRenderer = new BlockRenderer(new BulkRenderItemSupplier());
    MinecraftForge.EVENT_BUS.register(new RenderTickEventHandler(Collections.singletonList(blockRenderer)));
    ClientCommandHandler.instance.registerCommand(new ClientCommandExport());
    ClientCommandHandler.instance.registerCommand(new ClientCommandLang());

    new Injector().inject(ModuleCore.Utils.class, "BLOCK_RENDERER", blockRenderer);
  }

  @Override
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    super.onPostInitializationEvent(event);

    if (!Loader.isModLoaded("crafttweaker")) {
      this.onPostInitializationPreCrT();
    }
  }

  /**
   * If CrT is not installed, this is called in this mod's post-init.
   * If CrT is installed, this is called during the CrT ActionApplyEvent, immediately
   * before CrT recipe processing is done. Do not register anything to registries in
   * this method; will cause log warning vomit.
   */
  public void onPostInitializationPreCrT() {

    Path configurationPath = this.getConfigurationDirectory().toPath();
    CompatInitializerWood.create(configurationPath);
    CompatInitializerOre.create(configurationPath);

    VanillaCraftingRecipesRemove.apply(ForgeRegistries.RECIPES);
  }

  public static class Utils {

    public static final BlockRenderer BLOCK_RENDERER;

    static {
      BLOCK_RENDERER = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockLogPile.NAME)
    public static final BlockLogPile LOG_PILE;

    @GameRegistry.ObjectHolder(BlockCoalCokeBlock.NAME)
    public static final BlockCoalCokeBlock COAL_COKE_BLOCK;

    @GameRegistry.ObjectHolder(BlockThatch.NAME)
    public static final BlockThatch THATCH;

    @GameRegistry.ObjectHolder(BlockRefractoryBrick.NAME)
    public static final BlockRefractoryBrick REFRACTORY_BRICK;

    @GameRegistry.ObjectHolder(BlockRefractoryBricksStairs.NAME)
    public static final BlockRefractoryBricksStairs REFRACTORY_BRICKS_STAIRS;

    @GameRegistry.ObjectHolder(BlockRefractoryDoor.NAME)
    public static final BlockRefractoryDoor REFRACTORY_DOOR;

    @GameRegistry.ObjectHolder(BlockStoneDoor.NAME)
    public static final BlockStoneDoor STONE_DOOR;

    @GameRegistry.ObjectHolder(BlockLimestone.NAME)
    public static final BlockLimestone LIMESTONE;

    @GameRegistry.ObjectHolder(BlockRefractoryGlass.NAME)
    public static final BlockRefractoryGlass REFRACTORY_GLASS;

    @GameRegistry.ObjectHolder(BlockSlagGlass.NAME)
    public static final BlockSlagGlass SLAG_GLASS;

    @GameRegistry.ObjectHolder(BlockRock.NAME)
    public static final BlockRock ROCK;

    @GameRegistry.ObjectHolder(BlockRockGrass.NAME)
    public static final BlockRockGrass ROCK_GRASS;

    @GameRegistry.ObjectHolder(BlockRockNetherrack.NAME)
    public static final BlockRockNetherrack ROCK_NETHERRACK;

    @GameRegistry.ObjectHolder(BlockOreFossil.NAME)
    public static final BlockOreFossil ORE_FOSSIL;

    @GameRegistry.ObjectHolder(BlockOreDenseCoal.NAME)
    public static final BlockOreDenseCoal ORE_DENSE_COAL;

    @GameRegistry.ObjectHolder(BlockOreDenseNetherCoal.NAME)
    public static final BlockOreDenseNetherCoal ORE_DENSE_NETHER_COAL;

    @GameRegistry.ObjectHolder(BlockCobblestone.NAME)
    public static final BlockCobblestone COBBLESTONE;

    @GameRegistry.ObjectHolder(BlockStoneBricks.NAME)
    public static final BlockStoneBricks STONE_BRICKS;

    @GameRegistry.ObjectHolder(BlockStoneBricksSlab.Half.NAME)
    public static final BlockStoneBricksSlab.Half STONE_BRICKS_SLAB;

    @GameRegistry.ObjectHolder(BlockStoneBricksStairs.NAME)
    public static final BlockStoneBricksStairs STONE_BRICKS_STAIRS;

    @GameRegistry.ObjectHolder(BlockFarmlandMulched.NAME)
    public static final BlockFarmlandMulched FARMLAND_MULCHED;

    @GameRegistry.ObjectHolder(BlockPlanksTarred.NAME)
    public static final BlockPlanksTarred PLANKS_TARRED;

    @GameRegistry.ObjectHolder(BlockPileWoodChips.NAME)
    public static final BlockPileWoodChips PILE_WOOD_CHIPS;

    @GameRegistry.ObjectHolder(BlockPileAsh.NAME)
    public static final BlockPileAsh PILE_ASH;

    @GameRegistry.ObjectHolder(BlockWoolTarred.NAME)
    public static final BlockWoolTarred WOOL_TARRED;

    @GameRegistry.ObjectHolder(BlockCharcoalBlock.NAME)
    public static final BlockCharcoalBlock CHARCOAL_BLOCK;

    @GameRegistry.ObjectHolder(BlockWoodTarBlock.NAME)
    public static final BlockWoodTarBlock WOOD_TAR_BLOCK;

    @GameRegistry.ObjectHolder(BlockLivingTar.NAME)
    public static final BlockLivingTar LIVING_TAR;

    @GameRegistry.ObjectHolder(BlockOreDenseRedstoneLarge.NAME)
    public static final BlockOreDenseRedstoneLarge ORE_DENSE_REDSTONE_LARGE;

    @GameRegistry.ObjectHolder(BlockOreDenseRedstoneLarge.NAME_ACTIVATED)
    public static final BlockOreDenseRedstoneLarge ORE_DENSE_REDSTONE_LARGE_ACTIVATED;

    @GameRegistry.ObjectHolder(BlockOreDenseRedstoneSmall.NAME)
    public static final BlockOreDenseRedstoneSmall ORE_DENSE_REDSTONE_SMALL;

    @GameRegistry.ObjectHolder(BlockOreDenseRedstoneSmall.NAME_ACTIVATED)
    public static final BlockOreDenseRedstoneSmall ORE_DENSE_REDSTONE_SMALL_ACTIVATED;

    @GameRegistry.ObjectHolder(BlockOreDenseRedstoneRocks.NAME)
    public static final BlockOreDenseRedstoneRocks ORE_DENSE_REDSTONE_ROCKS;

    @GameRegistry.ObjectHolder(BlockOreDenseRedstoneRocks.NAME_ACTIVATED)
    public static final BlockOreDenseRedstoneRocks ORE_DENSE_REDSTONE_ROCKS_ACTIVATED;

    @GameRegistry.ObjectHolder(BlockOreDenseQuartzLarge.NAME)
    public static final BlockOreDenseQuartzLarge ORE_DENSE_QUARTZ_LARGE;

    @GameRegistry.ObjectHolder(BlockOreDenseQuartzSmall.NAME)
    public static final BlockOreDenseQuartzSmall ORE_DENSE_QUARTZ_SMALL;

    @GameRegistry.ObjectHolder(BlockOreDenseQuartzRocks.NAME)
    public static final BlockOreDenseQuartzRocks ORE_DENSE_QUARTZ_ROCKS;

    @GameRegistry.ObjectHolder(BlockStrawBed.NAME)
    public static final BlockStrawBed STRAW_BED;

    static {
      LOG_PILE = null;
      COAL_COKE_BLOCK = null;
      THATCH = null;
      REFRACTORY_BRICK = null;
      REFRACTORY_BRICKS_STAIRS = null;
      REFRACTORY_DOOR = null;
      STONE_DOOR = null;
      LIMESTONE = null;
      REFRACTORY_GLASS = null;
      SLAG_GLASS = null;
      ROCK = null;
      ROCK_GRASS = null;
      ROCK_NETHERRACK = null;
      ORE_FOSSIL = null;
      ORE_DENSE_COAL = null;
      ORE_DENSE_NETHER_COAL = null;
      COBBLESTONE = null;
      STONE_BRICKS = null;
      STONE_BRICKS_SLAB = null;
      STONE_BRICKS_STAIRS = null;
      FARMLAND_MULCHED = null;
      PLANKS_TARRED = null;
      PILE_WOOD_CHIPS = null;
      PILE_ASH = null;
      WOOL_TARRED = null;
      CHARCOAL_BLOCK = null;
      WOOD_TAR_BLOCK = null;
      LIVING_TAR = null;
      ORE_DENSE_REDSTONE_LARGE = null;
      ORE_DENSE_REDSTONE_LARGE_ACTIVATED = null;
      ORE_DENSE_REDSTONE_SMALL = null;
      ORE_DENSE_REDSTONE_SMALL_ACTIVATED = null;
      ORE_DENSE_REDSTONE_ROCKS = null;
      ORE_DENSE_REDSTONE_ROCKS_ACTIVATED = null;
      ORE_DENSE_QUARTZ_LARGE = null;
      ORE_DENSE_QUARTZ_SMALL = null;
      ORE_DENSE_QUARTZ_ROCKS = null;
      STRAW_BED = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(BlockRock.NAME)
    public static final ItemRock ROCK;

    @GameRegistry.ObjectHolder(BlockRockGrass.NAME)
    public static final ItemRockGrass ROCK_GRASS;

    @GameRegistry.ObjectHolder(BlockRockNetherrack.NAME)
    public static final ItemRockNetherrack ROCK_NETHERRACK;

    @GameRegistry.ObjectHolder(ItemMaterial.NAME)
    public static final ItemMaterial MATERIAL;

    @GameRegistry.ObjectHolder(BlockRefractoryDoor.NAME)
    public static final ItemDoor REFRACTORY_DOOR;

    @GameRegistry.ObjectHolder(BlockStoneDoor.NAME)
    public static final ItemDoor STONE_DOOR;

    @GameRegistry.ObjectHolder(ItemMulch.NAME)
    public static final ItemMulch MULCH;

    @GameRegistry.ObjectHolder(ItemAppleBaked.NAME)
    public static final ItemAppleBaked APPLE_BAKED;

    @GameRegistry.ObjectHolder(ItemCarrotRoasted.NAME)
    public static final ItemCarrotRoasted CARROT_ROASTED;

    @GameRegistry.ObjectHolder(ItemEggRoasted.NAME)
    public static final ItemEggRoasted EGG_ROASTED;

    @GameRegistry.ObjectHolder(ItemMushroomBrownRoasted.NAME)
    public static final ItemMushroomBrownRoasted MUSHROOM_BROWN_ROASTED;

    @GameRegistry.ObjectHolder(ItemMushroomRedRoasted.NAME)
    public static final ItemMushroomRedRoasted MUSHROOM_RED_ROASTED;

    @GameRegistry.ObjectHolder(ItemBeetrootRoasted.NAME)
    public static final ItemBeetrootRoasted BEETROOT_ROASTED;

    @GameRegistry.ObjectHolder(ItemBurnedFood.NAME)
    public static final ItemBurnedFood BURNED_FOOD;

    @GameRegistry.ObjectHolder(ItemStrangeTuber.NAME)
    public static final ItemStrangeTuber STRANGE_TUBER;

    @GameRegistry.ObjectHolder(ItemBoneHammer.NAME)
    public static final ItemBoneHammer BONE_HAMMER;

    @GameRegistry.ObjectHolder(ItemDiamondHammer.NAME)
    public static final ItemDiamondHammer DIAMOND_HAMMER;

    @GameRegistry.ObjectHolder(ItemFlintHammer.NAME)
    public static final ItemFlintHammer FLINT_HAMMER;

    @GameRegistry.ObjectHolder(ItemIronHammer.NAME)
    public static final ItemIronHammer IRON_HAMMER;

    @GameRegistry.ObjectHolder(ItemGoldHammer.NAME)
    public static final ItemGoldHammer GOLD_HAMMER;

    @GameRegistry.ObjectHolder(ItemStoneHammer.NAME)
    public static final ItemStoneHammer STONE_HAMMER;

    @GameRegistry.ObjectHolder(ItemCrudeHammer.NAME)
    public static final ItemCrudeHammer CRUDE_HAMMER;

    @GameRegistry.ObjectHolder(ItemObsidianHammer.NAME)
    public static final ItemObsidianHammer OBSIDIAN_HAMMER;

    @GameRegistry.ObjectHolder(ItemBook.NAME)
    public static final ItemBook BOOK;

    @GameRegistry.ObjectHolder(BlockStrawBed.NAME)
    public static final ItemStrawBed STRAW_BED;

    static {
      ROCK = null;
      ROCK_GRASS = null;
      ROCK_NETHERRACK = null;
      MATERIAL = null;
      REFRACTORY_DOOR = null;
      STONE_DOOR = null;
      MULCH = null;
      APPLE_BAKED = null;
      CARROT_ROASTED = null;
      EGG_ROASTED = null;
      MUSHROOM_BROWN_ROASTED = null;
      MUSHROOM_RED_ROASTED = null;
      BEETROOT_ROASTED = null;
      BURNED_FOOD = null;
      STRANGE_TUBER = null;
      BONE_HAMMER = null;
      DIAMOND_HAMMER = null;
      FLINT_HAMMER = null;
      IRON_HAMMER = null;
      GOLD_HAMMER = null;
      STONE_HAMMER = null;
      CRUDE_HAMMER = null;
      OBSIDIAN_HAMMER = null;
      BOOK = null;
      STRAW_BED = null;
    }
  }

  public static class Fluids {

    /*
    Fluids are injected from the fluid initializer.
     */

    public static final Fluid CLAY;

    static {
      CLAY = null;
    }
  }

  public static class Materials {

    public static final Item.ToolMaterial REDSTONE = EnumHelper.addToolMaterial("pyrotech:redstone", 1, 200, 2.8f, 1.0f, 9);
    public static final Item.ToolMaterial QUARTZ = EnumHelper.addToolMaterial("pyrotech:quartz", 1, 350, 3.2f, 1.0f, 2);
  }

  public static class Sounds {

    public static final SoundEvent DENSE_REDSTONE_ORE_ACTIVATE_00;
    public static final SoundEvent DENSE_REDSTONE_ORE_ACTIVATE_01;
    public static final SoundEvent DENSE_REDSTONE_ORE_ACTIVATE_02;
    public static final SoundEvent DENSE_REDSTONE_ORE_ACTIVATE_03;
    public static final SoundEvent DENSE_REDSTONE_ORE_ACTIVATE_04;
    public static final SoundEvent DENSE_REDSTONE_ORE_ACTIVATE_05;
    public static final SoundEvent DENSE_REDSTONE_ORE_ACTIVATE_06;
    public static final SoundEvent DENSE_REDSTONE_ORE_ACTIVATE_07;

    public static final SoundEvent REDSTONE_TOOL_ACTIVATE_00;
    public static final SoundEvent REDSTONE_TOOL_ACTIVATE_01;
    public static final SoundEvent REDSTONE_TOOL_ACTIVATE_02;
    public static final SoundEvent REDSTONE_TOOL_ACTIVATE_03;
    public static final SoundEvent REDSTONE_TOOL_ACTIVATE_04;
    public static final SoundEvent REDSTONE_TOOL_ACTIVATE_05;

    static {
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "dense_redstone_ore_activate_00");
        DENSE_REDSTONE_ORE_ACTIVATE_00 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "dense_redstone_ore_activate_01");
        DENSE_REDSTONE_ORE_ACTIVATE_01 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "dense_redstone_ore_activate_02");
        DENSE_REDSTONE_ORE_ACTIVATE_02 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "dense_redstone_ore_activate_03");
        DENSE_REDSTONE_ORE_ACTIVATE_03 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "dense_redstone_ore_activate_04");
        DENSE_REDSTONE_ORE_ACTIVATE_04 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "dense_redstone_ore_activate_05");
        DENSE_REDSTONE_ORE_ACTIVATE_05 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "dense_redstone_ore_activate_06");
        DENSE_REDSTONE_ORE_ACTIVATE_06 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "dense_redstone_ore_activate_07");
        DENSE_REDSTONE_ORE_ACTIVATE_07 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }

      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "redstone_tool_activate_00");
        REDSTONE_TOOL_ACTIVATE_00 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "redstone_tool_activate_01");
        REDSTONE_TOOL_ACTIVATE_01 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "redstone_tool_activate_02");
        REDSTONE_TOOL_ACTIVATE_02 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "redstone_tool_activate_03");
        REDSTONE_TOOL_ACTIVATE_03 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "redstone_tool_activate_04");
        REDSTONE_TOOL_ACTIVATE_04 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
      {
        ResourceLocation resourceLocation = new ResourceLocation(ModuleCore.MOD_ID, "redstone_tool_activate_05");
        REDSTONE_TOOL_ACTIVATE_05 = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
      }
    }
  }
}
