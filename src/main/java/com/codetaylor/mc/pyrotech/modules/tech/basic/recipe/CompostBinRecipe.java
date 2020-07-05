package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.CompostBinRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CompostBinRecipe
    extends CompostBinRecipeBase<CompostBinRecipe> {

  @Nullable
  public static CompostBinRecipe getRecipe(ItemStack input) {

    for (CompostBinRecipe recipe : ModuleTechBasic.Registries.COMPOST_BIN_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  @Nullable
  public static CompostBinRecipe getRecipe(ItemStack input, ItemStack output) {

    for (CompostBinRecipe recipe : ModuleTechBasic.Registries.COMPOST_BIN_RECIPE) {

      if (recipe.matches(input, output)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipesByOutput(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.COMPOST_BIN_RECIPE, output);
  }

  public static boolean removeRecipesByInput(Ingredient output) {

    IForgeRegistryModifiable<CompostBinRecipe> registry = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE;
    Collection<CompostBinRecipe> recipes = registry.getValuesCollection();
    List<ResourceLocation> toRemove = new ArrayList<>(1);
    Iterator<CompostBinRecipe> iterator = recipes.iterator();

    while (iterator.hasNext()) {
      CompostBinRecipe recipe = iterator.next();

      if (output.apply(recipe.getInput())) {
        toRemove.add(recipe.getRegistryName());
      }
    }

    for (ResourceLocation resourceLocation : toRemove) {
      registry.remove(resourceLocation);
    }

    return !toRemove.isEmpty();
  }

  public CompostBinRecipe(ItemStack output, ItemStack input) {

    super(output, input);
  }

  public CompostBinRecipe(ItemStack output, ItemStack input, int compostValue) {

    super(output, input, compostValue);
  }
}
