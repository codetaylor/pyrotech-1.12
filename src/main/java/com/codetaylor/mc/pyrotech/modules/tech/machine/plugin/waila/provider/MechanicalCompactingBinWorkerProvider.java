package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.MechanicalCompactingBinWorkerProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBinWorker;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class MechanicalCompactingBinWorkerProvider
    extends BodyProviderAdapter
    implements MechanicalCompactingBinWorkerProviderDelegate.IMechanicalCompactingBinWorkerDisplay {

  private final MechanicalCompactingBinWorkerProviderDelegate delegate;

  private List<String> tooltip;

  public MechanicalCompactingBinWorkerProvider() {

    this.delegate = new MechanicalCompactingBinWorkerProviderDelegate(this);
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

    if (tileEntity instanceof TileMechanicalCompactingBinWorker) {
      this.tooltip = tooltip;
      this.delegate.display((TileMechanicalCompactingBinWorker) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setOutput(ItemStack cog, ItemStack output) {

    StringBuilder builder = new StringBuilder();

    if (!cog.isEmpty()) {
      builder.append(WailaUtil.getStackRenderString(cog));
    }

    if (!output.isEmpty()) {
      builder.append(WailaUtil.getStackRenderString(output));
    }

    if (builder.length() > 0) {
      this.tooltip.add(builder.toString());
    }
  }

  @Override
  public void setCogName(String langKey, ItemStack cog) {

    this.tooltip.add(Util.translateFormatted(langKey, cog.getDisplayName()));
  }
}
