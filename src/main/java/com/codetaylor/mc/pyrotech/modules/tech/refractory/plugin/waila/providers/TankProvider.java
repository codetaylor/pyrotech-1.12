package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.waila.providers;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.waila.delegate.TankProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarTankBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class TankProvider
    extends BodyProviderAdapter
    implements TankProviderDelegate.ITankDisplay {

  private final TankProviderDelegate delegate;

  private List<String> tooltip;

  public TankProvider() {

    this.delegate = new TankProviderDelegate(this);
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

    if (tileEntity instanceof TileTarTankBase) {
      this.tooltip = tooltip;
      this.delegate.display((TileTarTankBase) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setFluid(String langKey, FluidStack fluid, int capacity) {

    String fluidLocalizedName = fluid.getLocalizedName();
    int amount = fluid.amount;
    this.tooltip.add(Util.translateFormatted(langKey, fluidLocalizedName, amount, capacity));
  }

  @Override
  public void setFluidEmpty(String langKey, int capacity) {

    this.tooltip.add(Util.translateFormatted(langKey, 0, capacity));
  }
}
