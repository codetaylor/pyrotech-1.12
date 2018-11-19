package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.TESRInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.TESRTarCollector;
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

public class ModuleBlocks {

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
  public static final BlockKilnBrick KILN_BRICK = new BlockKilnBrick();
  public static final BlockCampfire CAMPFIRE = new BlockCampfire();
  public static final BlockDryingRack DRYING_RACK = new BlockDryingRack();
  public static final BlockRock ROCK = new BlockRock();
  public static final BlockRockGrass ROCK_GRASS = new BlockRockGrass();

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
    registry.registerBlockWithItem(ModuleBlocks.KILN_BRICK, BlockKilnBrick.NAME);
    registry.registerBlockWithItem(ModuleBlocks.DRYING_RACK, BlockDryingRack.NAME);

    registry.registerTileEntities(
        TileTarCollector.class,
        TileTarDrain.class,
        TileKilnPit.class,
        TilePitAsh.class,
        TileActivePile.class,
        TileKilnBrick.class,
        TileKilnBrickTop.class,
        TileCampfire.class,
        TileDryingRack.class,
        TileDryingRackCrude.class
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
          ModuleBlocks.KILN_BRICK
      );

      ModelLoader.setCustomStateMapper(
          ModuleBlocks.REFRACTORY_DOOR,
          (new StateMap.Builder()).ignore(BlockDoor.POWERED).build()
      );

      ModelRegistrationHelper.registerBlockItemModel(ModuleBlocks.KILN_PIT.getDefaultState()
          .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.EMPTY));

      // tar collector
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.TAR_COLLECTOR.getDefaultState(),
          BlockTarCollector.VARIANT
      );

      // tar drain
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleBlocks.TAR_DRAIN.getDefaultState(),
          BlockTarDrain.VARIANT
      );

      // igniter
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

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileTarCollector.class, new TESRTarCollector());

      ClientRegistry.bindTileEntitySpecialRenderer(TileKilnPit.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileKilnBrick.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRack.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRackCrude.class, new TESRInteractable<>());

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

}
