package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeBaseKiln;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class TileKilnBase<E extends MachineRecipeBaseKiln<E>>
    extends TileCombustionWorkerStoneItemInItemOutBase<E> {

  @Override
  protected List<ItemStack> getRecipeOutput(E recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

    ItemStack output = recipe.getOutput();
    output.setCount(1);

    ItemStack[] failureItems = recipe.getFailureItems();
    float failureChance = recipe.getFailureChance();
    int inputCount = input.getCount();

    for (int i = 0; i < inputCount; i++) {

      if (Util.RANDOM.nextFloat() < failureChance) {

        if (failureItems.length > 0) {
          ItemStack failureItemStack = failureItems[Util.RANDOM.nextInt(failureItems.length)].copy();
          failureItemStack.setCount(1);
          outputItemStacks.add(failureItemStack);

        } else {
          outputItemStacks.add(ItemMaterial.EnumType.PIT_ASH.asStack(inputCount));
        }

      } else {
        outputItemStacks.add(output.copy());
      }
    }

    return outputItemStacks;
  }
}
