package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCampfire;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class CampfireDataProvider
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

    if (tileEntity instanceof TileCampfire) {

      TileCampfire tileCampfire;
      tileCampfire = (TileCampfire) tileEntity;

      float progress = tileCampfire.getProgress();

      ItemStackHandler stackHandler = tileCampfire.getStackHandler();
      ItemStackHandler outputStackHandler = tileCampfire.getOutputStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);
      boolean hasOutput = !outputStackHandler.getStackInSlot(0).isEmpty();

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();
        renderString.append(WailaUtil.getStackRenderString(input));
        //renderString.append(WailaUtil.getStackRenderString(fuel));

        ItemStack recipeOutput = FurnaceRecipes.instance().getSmeltingResult(input);

        if (!recipeOutput.isEmpty()) {
          recipeOutput.setCount(input.getCount());
          renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
          renderString.append(WailaUtil.getStackRenderString(recipeOutput));
        }

        tooltip.add(renderString.toString());

      } else if (hasOutput) {

        // Display output items.

        StringBuilder renderString = new StringBuilder();

        for (int i = 0; i < outputStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            renderString.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        tooltip.add(renderString.toString());
      }

      {
        int fuelRemaining = tileCampfire.getFuelRemaining();

        if (fuelRemaining > 0) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModulePyrotech.MOD_ID + ".waila.campfire.fuel",
              String.valueOf(fuelRemaining)
          ));
        }

        if (tileCampfire.isActive()
            && tileCampfire.getRemainingBurnTimeTicks() > 0) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModulePyrotech.MOD_ID + ".waila.campfire.heat",
              StringHelper.ticksToHMS(tileCampfire.getRemainingBurnTimeTicks())
          ));
        }
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
