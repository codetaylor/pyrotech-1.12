package com.codetaylor.mc.pyrotech.modules.tech.refractory.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.storage.ModuleStorage;
import com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.delegate.TankProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class TankProvider
    implements IProbeInfoProvider,
    TankProviderDelegate.ITankDisplay {

  private final TankProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public TankProvider() {

    this.delegate = new TankProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleStorage.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileTankBase) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setFluid(String langKey, FluidStack fluid, int capacity) {

    this.probeInfo.element(new PluginTOP.ElementTankLabel(null, fluid, capacity));
  }

  @Override
  public void setFluidEmpty(String langKey, int capacity) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, 0, capacity));
  }
}
