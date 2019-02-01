package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.waila.providers;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarTankBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;
import java.util.List;

public class TankProvider
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

    if (tileEntity instanceof TileTarTankBase) {
      FluidTank fluidTank = ((TileTarTankBase) tileEntity).getFluidTank();
      FluidStack fluid = fluidTank.getFluid();

      if (fluid != null) {
        tooltip.add(Util.translateFormatted(
            "gui." + ModuleTechRefractory.MOD_ID + ".waila.tank.fluid",
            fluid.getLocalizedName(),
            fluid.amount,
            fluidTank.getCapacity()
        ));

      } else {
        tooltip.add(Util.translateFormatted(
            Util.translate("gui." + ModuleTechRefractory.MOD_ID + ".waila.tank.empty"),
            0,
            fluidTank.getCapacity()
        ));
      }
    }

    return tooltip;
  }
}
