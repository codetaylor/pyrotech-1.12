package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.CompactingBinRecipeBase;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.MechanicalCompactingBinProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBin;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class MechanicalCompactingBinProvider
    extends BodyProviderAdapter
    implements MechanicalCompactingBinProviderDelegate.IMechanicalCompactingBinDisplay {

  private final MechanicalCompactingBinProviderDelegate delegate;

  private List<String> tooltip;

  public MechanicalCompactingBinProvider() {

    this.delegate = new MechanicalCompactingBinProviderDelegate(this);
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

    if (tileEntity instanceof TileMechanicalCompactingBin) {
      this.tooltip = tooltip;
      this.delegate.display((TileMechanicalCompactingBin) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStackHandler inputStackHandler, CompactingBinRecipeBase currentRecipe, int completeRecipeCount, int progress, int maxProgress) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    if (completeRecipeCount > 0) {
      renderString.append(WailaUtil.getProgressRenderString(progress, maxProgress));
      ItemStack output = currentRecipe.getOutput();
      output.setCount(completeRecipeCount);
      renderString.append(WailaUtil.getStackRenderString(output));
    }

    this.tooltip.add(renderString.toString());
  }
}
