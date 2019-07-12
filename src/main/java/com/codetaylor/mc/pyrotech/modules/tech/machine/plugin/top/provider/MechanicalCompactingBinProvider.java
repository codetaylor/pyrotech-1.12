package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top.provider;

import com.codetaylor.mc.pyrotech.library.CompactingBinRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.MechanicalCompactingBinProviderDelegate;
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

public class MechanicalCompactingBinProvider
    implements IProbeInfoProvider,
    MechanicalCompactingBinProviderDelegate.IMechanicalCompactingBinDisplay {

  private final MechanicalCompactingBinProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public MechanicalCompactingBinProvider() {

    this.delegate = new MechanicalCompactingBinProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileMechanicalCompactingBin) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileMechanicalCompactingBin) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeProgress(ItemStackHandler inputStackHandler, CompactingBinRecipeBase currentRecipe, int completeRecipeCount, int progress, int maxProgress) {

    IProbeInfo horizontal = this.probeInfo.horizontal();

    for (int i = 0; i < inputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = inputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        horizontal.item(stackInSlot);
      }
    }

    if (completeRecipeCount > 0) {
      ItemStack output = currentRecipe.getOutput();
      output.setCount(completeRecipeCount);
      horizontal.progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false));
      horizontal.item(output);
    }
  }
}
