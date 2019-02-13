package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class BagProvider
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

    if (tileEntity instanceof TileBagBase) {
      TileBagBase tile = (TileBagBase) tileEntity;
      TileBagBase.StackHandler stackHandler = tile.getStackHandler();
      StringBuilder sb = new StringBuilder();

      if (tile.isOpen()) {

        for (int i = 0; i < stackHandler.getSlots(); i++) {
          ItemStack stackInSlot = stackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            sb.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        if (sb.length() > 0) {
          tooltip.add(sb.toString());
        }
      }

      tooltip.add(Util.translateFormatted(
          Util.translate("gui." + ModuleTechRefractory.MOD_ID + ".waila.capacity"),
          tile.getItemCount(),
          tile.getItemCapacity()
      ));
    }

    return tooltip;
  }
}
