package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompactingBinRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompactingBin;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

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

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileCompactingBin) {

      TileCompactingBin tile;
      tile = (TileCompactingBin) tileEntity;
      CompactingBinRecipe currentRecipe = tile.getCurrentRecipe();

      if (currentRecipe == null) {
        return tooltip;
      }

      float progress = tile.getRecipeProgress();
      TileCompactingBin.InputStackHandler inputStackHandler = tile.getInputStackHandler();
      int totalItemCount = tile.getInputStackHandler().getTotalItemCount();
      int completeRecipeCount = totalItemCount / currentRecipe.getAmount();

      if (totalItemCount > 0) {
        StringBuilder renderString = new StringBuilder();

        for (int i = 0; i < inputStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            renderString.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        if (completeRecipeCount > 0) {
          renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
          ItemStack output = currentRecipe.getOutput();
          output.setCount(completeRecipeCount);
          renderString.append(WailaUtil.getStackRenderString(output));
        }

        tooltip.add(renderString.toString());
      }
    }

    return tooltip;
  }
}
