package com.codetaylor.mc.pyrotech.modules.core.plugin.waila;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class FreckleberryPlantStackProvider
    implements IWailaDataProvider {

  private ItemStack itemStackFreckleberries;

  @Nonnull
  @Override
  public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {

    Block block = accessor.getBlock();

    if (block == ModuleCore.Blocks.FRECKLEBERRY_PLANT) {

      if (this.itemStackFreckleberries == null) {
        this.itemStackFreckleberries = new ItemStack(ModuleCore.Items.FRECKLEBERRIES);
      }

      return this.itemStackFreckleberries;
    }

    return IWailaDataProvider.super.getWailaStack(accessor, config);
  }
}
