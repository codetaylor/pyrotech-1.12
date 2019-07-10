package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.WorktableProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileWorktable;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class WorktableProvider
    extends BodyProviderAdapter
    implements WorktableProviderDelegate.IWorktableDisplay {

  private final WorktableProviderDelegate delegate;

  private List<String> tooltip;

  public WorktableProvider() {

    this.delegate = new WorktableProviderDelegate(this);
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

    if (tileEntity instanceof TileWorktable) {
      this.tooltip = tooltip;
      this.delegate.display((TileWorktable) tileEntity, Minecraft.getMinecraft().player);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    String renderString = WailaUtil.getStackRenderString(input)
        + WailaUtil.getProgressRenderString(progress, maxProgress)
        + WailaUtil.getStackRenderString(output);
    this.tooltip.add(renderString);
  }

  @Override
  public void setRecipeOutputName(ItemStack itemStack) {

    this.tooltip.add(itemStack.getDisplayName());
  }

  @Override
  public void setCondition(String langKey, String textColorString, String conditionLangKey) {

    String condition = Util.translateFormatted(conditionLangKey);
    this.tooltip.add(Util.translateFormatted(langKey, textColorString, condition));
  }

  @Override
  public void setHoveredItem(ItemStack stackInSlot) {

    String displayName = stackInSlot.getDisplayName();
    int count = stackInSlot.getCount();

    if (count == 1) {
      this.tooltip.add(displayName);

    } else {
      String localized = Util.translateFormatted(WorktableProviderDelegate.LANG_KEY_HOVERED_ITEM_QUANTITY, displayName, count);
      this.tooltip.add(localized);
    }
  }
}
