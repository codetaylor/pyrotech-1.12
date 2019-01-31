package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileTorchStone;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class BlockTorchStone
    extends BlockTorchBase {

  public static final String NAME = "torch_stone";

  @Override
  public TileEntity createTileEntity() {

    return new TileTorchStone();
  }

  @Override
  protected void getLitDrops(NonNullList<ItemStack> drops) {

    drops.add(new ItemStack(ModuleBlocks.TORCH_STONE));
  }
}
