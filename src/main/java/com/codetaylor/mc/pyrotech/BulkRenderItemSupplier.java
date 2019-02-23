package com.codetaylor.mc.pyrotech;

import com.codetaylor.mc.pyrotech.library.blockrenderer.ModBulkRenderItemSupplier;
import com.codetaylor.mc.pyrotech.library.blockrenderer.RenderItemData;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.BlockMechanicalBellows;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockCombustionWorkerStoneBase;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BulkRenderItemSupplier
    extends ModBulkRenderItemSupplier {

  public BulkRenderItemSupplier() {

    super(ModPyrotech.MOD_ID, itemStack -> {

      Item item = itemStack.getItem();

      if (item instanceof ItemBlock) {

        ItemBlock itemBlock = (ItemBlock) item;
        Block block = itemBlock.getBlock();

        if (block instanceof BlockCombustionWorkerStoneBase
            || block instanceof BlockBloomery
            || block instanceof BlockMechanicalBellows) {

          return new RenderItemData(itemStack, Lists.newArrayList(
              new RenderItemData.Offset(0, 0, 0, 1),
              new RenderItemData.Offset(0, 1, 0, 0)
          ));

        } else if (block == ModuleTechMachine.Blocks.MECHANICAL_COMPACTING_BIN) {

          return new RenderItemData(itemStack, Lists.newArrayList(
              new RenderItemData.Offset(0, 0, 1, 0),
              new RenderItemData.Offset(1, 0, 0, 0),
              new RenderItemData.Offset(0, -1, 1, 1),
              new RenderItemData.Offset(1, -1, 0, 1)
          ));

        } else if (block == ModuleTechMachine.Blocks.MECHANICAL_MULCH_SPREADER) {

          return new RenderItemData(itemStack, Lists.newArrayList(
              new RenderItemData.Offset(0, 0, 1, 0),
              new RenderItemData.Offset(1, 0, 0, 0),
              new RenderItemData.Offset(0, -1, 1, 1),
              new RenderItemData.Offset(-1, 0, 2, 0)
          ));
        }

      }

      return new RenderItemData(itemStack);
    });
  }
}
