package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.inventory.DynamicStackHandler;
import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileBloom;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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
  private final int burnTimeTicks;
  private final float failureChance;
  private final ItemStack[] failureItems;

  public BloomeryRecipe(
      ItemStack output,
      Ingredient input,
      int burnTimeTicks,
      float failureChance,
      ItemStack[] failureItems,
      @Nullable String langKey
  ) {

    this.input = input;
    this.burnTimeTicks = burnTimeTicks;
    this.failureChance = MathHelper.clamp(failureChance, 0, 1);
    this.failureItems = failureItems;

    TileBloom bloom = new TileBloom();
    DynamicStackHandler stackHandler = bloom.getStackHandler();
    stackHandler.insertItem(output, false);

    if (langKey != null) {
      bloom.setLangKey(langKey);

    } else {
      bloom.setLangKey(this.getLangKeyFrom(this.input));
    }

    this.output = StackHelper.writeTileEntityToItemStack(bloom, new ItemStack(ModuleBlocks.BLOOM));
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public ItemStack getUniqueBloomFromOutput() {

    TileBloom fromBloom = TileBloom.fromItemStack(this.output);
    DynamicStackHandler fromStackHandler = fromBloom.getStackHandler();

    TileBloom toBloom = new TileBloom();
    DynamicStackHandler toStackHandler = toBloom.getStackHandler();

    ItemStack candidate;

    while (!(candidate = fromStackHandler.extractItem(false)).isEmpty()) {

      if (RandomHelper.random().nextDouble() > this.failureChance) {
        toStackHandler.insertItem(candidate, false);

      } else {
        toStackHandler.insertItem(this.selectRandomFailureItemStack().copy(), false);
      }
    }

    toBloom.setLangKey(fromBloom.getLangKey());
    toBloom.setMaxIntegrity(toStackHandler.getTotalItemCount());
    return StackHelper.writeTileEntityToItemStack(toBloom, new ItemStack(ModuleBlocks.BLOOM));
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

  private ItemStack selectRandomFailureItemStack() {

    if (this.failureItems.length == 0) {
      return ItemStack.EMPTY;
    }

    return ArrayHelper.randomElement(this.failureItems, RandomHelper.random());
  }

  private String getLangKeyFrom(Ingredient input) {

    ItemStack[] matchingStacks = input.getMatchingStacks();
    return matchingStacks[0].getUnlocalizedName();
  }
}
