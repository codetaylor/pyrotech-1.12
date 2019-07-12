package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.CompactingBinProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompactingBin;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBin;
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
import net.minecraftforge.items.ItemStackHandler;

public class CompactingBinProvider
    implements IProbeInfoProvider,
    CompactingBinProviderDelegate.ICompactingBinDisplay {

  private final CompactingBinProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public CompactingBinProvider() {

    this.delegate = new CompactingBinProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCompactingBin
        && !(tileEntity instanceof TileMechanicalCompactingBin)) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileCompactingBin) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeInput(ItemStackHandler inputStackHandler) {

    IProbeInfo horizontal = this.probeInfo.horizontal();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        horizontal.item(stackInSlot);
      }
    }
  }

  @Override
  public void setRecipeProgress(ItemStackHandler inputStackHandler, ItemStack output, int progress, int maxProgress) {

    IProbeInfo horizontal = this.probeInfo.horizontal();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        horizontal.item(stackInSlot);
      }
    }

    horizontal
        .progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false))
        .item(output);
  }
}
