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

    ResourceLocation resourceLocation = CompostBinRecipe.getResourceLocation(ModuleTechBasic.MOD_ID, input, input.getMetadata());
    CompostBinRecipe recipe = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocation);

    if (recipe != null) {
      return recipe;
    }

    resourceLocation = CompostBinRecipe.getResourceLocation(ModuleTechBasic.MOD_ID, input, OreDictionary.WILDCARD_VALUE);
    return ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocation);
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

    ResourceLocation resourceLocation = CompostBinRecipe.getResourceLocation(ModuleTechBasic.MOD_ID, input, input.getMetadata());
    CompostBinRecipe recipe = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocation);

    if (recipe != null && recipe.matches(input, output)) {
      return recipe;
    }

    resourceLocation = CompostBinRecipe.getResourceLocation(ModuleTechBasic.MOD_ID, input, OreDictionary.WILDCARD_VALUE);
    recipe = ModuleTechBasic.Registries.COMPOST_BIN_RECIPE.getValue(resourceLocation);

    if (recipe != null && recipe.matches(input, output)) {
      return recipe;
    }

    return null;
  }

  public static ResourceLocation getResourceLocation(String resourceDomain, ItemStack itemStack, int meta) {

    Item item = itemStack.getItem();
    ResourceLocation resourceLocation = item.getRegistryName();

    /*
    This will throw an NPE if the item's resource location is null.
    The resource location will never be null during normal gameplay because this
    null check is performed during recipe addition. No recipe will ever be added
    for an item with a null resource location.
     */
    if (resourceLocation == null) {
      throw new NullPointerException(String.format("Item %s is missing a resource location", itemStack.toString()));
    }

    String domain = resourceLocation.getResourceDomain().toLowerCase();
    String path = resourceLocation.getResourcePath().toLowerCase();
    return new ResourceLocation(resourceDomain, domain + "_" + path + "_" + meta);
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
