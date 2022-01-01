package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.CompostBinRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
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

    Item inputItem = input.getItem();
    ResourceLocation registryName = inputItem.getRegistryName();
    if (registryName == null) return null;
    String resourceDomain = registryName.getResourceDomain().toLowerCase();
    String resourcePath = registryName.getResourcePath().toLowerCase();
    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechBasic.MOD_ID, resourceDomain + "_" + resourcePath + "_" + input.getMetadata());
    ResourceLocation resourceLocationWild = new ResourceLocation(ModuleTechBasic.MOD_ID, resourceDomain + "_" + resourcePath + "_" + OreDictionary.WILDCARD_VALUE);
    
    CompostBinRecipe recipe = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocation);
    return (recipe != null) ? recipe : ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocationWild);
  }

  @Nullable
  public static CompostBinRecipe getRecipe(ItemStack input, ItemStack output) {

    Item inputItem = input.getItem();
    ResourceLocation registryName = inputItem.getRegistryName();
    if (registryName == null) return null;
    String resourceDomain = registryName.getResourceDomain().toLowerCase();
    String resourcePath = registryName.getResourcePath().toLowerCase();
    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechBasic.MOD_ID, resourceDomain + "_" + resourcePath + "_" + input.getMetadata());
    ResourceLocation resourceLocationWild = new ResourceLocation(ModuleTechBasic.MOD_ID, resourceDomain + "_" + resourcePath + "_" + OreDictionary.WILDCARD_VALUE);

    CompostBinRecipe recipe = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocation);

    if (recipe != null && recipe.matches(input, output)) {
      return recipe;
    }

    recipe = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocationWild);

    if (recipe != null && recipe.matches(input, output)) {
      return recipe;
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
