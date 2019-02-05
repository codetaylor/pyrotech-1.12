package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInItemOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCapabilityDelegateMachineTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class CombustionWorkerStoneItemInItemOutProvider
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

    if (tileEntity instanceof TileCombustionWorkerStoneItemInItemOutBase
        || tileEntity instanceof TileCapabilityDelegateMachineTop) {

      TileCombustionWorkerStoneItemInItemOutBase tile = null;

      if (tileEntity instanceof TileCombustionWorkerStoneItemInItemOutBase) {
        tile = (TileCombustionWorkerStoneItemInItemOutBase) tileEntity;

      } else {
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
      StoneMachineRecipeItemInItemOutBase recipe = null;

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();
        renderString.append(WailaUtil.getStackRenderString(input));

        if (!fuel.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(fuel));
        }

        recipe = (StoneMachineRecipeItemInItemOutBase) tile.getRecipe(input);

        if (recipe != null) {
          ItemStack recipeOutput = recipe.getOutput();
          recipeOutput.setCount(input.getCount());
          renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
          renderString.append(WailaUtil.getStackRenderString(recipeOutput));
        }

        tooltip.add(renderString.toString());

        if (recipe != null) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModuleTechMachine.MOD_ID + ".waila.recipe",
              StringHelper.ticksToHMS((int) (recipe.getTimeTicks() * (1 - progress)))
          ));
        }

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
        ItemStack fuelStack = tile.getFuelStackHandler().getStackInSlot(0);

        if (tile.combustionGetBurnTimeRemaining() > 0
            || !fuelStack.isEmpty()) {

          int ticks = tile.combustionGetBurnTimeRemaining() + fuelStack.getCount() * StackHelper.getItemBurnTime(fuelStack);

          if (recipe != null) {

            if (ticks < recipe.getTimeTicks() * (1 - progress)) {
              tooltip.add(TextFormatting.RED + Util.translateFormatted(
                  "gui." + ModuleTechMachine.MOD_ID + ".waila.burn.time",
                  StringHelper.ticksToHMS(ticks)
              ));

            } else {
              tooltip.add(TextFormatting.GREEN + Util.translateFormatted(
                  "gui." + ModuleTechMachine.MOD_ID + ".waila.burn.time",
                  StringHelper.ticksToHMS(ticks)
              ));
            }

          } else {
            tooltip.add(Util.translateFormatted(
                "gui." + ModuleTechMachine.MOD_ID + ".waila.burn.time",
                StringHelper.ticksToHMS(ticks)
            ));
          }
        }

        if (!fuel.isEmpty()) {
          tooltip.add(Util.translateFormatted(
              "gui." + ModuleTechMachine.MOD_ID + ".waila.fuel",
              fuel.getItem().getItemStackDisplayName(fuel) + " * " + fuel.getCount()
          ));
        }
      }

    }

    return tooltip;
  }

}
