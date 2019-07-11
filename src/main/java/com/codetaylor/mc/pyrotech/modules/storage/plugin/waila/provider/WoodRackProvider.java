package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileWoodRack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class WoodRackProvider
    extends BodyProviderAdapter {

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileWoodRack) {

      TileWoodRack tile;
      tile = (TileWoodRack) tileEntity;

      ItemStackHandler stackHandler = tile.getStackHandler();
      StringBuilder renderString = new StringBuilder();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        ItemStack input = stackHandler.getStackInSlot(i);

        if (!input.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(input));
        }
      }

      if (renderString.length() > 0) {
        tooltip.add(renderString.toString());
      }

    }

    return tooltip;
  }
}
