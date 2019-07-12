package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInItemOutBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CombustionMachineItemInItemOutProviderDelegate
    extends CombustionMachineProviderDelegateBase<CombustionMachineItemInItemOutProviderDelegate.ICombustionMachineItemInItemOutDisplay, TileCombustionWorkerStoneItemInItemOutBase, MachineRecipeItemInItemOutBase> {

  public CombustionMachineItemInItemOutProviderDelegate(ICombustionMachineItemInItemOutDisplay display) {

    super(display);
  }

  @Override
  public void display(TileCombustionWorkerStoneItemInItemOutBase tile) {

    float progress = tile.workerGetProgress(0);

    ItemStackHandler stackHandler = tile.getInputStackHandler();
    ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
    ItemStackHandler fuelStackHandler = tile.getFuelStackHandler();

    ItemStack input = stackHandler.getStackInSlot(0);
    boolean hasOutput = !outputStackHandler.getStackInSlot(0).isEmpty();
    ItemStack fuel = fuelStackHandler.getStackInSlot(0);
    MachineRecipeItemInItemOutBase recipe = null;

    if (!input.isEmpty()) {
      recipe = (MachineRecipeItemInItemOutBase) tile.getRecipe(input);
      this.display.setRecipeProgress(input, fuel, recipe, (int) (100 * progress), 100);

      if (recipe != null) {
        String langKey = "gui." + ModuleTechMachine.MOD_ID + ".waila.recipe";
        String duration = StringHelper.ticksToHMS((int) (recipe.getTimeTicks() * (1 - progress)));
        this.display.setRecipeDuration(langKey, duration);
      }

    } else if (hasOutput) {
      this.display.setOutputItems(outputStackHandler);

    } else {
      this.display.optionalNoInputNoOutput();
    }

    this.display.optionalPreBurnTimeInfo();

    this.addBurnTimeInfo(tile, progress, input, fuel, recipe);
  }

  public interface ICombustionMachineItemInItemOutDisplay
      extends CombustionMachineProviderDelegateBase.ICombustionMachineDisplay {

    void setRecipeProgress(ItemStack input, ItemStack fuel, MachineRecipeItemInItemOutBase recipe, int progress, int maxProgress);

    void setRecipeDuration(String langKey, String duration);

    void setOutputItems(ItemStackHandler outputStackHandler);

    default void optionalNoInputNoOutput() {
      //
    }

    default void optionalPreBurnTimeInfo() {
      //
    }
  }
}
