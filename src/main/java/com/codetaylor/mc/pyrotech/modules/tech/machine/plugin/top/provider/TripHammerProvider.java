package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.TripHammerProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileTripHammer;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TripHammerProvider
    implements IProbeInfoProvider,
    TripHammerProviderDelegate.ITripHammerSpreaderDisplay {

  private final TripHammerProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public TripHammerProvider() {

    this.delegate = new TripHammerProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTripHammer) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileTripHammer) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setItems(ItemStack toolItemStack, ItemStack cog) {

    if (toolItemStack.isEmpty() && cog.isEmpty()) {
      return;
    }

    IProbeInfo horizontal = this.probeInfo.horizontal();

    if (!toolItemStack.isEmpty()) {
      horizontal.item(toolItemStack);
    }

    if (!cog.isEmpty()) {
      horizontal.item(cog);
    }
  }

  @Override
  public void setCogName(String langKey, ItemStack cog) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, cog));
  }
}
