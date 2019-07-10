package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.ChoppingBlockProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileChoppingBlock;
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

public class ChoppingBlockProvider
    implements IProbeInfoProvider,
    ChoppingBlockProviderDelegate.IChoppingBlockDisplay {

  private final ChoppingBlockProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public ChoppingBlockProvider() {

    this.delegate = new ChoppingBlockProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileChoppingBlock) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileChoppingBlock) tileEntity);
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
}
