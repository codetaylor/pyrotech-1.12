package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.delegate.BloomProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloom;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BloomProvider
    implements IProbeInfoProvider,
    BloomProviderDelegate.IBloomDisplay {

  private final BloomProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public BloomProvider() {

    this.delegate = new BloomProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileBloom) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileBloom) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setIntegrity(String langKey, int integrity) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, integrity));
  }

  @Override
  public void setHammered(String langKey, int recipeProgress) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, recipeProgress));
  }

  @Override
  public void setHammerPower(String langKey, int hammerPower) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, hammerPower));
  }

  @Override
  public void setHammerPower(TextFormatting formatting, String langKey, int hammerPower) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(formatting, langKey, hammerPower));

  }
}
