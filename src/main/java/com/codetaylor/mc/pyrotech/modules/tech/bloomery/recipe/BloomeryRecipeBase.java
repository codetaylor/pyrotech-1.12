package com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.WeightedPicker;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class BloomeryRecipeBase<T extends BloomeryRecipeBase<T>>
    extends IForgeRegistryEntry.Impl<T>
    implements IRecipeSingleOutput,
    IRecipeTimed {

  protected final Ingredient input;
  protected final ItemStack output;
  protected ItemStack outputBloom;
  protected final int burnTimeTicks;
  protected final float failureChance;
  protected final int bloomYieldMin;
  protected final int bloomYieldMax;
  protected final int slagCount;
  protected final FailureItem[] failureItems;
  protected final ItemStack slagItem;
  protected final AnvilRecipe.EnumTier[] anvilTiers;
  @Nullable
  protected final String langKey;

  public BloomeryRecipeBase(Ingredient input, ItemStack output, int burnTimeTicks, float failureChance, int bloomYieldMin, int bloomYieldMax, int slagCount, FailureItem[] failureItems, ItemStack slagItem, AnvilRecipe.EnumTier[] anvilTiers, @Nullable String langKey) {

    this.input = input;
    this.output = output;
    this.burnTimeTicks = burnTimeTicks;
    this.failureChance = MathHelper.clamp(failureChance, 0, 1);
    this.bloomYieldMin = bloomYieldMin;
    this.bloomYieldMax = bloomYieldMax;
    this.slagCount = slagCount;
    this.failureItems = failureItems;
    this.slagItem = slagItem;
    this.anvilTiers = anvilTiers;

    if (langKey != null) {
      this.langKey = langKey;

    } else {
      this.langKey = this.getLangKeyFrom(this.input);
    }
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public ItemStack getOutputBloom() {

    if (this.outputBloom == null) {
      // We're lazily instantiating the output bloom here
      // so we can pass the registry name, which is important
      // for comparing blooms in JEI.
      this.outputBloom = BloomHelper.createBloomAsItemStack(
          this.bloomYieldMax,
          this.getRegistryName().toString(),
          this.langKey
      );
    }

    return this.outputBloom.copy();
  }

  public int getBloomYieldMin() {

    return this.bloomYieldMin;
  }

  public int getBloomYieldMax() {

    return this.bloomYieldMax;
  }

  public AnvilRecipe.EnumTier[] getAnvilTiers() {

    return this.anvilTiers;
  }

  public boolean isAnvilTier(AnvilRecipe.EnumTier tier) {

    return ArrayHelper.contains(this.anvilTiers, tier);
  }

  public ItemStack getUniqueBloomFromOutput(int quantity) {

    int integrity = 0;
    int iterations = Math.max(1, quantity);

    for (int i = 0; i < iterations; i++) {
      integrity += MathHelper.getInt(RandomHelper.random(), this.bloomYieldMin, this.bloomYieldMax);
    }

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

  public FailureItem[] getFailureItems() {

    return this.failureItems;
  }

  public ItemStack getSlagItemStack() {

    return this.slagItem;
  }

  @Nullable
  public String getLangKey() {

    return this.langKey;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }

  public ItemStack getRandomOutput(EntityPlayer player) {

    ItemStack heldItemMainhand = player.getHeldItemMainhand();
    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(heldItemMainhand);

    double modifier = 1;

    if (enchantments.containsKey(Enchantments.SILK_TOUCH)) {
      modifier = MathHelper.clamp(ModuleTechBloomeryConfig.BLOOM.SILK_TOUCH_FAILURE_MODIFIER, 0, 1);
    }

    if (RandomHelper.random().nextDouble() < this.failureChance * modifier) {
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

    for (FailureItem failureItem : this.failureItems) {
      picker.add(failureItem.getWeight(), failureItem.getItemStack());
    }

    ItemStack result = picker.get().copy();
    result.setCount(1);
    return result;
  }

  protected String getLangKeyFrom(Ingredient input) {

    ItemStack[] matchingStacks = input.getMatchingStacks();
    return matchingStacks[0].getUnlocalizedName();
  }

  public static class FailureItem {

    private final ItemStack itemStack;
    private final int weight;

    public FailureItem(ItemStack itemStack, int weight) {

      this.itemStack = itemStack;
      this.weight = weight;
    }

    public int getWeight() {

      return this.weight;
    }

    public ItemStack getItemStack() {

      return this.itemStack;
    }
  }
}
