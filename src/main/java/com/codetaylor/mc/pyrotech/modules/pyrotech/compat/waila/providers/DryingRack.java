package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.DryingRackRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileDryingRackBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class DryingRack
    implements IWailaDataProvider {

  @Nonnull
  @Override
  public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {

    return ItemStack.EMPTY;
  }

  @Nonnull
  @Override
  public List<String> getWailaHead(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    return tooltip;
  }

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    if (!config.getConfig(WailaRegistrar.CONFIG_PROGRESS)) {
      return tooltip;
    }

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileDryingRackBase) {

      TileDryingRackBase tile = (TileDryingRackBase) tileEntity;

      tooltip.add(Util.translateFormatted(
          "gui." + ModulePyrotech.MOD_ID + ".waila.drying_rack.speed",
          String.valueOf((int) (tile.getSpeed() * 100)) + "%"
      ));

      ItemStackHandler stackHandler = tile.getStackHandler();
      ItemStackHandler outputStackHandler = tile.getOutputStackHandler();

      for (int i = 0; i < stackHandler.getSlots(); i++) {

        ItemStack inputStack = stackHandler.getStackInSlot(i);
        float progress = tile.getProgress(i);

        if (!inputStack.isEmpty()) {

          // Display input item and recipe output.

          StringBuilder renderString = new StringBuilder();
          renderString.append(WailaUtil.getStackRenderString(inputStack));

          DryingRackRecipe recipe = DryingRackRecipe.getRecipe(inputStack);

          if (recipe != null) {
            ItemStack recipeOutput = recipe.getOutput();
            recipeOutput.setCount(inputStack.getCount());
            renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
            renderString.append(WailaUtil.getStackRenderString(recipeOutput));
          }

          tooltip.add(renderString.toString());
        }
      }

      StringBuilder renderString = new StringBuilder();

      for (int i = 0; i < outputStackHandler.getSlots(); i++) {

        // Display output items.

        ItemStack outputStack = outputStackHandler.getStackInSlot(i);

        if (!outputStack.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(outputStack));
        }

      }

      if (renderString.length() > 0) {
        tooltip.add(renderString.toString());
      }

    }

    return tooltip;
  }

  @Nonnull
  @Override
  public List<String> getWailaTail(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    return tooltip;
  }

  @Nonnull
  @Override
  public NBTTagCompound getNBTData(
      EntityPlayerMP player,
      TileEntity tileEntity,
      NBTTagCompound tag,
      World world,
      BlockPos pos
  ) {

    return tag;
  }
}
