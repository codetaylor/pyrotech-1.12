package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.providers;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaRegistrar;
import com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.StoneMachineRecipeItemInItemOutBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileStoneTop;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
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

public class CombustionWorkerStone
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

    if (tileEntity instanceof TileCombustionWorkerStoneItemInItemOutBase
        || tileEntity instanceof TileStoneTop) {

      TileCombustionWorkerStoneItemInItemOutBase tile = null;

      if (tileEntity instanceof TileCombustionWorkerStoneItemInItemOutBase) {
        tile = (TileCombustionWorkerStoneItemInItemOutBase) tileEntity;

      } else {

        if (((TileStoneTop) tileEntity).isCustom()) {
          return tooltip;
        }

        World world = tileEntity.getWorld();
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileCombustionWorkerStoneItemInItemOutBase) {
          tile = (TileCombustionWorkerStoneItemInItemOutBase) candidate;
        }
      }

      if (tile == null) {
        return tooltip;
      }

      float progress = tile.workerGetProgress(0);

      ItemStackHandler stackHandler = tile.getInputStackHandler();
      ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
      ItemStackHandler fuelStackHandler = tile.getFuelStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);
      boolean hasOutput = !outputStackHandler.getStackInSlot(0).isEmpty();
      ItemStack fuel = fuelStackHandler.getStackInSlot(0);

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();
        renderString.append(WailaUtil.getStackRenderString(input));

        if (!fuel.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(fuel));
        }

        StoneMachineRecipeItemInItemOutBase recipe = tile.getRecipe(input);

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();
          recipeOutput.setCount(input.getCount());
          renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
          renderString.append(WailaUtil.getStackRenderString(recipeOutput));
        }

        tooltip.add(renderString.toString());

      } else if (hasOutput) {

        // Display output items.

        //tooltip.add(Util.translate("gui." + ModuleCharcoal.MOD_ID + ".waila.kiln.brick.finished"));

        StringBuilder renderString = new StringBuilder();

        for (int i = 0; i < outputStackHandler.getSlots(); i++) {
          ItemStack stackInSlot = outputStackHandler.getStackInSlot(i);

          if (!stackInSlot.isEmpty()) {
            renderString.append(WailaUtil.getStackRenderString(stackInSlot));
          }
        }

        //tooltip.add(Util.translate("gui." + ModuleCharcoal.MOD_ID + ".waila.result"));
        tooltip.add(renderString.toString());
      }

      {
        if (tile.combustionGetBurnTimeRemaining() > 0) {
          ItemStack fuelStack = tile.getFuelStackHandler().getStackInSlot(0);
          tooltip.add(Util.translateFormatted(
              "gui." + ModulePyrotech.MOD_ID + ".waila.burn.time",
              StringHelper.ticksToHMS(tile.combustionGetBurnTimeRemaining() + fuelStack.getCount() * StackHelper.getItemBurnTime(fuelStack))
          ));
        }

        if (!fuel.isEmpty()) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModulePyrotech.MOD_ID + ".waila.kiln.brick.fuel",
              fuel.getItem().getItemStackDisplayName(fuel) + " * " + fuel.getCount()
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
