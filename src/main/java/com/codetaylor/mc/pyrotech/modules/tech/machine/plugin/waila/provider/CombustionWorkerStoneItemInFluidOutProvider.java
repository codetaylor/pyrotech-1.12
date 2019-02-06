package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.provider;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInFluidOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCapabilityDelegateMachineTop;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInFluidOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class CombustionWorkerStoneItemInFluidOutProvider
    extends CombustionWorkerProvider<TileCombustionWorkerStoneItemInFluidOutBase, StoneMachineRecipeItemInFluidOutBase> {

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileCombustionWorkerStoneItemInFluidOutBase
        || tileEntity instanceof TileCapabilityDelegateMachineTop) {

      TileCombustionWorkerStoneItemInFluidOutBase tile = null;

      if (tileEntity instanceof TileCombustionWorkerStoneItemInFluidOutBase) {
        tile = (TileCombustionWorkerStoneItemInFluidOutBase) tileEntity;

      } else {
        World world = tileEntity.getWorld();
        TileEntity candidate = world.getTileEntity(tileEntity.getPos().down());

        if (candidate instanceof TileCombustionWorkerStoneItemInFluidOutBase) {
          tile = (TileCombustionWorkerStoneItemInFluidOutBase) candidate;
        }
      }

      if (tile == null) {
        return tooltip;
      }

      float progress = tile.workerGetProgress(0);

      ItemStackHandler stackHandler = tile.getInputStackHandler();
      FluidTank outputFluidTank = tile.getOutputFluidTank();
      ItemStackHandler fuelStackHandler = tile.getFuelStackHandler();

      ItemStack input = stackHandler.getStackInSlot(0);
      boolean hasOutput = outputFluidTank.getFluid() != null && outputFluidTank.getFluid().amount > 0;
      ItemStack fuel = fuelStackHandler.getStackInSlot(0);
      StoneMachineRecipeItemInFluidOutBase recipe = null;

      if (!input.isEmpty()) {

        // Display input item and recipe output.

        StringBuilder renderString = new StringBuilder();
        renderString.append(WailaUtil.getStackRenderString(input));

        if (!fuel.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(fuel));
        }

        recipe = (StoneMachineRecipeItemInFluidOutBase) tile.getRecipe(input);

        if (recipe != null) {
          FluidStack output = recipe.getOutput();
          ItemStack filledBucket = FluidUtil.getFilledBucket(output);
          renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
          renderString.append(WailaUtil.getStackRenderString(filledBucket));
        }

        tooltip.add(renderString.toString());

        if (recipe != null) {
          float recipeTimeTicks = recipe.getTimeTicks() * (1 - progress);

          if (!tile.processAsynchronous()
              && input.getCount() > 1) {

            float totalRecipeTimeTicks = recipeTimeTicks + recipe.getTimeTicks() * (input.getCount() - 1);

            tooltip.add(Util.translateFormatted(
                "gui." + ModuleTechMachine.MOD_ID + ".waila.recipe.synchronous",
                StringHelper.ticksToHMS((int) (recipeTimeTicks)),
                StringHelper.ticksToHMS((int) (totalRecipeTimeTicks))
            ));

          } else {
            tooltip.add(Util.translateFormatted(
                "gui." + ModuleTechMachine.MOD_ID + ".waila.recipe",
                StringHelper.ticksToHMS((int) (recipeTimeTicks))
            ));
          }
        }
      }

      if (hasOutput) {

        // Display output fluid.

        FluidStack fluid = outputFluidTank.getFluid();
        tooltip.add(Util.translateFormatted(
            "gui." + ModuleTechMachine.MOD_ID + ".waila.tank.fluid",
            fluid.getLocalizedName(),
            fluid.amount,
            outputFluidTank.getCapacity()
        ));

      } else {
        tooltip.add(Util.translateFormatted(
            Util.translate("gui." + ModuleTechRefractory.MOD_ID + ".waila.tank.empty"),
            0,
            outputFluidTank.getCapacity()
        ));
      }

      this.addBurnTimeInfo(tooltip, tile, progress, input, fuel, recipe);
    }

    return tooltip;
  }

  @Override
  protected float getModifiedRecipeTimeTicks(float recipeTimeTicks, TileCombustionWorkerStoneItemInFluidOutBase tile, ItemStack input, StoneMachineRecipeItemInFluidOutBase recipe) {

    if (!tile.processAsynchronous()
        && input.getCount() > 1) {

      recipeTimeTicks += recipe.getTimeTicks() * (input.getCount() - 1);
    }

    return recipeTimeTicks;
  }
}
