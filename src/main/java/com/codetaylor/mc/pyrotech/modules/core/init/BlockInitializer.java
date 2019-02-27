package com.codetaylor.mc.pyrotech.modules.core.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.*;
import com.codetaylor.mc.pyrotech.modules.core.block.item.ItemDoorStone;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemRock;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemRockGrass;
import com.codetaylor.mc.pyrotech.modules.core.tile.TileFarmlandMulched;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockInitializer {

  public static void onRegister(Registry registry) {

    final BlockRock blockRock = new BlockRock();
    final ItemRock itemRock = new ItemRock(blockRock);
    registry.registerBlock(blockRock, itemRock, BlockRock.NAME);

    final BlockRockGrass blockRockGrass = new BlockRockGrass();
    ItemRockGrass itemRockGrass = new ItemRockGrass(blockRockGrass);
    registry.registerBlock(blockRockGrass, itemRockGrass, BlockRockGrass.NAME);

    final BlockRefractoryDoor blockRefractoryDoor = new BlockRefractoryDoor();
    registry.registerBlock(blockRefractoryDoor, BlockRefractoryDoor.NAME);
    registry.registerItem(new ItemDoorStone(blockRefractoryDoor), blockRefractoryDoor.getRegistryName());

    final BlockStoneDoor blockStoneDoor = new BlockStoneDoor();
    registry.registerBlock(blockStoneDoor, BlockStoneDoor.NAME);
    registry.registerItem(new ItemDoorStone(blockStoneDoor), blockStoneDoor.getRegistryName());

    registry.registerBlockWithItem(new BlockLogPile(), BlockLogPile.NAME);
    registry.registerBlockWithItem(new BlockCoalCokeBlock(), BlockCoalCokeBlock.NAME);
    registry.registerBlockWithItem(new BlockThatch(), BlockThatch.NAME);
    registry.registerBlockWithItem(new BlockRefractoryBrick(), BlockRefractoryBrick.NAME);
    registry.registerBlockWithItem(new BlockLimestone(), BlockLimestone.NAME);
    registry.registerBlockWithItem(new BlockRefractoryGlass(), BlockRefractoryGlass.NAME);
    registry.registerBlockWithItem(new BlockSlagGlass(), BlockSlagGlass.NAME);
    registry.registerBlockWithItem(new BlockOre(), BlockOre.NAME);
    registry.registerBlockWithItem(new BlockCobblestone(), BlockCobblestone.NAME);
    registry.registerBlockWithItem(new BlockStoneBricks(), BlockStoneBricks.NAME);
    registry.registerBlockWithItem(new BlockFarmlandMulched(), BlockFarmlandMulched.NAME);
    registry.registerBlockWithItem(new BlockPlanksTarred(), BlockPlanksTarred.NAME);
    registry.registerBlockWithItem(new BlockPileWoodChips(), BlockPileWoodChips.NAME);
    registry.registerBlockWithItem(new BlockPileAsh(), BlockPileAsh.NAME);
    registry.registerBlockWithItem(new BlockWoolTarred(), BlockWoolTarred.NAME);
    registry.registerBlockWithItem(new BlockCharcoalBlock(), BlockCharcoalBlock.NAME);
    registry.registerBlockWithItem(new BlockWoodTarBlock(), BlockWoodTarBlock.NAME);
    registry.registerBlockWithItem(new BlockLivingTar(), BlockLivingTar.NAME);

    registry.registerTileEntities(
        TileFarmlandMulched.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleCore.Blocks.LOG_PILE,
          ModuleCore.Blocks.COAL_COKE_BLOCK,
          ModuleCore.Blocks.THATCH,
          ModuleCore.Blocks.REFRACTORY_BRICK,
          ModuleCore.Blocks.LIMESTONE,
          ModuleCore.Blocks.REFRACTORY_GLASS,
          ModuleCore.Blocks.SLAG_GLASS,
          ModuleCore.Blocks.STONE_BRICKS,
          ModuleCore.Blocks.FARMLAND_MULCHED,
          ModuleCore.Blocks.PLANKS_TARRED,
          ModuleCore.Blocks.PILE_WOOD_CHIPS,
          ModuleCore.Blocks.PILE_ASH,
          ModuleCore.Blocks.WOOL_TARRED,
          ModuleCore.Blocks.CHARCOAL_BLOCK,
          ModuleCore.Blocks.WOOD_TAR_BLOCK,
          ModuleCore.Blocks.LIVING_TAR
      );

      ModelRegistrationHelper.registerItemModels(
          ModuleCore.Items.REFRACTORY_DOOR,
          ModuleCore.Items.STONE_DOOR,
          ModuleCore.Items.ROCK_GRASS
      );

      // Refractory Door
      ModelLoader.setCustomStateMapper(
          ModuleCore.Blocks.REFRACTORY_DOOR,
          (new StateMap.Builder()).ignore(BlockDoor.POWERED).build()
      );

      // Stone Door
      ModelLoader.setCustomStateMapper(
          ModuleCore.Blocks.STONE_DOOR,
          (new StateMap.Builder()).ignore(BlockDoor.POWERED).build()
      );

      // Cobblestone
      ModelRegistrationHelper.registerVariantBlockItemModels(
          ModuleCore.Blocks.COBBLESTONE.getDefaultState(),
          BlockCobblestone.VARIANT
      );

      // Ore
      ModelLoader.setCustomStateMapper(
          ModuleCore.Blocks.ORE,
          (new StateMap.Builder()).withName(BlockOre.VARIANT).build()
      );
      ModelRegistrationHelper.registerVariantBlockItemModelsSeparately(
          ModuleCore.MOD_ID,
          ModuleCore.Blocks.ORE,
          BlockOre.VARIANT
      );

      // Rock Pieces
      ModelLoader.setCustomStateMapper(
          ModuleCore.Blocks.ROCK,
          (new StateMap.Builder()).withName(BlockRock.VARIANT).build()
      );

      // Rock
      ModelRegistrationHelper.registerVariantBlockItemModelsSeparately(
          ModuleCore.MOD_ID,
          ModuleCore.Blocks.ROCK,
          BlockRock.VARIANT
      );

    });
  }

  @SideOnly(Side.CLIENT)
  public static void onClientInitialization() {

    // -------------------------------------------------------------------------
    // - Block Colors
    // -------------------------------------------------------------------------

    Minecraft minecraft = Minecraft.getMinecraft();
    BlockColors blockColors = minecraft.getBlockColors();

    // Grass Rock
    blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {

      if (world != null && pos != null) {
        BiomeColorHelper.getGrassColorAtPos(world, pos);
      }

      return ColorizerGrass.getGrassColor(0.5D, 1.0D);

    }, ModuleCore.Blocks.ROCK_GRASS);
  }

  private BlockInitializer() {
    //
  }
}
