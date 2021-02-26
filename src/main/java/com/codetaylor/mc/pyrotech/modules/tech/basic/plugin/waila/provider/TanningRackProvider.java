package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.TanningRackProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileTanningRack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class TanningRackProvider
    extends BodyProviderAdapter
    implements TanningRackProviderDelegate.ITanningRackDisplay {

  private final TanningRackProviderDelegate delegate;
  private List<String> tooltip;

  public TanningRackProvider() {

    this.delegate = new TanningRackProviderDelegate(this);
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

    if (tileEntity instanceof TileTanningRack) {
      this.tooltip = tooltip;
      this.delegate.display((TileTanningRack) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    String renderString = WailaUtil.getStackRenderString(input);

    if (!output.isEmpty()) {
      renderString += WailaUtil.getProgressRenderString(progress, maxProgress)
          + WailaUtil.getStackRenderString(output);
    }

    this.tooltip.add(renderString);
  }

  @Override
  public void setOutputItems(ItemStackHandler outputStackHandler) {

    ItemStack stackInSlot = outputStackHandler.getStackInSlot(0);

    if (!stackInSlot.isEmpty()) {
      this.tooltip.add(WailaUtil.getStackRenderString(stackInSlot));
    }
  }
}
