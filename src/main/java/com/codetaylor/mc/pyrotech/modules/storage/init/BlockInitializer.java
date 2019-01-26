package com.codetaylor.mc.pyrotech.modules.storage.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.TESRInteractable;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.block.*;
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

    registry.registerTileEntities(
        TileShelf.class,
        TileShelfStone.class,
        TileStash.class,
        TileStashStone.class,
        TileCrate.class,
        TileCrateStone.class
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
          ModuleStorage.Blocks.CRATE_STONE
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileShelf.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStash.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCrate.class, new TESRInteractable<>());
    });
  }

  private BlockInitializer() {
    //
  }
}
