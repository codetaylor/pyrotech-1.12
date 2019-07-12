package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.top.provider;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.MechanicalMulchSpreaderProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalMulchSpreader;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MechanicalMulchSpreaderProvider
    implements IProbeInfoProvider,
    MechanicalMulchSpreaderProviderDelegate.IMechanicalMulchSpreaderDisplay {

  private final MechanicalMulchSpreaderProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public MechanicalMulchSpreaderProvider() {

    this.delegate = new MechanicalMulchSpreaderProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();

    if (world.getBlockState(pos).getBlock() != ModuleTechMachine.Blocks.MECHANICAL_MULCH_SPREADER) {
      return;
    }

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity == null) {
      EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);
      BlockPos offset = data.getPos().offset(facing);
      tileEntity = world.getTileEntity(offset);
    }

    if (tileEntity instanceof TileMechanicalMulchSpreader) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileMechanicalMulchSpreader) tileEntity);
      this.probeInfo = null;
    }
  }

  @Override
  public void setInput(ItemStack mulchStack, ItemStack cog) {

    if (mulchStack.isEmpty() && cog.isEmpty()) {
      return;
    }

    IProbeInfo horizontal = this.probeInfo.horizontal();

    if (!mulchStack.isEmpty()) {
      horizontal.item(mulchStack);
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
