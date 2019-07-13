package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.delegate.BloomProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

public class BloomProvider
    extends BodyProviderAdapter
    implements BloomProviderDelegate.IBloomDisplay {

  private final BloomProviderDelegate delegate;

  private List<String> tooltip;

  public BloomProvider() {

    this.delegate = new BloomProviderDelegate(this);
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

    if (tileEntity instanceof TileBloom) {
      this.tooltip = tooltip;
      EntityPlayer player = Minecraft.getMinecraft().player;
      this.delegate.display((TileBloom) tileEntity, player);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setIntegrity(String langKey, int integrity) {

    this.tooltip.add(Util.translateFormatted(langKey, integrity));
  }

  @Override
  public void setHammered(String langKey, int recipeProgress) {

    this.tooltip.add(Util.translateFormatted(langKey, recipeProgress));
  }

  @Override
  public void setHammerPower(String langKey, int hammerPower) {

    this.tooltip.add(Util.translateFormatted(langKey, hammerPower));
  }

  @Override
  public void setHammerPower(TextFormatting formatting, String langKey, int hammerPower) {

    this.tooltip.add(formatting + Util.translateFormatted(langKey, hammerPower));
  }
}
