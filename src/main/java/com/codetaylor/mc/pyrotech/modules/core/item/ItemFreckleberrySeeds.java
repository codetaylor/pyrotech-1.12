package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.spi.ItemBushSeedsBase;
import net.minecraft.block.state.IBlockState;

public class ItemFreckleberrySeeds
    extends ItemBushSeedsBase {

  public static final String NAME = "freckleberry_seeds";

  @Override
  protected boolean isValidBlock(IBlockState blockState) {

    return ModuleCore.Blocks.FRECKLEBERRY_PLANT.isValidBlock(blockState);
  }

  @Override
  protected IBlockState getPlacementState() {

    return ModuleCore.Blocks.FRECKLEBERRY_PLANT.getDefaultState();
  }
}
