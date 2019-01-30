package com.codetaylor.mc.pyrotech.modules.tech.basic.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.TESRInteractable;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockInitializer {

  public static void onRegister(Registry registry) {

    BlockChoppingBlock blockChoppingBlock = new BlockChoppingBlock();
    registry.registerBlock(blockChoppingBlock, new BlockChoppingBlock.ItemChoppingBlock(blockChoppingBlock), BlockChoppingBlock.NAME);
    BlockAnvilGranite blockAnvilGranite = new BlockAnvilGranite();
    registry.registerBlock(blockAnvilGranite, new BlockAnvilBase.ItemAnvil(blockAnvilGranite), BlockAnvilGranite.NAME);
    BlockAnvilIronPlated blockAnvilIronPlated = new BlockAnvilIronPlated();
    registry.registerBlock(blockAnvilIronPlated, new BlockAnvilIronPlated.ItemAnvil(blockAnvilIronPlated), BlockAnvilIronPlated.NAME);

    registry.registerBlockWithItem(new BlockKilnPit(), BlockKilnPit.NAME);
    registry.registerBlockWithItem(new BlockCampfire(), BlockCampfire.NAME);
    registry.registerBlockWithItem(new BlockDryingRack(), BlockDryingRack.NAME);
    registry.registerBlockWithItem(new BlockWorktable(), BlockWorktable.NAME);
    registry.registerBlockWithItem(new BlockWorktableStone(), BlockWorktableStone.NAME);
    registry.registerBlockWithItem(new BlockCompactingBin(), BlockCompactingBin.NAME);
    registry.registerBlockWithItem(new BlockSoakingPot(), BlockSoakingPot.NAME);

    registry.registerTileEntities(
        TileKilnPit.class,
        TileCampfire.class,
        TileDryingRack.class,
        TileDryingRackCrude.class,
        TileChoppingBlock.class,
        TileAnvilGranite.class,
        TileAnvilIronPlated.class,
        TileWorktable.class,
        TileWorktableStone.class,
        TileCompactingBin.class,
        TileSoakingPot.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleTechBasic.Blocks.WORKTABLE,
          ModuleTechBasic.Blocks.WORKTABLE_STONE,
          ModuleTechBasic.Blocks.COMPACTING_BIN,
          ModuleTechBasic.Blocks.SOAKING_POT
      );

      // Pit Kiln
      ModelRegistrationHelper.registerBlockItemModel(ModuleTechBasic.Blocks.KILN_PIT.getDefaultState()
          .withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.EMPTY));

      // Drying Rack
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleTechBasic.Blocks.DRYING_RACK.getDefaultState(),
          BlockDryingRack.VARIANT
      );

      // Chopping Block
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleTechBasic.Blocks.CHOPPING_BLOCK.getDefaultState(),
          BlockChoppingBlock.DAMAGE,
          value -> value
      );

      // Granite Anvil
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleTechBasic.Blocks.ANVIL_GRANITE.getDefaultState(),
          BlockAnvilBase.DAMAGE,
          value -> value
      );

      // Iron Plated Anvil
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleTechBasic.Blocks.ANVIL_IRON_PLATED.getDefaultState(),
          BlockAnvilBase.DAMAGE,
          value -> value
      );

      // Campfire
      ModelRegistrationHelper.registerBlockItemModel(
          ModuleTechBasic.Blocks.CAMPFIRE.getDefaultState()
              .withProperty(BlockCampfire.VARIANT, BlockCampfire.EnumType.ITEM)
      );

      // TESRs
      ClientRegistry.bindTileEntitySpecialRenderer(TileKilnPit.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRack.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRackCrude.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileChoppingBlock.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileAnvilBase.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileWorktable.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileCompactingBin.class, new TESRInteractable<>());
      ClientRegistry.bindTileEntitySpecialRenderer(TileSoakingPot.class, new TESRInteractable<>());
    });
  }

  private BlockInitializer() {
    //
  }
}
