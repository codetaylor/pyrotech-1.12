package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class GraniteAnvilRecipe
    extends IForgeRegistryEntry.Impl<GraniteAnvilRecipe>
    implements IRecipeSingleOutput {

  public enum EnumType {
    HAMMER, PICKAXE
  }

  @Nullable
  public static GraniteAnvilRecipe getRecipe(ItemStack input) {

    for (GraniteAnvilRecipe recipe : ModulePyrotechRegistries.GRANITE_ANVIL_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.GRANITE_ANVIL_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int hits;
  private final EnumType type;

  public GraniteAnvilRecipe(
      ItemStack output,
      Ingredient input,
      int hits,
      EnumType type
  ) {

    this.input = input;
    this.output = output;
    this.hits = hits;
    this.type = type;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getHits() {

    return this.hits;
  }

  public EnumType getType() {

    return this.type;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }

  public static class BloomAnvilRecipe
      extends GraniteAnvilRecipe {

    private final BloomeryRecipe bloomeryRecipe;

    public BloomAnvilRecipe(ItemStack output, Ingredient input, int hits, EnumType type, BloomeryRecipe bloomeryRecipe) {

      super(output, input, hits, type);
      this.bloomeryRecipe = bloomeryRecipe;
    }

    public BloomeryRecipe getBloomeryRecipe() {

      return this.bloomeryRecipe;
    }

    @Override
    public boolean matches(ItemStack input) {

      if (input.getItem() != Item.getItemFromBlock(ModuleBlocks.BLOOM)) {
        return false;
      }

      NBTTagCompound inputTag = input.getTagCompound();

      if (inputTag == null) {
        return false;
      }

      NBTTagCompound inputTileTag = inputTag.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);

      if (inputTileTag.getSize() == 0) {
        return false;
      }

      String recipeId = inputTileTag.getString("recipeId");

      if (recipeId.isEmpty()) {
        return false;
      }

      //noinspection ConstantConditions
      return this.getRegistryName().toString().equals(recipeId);
    }
  }
}
