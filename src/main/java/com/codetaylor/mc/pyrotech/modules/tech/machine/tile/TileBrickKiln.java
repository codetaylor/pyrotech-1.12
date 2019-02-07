package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneKilnRecipe;
import net.minecraft.item.ItemStack;

public class TileBrickKiln
    extends TileKilnBase<StoneKilnRecipe> {

  @Override
  public StoneKilnRecipe getRecipe(ItemStack itemStack) {

    return StoneKilnRecipe.getRecipe(itemStack);
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.BRICK_KILN.ALLOW_AUTOMATION;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.BRICK_KILN.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.BRICK_KILN.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.BRICK_KILN.FUEL_SLOT_SIZE;
  }

}
