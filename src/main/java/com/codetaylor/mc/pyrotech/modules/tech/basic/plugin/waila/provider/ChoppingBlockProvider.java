package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.ChoppingBlockProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileChoppingBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class ChoppingBlockProvider
    extends BodyProviderAdapter
    implements ChoppingBlockProviderDelegate.IChoppingBlockDisplay {

  private final ChoppingBlockProviderDelegate delegate;

  private List<String> tooltip;

  public ChoppingBlockProvider() {

    this.delegate = new ChoppingBlockProviderDelegate(this);
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

    if (tileEntity instanceof TileChoppingBlock) {
      this.tooltip = tooltip;
      this.delegate.display((TileChoppingBlock) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeInput(ItemStack input) {

    this.tooltip.add(WailaUtil.getStackRenderString(input));
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    String renderString = WailaUtil.getStackRenderString(input) +
        WailaUtil.getProgressRenderString(progress, maxProgress) +
        WailaUtil.getStackRenderString(output);
    this.tooltip.add(renderString);
  }
}
