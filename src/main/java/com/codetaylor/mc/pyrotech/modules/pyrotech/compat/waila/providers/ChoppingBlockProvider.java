package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.ChoppingBlockRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileChoppingBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class ChoppingBlockProvider
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

    if (tileEntity instanceof TileChoppingBlock) {

      TileChoppingBlock tile;
      tile = (TileChoppingBlock) tileEntity;

      float progress = tile.getRecipeProgress();

      ItemStackHandler stackHandler = tile.getStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();
        renderString.append(WailaUtil.getStackRenderString(input));

        ChoppingBlockRecipe recipe = ChoppingBlockRecipe.getRecipe(input);

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();

          if (!recipeOutput.isEmpty()) {
            recipeOutput.setCount(input.getCount());
            renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
            renderString.append(WailaUtil.getStackRenderString(recipeOutput));
          }
        }

        tooltip.add(renderString.toString());

      }

    }

    return tooltip;
  }
}
