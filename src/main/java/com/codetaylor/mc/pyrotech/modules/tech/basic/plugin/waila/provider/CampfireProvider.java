package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CampfireRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class CampfireProvider
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

    if (tileEntity instanceof TileCampfire) {

      TileCampfire tileCampfire;
      tileCampfire = (TileCampfire) tileEntity;

      float progress = tileCampfire.workerGetProgress(0);

      ItemStackHandler stackHandler = tileCampfire.getInputStackHandler();
      ItemStackHandler outputStackHandler = tileCampfire.getOutputStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);
      boolean hasOutput = !outputStackHandler.getStackInSlot(0).isEmpty();

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();
        renderString.append(WailaUtil.getStackRenderString(input));
        //renderString.append(WailaUtil.getStackRenderString(fuel));

        CampfireRecipe recipe = CampfireRecipe.getRecipe(input);

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();
          recipeOutput.setCount(input.getCount());
          renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
          renderString.append(WailaUtil.getStackRenderString(recipeOutput));
        }

        tooltip.add(renderString.toString());

      } else if (hasOutput) {

        // Display output items.

        StringBuilder renderString = new StringBuilder();

        for (int i = 0; i < outputStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            renderString.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        tooltip.add(renderString.toString());
      }

      {
        int fuelRemaining = tileCampfire.getFuelRemaining();

        if (tileCampfire.workerIsActive()
            && tileCampfire.combustionGetBurnTimeRemaining() > 0) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModuleTechBasic.MOD_ID + ".waila.burn.time",
              StringHelper.ticksToHMS(tileCampfire.combustionGetBurnTimeRemaining() + tileCampfire.getFuelRemaining() * ModuleTechBasicConfig.CAMPFIRE.BURN_TIME_TICKS_PER_LOG)
          ));
        }

        if (fuelRemaining > 0) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModuleTechBasic.MOD_ID + ".waila.campfire.fuel",
              fuelRemaining,
              8
          ));
        }

        if (tileCampfire.getAshLevel() > 0) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModuleTechBasic.MOD_ID + ".waila.ash",
              tileCampfire.getAshLevel(),
              8
          ));
        }
      }

    }

    return tooltip;
  }
}
