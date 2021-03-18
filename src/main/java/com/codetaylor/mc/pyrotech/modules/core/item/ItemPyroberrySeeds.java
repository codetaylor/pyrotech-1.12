package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.spi.ItemBushSeedsBase;
import net.minecraft.block.state.IBlockState;

public class ItemPyroberrySeeds
    extends ItemBushSeedsBase {

  public static final String NAME = "pyroberry_seeds";

  @Override
  protected boolean isValidBlock(IBlockState blockState) {

    return ModuleCore.Blocks.PYROBERRY_BUSH.isValidBlock(blockState);
  }

  @Override
  protected IBlockState getPlacementState() {

    return ModuleCore.Blocks.PYROBERRY_BUSH.getDefaultState();
  }
}
