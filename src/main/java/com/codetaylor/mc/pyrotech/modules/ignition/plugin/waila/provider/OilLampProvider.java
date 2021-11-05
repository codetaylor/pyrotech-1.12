package com.codetaylor.mc.pyrotech.modules.ignition.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.ignition.plugin.waila.delegate.OilLampProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.ignition.tile.TileLampOil;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class OilLampProvider
    extends BodyProviderAdapter
    implements OilLampProviderDelegate.ITankDisplay {

  private final OilLampProviderDelegate delegate;

  private List<String> tooltip;

  public OilLampProvider() {

    this.delegate = new OilLampProviderDelegate(this);
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

    if (tileEntity instanceof TileLampOil) {
      this.tooltip = tooltip;
      this.delegate.display((TileLampOil) tileEntity);
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
