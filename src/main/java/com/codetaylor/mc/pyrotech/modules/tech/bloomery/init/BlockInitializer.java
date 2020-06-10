package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.athenaeum.interaction.spi.TESRInteractable;
import com.codetaylor.mc.pyrotech.library.util.RegistryHelper;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockPileSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockWitherForge;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TilePileSlag;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileWitherForge;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockInitializer {

  public static void onRegister(Registry registry) {

    registry.registerBlockWithItem(new BlockBloomery(), BlockBloomery.NAME);
    registry.registerBlockWithItem(new BlockWitherForge(), BlockWitherForge.NAME);

    {
      BlockPileSlag blockPileSlag = new BlockPileSlag();
      registry.registerBlock(blockPileSlag, new BlockPileSlag.ItemBlockPileSlag(blockPileSlag), BlockPileSlag.NAME);
    }

    {
      BlockBloom blockBloom = new BlockBloom();
      BlockBloom.ItemBlockBloom itemBlock = new BlockBloom.ItemBlockBloom(blockBloom);
      registry.registerBlock(blockBloom, BlockBloom.NAME);
      ResourceLocation registryName = new ResourceLocation(ModuleTechBloomery.MOD_ID, BlockBloom.NAME);
      registry.registerItem(itemBlock, registryName, true);
    }

    RegistryHelper.registerTileEntities(
        registry,
        TileBloomery.class,
        TileBloomery.Top.class,
        TileWitherForge.class,
        TileBloom.class,
        TilePileSlag.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleTechBloomery.Blocks.BLOOMERY,
          ModuleTechBloomery.Blocks.WITHER_FORGE,
          ModuleTechBloomery.Blocks.PILE_SLAG,
          ModuleTechBloomery.Blocks.BLOOM
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileBloomery.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileWitherForge.class, new TESRInteractable<>());
    });
  }

  private BlockInitializer() {
    //
  }
}
