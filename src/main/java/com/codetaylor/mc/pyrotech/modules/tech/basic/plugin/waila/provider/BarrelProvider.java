package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.BarrelProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileBarrel;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class BarrelProvider
    extends BodyProviderAdapter
    implements BarrelProviderDelegate.IBarrelDisplay {

  private final BarrelProviderDelegate delegate;

  private List<String> tooltip;

  public BarrelProvider() {

    this.delegate = new BarrelProviderDelegate(this);
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

    if (tileEntity instanceof TileBarrel) {
      this.tooltip = tooltip;
      this.delegate.display((TileBarrel) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStackHandler inputStackHandler, FluidStack inputFluid, FluidStack outputFluid, int progress, int maxProgress) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    if (inputFluid != null) {

      Block fluidBlock = inputFluid.getFluid().getBlock();

      if (fluidBlock instanceof BlockFluidBase) {
        renderString.append(WailaUtil.getStackRenderString(new ItemStack(inputFluid.getFluid().getBlock())));

      } else {
        renderString.append(WailaUtil.getStackRenderString(FluidUtil.getFilledBucket(inputFluid)));
      }
    }

    if (outputFluid != null) {
      renderString.append(WailaUtil.getProgressRenderString(progress, maxProgress));

      Block fluidBlock = outputFluid.getFluid().getBlock();

      if (fluidBlock instanceof BlockFluidBase) {
        renderString.append(WailaUtil.getStackRenderString(new ItemStack(outputFluid.getFluid().getBlock())));

      } else {
        renderString.append(WailaUtil.getStackRenderString(FluidUtil.getFilledBucket(outputFluid)));
      }
    }

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void setFluid(FluidStack fluidStack, int capacity) {

    String langKey = "gui." + ModuleTechBasic.MOD_ID + ".waila.tank.fluid";
    String localizedFluidName = fluidStack.getLocalizedName();
    this.tooltip.add(Util.translateFormatted(langKey, localizedFluidName, fluidStack.amount, capacity));
  }
}
