package com.codetaylor.mc.pyrotech.modules.storage.init;

import com.codetaylor.mc.athenaeum.interaction.spi.TESRInteractable;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.library.util.RegistryHelper;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.block.*;
import com.codetaylor.mc.pyrotech.modules.storage.block.item.ItemBlockBag;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockBagBase;
import com.codetaylor.mc.pyrotech.modules.storage.client.render.TESRFaucet;
import com.codetaylor.mc.pyrotech.modules.storage.client.render.TESRTank;
import com.codetaylor.mc.pyrotech.modules.storage.tile.*;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileFaucetBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;
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
    registry.registerBlockWithItem(new BlockTankStone(), BlockTankStone.NAME);
    registry.registerBlockWithItem(new BlockTankBrick(), BlockTankBrick.NAME);
    registry.registerBlockWithItem(new BlockFaucetStone(), BlockFaucetStone.NAME);
    registry.registerBlockWithItem(new BlockFaucetBrick(), BlockFaucetBrick.NAME);

    BlockBagSimple blockBagSimple = new BlockBagSimple();
    registry.registerBlock(blockBagSimple, new ItemBlockBag(blockBagSimple), BlockBagSimple.NAME);

    BlockBagDurable blockBagDurable = new BlockBagDurable();
    registry.registerBlock(blockBagDurable, new ItemBlockBag(blockBagDurable), BlockBagDurable.NAME);

    RegistryHelper.registerTileEntities(
        registry,
        TileShelf.class,
        TileShelfStone.class,
        TileStash.class,
        TileStashStone.class,
        TileCrate.class,
        TileCrateStone.class,
        TileWoodRack.class,
        TileTankStone.class,
        TileTankBrick.class,
        TileBagSimple.class,
        TileBagDurable.class,
        TileFaucetStone.class,
        TileFaucetBrick.class
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
          ModuleStorage.Blocks.BAG_SIMPLE,
          ModuleStorage.Blocks.BAG_DURABLE,
          ModuleStorage.Blocks.FAUCET_STONE,
          ModuleStorage.Blocks.FAUCET_BRICK,
          ModuleStorage.Blocks.TANK_STONE,
          ModuleStorage.Blocks.TANK_BRICK
      );

      ModelRegistrationHelper.registerBlockItemModelForMeta(
          ModuleStorage.Blocks.BAG_SIMPLE.getDefaultState().withProperty(BlockBagBase.TYPE, BlockBagBase.EnumType.OPEN),
          1
      );

      ModelRegistrationHelper.registerBlockItemModelForMeta(
          ModuleStorage.Blocks.BAG_DURABLE.getDefaultState().withProperty(BlockBagBase.TYPE, BlockBagBase.EnumType.OPEN),
          1
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileTankBase.class, new TESRTank());
      ClientRegistry.bindTileEntitySpecialRenderer(TileFaucetBase.class, new TESRFaucet());

      ClientRegistry.bindTileEntitySpecialRenderer(TileShelf.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStash.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCrate.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileWoodRack.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileBagSimple.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileBagDurable.class, new TESRInteractable<>());
    });
  }

  private BlockInitializer() {
    //
  }
}
