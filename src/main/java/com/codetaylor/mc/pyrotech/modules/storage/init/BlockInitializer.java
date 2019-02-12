package com.codetaylor.mc.pyrotech.modules.storage.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.TESRInteractable;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.block.*;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockBagBase;
import com.codetaylor.mc.pyrotech.modules.storage.client.render.TESRTank;
import com.codetaylor.mc.pyrotech.modules.storage.tile.*;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockInitializer {

  public static void onRegister(Registry registry) {

    registry.registerBlockWithItem(new BlockShelf(), BlockShelf.NAME);
    registry.registerBlockWithItem(new BlockShelfStone(), BlockShelfStone.NAME);
    registry.registerBlockWithItem(new BlockStash(), BlockStash.NAME);
    registry.registerBlockWithItem(new BlockStashStone(), BlockStashStone.NAME);
    registry.registerBlockWithItem(new BlockCrate(), BlockCrate.NAME);
    registry.registerBlockWithItem(new BlockCrateStone(), BlockCrateStone.NAME);
    registry.registerBlockWithItem(new BlockWoodRack(), BlockWoodRack.NAME);
    registry.registerBlockWithItem(new BlockTank(), BlockTank.NAME);

    BlockBagSimple blockBagSimple = new BlockBagSimple();
    registry.registerBlock(blockBagSimple, new BlockBagBase.Item(blockBagSimple), BlockBagSimple.NAME);

    registry.registerTileEntities(
        TileShelf.class,
        TileShelfStone.class,
        TileStash.class,
        TileStashStone.class,
        TileCrate.class,
        TileCrateStone.class,
        TileWoodRack.class,
        TileTankStone.class,
        TileTankBrick.class,
        TileBagSimple.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleStorage.Blocks.SHELF,
          ModuleStorage.Blocks.SHELF_STONE,
          ModuleStorage.Blocks.STASH,
          ModuleStorage.Blocks.STASH_STONE,
          ModuleStorage.Blocks.CRATE,
          ModuleStorage.Blocks.CRATE_STONE,
          ModuleStorage.Blocks.WOOD_RACK,
          ModuleStorage.Blocks.BAG_SIMPLE
      );

      ModelRegistrationHelper.registerBlockItemModelForMeta(
          ModuleStorage.Blocks.BAG_SIMPLE.getDefaultState().withProperty(BlockBagBase.TYPE, BlockBagBase.EnumType.OPEN),
          1
      );

      // Tank
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleStorage.Blocks.TANK.getDefaultState(),
          BlockTank.TYPE
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileTankBase.class, new TESRTank());

      ClientRegistry.bindTileEntitySpecialRenderer(TileShelf.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStash.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCrate.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileWoodRack.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileBagSimple.class, new TESRInteractable<>());
    });
  }

  private BlockInitializer() {
    //
  }
}
