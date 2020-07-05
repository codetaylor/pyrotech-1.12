package com.codetaylor.mc.pyrotech.library;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class CompostBinRecipeBase<T extends IForgeRegistryEntry<T>>
    extends IForgeRegistryEntry.Impl<T>
    implements IRecipeSingleOutput {

  private final ItemStack input;
  private final ItemStack output;
  private final int compostValue;

  public CompostBinRecipeBase(ItemStack output, ItemStack input) {

    this.input = input;
    this.output = output;
    this.compostValue = this.calculateCompostValue(input);
  }

  public CompostBinRecipeBase(
      ItemStack output,
      ItemStack input,
      int compostValue
  ) {

    this.input = input;
    this.output = output;
    this.compostValue = compostValue;
  }

  public ItemStack getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getCompostValue() {

    return this.compostValue;
  }

  public int calculateCompostValue(ItemStack input) {

    Item item = input.getItem();

    if (item instanceof ItemFood) {
      int min = ModuleTechBasicConfig.COMPOST_BIN.GENERATED_FOOD_COMPOST_VALUE_RANGE[0];
      int max = ModuleTechBasicConfig.COMPOST_BIN.GENERATED_FOOD_COMPOST_VALUE_RANGE[1];

      float hunger = Math.max(0, Math.min(1, ((ItemFood) item).getHealAmount(input) / 20f));
      float saturation = Math.max(0, Math.min(1, (hunger * ((ItemFood) item).getSaturationModifier(input) * 2) / 20f));
      float scalar = (saturation + hunger) * 0.5f;

      return Math.max(min, Math.min(max, (int) ((max - min) * scalar + min)));
    }

    return 1;
  }

  public boolean matches(ItemStack input) {

    if (this.input.getItem() == input.getItem()) {
      int i = this.input.getMetadata();
      return i == OreDictionary.WILDCARD_VALUE || i == input.getMetadata();
    }

    return false;
  }

  public boolean matches(ItemStack input, ItemStack output) {

    if (!this.matches(input)) {
      return false;
    }

    if (this.output.getItem() == output.getItem()) {
      return (this.output.getMetadata() == output.getMetadata());
    }

    return false;
  }
}
