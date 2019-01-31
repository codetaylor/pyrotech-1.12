package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class BloomProvider
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

    if (tileEntity instanceof TileBloom) {

      TileBloom tile;
      tile = (TileBloom) tileEntity;

      int integrity = (int) ((tile.getIntegrity() / (float) tile.getMaxIntegrity()) * 100);

      tooltip.add(Util.translateFormatted(
          "gui." + ModuleCore.MOD_ID + ".waila.bloom.integrity",
          integrity
      ));

      int recipeProgress = (int) (tile.getRecipeProgress() * 100);

      tooltip.add(Util.translateFormatted(
          "gui." + ModuleCore.MOD_ID + ".waila.bloom.hammered",
          recipeProgress
      ));

      EntityPlayerSP player = Minecraft.getMinecraft().player;
      int hammerPower = (int) (BloomHelper.calculateHammerPower(tile.getPos(), player) * 100);

      if (hammerPower > 0) {
        tooltip.add(Util.translateFormatted(
            "gui." + ModuleCore.MOD_ID + ".waila.bloom.hammer.power",
            hammerPower
        ));
      }
    }

    return tooltip;
  }
}
