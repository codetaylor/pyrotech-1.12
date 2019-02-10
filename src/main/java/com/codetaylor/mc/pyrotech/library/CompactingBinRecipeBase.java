package com.codetaylor.mc.pyrotech.library;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class CompactingBinRecipeBase<T extends IForgeRegistryEntry<T>>
    extends IForgeRegistryEntry.Impl<T>
    implements IRecipeSingleOutput {

  private final Ingredient input;
  private final ItemStack output;
  private final int amount;
  private final int[] requiredToolUses;

  public CompactingBinRecipeBase(
      ItemStack output,
      Ingredient input,
      int amount,
      int[] requiredToolUses
  ) {

    this.input = input;
    this.output = output;
    this.amount = amount;
    this.requiredToolUses = requiredToolUses;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getAmount() {

    return this.amount;
  }

  public int[] getRequiredToolUses() {

    return this.requiredToolUses;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
