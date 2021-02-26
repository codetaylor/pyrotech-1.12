package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.TanningRackProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileTanningRack;
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

public class TanningRackProvider
    implements IProbeInfoProvider,
    TanningRackProviderDelegate.ITanningRackDisplay {

  private final TanningRackProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public TanningRackProvider() {

    this.delegate = new TanningRackProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTanningRack) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileTanningRack) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    this.probeInfo.horizontal()
        .item(input);

    if (!output.isEmpty()) {
      this.probeInfo.horizontal()
          .progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false))
          .item(output);
    }
  }

  @Override
  public void setOutputItems(ItemStackHandler outputStackHandler) {

    ItemStack stackInSlot = outputStackHandler.getStackInSlot(0);

    if (!stackInSlot.isEmpty()) {

      IProbeInfo horizontal = this.probeInfo.horizontal();

      if (!stackInSlot.isEmpty()) {
        horizontal.item(stackInSlot);
      }
    }
  }
}
