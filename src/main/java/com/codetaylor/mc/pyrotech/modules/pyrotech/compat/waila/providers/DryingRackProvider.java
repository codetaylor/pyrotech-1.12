package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileDryingRackBase;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class DryingRackProvider
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

    if (tileEntity instanceof TileDryingRackBase) {

      TileDryingRackBase tile = (TileDryingRackBase) tileEntity;

      tooltip.add(Util.translateFormatted(
          "gui." + ModulePyrotech.MOD_ID + ".waila.speed",
          (int) (tile.getSpeed() * 100)
      ));

      ItemStackHandler stackHandler = tile.getInputStackHandler();
      ItemStackHandler outputStackHandler = tile.getOutputStackHandler();

      for (int i = 0; i < stackHandler.getSlots(); i++) {

        ItemStack inputStack = stackHandler.getStackInSlot(i);
        float progress = tile.workerGetProgress(i);

        if (!inputStack.isEmpty()) {

          // Display input item and recipe output.

          StringBuilder renderString = new StringBuilder();
          renderString.append(WailaUtil.getStackRenderString(inputStack));

          DryingRackRecipe recipe = DryingRackRecipe.getRecipe(inputStack);

          if (recipe != null) {
            ItemStack recipeOutput = recipe.getOutput();
            recipeOutput.setCount(inputStack.getCount());
            renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
            renderString.append(WailaUtil.getStackRenderString(recipeOutput));
          }

          tooltip.add(renderString.toString());
        }
      }

      StringBuilder renderString = new StringBuilder();

      for (int i = 0; i < outputStackHandler.getSlots(); i++) {

        // Display output items.

        ItemStack outputStack = outputStackHandler.getStackInSlot(i);

        if (!outputStack.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(outputStack));
        }

      }

      if (renderString.length() > 0) {
        tooltip.add(renderString.toString());
      }

    }

    return tooltip;
  }
}
