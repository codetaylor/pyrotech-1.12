package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.util.BloomHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class BloomeryRecipe
    extends IForgeRegistryEntry.Impl<BloomeryRecipe>
    implements IRecipeSingleOutput,
    IRecipeTimed {

  @Nullable
  public static BloomeryRecipe getRecipe(ItemStack input) {

    for (BloomeryRecipe recipe : ModulePyrotechRegistries.BLOOMERY_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.BLOOMERY_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final ItemStack outputBloom;
  private final int burnTimeTicks;
  private final float failureChance;
  private final int bloomYieldMin;
  private final int bloomYieldMax;
  private final int slag;
  private final boolean bloomSlag;
  private final int slagColor;
  private final ItemStack[] failureItems;
  private final String langKey;

  public BloomeryRecipe(
      ResourceLocation resourceLocation,
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      int bloomYieldMin,
      int bloomYieldMax,
      int slag,
      boolean bloomSlag,
      int slagColor,
      ItemStack[] failureItems,
      @Nullable String langKey
  ) {

    this.setRegistryName(resourceLocation);

    this.output = output;
    this.input = input;
    this.burnTimeTicks = burnTimeTicks;
    this.failureChance = MathHelper.clamp(failureChance, 0, 1);
    this.bloomYieldMin = bloomYieldMin;
    this.bloomYieldMax = bloomYieldMax;
    this.slag = slag;
    this.bloomSlag = bloomSlag;
    this.slagColor = slagColor;

    if (langKey != null) {
      this.langKey = langKey;

    } else {
      this.langKey = this.getLangKeyFrom(this.input);
    }

    this.outputBloom = BloomHelper.createBloomAsItemStack(
        bloomYieldMax,
        null,
        this.langKey
    );

    if (this.bloomSlag) {
      ItemStack[] toAdd = {BloomHelper.createSlagItem(resourceLocation, this.langKey, this.slagColor)};
      this.failureItems = ArrayHelper.combine(failureItems, toAdd);

    } else {
      this.failureItems = failureItems;
    }
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public ItemStack getOutputBloom() {

    return this.outputBloom.copy();
  }

  public ItemStack getUniqueBloomFromOutput() {

    int integrity = MathHelper.getInt(RandomHelper.random(), this.bloomYieldMin, this.bloomYieldMax);

    return BloomHelper.createBloomAsItemStack(
        integrity,
        this.getRegistryName().toString(),
        this.langKey
    );
  }

  public int getSlag() {

    return this.slag;
  }

  public boolean isBloomSlag() {

    return this.bloomSlag;
  }

  public int getSlagColor() {

    return this.slagColor;
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

  public String getLangKey() {

    return this.langKey;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }

  public ItemStack getRandomOutput() {

    if (RandomHelper.random().nextDouble() < this.failureChance) {
      return this.selectRandomFailureItemStack();

    } else {
      return this.getOutput();
    }
  }

  private ItemStack selectRandomFailureItemStack() {

    if (this.failureItems.length == 0) {
      return ItemStack.EMPTY;
    }

    return ArrayHelper.randomElement(this.failureItems, RandomHelper.random()).copy();
  }

  private String getLangKeyFrom(Ingredient input) {

    ItemStack[] matchingStacks = input.getMatchingStacks();
    return matchingStacks[0].getUnlocalizedName();
  }
}
