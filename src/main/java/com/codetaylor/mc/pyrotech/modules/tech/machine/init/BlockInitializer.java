package com.codetaylor.mc.pyrotech.modules.tech.machine.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.TESRInteractable;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.*;
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

    registry.registerBlockWithItem(new BlockBrickKiln(), BlockBrickKiln.NAME);
    registry.registerBlockWithItem(new BlockBrickOven(), BlockBrickOven.NAME);
    registry.registerBlockWithItem(new BlockBrickSawmill(), BlockBrickSawmill.NAME);
    registry.registerBlockWithItem(new BlockBrickCrucible(), BlockBrickCrucible.NAME);

    registry.registerBlockWithItem(new BlockStoneHopper(), BlockStoneHopper.NAME);

    registry.registerTileEntities(
        TileStoneKiln.class,
        TileStoneKilnTop.class,
        TileStoneOven.class,
        TileStoneOvenTop.class,
        TileStoneSawmill.class,
        TileStoneSawmillTop.class,
        TileStoneCrucible.class,
        TileStoneCrucibleTop.class,

        TileBrickKiln.class,
        TileBrickKilnTop.class,
        TileBrickOven.class,
        TileBrickOvenTop.class,
        TileBrickSawmill.class,
        TileBrickSawmillTop.class,
        TileBrickCrucible.class,
        TileBrickCrucibleTop.class,

        TileStoneHopper.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleTechMachine.Blocks.STONE_KILN,
          ModuleTechMachine.Blocks.STONE_SAWMILL,
          ModuleTechMachine.Blocks.STONE_OVEN,
          ModuleTechMachine.Blocks.STONE_CRUCIBLE,

          ModuleTechMachine.Blocks.BRICK_KILN,
          ModuleTechMachine.Blocks.BRICK_SAWMILL,
          ModuleTechMachine.Blocks.BRICK_OVEN,
          ModuleTechMachine.Blocks.BRICK_CRUCIBLE,

          ModuleTechMachine.Blocks.STONE_HOPPER
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneKiln.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneOven.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneSawmill.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneCrucible.class, new TESRInteractable<>());

      ClientRegistry.bindTileEntitySpecialRenderer(TileBrickKiln.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileBrickOven.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileBrickSawmill.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileBrickCrucible.class, new TESRInteractable<>());

      ClientRegistry.bindTileEntitySpecialRenderer(TileStoneHopper.class, new TESRInteractable<>());
    });
  }

  private BlockInitializer() {
    //
  }
}
