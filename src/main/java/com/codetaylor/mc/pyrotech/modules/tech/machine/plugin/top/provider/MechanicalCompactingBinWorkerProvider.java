package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.MechanicalCompactingBinWorkerProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBinWorker;
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

public class MechanicalCompactingBinWorkerProvider
    implements IProbeInfoProvider,
    MechanicalCompactingBinWorkerProviderDelegate.IMechanicalCompactingBinWorkerDisplay {

  private final MechanicalCompactingBinWorkerProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public MechanicalCompactingBinWorkerProvider() {

    this.delegate = new MechanicalCompactingBinWorkerProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileMechanicalCompactingBinWorker) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileMechanicalCompactingBinWorker) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setOutput(ItemStack cog, ItemStack output) {

    if (cog.isEmpty() && output.isEmpty()) {
      return;
    }

    IProbeInfo horizontal = this.probeInfo.horizontal();

    if (!cog.isEmpty()) {
      horizontal.item(cog);
    }

    if (!output.isEmpty()) {
      horizontal.item(output);
    }
  }

  @Override
  public void setCogName(String langKey, ItemStack cog) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, cog));
  }
}
