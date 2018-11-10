package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.pyrotech.modules.pyrotech.Registries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Iterator;

public class KilnBrickRecipe
    extends IForgeRegistryEntry.Impl<KilnBrickRecipe>
    implements IRecipeTimed {

  @Nullable
  public static KilnBrickRecipe getRecipe(ItemStack input) {

    for (KilnBrickRecipe recipe : Registries.KILN_BRICK_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    boolean recipesRemoved = false;
    Iterator<KilnBrickRecipe> iterator = Registries.KILN_BRICK_RECIPE.iterator();

    while (iterator.hasNext()) {
      KilnBrickRecipe recipe = iterator.next();

      if (output.apply(recipe.output)) {
        iterator.remove();
        recipesRemoved = true;
      }
    }

    return recipesRemoved;
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int burnTimeTicks;
  private final float failureChance;
  private final ItemStack[] failureItems;

  public KilnBrickRecipe(
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      ItemStack[] failureItems
  ) {

    this.input = input;
    this.output = output;
    this.burnTimeTicks = burnTimeTicks;
    this.failureChance = MathHelper.clamp(failureChance, 0, 1);
    this.failureItems = failureItems;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getTimeTicks() {

    return this.burnTimeTicks;
  }

  public float getFailureChance() {

    return this.failureChance;
  }

  public ItemStack[] getFailureItems() {

    return this.failureItems;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
