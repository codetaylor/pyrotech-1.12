package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate.WoodRackProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileWoodRack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class WoodRackProvider
    extends BodyProviderAdapter
    implements WoodRackProviderDelegate.IWoodRackDisplay {

  public final WoodRackProviderDelegate delegate;

  public List<String> tooltip;

  public WoodRackProvider() {

    this.delegate = new WoodRackProviderDelegate(this);
  }

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
      this.tooltip = tooltip;
      this.delegate.display((TileWoodRack) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setContents(ItemStackHandler stackHandler) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack itemStack = stackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(itemStack));
      }
    }

    if (renderString.length() > 0) {
      this.tooltip.add(renderString.toString());
    }
  }
}
