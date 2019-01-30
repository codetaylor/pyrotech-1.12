package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockKilnPit;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.KilnPitRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileKilnPit;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class KilnPitProvider
    implements IWailaDataProvider {

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    if (accessor.getTileEntity() instanceof TileKilnPit) {

      IBlockState blockState = accessor.getBlockState();
      TileKilnPit tileKiln = (TileKilnPit) accessor.getTileEntity();
      float progress = tileKiln.getProgress();
      ItemStackHandler stackHandler = tileKiln.getStackHandler();
      ItemStack input = stackHandler.getStackInSlot(0);
      KilnPitRecipe recipe = KilnPitRecipe.getRecipe(input);
      ItemStack output = ItemStack.EMPTY;

      StringBuilder renderString = new StringBuilder();

      if (recipe != null) {
        output = recipe.getOutput();
        output.setCount(input.getCount());
      }

      if (blockState.getValue(BlockKilnPit.VARIANT) == BlockKilnPit.EnumType.COMPLETE) {

        ItemStackHandler outputStackHandler = tileKiln.getOutputStackHandler();
        StringBuilder outputString = new StringBuilder();
        boolean hasOutput = false;

        for (int i = 0; i < outputStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            outputString.append(WailaUtil.getStackRenderString(stackInSlot));
            hasOutput = true;
          }
        }

        if (hasOutput) {
          renderString.append(outputString.toString());
        }

      } else {

        if (!input.isEmpty()) {
          tooltip.add(WailaUtil.getStackRenderString(input)
              + WailaUtil.getProgressRenderString((int) (100 * progress), 100)
              + WailaUtil.getStackRenderString(output));
        }
      }

      tooltip.add(renderString.toString());
    }

    return tooltip;
  }
}
