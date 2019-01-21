package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.athenaeum.inventory.DynamicStackHandler;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileBloom;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class BloomProvider
    extends BodyProviderAdapter {

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    if (!config.getConfig(WailaRegistrar.CONFIG_PROGRESS)) {
      return tooltip;
    }

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileBloom) {

      TileBloom tile;
      tile = (TileBloom) tileEntity;

      DynamicStackHandler stackHandler = tile.getStackHandler();
      int totalItemCount = stackHandler.getTotalItemCount();
      int maxIntegrity = tile.getMaxIntegrity();
      int integrity = (int) ((totalItemCount / (float) maxIntegrity) * 100);

      tooltip.add(Util.translateFormatted(
          "gui." + ModulePyrotech.MOD_ID + ".waila.bloom.integrity",
          integrity
      ));

      int recipeProgress = (int) (tile.getRecipeProgress() * 100);

      tooltip.add(Util.translateFormatted(
          "gui." + ModulePyrotech.MOD_ID + ".waila.bloom.hammered",
          recipeProgress
      ));
    }

    return tooltip;
  }
}
