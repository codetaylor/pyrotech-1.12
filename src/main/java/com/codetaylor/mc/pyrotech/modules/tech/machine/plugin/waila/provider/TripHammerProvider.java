package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.TripHammerProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileTripHammer;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class TripHammerProvider
    extends BodyProviderAdapter
    implements TripHammerProviderDelegate.ITripHammerSpreaderDisplay {

  private final TripHammerProviderDelegate delegate;

  private List<String> tooltip;

  public TripHammerProvider() {

    this.delegate = new TripHammerProviderDelegate(this);
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

    if (tileEntity instanceof TileTripHammer) {
      this.tooltip = tooltip;
      this.delegate.display((TileTripHammer) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setItems(ItemStack toolItemStack, ItemStack cog) {

    if (toolItemStack.isEmpty() && cog.isEmpty()) {
      return;
    }

    StringBuilder stringBuilder = new StringBuilder();

    if (!toolItemStack.isEmpty()) {
      stringBuilder.append(WailaUtil.getStackRenderString(toolItemStack));
    }

    if (!cog.isEmpty()) {
      stringBuilder.append(WailaUtil.getStackRenderString(cog));
    }

    this.tooltip.add(stringBuilder.toString());
  }

  @Override
  public void setCogName(String langKey, ItemStack cog) {

    this.tooltip.add(Util.translateFormatted(langKey, cog.getDisplayName()));
  }
}
