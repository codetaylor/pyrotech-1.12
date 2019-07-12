package com.codetaylor.mc.pyrotech.modules.tech.machine.plugin.waila.delegate;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public abstract class CombustionMachineProviderDelegateBase<D extends CombustionMachineProviderDelegateBase.ICombustionMachineDisplay, T extends TileCombustionWorkerStoneBase, R extends MachineRecipeBase>
    extends ProviderDelegateBase<D, T> {

  protected CombustionMachineProviderDelegateBase(D display) {

    super(display);
  }

  public void addBurnTimeInfo(T tile, float progress, ItemStack input, ItemStack fuel, @Nullable R recipe) {

    ItemStack fuelStack = tile.getFuelStackHandler().getStackInSlot(0);
    int ticks = tile.combustionGetBurnTimeRemaining();

    if (!fuelStack.isEmpty()) {
      ticks += fuelStack.getCount() * StackHelper.getItemBurnTime(fuelStack);
    }

    {
      String langKey = "gui." + ModuleTechMachine.MOD_ID + ".waila.burn.time";
      String burnTimeString = StringHelper.ticksToHMS(ticks);

      if (recipe != null) {
        float recipeTimeTicks = recipe.getTimeTicks() * (1 - progress);
        recipeTimeTicks = this.getModifiedRecipeTimeTicks(recipeTimeTicks, tile, input, recipe);

        if (ticks < recipeTimeTicks) {
          this.display.setBurnTime(TextFormatting.RED, langKey, burnTimeString);

        } else {
          this.display.setBurnTime(TextFormatting.GREEN, langKey, burnTimeString);
        }

      } else {
        this.display.setBurnTime(null, langKey, burnTimeString);
      }
    }

    if (!fuel.isEmpty()) {
      String langKey = "gui." + ModuleTechMachine.MOD_ID + ".waila.fuel";
      String fuelCountString = (fuel.getCount() > 1) ? " * " + fuel.getCount() : "";
      this.display.setFuel(langKey, fuel, fuelCountString);
    }
  }

  public float getModifiedRecipeTimeTicks(float recipeTimeTicks, T tile, ItemStack input, R recipe) {

    return recipeTimeTicks;
  }

  public interface ICombustionMachineDisplay {

    void setBurnTime(@Nullable TextFormatting formatting, String langKey, String burnTimeString);

    void setFuel(String langKey, ItemStack fuel, String count);
  }
}
