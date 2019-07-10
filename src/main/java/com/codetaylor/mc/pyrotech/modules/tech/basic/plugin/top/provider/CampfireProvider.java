package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.CampfireProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
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

public class CampfireProvider
    implements IProbeInfoProvider,
    CampfireProviderDelegate.ICampfireDisplay {

  private final CampfireProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public CampfireProvider() {

    this.delegate = new CampfireProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCampfire) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileCampfire) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeInput(ItemStack input) {

    this.probeInfo.item(input);
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    this.probeInfo.horizontal()
        .item(input)
        .progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false))
        .item(output);
  }

  @Override
  public void setRecipeOutput(ItemStack output) {

    this.probeInfo.item(output);
  }

  @Override
  public void setBurnTime(String langKey, String burnTime) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, burnTime));
  }

  @Override
  public void setFuelRemaining(String langKey, int fuelRemaining, int maxFuel) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, fuelRemaining, maxFuel));
  }

  @Override
  public void setAshLevel(String langKey, int ashLevel, int maxAshLevel) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, ashLevel, maxAshLevel));
  }
}
