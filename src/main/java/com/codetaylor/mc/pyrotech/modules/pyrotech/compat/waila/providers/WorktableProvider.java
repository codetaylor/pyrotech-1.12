package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileWorktable;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class WorktableProvider
    extends BodyProviderAdapter {

  private String craftingTableRenderString;

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

    if (tileEntity instanceof TileWorktable) {

      TileWorktable tile;
      tile = (TileWorktable) tileEntity;

      float progress = tile.getRecipeProgress();

      ItemStackHandler stackHandler = tile.getInputStackHandler();
      boolean notEmpty = false;

      for (int i = 0; i < 9; i++) {

        if (!stackHandler.getStackInSlot(i).isEmpty()) {
          notEmpty = true;
          break;
        }
      }

      if (notEmpty) {

        // Display input item and recipe output.
        IRecipe recipe = tile.getRecipe();

        if (recipe != null) {
          StringBuilder renderString = new StringBuilder();
          ItemStack recipeOutput = recipe.getRecipeOutput();

          if (!recipeOutput.isEmpty()) {

            if (this.craftingTableRenderString == null) {
              this.craftingTableRenderString = WailaUtil.getStackRenderString(new ItemStack(Blocks.CRAFTING_TABLE));
            }

            renderString.append(this.craftingTableRenderString);
            renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
            renderString.append(WailaUtil.getStackRenderString(recipeOutput));
          }
          tooltip.add(renderString.toString());
        }
      }
    }

    return tooltip;
  }
}
