package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInFluidOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInFluidOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class CombustionMachineItemInFluidOutProviderDelegate
    extends CombustionMachineProviderDelegateBase<CombustionMachineItemInFluidOutProviderDelegate.ICombustionMachineItemInFluidOutDisplay, TileCombustionWorkerStoneItemInFluidOutBase, MachineRecipeItemInFluidOutBase> {

  public CombustionMachineItemInFluidOutProviderDelegate(ICombustionMachineItemInFluidOutDisplay display) {

    super(display);
  }

  @Override
  public void display(TileCombustionWorkerStoneItemInFluidOutBase tile) {

    float progress = tile.workerGetProgress(0);

    ItemStackHandler stackHandler = tile.getInputStackHandler();
    FluidTank outputFluidTank = tile.getOutputFluidTank();
    ItemStackHandler fuelStackHandler = tile.getFuelStackHandler();

    ItemStack input = stackHandler.getStackInSlot(0);
    boolean hasOutput = outputFluidTank.getFluid() != null && outputFluidTank.getFluid().amount > 0;
    ItemStack fuel = fuelStackHandler.getStackInSlot(0);
    MachineRecipeItemInFluidOutBase recipe = null;

    if (!input.isEmpty()) {
      recipe = (MachineRecipeItemInFluidOutBase) tile.getRecipe(input);
      this.display.setRecipeProgress(input, fuel, recipe, (int) (100 * progress), 100);

      if (recipe != null) {
        float recipeTimeTicks = recipe.getTimeTicks() * (1 - progress);

        if (!tile.processAsynchronous()
            && input.getCount() > 1) {

          float totalRecipeTimeTicks = recipeTimeTicks + recipe.getTimeTicks() * (input.getCount() - 1);

          this.display.setSynchronous(
              "gui." + ModuleTechMachine.MOD_ID + ".waila.recipe.synchronous",
              StringHelper.ticksToHMS((int) (recipeTimeTicks)),
              StringHelper.ticksToHMS((int) (totalRecipeTimeTicks))
          );

        } else {
          this.display.setAsynchronous(
              "gui." + ModuleTechMachine.MOD_ID + ".waila.recipe",
              StringHelper.ticksToHMS((int) (recipeTimeTicks))
          );
        }
      }
    }

    if (hasOutput) {

      // Display output fluid.

      FluidStack fluid = outputFluidTank.getFluid();
      this.display.setFluidTank(
          "gui." + ModuleTechMachine.MOD_ID + ".waila.tank.fluid",
          fluid,
          fluid.amount,
          outputFluidTank.getCapacity()
      );

    } else {

      this.display.setFluidTankEmpty(
          "gui." + ModuleTechRefractory.MOD_ID + ".waila.tank.empty",
          outputFluidTank.getCapacity()
      );
    }

    this.addBurnTimeInfo(tile, progress, input, fuel, recipe);
  }

  @Override
  public float getModifiedRecipeTimeTicks(float recipeTimeTicks, TileCombustionWorkerStoneItemInFluidOutBase tile, ItemStack input, MachineRecipeItemInFluidOutBase recipe) {

    if (!tile.processAsynchronous()
        && input.getCount() > 1) {

      recipeTimeTicks += recipe.getTimeTicks() * (input.getCount() - 1);
    }

    return recipeTimeTicks;
  }

  public interface ICombustionMachineItemInFluidOutDisplay
      extends CombustionMachineProviderDelegateBase.ICombustionMachineDisplay {

    void setRecipeProgress(ItemStack input, ItemStack fuel, MachineRecipeItemInFluidOutBase recipe, int progress, int maxProgress);

    void setSynchronous(String langKey, String recipeTime, String totalRecipeTime);

    void setAsynchronous(String langKey, String recipeTime);

    void setFluidTank(String langKey, FluidStack fluid, int amount, int capacity);

    void setFluidTankEmpty(String langKey, int capacity);
  }
}
