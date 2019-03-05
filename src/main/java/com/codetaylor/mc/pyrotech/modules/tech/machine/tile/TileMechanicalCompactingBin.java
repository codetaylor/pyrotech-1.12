package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.library.CompactingBinRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompactingBin;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.MechanicalCompactingBinRecipe;
import net.minecraft.item.ItemStack;

public class TileMechanicalCompactingBin
    extends TileCompactingBin {

  @Override
  public CompactingBinRecipeBase getRecipe(ItemStack itemStack) {

    return MechanicalCompactingBinRecipe.getRecipe(itemStack);
  }

  @Override
  public int getInputCapacity() {

    return ModuleTechMachineConfig.MECHANICAL_COMPACTING_BIN.INPUT_CAPACITY;
  }
}
