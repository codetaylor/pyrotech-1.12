package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.KilnStoneRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInItemOutBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileStoneKiln
    extends TileCombustionWorkerStoneItemInItemOutBase<KilnStoneRecipe> {

  @Override
  public KilnStoneRecipe getRecipe(ItemStack itemStack) {

    return KilnStoneRecipe.getRecipe(itemStack);
  }

  @Override
  protected List<ItemStack> getRecipeOutput(KilnStoneRecipe recipe, ItemStack input, ArrayList<ItemStack> outputItemStacks) {

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

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.STONE_KILN.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.STONE_KILN.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.STONE_KILN.FUEL_SLOT_SIZE;
  }

}
