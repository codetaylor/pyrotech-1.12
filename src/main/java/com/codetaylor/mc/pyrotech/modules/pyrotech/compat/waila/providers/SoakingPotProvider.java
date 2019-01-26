package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.SoakingPotRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileSoakingPot;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SoakingPotProvider
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

    if (tileEntity instanceof TileSoakingPot) {

      TileSoakingPot tile;
      tile = (TileSoakingPot) tileEntity;
      SoakingPotRecipe currentRecipe = tile.getCurrentRecipe();

      if (currentRecipe != null) {
        TileSoakingPot.InputStackHandler inputStackHandler = tile.getInputStackHandler();
        ItemStack inputStack = inputStackHandler.getStackInSlot(0);
        float progress = tile.getRecipeProgress();
        ItemStack outputStack = currentRecipe.getOutput();
        outputStack.setCount(inputStack.getCount());

        String renderString = WailaUtil.getStackRenderString(inputStack) +
            WailaUtil.getProgressRenderString((int) (100 * progress), 100) +
            WailaUtil.getStackRenderString(outputStack);
        tooltip.add(renderString);
      }

      TileSoakingPot.InputFluidTank inputFluidTank = tile.getInputFluidTank();
      FluidStack fluid = inputFluidTank.getFluid();

      if (fluid != null) {
        tooltip.add(String.format(
            "%s: %d / %d mB",
            fluid.getLocalizedName(),
            fluid.amount,
            inputFluidTank.getCapacity()
        ));
      }

      TileSoakingPot.OutputStackHandler outputStackHandler = tile.getOutputStackHandler();
      ItemStack outputStack = outputStackHandler.getStackInSlot(0);

      if (!outputStack.isEmpty()) {
        tooltip.add(WailaUtil.getStackRenderString(outputStack));
      }
    }

    return tooltip;
  }
}
