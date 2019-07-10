package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.DryingRackProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileDryingRackBase;
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

public class DryingRackProvider
    implements IProbeInfoProvider,
    DryingRackProviderDelegate.IDryingRackDisplay {

  private final DryingRackProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public DryingRackProvider() {

    this.delegate = new DryingRackProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileDryingRackBase) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileDryingRackBase) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setSpeed(String langKey, int speed) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, speed));
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    this.probeInfo.horizontal()
        .item(input)
        .progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false))
        .item(output);
  }

  @Override
  public void setOutputItems(ItemStackHandler outputStackHandler) {

    boolean found = false;

    for (int i = 0; i < outputStackHandler.getSlots(); i++) {
      ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        found = true;
        break;
      }
    }

    if (found) {

      IProbeInfo horizontal = this.probeInfo.horizontal();

      for (int i = 0; i < outputStackHandler.getSlots(); i++) {
        ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          horizontal.item(stackInSlot);
        }
      }
    }
  }
}
