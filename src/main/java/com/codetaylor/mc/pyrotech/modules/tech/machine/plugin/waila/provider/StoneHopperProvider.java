package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneHopper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class StoneHopperProvider
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

    if (tileEntity instanceof TileStoneHopper) {

      TileStoneHopper tile = (TileStoneHopper) tileEntity;
      ItemStackHandler cogStackHandler = tile.getCogStackHandler();
      ItemStack blade = cogStackHandler.getStackInSlot(0);

      if (!blade.isEmpty()) {
        tooltip.add(WailaUtil.getStackRenderString(blade));
        tooltip.add(Util.translateFormatted(
            "gui." + ModuleTechMachine.MOD_ID + ".waila.cog",
            blade.getItem().getItemStackDisplayName(blade)
        ));
      }
    }

    return tooltip;
  }
}
