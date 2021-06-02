package com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.delegate.ButchersBlockProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileButchersBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class ButchersBlockProvider
    implements IWailaDataProvider,
    ButchersBlockProviderDelegate.IButchersBlockDisplay {

  private final ButchersBlockProviderDelegate delegate;

  private List<String> tooltip;

  public ButchersBlockProvider() {

    this.delegate = new ButchersBlockProviderDelegate(this);
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

    if (tileEntity instanceof TileButchersBlock) {
      this.tooltip = tooltip;
      this.delegate.display((TileButchersBlock) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    String renderString = WailaUtil.getStackRenderString(input)
        + WailaUtil.getProgressRenderString(progress, maxProgress)
        + WailaUtil.getStackRenderString(output);
    this.tooltip.add(renderString);
  }
}
