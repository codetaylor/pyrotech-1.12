package com.codetaylor.mc.pyrotech.modules.tech.refractory.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockActivePile;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockPitAsh;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockTarCollector;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.block.BlockTarDrain;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.client.render.TESRTarCollector;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileActivePile;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TilePitAsh;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileTarCollector;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileTarDrain;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockInitializer {

  public static void onRegister(Registry registry) {

    registry.registerBlock(new BlockActivePile(), BlockActivePile.NAME);
    registry.registerBlock(new BlockPitAsh(), BlockPitAsh.NAME);

    registry.registerBlockWithItem(new BlockTarCollector(), BlockTarCollector.NAME);
    registry.registerBlockWithItem(new BlockTarDrain(), BlockTarDrain.NAME);

    registry.registerTileEntities(
        TileTarCollector.class,
        TileTarDrain.class,
        TilePitAsh.class,
        TileActivePile.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      // Tar Collector
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleTechRefractory.Blocks.TAR_COLLECTOR.getDefaultState(),
          BlockTarCollector.VARIANT
      );

      // Tar Drain
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleTechRefractory.Blocks.TAR_DRAIN.getDefaultState(),
          BlockTarDrain.VARIANT
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileTarCollector.class, new TESRTarCollector());
    });
  }

  private BlockInitializer() {
    //
  }
}
