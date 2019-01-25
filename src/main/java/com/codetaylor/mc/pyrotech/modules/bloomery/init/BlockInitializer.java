package com.codetaylor.mc.pyrotech.modules.bloomery.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.TESRInteractable;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.bloomery.block.BlockBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TilePileSlag;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModuleBlocks {

  public static void onRegister(Registry registry) {

    registry.registerBlockWithItem(ModuleBloomery.Blocks.BLOOMERY, BlockBloomery.NAME);

    registry.registerBlock(ModuleBloomery.Blocks.BLOOM, new BlockBloom.ItemBlockBloom(ModuleBloomery.Blocks.BLOOM), BlockBloom.NAME);
    registry.registerBlock(ModuleBloomery.Blocks.PILE_SLAG, new BlockPileSlag.ItemBlockPileSlag(ModuleBloomery.Blocks.PILE_SLAG), BlockPileSlag.NAME);

    registry.registerTileEntities(
        TileBloomery.class,
        TileBloom.class,
        TilePileSlag.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleBloomery.Blocks.BLOOMERY,
          ModuleBloomery.Blocks.PILE_SLAG,
          ModuleBloomery.Blocks.BLOOM
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileBloomery.class, new TESRInteractable<>());
    });
  }

  private ModuleBlocks() {
    //
  }
}
