package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalMulchSpreader;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class MechanicalMulchSpreaderProvider
    extends BodyProviderAdapter {

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

      TileMechanicalMulchSpreader tile = (TileMechanicalMulchSpreader) tileEntity;

      StringBuilder stringBuilder = new StringBuilder();

      TileMechanicalMulchSpreader.MulchStackHandler mulchStackHandler = tile.getMulchStackHandler();
      ItemStack mulchStack = mulchStackHandler.getStackInSlot(0);

      if (!mulchStack.isEmpty()) {
        stringBuilder.append(WailaUtil.getStackRenderString(mulchStack));
      }

      ItemStackHandler cogStackHandler = tile.getCogStackHandler();
      ItemStack cog = cogStackHandler.getStackInSlot(0);

      if (!cog.isEmpty()) {
        stringBuilder.append(WailaUtil.getStackRenderString(cog));
      }

      if (stringBuilder.length() > 0) {
        tooltip.add(stringBuilder.toString());
      }

      if (!cog.isEmpty()) {
        tooltip.add(Util.translateFormatted(
            "gui." + ModuleTechMachine.MOD_ID + ".waila.cog",
            cog.getItem().getItemStackDisplayName(cog)
        ));
      }
    }

    return tooltip;
  }
}
