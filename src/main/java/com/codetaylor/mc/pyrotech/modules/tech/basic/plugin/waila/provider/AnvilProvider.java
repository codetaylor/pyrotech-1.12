package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.AnvilProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AnvilProvider
    extends BodyProviderAdapter
    implements AnvilProviderDelegate.IAnvilDisplay {

  private final AnvilProviderDelegate delegate;

  private List<String> tooltip;

  public AnvilProvider(AnvilRecipe.EnumTier tier) {

    this.delegate = new AnvilProviderDelegate(tier, this);
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

    if (tileEntity instanceof TileAnvilBase) {

      this.tooltip = tooltip;
      this.delegate.display((TileAnvilBase) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    this.tooltip.add(WailaUtil.getStackRenderString(input) +
        WailaUtil.getProgressRenderString(progress, maxProgress) +
        WailaUtil.getStackRenderString(output));
  }

  @Override
  public void setRecipeType(String langKey, String typeLangKey) {

    this.tooltip.add(Util.translateFormatted(langKey, Util.translateFormatted(typeLangKey)));
  }

  @Override
  public void setBloomName(@Nullable TextFormatting textFormatting, ItemStack input) {

    if (textFormatting == null) {
      this.tooltip.add(input.getDisplayName());

    } else {
      this.tooltip.add(textFormatting + input.getDisplayName());
    }
  }

  @Override
  public void setIntegrity(String langKey, int integrity) {

    this.tooltip.add(Util.translateFormatted(langKey, integrity));
  }

  @Override
  public void setHammerPower(@Nullable TextFormatting textFormatting, String langKey, int hammerPower) {

    if (textFormatting == null) {
      this.tooltip.add(Util.translateFormatted(langKey, hammerPower));

    } else {
      this.tooltip.add(textFormatting + Util.translateFormatted(langKey, hammerPower));
    }
  }
}
