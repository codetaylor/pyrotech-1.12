package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate.MechanicalMulchSpreaderProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalMulchSpreader;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

public class MechanicalMulchSpreaderProvider
    extends BodyProviderAdapter
    implements MechanicalMulchSpreaderProviderDelegate.IMechanicalMulchSpreaderDisplay {

  private final MechanicalMulchSpreaderProviderDelegate delegate;

  private List<String> tooltip;

  public MechanicalMulchSpreaderProvider() {

    this.delegate = new MechanicalMulchSpreaderProviderDelegate(this);
  }

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity == null) {
      IBlockState blockState = accessor.getBlockState();
      EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);
      BlockPos offset = accessor.getPosition().offset(facing);
      tileEntity = accessor.getWorld().getTileEntity(offset);
    }

    if (tileEntity instanceof TileMechanicalMulchSpreader) {
      this.tooltip = tooltip;
      this.delegate.display((TileMechanicalMulchSpreader) tileEntity);
      this.tooltip = null;
    }

    return tooltip;
  }

  @Override
  public void setInput(ItemStack mulchStack, ItemStack cog) {

    if (mulchStack.isEmpty() && cog.isEmpty()) {
      return;
    }

    StringBuilder stringBuilder = new StringBuilder();

    if (!mulchStack.isEmpty()) {
      stringBuilder.append(WailaUtil.getStackRenderString(mulchStack));
    }

    if (!cog.isEmpty()) {
      stringBuilder.append(WailaUtil.getStackRenderString(cog));
    }

    this.tooltip.add(stringBuilder.toString());
  }

  @Override
  public void setCogName(String langKey, ItemStack cog) {

    this.tooltip.add(Util.translateFormatted(langKey, cog.getDisplayName()));
  }
}
