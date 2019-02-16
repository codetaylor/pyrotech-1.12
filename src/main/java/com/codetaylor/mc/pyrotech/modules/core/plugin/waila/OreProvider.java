package com.codetaylor.mc.pyrotech.modules.core.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockOre;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class OreProvider
    implements IWailaDataProvider {

  @Nonnull
  @Override
  public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {

    IBlockState blockState = accessor.getBlockState();
    Block block = blockState.getBlock();

    if (block == ModuleCore.Blocks.ORE) {
      int metaFromState = block.getMetaFromState(blockState);
      return new ItemStack(ModuleCore.Blocks.ORE, 1, metaFromState);
    }

    return accessor.getStack();
  }
}
