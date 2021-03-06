package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.delegate.BloomeryProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloomery;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class BloomeryProvider
    extends BodyProviderAdapter
    implements BloomeryProviderDelegate.IBloomeryDisplay {

  private final BloomeryProviderDelegate delegate;

  private List<String> tooltip;

  public BloomeryProvider() {

    this.delegate = new BloomeryProviderDelegate(this);
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

    if (tileEntity instanceof TileBloomery
        || tileEntity instanceof TileBloomery.Top) {

      TileBloomery tile = null;

      if (tileEntity instanceof TileBloomery) {
        tile = (TileBloomery) tileEntity;

      } else {
        World world = tileEntity.getWorld();
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileBloomery) {
          tile = (TileBloomery) candidate;
        }
      }

      if (tile == null) {
        return tooltip;
      }

      this.tooltip = tooltip;
      this.delegate.display(tile);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    String renderString = WailaUtil.getStackRenderString(input) +
        WailaUtil.getProgressRenderString(progress, maxProgress) +
        WailaUtil.getStackRenderString(output);
    this.tooltip.add(renderString);
  }

  @Override
  public void setInput(ItemStack input) {

    this.tooltip.add(WailaUtil.getStackRenderString(input));
  }

  @Override
  public void setOutputItems(ItemStackHandler stackHandler) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = stackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void setFuelItems(ItemStackHandler stackHandler) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = stackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void setSpeed(String langKey, int speed) {

    this.tooltip.add(Util.translateFormatted(langKey, speed));
  }

  @Override
  public void setAirflow(String langKey, int airflow) {

    this.tooltip.add(Util.translateFormatted(langKey, airflow));
  }

  @Override
  public void setFuelCount(String langKey, int fuelCount, int maxFuelCount) {

    this.tooltip.add(Util.translateFormatted(langKey, fuelCount, maxFuelCount));
  }
}
