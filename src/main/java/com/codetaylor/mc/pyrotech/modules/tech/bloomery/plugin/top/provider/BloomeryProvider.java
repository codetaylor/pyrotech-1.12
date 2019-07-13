package com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.plugin.waila.delegate.BloomeryProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloomery;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class BloomeryProvider
    implements IProbeInfoProvider,
    BloomeryProviderDelegate.IBloomeryDisplay {

  private final BloomeryProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public BloomeryProvider() {

    this.delegate = new BloomeryProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    TileEntity tileEntity = world.getTileEntity(data.getPos());

    if (tileEntity instanceof TileBloomery
        || tileEntity instanceof TileBloomery.Top) {

      TileBloomery tile = null;

      if (tileEntity instanceof TileBloomery) {
        tile = (TileBloomery) tileEntity;

      } else {
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileBloomery) {
          tile = (TileBloomery) candidate;
        }
      }

      if (tile == null) {
        return;
      }

      this.probeInfo = probeInfo;
      this.delegate.display(tile);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    this.probeInfo.horizontal()
        .item(input)
        .progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false))
        .item(output);
  }

  @Override
  public void setInput(ItemStack input) {

    this.probeInfo.item(input);
  }

  @Override
  public void setOutputItems(ItemStackHandler stackHandler) {

    IProbeInfo horizontal = this.probeInfo.horizontal();

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = stackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        horizontal.item(stackInSlot);
      }
    }
  }

  @Override
  public void setFuelItems(ItemStackHandler stackHandler) {

    IProbeInfo horizontal = this.probeInfo.horizontal();

    for (int i = 0; i < stackHandler.getSlots(); i++) {
      ItemStack stackInSlot = stackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        horizontal.item(stackInSlot);
      }
    }
  }

  @Override
  public void setSpeed(String langKey, int speed) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, speed));
  }

  @Override
  public void setAirflow(String langKey, int airflow) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, airflow));
  }

  @Override
  public void setFuelCount(String langKey, int fuelCount, int maxFuelCount) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, fuelCount, maxFuelCount));
  }
}
