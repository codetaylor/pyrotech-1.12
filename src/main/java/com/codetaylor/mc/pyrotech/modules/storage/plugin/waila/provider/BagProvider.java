package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate.BagProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileBagBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class BagProvider
    extends BodyProviderAdapter
    implements BagProviderDelegate.IBagDisplay {

  private final BagProviderDelegate delegate;

  private List<String> tooltip;

  public BagProvider() {

    this.delegate = new BagProviderDelegate(this);
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

    if (tileEntity instanceof TileBagBase) {
      this.tooltip = tooltip;
      this.delegate.setPlayer(Minecraft.getMinecraft().player);
      this.delegate.display((TileBagBase) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setContents(ItemStackHandler stackHandler) {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = stackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        sb.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    if (sb.length() > 0) {
      this.tooltip.add(sb.toString());
    }
  }

  @Override
  public void setItemCount(String langKey, int itemCount, int itemCapacity) {

    this.tooltip.add(Util.translateFormatted(langKey, itemCount, itemCapacity));
  }

  @Override
  public void setExtendedInfoOff(String langKey, TextFormatting startColor, TextFormatting endColor) {

    this.tooltip.add(Util.translateFormatted(langKey, startColor, endColor));
  }

  @Override
  public void setExtendedInfoOn(String count, TextFormatting formatting, ItemStack itemStack) {

    this.tooltip.add(count + TextFormatting.GOLD + itemStack.getDisplayName());
  }
}
