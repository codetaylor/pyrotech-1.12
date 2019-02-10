package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBinWorker;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class MechanicalCompactingBinWorkerProvider
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

    if (tileEntity instanceof TileMechanicalCompactingBinWorker) {

      StringBuilder builder = new StringBuilder();

      TileMechanicalCompactingBinWorker tile = (TileMechanicalCompactingBinWorker) tileEntity;
      ItemStackHandler cogStackHandler = tile.getCogStackHandler();
      ItemStack cog = cogStackHandler.getStackInSlot(0);

      if (!cog.isEmpty()) {
        builder.append(WailaUtil.getStackRenderString(cog));
      }

      TileMechanicalCompactingBinWorker.OutputStackHandler outputStackHandler = tile.getOutputStackHandler();
      ItemStack stackInSlot = outputStackHandler.getStackInSlot(0);

      if (stackInSlot != ItemStack.EMPTY) {
        builder.append(WailaUtil.getStackRenderString(stackInSlot));
      }

      if (builder.length() > 0) {
        tooltip.add(builder.toString());
      }

      if (!cog.isEmpty()) {
        tooltip.add(Util.translateFormatted(
            "gui." + ModuleTechMachine.MOD_ID + ".waila.cog",
            cog.getItem().getItemStackDisplayName(cog)
        ));
      }
    }

    return tooltip;
  }
}
