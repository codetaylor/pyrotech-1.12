package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.CogWorkerProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CogWorkerProvider
    implements IProbeInfoProvider,
    CogWorkerProviderDelegate.ICogWorkerDisplay {

  private final CogWorkerProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public CogWorkerProvider() {

    this.delegate = new CogWorkerProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCogWorkerBase) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileCogWorkerBase) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setCog(ItemStack cog) {

    this.probeInfo.item(cog);
  }

  @Override
  public void setCogName(String langKey, ItemStack cog) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, cog));
  }
}
