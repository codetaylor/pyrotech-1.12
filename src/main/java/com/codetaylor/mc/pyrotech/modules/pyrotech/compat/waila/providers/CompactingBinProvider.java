package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCompactingBin;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class CompactingBinProvider
    extends BodyProviderAdapter {

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    if (!config.getConfig(WailaRegistrar.CONFIG_PROGRESS)) {
      return tooltip;
    }

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileCompactingBin) {

      TileCompactingBin tile;
      tile = (TileCompactingBin) tileEntity;

      float progress = tile.getRecipeProgress();
      ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
      ItemStackHandler storedInputStackHandler = tile.getStoredInputStackHandler();
      ItemStack output = outputStackHandler.getStackInSlot(0);
      ItemStack pendingOutput = outputStackHandler.getStackInSlot(1);

      if (!storedInputStackHandler.getStackInSlot(0).isEmpty()) {
        StringBuilder renderString = new StringBuilder();

        for (int i = 0; i < storedInputStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = storedInputStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            renderString.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));

        if (output.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(pendingOutput));

        } else {
          renderString.append(WailaUtil.getStackRenderString(output));
        }

        tooltip.add(renderString.toString());
      }

      if (!output.isEmpty()) {
        tooltip.add(WailaUtil.getStackRenderString(output));
      }

    }

    return tooltip;
  }
}
