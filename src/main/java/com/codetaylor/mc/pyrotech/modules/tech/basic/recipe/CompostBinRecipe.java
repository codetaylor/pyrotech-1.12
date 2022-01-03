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
import java.util.List;

public class CompostBinRecipe
    extends CompostBinRecipeBase<CompostBinRecipe> {

  /**
   * Attempt to retrieve a recipe from the given {@link ItemStack} by rebuilding
   * the recipe's name from the stack's resource location and metadata.
   * <p>
   * First attempts to retrieve a recipe using the stack's exact metadata, then
   * attempts to retrieve a recipe using the wildcard value.
   *
   * @param input item stack
   * @return recipe or null
   */
  @Nullable
  public static CompostBinRecipe getRecipe(ItemStack input) {

    Item inputItem = input.getItem();
    ResourceLocation registryName = inputItem.getRegistryName();

    if (registryName == null) {
      return null;
    }

    String resourceDomain = registryName.getResourceDomain().toLowerCase();
    String resourcePath = registryName.getResourcePath().toLowerCase();
    String resourceStringPrefix = resourceDomain + "_" + resourcePath + "_";

    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechBasic.MOD_ID, resourceStringPrefix + input.getMetadata());
    CompostBinRecipe recipe = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocation);

    if (recipe != null) {
      return recipe;
    }

    ResourceLocation resourceLocationWild = new ResourceLocation(ModuleTechBasic.MOD_ID, resourceStringPrefix + OreDictionary.WILDCARD_VALUE);
    return ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocationWild);
  }

  /**
   * Attempt to retrieve a recipe from the given {@link ItemStack} by rebuilding
   * the recipe's name from the input stack's resource location and metadata.
   * <p>
   * First attempts to retrieve a recipe using the stack's exact metadata, then
   * attempts to retrieve a recipe using the wildcard value.
   * <p>
   * When a recipe is successfully retrieved from the registry, a subsequent
   * check is made that the recipe matches the given output {@link ItemStack}.
   *
   * @param input  the input item
   * @param output the output item
   * @return recipe or null
   */
  @Nullable
  public static CompostBinRecipe getRecipe(ItemStack input, ItemStack output) {

    Item inputItem = input.getItem();
    ResourceLocation registryName = inputItem.getRegistryName();

    if (registryName == null) {
      return null;
    }

    String resourceDomain = registryName.getResourceDomain().toLowerCase();
    String resourcePath = registryName.getResourcePath().toLowerCase();
    String resourceStringPrefix = resourceDomain + "_" + resourcePath + "_";

    ResourceLocation resourceLocation = new ResourceLocation(ModuleTechBasic.MOD_ID, resourceStringPrefix + input.getMetadata());
    CompostBinRecipe recipe = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocation);

    if (recipe != null && recipe.matches(input, output)) {
      return recipe;
    }

    ResourceLocation resourceLocationWild = new ResourceLocation(ModuleTechBasic.MOD_ID, resourceStringPrefix + OreDictionary.WILDCARD_VALUE);
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

    for (CompostBinRecipe recipe : recipes) {

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
