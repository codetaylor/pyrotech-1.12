package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloomery;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class BloomeryProvider
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

    if (tileEntity instanceof TileBloomery
        || tileEntity instanceof TileBloomery.Top) {

      TileBloomery tile = null;

      if (tileEntity instanceof TileBloomery) {
        tile = (TileBloomery) tileEntity;

      } else {
        World world = tileEntity.getWorld();
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileBloomery) {
          tile = (TileBloomery) candidate;
        }
      }

      if (tile == null) {
        return tooltip;
      }

      float progress = tile.getRecipeProgress();

      ItemStackHandler stackHandler = tile.getInputStackHandler();
      ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
      TileBloomery.FuelStackHandler fuelStackHandler = tile.getFuelStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);
      boolean hasOutput = !outputStackHandler.getStackInSlot(0).isEmpty();
      int fuelCount = fuelStackHandler.getTotalItemCount();

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();
        renderString.append(WailaUtil.getStackRenderString(input));

        BloomeryRecipeBase recipe = tile.getCurrentRecipe();

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutputBloom();
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

      if (fuelCount > 0) {

        StringBuilder renderString = new StringBuilder();

        for (int i = 0; i < fuelStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = fuelStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            renderString.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        tooltip.add(renderString.toString());
      }

      tooltip.add(Util.translateFormatted(
          "gui." + ModuleTechBloomery.MOD_ID + ".waila.speed",
          (int) (tile.getSpeed() * 100)
      ));

      tooltip.add(Util.translateFormatted(
          "gui." + ModuleTechBloomery.MOD_ID + ".waila.bloomery.airflow",
          (int) (tile.getAirflow() * 100)
      ));

      tooltip.add(Util.translateFormatted(
          "gui." + ModuleTechBloomery.MOD_ID + ".waila.bloomery.fuel",
          tile.getFuelCount(),
          tile.getMaxFuelCount()
      ));

      /*
      tooltip.add(Util.translateFormatted(
          "gui." + ModuleTechBloomery.MOD_ID + ".waila.ash",
          tile.getAshCount(),
          tile.getMaxAshCapacity()
      ));
      */

    }

    return tooltip;
  }

}
