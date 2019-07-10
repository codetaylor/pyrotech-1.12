package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.KilnPitProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileKilnPit;
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

public class KilnPitProvider
    implements IProbeInfoProvider,
    KilnPitProviderDelegate.IPitKilnDisplay {

  private final KilnPitProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public KilnPitProvider() {

    this.delegate = new KilnPitProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileKilnPit) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileKilnPit) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setOutputItems(ItemStackHandler stackHandler) {

    boolean found = false;

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = stackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        found = true;
        break;
      }
    }

    if (found) {

      IProbeInfo horizontal = this.probeInfo.horizontal();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        ItemStack stackInSlot = stackHandler.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          horizontal.item(stackInSlot);
        }
      }
    }
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    this.probeInfo.horizontal()
        .item(input)
        .progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false))
        .item(output);
  }
}
