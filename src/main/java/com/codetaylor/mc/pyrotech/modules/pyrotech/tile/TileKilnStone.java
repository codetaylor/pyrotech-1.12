package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.KilnStoneRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileCombustionWorkerStoneBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileKilnStone
    extends TileCombustionWorkerStoneBase<KilnStoneRecipe> {

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

    return ModulePyrotechConfig.STONE_KILN.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModulePyrotechConfig.STONE_KILN.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModulePyrotechConfig.STONE_KILN.FUEL_SLOT_SIZE;
  }
}
