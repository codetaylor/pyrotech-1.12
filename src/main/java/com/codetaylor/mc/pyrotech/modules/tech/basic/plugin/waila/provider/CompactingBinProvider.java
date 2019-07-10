package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.CompactingBinProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompactingBin;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class CompactingBinProvider
    extends BodyProviderAdapter
    implements CompactingBinProviderDelegate.ICompactingBinDisplay {

  private final CompactingBinProviderDelegate delegate;

  private List<String> tooltip;

  public CompactingBinProvider() {

    this.delegate = new CompactingBinProviderDelegate(this);
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

    if (tileEntity instanceof TileCompactingBin) {
      this.tooltip = tooltip;
      this.delegate.display((TileCompactingBin) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeInput(ItemStackHandler inputStackHandler) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void setRecipeProgress(ItemStackHandler inputStackHandler, ItemStack output, int progress, int maxProgress) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    renderString.append(WailaUtil.getProgressRenderString(progress, maxProgress));
    renderString.append(WailaUtil.getStackRenderString(output));

    this.tooltip.add(renderString.toString());
  }
}
