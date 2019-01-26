package com.codetaylor.mc.pyrotech.modules.bloomery.plugin.waila;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.bloomery.tile.TileBloomery;
import com.codetaylor.mc.pyrotech.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileStoneTop;
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

    if (!config.getConfig(WailaRegistrar.CONFIG_PROGRESS)) {
      return tooltip;
    }

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileBloomery
        || tileEntity instanceof TileStoneTop) {

      TileBloomery tile = null;

      if (tileEntity instanceof TileBloomery) {
        tile = (TileBloomery) tileEntity;

      } else {

        if (((TileStoneTop) tileEntity).isCustom()) {
          return tooltip;
        }

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

        BloomeryRecipe recipe = tile.getCurrentRecipe();

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutputBloom();
          recipeOutput.setCount(input.getCount());
          renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
          renderString.append(WailaUtil.getStackRenderString(recipeOutput));
        }

        tooltip.add(renderString.toString());

      } else if (hasOutput) {

        // Display output items.

        //tooltip.add(Util.translate("gui." + ModuleCharcoal.MOD_ID + ".waila.kiln.brick.finished"));

        StringBuilder renderString = new StringBuilder();

        for (int i = 0; i < outputStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            renderString.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        //tooltip.add(Util.translate("gui." + ModuleCharcoal.MOD_ID + ".waila.result"));
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
          "gui." + ModulePyrotech.MOD_ID + ".waila.speed",
          (int) (tile.getSpeed() * 100)
      ));

      tooltip.add(Util.translateFormatted(
          "gui." + ModulePyrotech.MOD_ID + ".waila.bloomery.airflow",
          (int) (tile.getAirflow() * 100)
      ));

      tooltip.add(Util.translateFormatted(
          "gui." + ModulePyrotech.MOD_ID + ".waila.bloomery.fuel",
          tile.getFuelCount(),
          tile.getMaxFuelCount()
      ));

      tooltip.add(Util.translateFormatted(
          "gui." + ModulePyrotech.MOD_ID + ".waila.ash",
          tile.getAshCount(),
          tile.getMaxAshCapacity()
      ));

    }

    return tooltip;
  }

}
