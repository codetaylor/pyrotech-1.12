package com.codetaylor.mc.pyrotech.modules.core;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import com.codetaylor.mc.pyrotech.BulkRenderItemSupplier;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.blockrenderer.BlockRenderer;
import com.codetaylor.mc.pyrotech.library.blockrenderer.RenderTickEventHandler;
import com.codetaylor.mc.pyrotech.modules.core.advancement.AdvancementTriggers;
import com.codetaylor.mc.pyrotech.modules.core.block.*;
import com.codetaylor.mc.pyrotech.modules.core.command.ClientCommandExport;
import com.codetaylor.mc.pyrotech.modules.core.event.TooltipEventHandler;
import com.codetaylor.mc.pyrotech.modules.core.init.*;
import com.codetaylor.mc.pyrotech.modules.core.init.recipe.VanillaCraftingRecipesRemove;
import com.codetaylor.mc.pyrotech.modules.core.init.recipe.VanillaFurnaceRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.core.init.recipe.VanillaFurnaceRecipesRemove;
import com.codetaylor.mc.pyrotech.modules.core.item.*;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;

public class ModuleCore
    extends ModuleBase {

  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleCore.class.getSimpleName());

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleCore() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.core.plugin.jei.PluginJEI"
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

    VanillaCraftingRecipesRemove.apply(event.getRegistry());
    VanillaFurnaceRecipesRemove.apply();
    VanillaFurnaceRecipesAdd.apply();
  }

  @Override
  public void onRegister(Registry registry) {

    FluidInitializer.onRegister(registry);
    BlockInitializer.onRegister(registry);
    ItemInitializer.onRegister(registry);
    OreDictInitializer.onRegister(registry, this.getConfigurationDirectory());
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

    new Injector().inject(ModuleCore.Utils.class, "BLOCK_RENDERER", blockRenderer);
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

    static {
      LOG_PILE = null;
      COAL_COKE_BLOCK = null;
      THATCH = null;
      REFRACTORY_BRICK = null;
      REFRACTORY_DOOR = null;
      STONE_DOOR = null;
      LIMESTONE = null;
      REFRACTORY_GLASS = null;
      SLAG_GLASS = null;
      ROCK = null;
      ROCK_GRASS = null;
      ORE_FOSSIL = null;
      ORE_DENSE_COAL = null;
      ORE_DENSE_NETHER_COAL = null;
      COBBLESTONE = null;
      STONE_BRICKS = null;
      FARMLAND_MULCHED = null;
      PLANKS_TARRED = null;
      PILE_WOOD_CHIPS = null;
      PILE_ASH = null;
      WOOL_TARRED = null;
      CHARCOAL_BLOCK = null;
      WOOD_TAR_BLOCK = null;
      LIVING_TAR = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleCore.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(BlockRockGrass.NAME)
    public static final ItemRockGrass ROCK_GRASS;

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

    static {
      ROCK_GRASS = null;
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
      BONE_HAMMER = null;
      DIAMOND_HAMMER = null;
      FLINT_HAMMER = null;
      IRON_HAMMER = null;
      GOLD_HAMMER = null;
      STONE_HAMMER = null;
      CRUDE_HAMMER = null;
      OBSIDIAN_HAMMER = null;
      BOOK = null;
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
}
