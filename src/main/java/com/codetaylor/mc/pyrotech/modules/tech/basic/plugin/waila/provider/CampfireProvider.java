package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.CampfireProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class CampfireProvider
    extends BodyProviderAdapter
    implements CampfireProviderDelegate.ICampfireDisplay {

  private final CampfireProviderDelegate delegate;

  private List<String> tooltip;

  public CampfireProvider() {

    this.delegate = new CampfireProviderDelegate(this);
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

    if (tileEntity instanceof TileCampfire) {

      this.tooltip = tooltip;
      this.delegate.display((TileCampfire) tileEntity);
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

    String renderString = WailaUtil.getStackRenderString(input)
        + WailaUtil.getProgressRenderString(progress, maxProgress)
        + WailaUtil.getStackRenderString(output);
    this.tooltip.add(renderString);
  }

  @Override
  public void setRecipeOutput(ItemStack output) {

    this.tooltip.add(WailaUtil.getStackRenderString(output));
  }

  @Override
  public void setBurnTime(String langKey, String burnTime) {

    this.tooltip.add(Util.translateFormatted(langKey, burnTime));
  }

  @Override
  public void setFuelRemaining(String langKey, int fuelRemaining, int maxFuel) {

    this.tooltip.add(Util.translateFormatted(langKey, fuelRemaining, maxFuel));
  }

  @Override
  public void setAshLevel(String langKey, int ashLevel, int maxAshLevel) {

    this.tooltip.add(Util.translateFormatted(langKey, ashLevel, maxAshLevel));
  }
}
