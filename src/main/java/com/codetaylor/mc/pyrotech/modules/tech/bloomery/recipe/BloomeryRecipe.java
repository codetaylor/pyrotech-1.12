package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.athenaeum.util.WeightedPicker;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
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

    for (BloomeryRecipe recipe : ModuleBloomery.Registries.BLOOMERY_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleBloomery.Registries.BLOOMERY_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final ItemStack outputBloom;
  private final int burnTimeTicks;
  private final float failureChance;
  private final int bloomYieldMin;
  private final int bloomYieldMax;
  private final int slagCount;
  private final ItemStack[] failureItems;
  private final ItemStack slagItem;

  @Nullable
  private final String langKey;

  public BloomeryRecipe(
      ResourceLocation resourceLocation,
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      int bloomYieldMin,
      int bloomYieldMax,
      int slagCount,
      ItemStack slagItem,
      ItemStack[] failureItems,
      @Nullable String langKey
  ) {

    this.slagItem = slagItem;

    this.setRegistryName(resourceLocation);

    this.output = output;
    this.input = input;
    this.burnTimeTicks = burnTimeTicks;
    this.failureChance = MathHelper.clamp(failureChance, 0, 1);
    this.bloomYieldMin = bloomYieldMin;
    this.bloomYieldMax = bloomYieldMax;
    this.slagCount = slagCount;

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

    this.failureItems = failureItems;
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
        this.getRegistryName().toString().replaceAll("\\.slag", ""),
        this.langKey
    );
  }

  public int getSlagCount() {

    return this.slagCount;
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

  public ItemStack getSlagItemStack() {

    return this.slagItem;
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

    WeightedPicker<ItemStack> picker = new WeightedPicker<>();

    for (ItemStack itemStack : this.failureItems) {
      picker.add(itemStack.getCount(), itemStack);
    }

    ItemStack result = picker.get().copy();
    result.setCount(1);
    return result;
  }

  private String getLangKeyFrom(Ingredient input) {

    ItemStack[] matchingStacks = input.getMatchingStacks();
    return matchingStacks[0].getUnlocalizedName();
  }
}
