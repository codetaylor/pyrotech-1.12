package com.codetaylor.mc.pyrotech.modules.bloomery.recipe;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.GraniteAnvilRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;

public class BloomAnvilRecipe
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

    if (input.getItem() != ModuleBloomery.Items.BLOOM) {
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
