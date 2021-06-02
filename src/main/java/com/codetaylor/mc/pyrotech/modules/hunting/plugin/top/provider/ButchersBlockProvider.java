package com.codetaylor.mc.pyrotech.modules.hunting.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.hunting.plugin.waila.delegate.ButchersBlockProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileButchersBlock;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
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

public class ButchersBlockProvider
    implements IProbeInfoProvider,
    ButchersBlockProviderDelegate.IButchersBlockDisplay {

  private final ButchersBlockProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public ButchersBlockProvider() {

    this.delegate = new ButchersBlockProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileButchersBlock) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileButchersBlock) tileEntity);
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
}
