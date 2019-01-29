package com.codetaylor.mc.pyrotech.modules.tech.machine.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.TESRInteractable;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneCrucible;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneKiln;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneOven;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockStoneSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.*;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockInitializer {

  public static void onRegister(Registry registry) {

    registry.registerBlockWithItem(new BlockStoneKiln(), BlockStoneKiln.NAME);
    registry.registerBlockWithItem(new BlockStoneOven(), BlockStoneOven.NAME);
    registry.registerBlockWithItem(new BlockStoneSawmill(), BlockStoneSawmill.NAME);
    registry.registerBlockWithItem(new BlockStoneCrucible(), BlockStoneCrucible.NAME);

    registry.registerTileEntities(
        TileStoneKiln.class,
        TileStoneKilnTop.class,
        TileStoneOven.class,
        TileStoneOvenTop.class,
        TileStoneSawmill.class,
        TileStoneSawmillTop.class,
        TileStoneCrucible.class,
        TileStoneCrucibleTop.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleTechMachine.Blocks.KILN_STONE,
          ModuleTechMachine.Blocks.MILL_STONE,
          ModuleTechMachine.Blocks.OVEN_STONE,
          ModuleTechMachine.Blocks.CRUCIBLE_STONE
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneKiln.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneOven.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneSawmill.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneCrucible.class, new TESRInteractable<>());
    });
  }

  private BlockInitializer() {
    //
  }
}
