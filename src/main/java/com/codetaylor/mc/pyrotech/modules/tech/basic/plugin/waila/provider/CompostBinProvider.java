package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.CompostBinProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompostBin;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CompostBinProvider
    extends BodyProviderAdapter
    implements CompostBinProviderDelegate.ICompostBinDisplay {

  private final CompostBinProviderDelegate delegate;

  private List<String> tooltip;

  public CompostBinProvider() {

    this.delegate = new CompostBinProviderDelegate(this);
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

    if (tileEntity instanceof TileCompostBin) {
      this.tooltip = tooltip;
      this.delegate.display((TileCompostBin) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setContents(ItemStackHandler inputStackHandler, ItemStack storedCompostValue, ItemStack outputStack) {

    StringBuilder renderString = new StringBuilder();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        renderString.append(WailaUtil.getStackRenderString(stackInSlot));
      }
    }

    renderString.append(WailaUtil.getStackRenderString(storedCompostValue));
    renderString.append(WailaUtil.getStackRenderString(outputStack));

    this.tooltip.add(renderString.toString());
  }

  @Override
  public void setRecipeProgress(ItemStack output, int progress, int maxProgress) {

    String renderString = WailaUtil.getStackRenderString(new ItemStack(ModuleTechBasic.Blocks.COMPOST_BIN)) +
        WailaUtil.getProgressRenderString(progress, maxProgress) +
        WailaUtil.getStackRenderString(output);
    this.tooltip.add(renderString);
  }

  @Override
  public void setMoistureLevel(@Nullable TextFormatting formatting, String langKey, int moistureLevel) {

    String formattingString = (formatting != null) ? formatting.toString() : "";
    this.tooltip.add(formattingString + Util.translateFormatted(langKey, moistureLevel));
  }
}
