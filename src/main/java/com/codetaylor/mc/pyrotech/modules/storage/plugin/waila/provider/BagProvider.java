package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class BagProvider
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

    if (tileEntity instanceof TileBagBase) {
      TileBagBase tile = (TileBagBase) tileEntity;
      TileBagBase.StackHandler stackHandler = tile.getStackHandler();
      StringBuilder sb = new StringBuilder();
      int maxDigits = 0;

      if (tile.isOpen()) {

        for (int i = 0; i < stackHandler.getSlots(); i++) {
          ItemStack stackInSlot = stackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            sb.append(WailaUtil.getStackRenderString(stackInSlot));

            int digits = String.valueOf(stackInSlot.getCount()).length();

            if (digits > maxDigits) {
              maxDigits = digits;
            }
          }
        }

        if (sb.length() > 0) {
          tooltip.add(sb.toString());
        }
      }

      tooltip.add(Util.translateFormatted(
          Util.translate("gui." + ModuleTechRefractory.MOD_ID + ".waila.capacity"),
          tile.getItemCount(),
          tile.getItemCapacity()
      ));

      if (tile.isOpen()) {

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
            || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

          for (int i = 0; i < stackHandler.getSlots(); i++) {
            ItemStack stackInSlot = stackHandler.getStackInSlot(i);

            if (!stackInSlot.isEmpty()) {
              String count = String.valueOf(stackInSlot.getCount());

              if (count.length() < maxDigits) {
                count = TextFormatting.DARK_GRAY + StringUtils.repeat("0", maxDigits - count.length()).concat(TextFormatting.YELLOW + count);

              } else {
                count = TextFormatting.YELLOW + count;
              }

              tooltip.add(" " + count + " " + TextFormatting.GOLD + stackInSlot.getDisplayName());
            }
          }

        } else {
          tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY));
        }
      }
    }

    return tooltip;
  }
}
