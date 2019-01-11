package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.TESRTarCollector;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.TESRInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.*;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModuleBlocks {

  public static final BlockLogPile LOG_PILE = new BlockLogPile();
  public static final BlockCoalCokeBlock COAL_COKE_BLOCK = new BlockCoalCokeBlock();
  public static final BlockThatch THATCH = new BlockThatch();
  public static final BlockTarCollector TAR_COLLECTOR = new BlockTarCollector();
  public static final BlockTarDrain TAR_DRAIN = new BlockTarDrain();
  public static final BlockRefractoryBrick REFRACTORY_BRICK = new BlockRefractoryBrick();
  public static final BlockKilnPit KILN_PIT = new BlockKilnPit();
  public static final BlockIgniter IGNITER = new BlockIgniter();
  public static final BlockPitAsh PIT_ASH_BLOCK = new BlockPitAsh();
  public static final BlockActivePile ACTIVE_PILE = new BlockActivePile();
  public static final BlockRefractoryDoor REFRACTORY_DOOR = new BlockRefractoryDoor();
  public static final BlockLimestone LIMESTONE = new BlockLimestone();
  public static final BlockRefractoryGlass REFRACTORY_GLASS = new BlockRefractoryGlass();
  public static final BlockKilnStone KILN_STONE = new BlockKilnStone();
  public static final BlockOvenStone OVEN_STONE = new BlockOvenStone();
  public static final BlockMillStone MILL_STONE = new BlockMillStone();
  public static final BlockCrucibleStone CRUCIBLE_STONE = new BlockCrucibleStone();
  public static final BlockCampfire CAMPFIRE = new BlockCampfire();
  public static final BlockDryingRack DRYING_RACK = new BlockDryingRack();
  public static final BlockRock ROCK = new BlockRock();
  public static final BlockRockGrass ROCK_GRASS = new BlockRockGrass();
  public static final BlockChoppingBlock CHOPPING_BLOCK = new BlockChoppingBlock();
  public static final BlockWoodRack WOOD_RACK = new BlockWoodRack();
  public static final BlockOre ORE = new BlockOre();
  public static final BlockCobblestone COBBLESTONE = new BlockCobblestone();
  public static final BlockStoneBricks STONE_BRICKS = new BlockStoneBricks();
  public static final BlockGraniteAnvil GRANITE_ANVIL = new BlockGraniteAnvil();
  public static final BlockWorktable WORKTABLE = new BlockWorktable();
  public static final BlockWorktableStone WORKTABLE_STONE = new BlockWorktableStone();
  public static final BlockTorchFiber TORCH_FIBER = new BlockTorchFiber();
  public static final BlockShelf SHELF = new BlockShelf();
  public static final BlockShelfStone SHELF_STONE = new BlockShelfStone();
  public static final BlockStash STASH = new BlockStash();
  public static final BlockStashStone STASH_STONE = new BlockStashStone();
  public static final BlockCrate CRATE = new BlockCrate();
  public static final BlockCrateStone CRATE_STONE = new BlockCrateStone();
  public static final BlockFarmlandMulched FARMLAND_MULCHED = new BlockFarmlandMulched();
  public static final BlockCompactingBin COMPACTING_BIN = new BlockCompactingBin();
  public static final BlockSoakingPot SOAKING_POT = new BlockSoakingPot();
  public static final BlockPlanksTarred PLANKS_TARRED = new BlockPlanksTarred();

  public static void onRegister(Registry registry) {

    registry.registerBlock(ModuleBlocks.ACTIVE_PILE, BlockActivePile.NAME);
    registry.registerBlock(ModuleBlocks.PIT_ASH_BLOCK, BlockPitAsh.NAME);
    registry.registerBlock(ModuleBlocks.KILN_PIT, BlockKilnPit.NAME);
    registry.registerBlock(ModuleBlocks.REFRACTORY_DOOR, BlockRefractoryDoor.NAME);
    registry.registerBlock(ModuleBlocks.CAMPFIRE, BlockCampfire.NAME);
    registry.registerBlock(ModuleBlocks.ROCK, BlockRock.NAME);
    registry.registerBlock(ModuleBlocks.ROCK_GRASS, BlockRockGrass.NAME);

    registry.registerBlockWithItem(ModuleBlocks.LOG_PILE, BlockLogPile.NAME);
    registry.registerBlockWithItem(ModuleBlocks.COAL_COKE_BLOCK, BlockCoalCokeBlock.NAME);
    registry.registerBlockWithItem(ModuleBlocks.THATCH, BlockThatch.NAME);
    registry.registerBlockWithItem(ModuleBlocks.TAR_COLLECTOR, BlockTarCollector.NAME);
    registry.registerBlockWithItem(ModuleBlocks.TAR_DRAIN, BlockTarDrain.NAME);
    registry.registerBlockWithItem(ModuleBlocks.REFRACTORY_BRICK, BlockRefractoryBrick.NAME);
    registry.registerBlockWithItem(ModuleBlocks.IGNITER, BlockIgniter.NAME);
    registry.registerBlockWithItem(ModuleBlocks.LIMESTONE, BlockLimestone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.REFRACTORY_GLASS, BlockRefractoryGlass.NAME);
    registry.registerBlockWithItem(ModuleBlocks.KILN_STONE, BlockKilnStone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.OVEN_STONE, BlockOvenStone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.MILL_STONE, BlockMillStone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.CRUCIBLE_STONE, BlockCrucibleStone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.DRYING_RACK, BlockDryingRack.NAME);
    registry.registerBlockWithItem(ModuleBlocks.WOOD_RACK, BlockWoodRack.NAME);
    registry.registerBlockWithItem(ModuleBlocks.ORE, BlockOre.NAME);
    registry.registerBlockWithItem(ModuleBlocks.COBBLESTONE, BlockCobblestone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.STONE_BRICKS, BlockStoneBricks.NAME);
    registry.registerBlockWithItem(ModuleBlocks.WORKTABLE, BlockWorktable.NAME);
    registry.registerBlockWithItem(ModuleBlocks.WORKTABLE_STONE, BlockWorktableStone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.TORCH_FIBER, BlockTorchFiber.NAME);
    registry.registerBlockWithItem(ModuleBlocks.SHELF, BlockShelf.NAME);
    registry.registerBlockWithItem(ModuleBlocks.SHELF_STONE, BlockShelfStone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.STASH, BlockStash.NAME);
    registry.registerBlockWithItem(ModuleBlocks.STASH_STONE, BlockStashStone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.CRATE, BlockCrate.NAME);
    registry.registerBlockWithItem(ModuleBlocks.CRATE_STONE, BlockCrateStone.NAME);
    registry.registerBlockWithItem(ModuleBlocks.FARMLAND_MULCHED, BlockFarmlandMulched.NAME);
    registry.registerBlockWithItem(ModuleBlocks.COMPACTING_BIN, BlockCompactingBin.NAME);
    registry.registerBlockWithItem(ModuleBlocks.SOAKING_POT, BlockSoakingPot.NAME);
    registry.registerBlockWithItem(ModuleBlocks.PLANKS_TARRED, BlockPlanksTarred.NAME);

    registry.registerBlock(ModuleBlocks.CHOPPING_BLOCK, new BlockChoppingBlock.ItemChoppingBlock(ModuleBlocks.CHOPPING_BLOCK), BlockChoppingBlock.NAME);
    registry.registerBlock(ModuleBlocks.GRANITE_ANVIL, new BlockGraniteAnvil.ItemGraniteAnvil(ModuleBlocks.GRANITE_ANVIL), BlockGraniteAnvil.NAME);

    registry.registerTileEntities(
        TileTarCollector.class,
        TileTarDrain.class,
        TileKilnPit.class,
        TilePitAsh.class,
        TileActivePile.class,
        TileKilnStone.class,
        TileOvenStone.class,
        TileMillStone.class,
        TileMillStoneTop.class,
        TileCrucibleStone.class,
        TileStoneTop.class,
        TileCampfire.class,
        TileDryingRack.class,
        TileDryingRackCrude.class,
        TileChoppingBlock.class,
        TileWoodRack.class,
        TileGraniteAnvil.class,
        TileWorktable.class,
        TileWorktableStone.class,
        TileTorch.class,
        TileShelf.class,
        TileShelfStone.class,
        TileStash.class,
        TileStashStone.class,
        TileCrate.class,
        TileCrateStone.class,
        TileFarmlandMulched.class,
        TileCompactingBin.class,
        TileSoakingPot.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleBlocks.LOG_PILE,
          ModuleBlocks.COAL_COKE_BLOCK,
          ModuleBlocks.THATCH,
          ModuleBlocks.REFRACTORY_BRICK,
          ModuleBlocks.LIMESTONE,
          ModuleBlocks.REFRACTORY_GLASS,
          ModuleBlocks.OVEN_STONE,
          ModuleBlocks.KILN_STONE,
          ModuleBlocks.OVEN_STONE,
          ModuleBlocks.MILL_STONE,
          ModuleBlocks.CRUCIBLE_STONE,
          ModuleBlocks.WOOD_RACK,
          ModuleBlocks.STONE_BRICKS,
          ModuleBlocks.WORKTABLE,
          ModuleBlocks.WORKTABLE_STONE,
          ModuleBlocks.TORCH_FIBER,
          ModuleBlocks.SHELF,
          ModuleBlocks.SHELF_STONE,
          ModuleBlocks.STASH,
          ModuleBlocks.STASH_STONE,
          ModuleBlocks.CRATE,
          ModuleBlocks.CRATE_STONE,
          ModuleBlocks.FARMLAND_MULCHED,
          ModuleBlocks.COMPACTING_BIN,
          ModuleBlocks.SOAKING_POT,
          ModuleBlocks.PLANKS_TARRED
      );

      // Refractory Door
      ModelLoader.setCustomStateMapper(
          ModuleBlocks.REFRACTORY_DOOR,
          (new StateMap.Builder()).ignore(BlockDoor.POWERED).build()
      );

      // Pit Kiln
      ModelRegistrationHelper.registerBlockItemModel(ModuleBlocks.KILN_PIT.getDefaultState()
          .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.EMPTY));

      // Tar Collector
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.TAR_COLLECTOR.getDefaultState(),
          BlockTarCollector.VARIANT
      );

      // Tar Drain
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.TAR_DRAIN.getDefaultState(),
          BlockTarDrain.VARIANT
      );

      // Igniter
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.IGNITER.getDefaultState(),
          BlockIgniter.VARIANT
      );

      // Drying Rack
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.DRYING_RACK.getDefaultState(),
          BlockDryingRack.VARIANT
      );

      // Rock Pieces
      ModelLoader.setCustomStateMapper(
          ModuleBlocks.ROCK,
          (new StateMap.Builder()).withName(BlockRock.VARIANT).build()
      );

      // Chopping Block
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.CHOPPING_BLOCK.getDefaultState(),
          BlockChoppingBlock.DAMAGE,
          value -> value
      );

      // Granite Anvil
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.GRANITE_ANVIL.getDefaultState(),
          BlockGraniteAnvil.DAMAGE,
          value -> value
      );

      // Cobblestone
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.COBBLESTONE.getDefaultState(),
          BlockCobblestone.VARIANT
      );

      // Ore
      ModelLoader.setCustomStateMapper(
          ModuleBlocks.ORE,
          (new StateMap.Builder()).withName(BlockOre.VARIANT).build()
      );
      ModelRegistrationHelper.registerVariantBlockItemModelsSeparately(
          ModulePyrotech.MOD_ID,
          ModuleBlocks.ORE,
          BlockOre.VARIANT
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileTarCollector.class, new TESRTarCollector());

      ClientRegistry.bindTileEntitySpecialRenderer(TileKilnPit.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileKilnStone.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileOvenStone.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileMillStone.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCrucibleStone.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRack.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRackCrude.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileChoppingBlock.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileWoodRack.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileGraniteAnvil.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileWorktable.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileShelf.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStash.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCrate.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCompactingBin.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileSoakingPot.class, new TESRInteractable<>());
    });
  }

  @SideOnly(Side.CLIENT)
  public static void onClientInitialization() {

    // -------------------------------------------------------------------------
    // - Block Colors
    // -------------------------------------------------------------------------

    Minecraft minecraft = Minecraft.getMinecraft();
    BlockColors blockColors = minecraft.getBlockColors();

    // Grass Block
    blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {

      if (world != null && pos != null) {
        BiomeColorHelper.getGrassColorAtPos(world, pos);
      }

      return ColorizerGrass.getGrassColor(0.5D, 1.0D);

    }, ModuleBlocks.ROCK_GRASS);
  }

  private ModuleBlocks() {
    //
  }
}
