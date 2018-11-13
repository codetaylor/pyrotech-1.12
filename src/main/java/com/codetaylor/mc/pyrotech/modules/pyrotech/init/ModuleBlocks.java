package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.*;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.*;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.block.statemap.StateMap;
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

  public static void onRegister(Registry registry) {

    registry.registerBlock(ModuleBlocks.ACTIVE_PILE, BlockActivePile.NAME);
    registry.registerBlock(ModuleBlocks.PIT_ASH_BLOCK, BlockPitAsh.NAME);
    registry.registerBlock(ModuleBlocks.KILN_PIT, BlockKilnPit.NAME);
    registry.registerBlock(ModuleBlocks.REFRACTORY_DOOR, BlockRefractoryDoor.NAME);
    registry.registerBlock(ModuleBlocks.CAMPFIRE, BlockCampfire.NAME);
    registry.registerBlock(ModuleBlocks.ROCK, BlockRock.NAME);

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
        TileDryingRack.class
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
          ModuleBlocks.KILN_BRICK,
          ModuleBlocks.DRYING_RACK
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

      // Rock Pieces
      ModelLoader.setCustomStateMapper(
          ModuleBlocks.ROCK,
          (new StateMap.Builder()).withName(BlockRock.VARIANT).build()
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileKilnPit.class, new TESRKilnPit());
      ClientRegistry.bindTileEntitySpecialRenderer(TileTarCollector.class, new TESRTarCollector());
      ClientRegistry.bindTileEntitySpecialRenderer(TileKilnBrick.class, new TESRKilnBrick());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire.class, new TESRCampfire());
      ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRack.class, new TESRDryingRack());

    });
  }

}
