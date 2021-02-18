package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.BarrelProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileBarrel;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;

public class BarrelProvider
    implements IProbeInfoProvider,
    BarrelProviderDelegate.IBarrelDisplay {

  private final BarrelProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public BarrelProvider() {

    this.delegate = new BarrelProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileBarrel) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileBarrel) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeProgress(ItemStackHandler inputStackHandler, FluidStack inputFluid, FluidStack outputFluid, int progress, int maxProgress) {

    IProbeInfo info = this.probeInfo.horizontal();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        info.item(stackInSlot);
      }
    }

    if (inputFluid != null) {
      info.item(FluidUtil.getFilledBucket(inputFluid));
    }

    if (outputFluid != null) {
      info.progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false));
      info.item(FluidUtil.getFilledBucket(outputFluid));
    }
  }

  @Override
  public void setFluid(FluidStack fluidStack, int capacity) {

    this.probeInfo.element(new PluginTOP.ElementTankLabel(null, fluidStack, capacity));
  }
}