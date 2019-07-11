package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate.StorageProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileCrate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileShelf;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileStash;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class StorageProvider
    extends BodyProviderAdapter
    implements StorageProviderDelegate.IStorageDisplay {

  private final StorageProviderDelegate delegate;

  private List<String> tooltip;

  public StorageProvider() {

    this.delegate = new StorageProviderDelegate(this);
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

    if (tileEntity instanceof TileCrate
        || tileEntity instanceof TileShelf
        || tileEntity instanceof TileStash) {
      this.tooltip = tooltip;
      this.delegate.display(tileEntity, Minecraft.getMinecraft().player);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setItem(ItemStack itemStack) {

    this.tooltip.add(WailaUtil.getStackRenderString(itemStack));
  }

  @Override
  public void setItemLabel(ItemStack itemStack) {

    this.tooltip.add(itemStack.getDisplayName());
  }
}
