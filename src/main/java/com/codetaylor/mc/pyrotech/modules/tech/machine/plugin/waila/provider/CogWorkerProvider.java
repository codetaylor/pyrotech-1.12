package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CogWorkerProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class CogWorkerProvider
    extends BodyProviderAdapter
    implements CogWorkerProviderDelegate.ICogWorkerDisplay {

  private final CogWorkerProviderDelegate delegate;

  private List<String> tooltip;

  public CogWorkerProvider() {

    this.delegate = new CogWorkerProviderDelegate(this);
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

    if (tileEntity instanceof TileCogWorkerBase) {
      this.tooltip = tooltip;
      this.delegate.display((TileCogWorkerBase) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setCog(ItemStack cog) {

    this.tooltip.add(WailaUtil.getStackRenderString(cog));
  }

  @Override
  public void setCogName(String langKey, ItemStack cog) {

    this.tooltip.add(Util.translateFormatted(
        langKey,
        cog.getDisplayName()
    ));
  }
}
