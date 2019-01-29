package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.ModPyrotechRegistries;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorktableRecipe
    extends IForgeRegistryEntry.Impl<WorktableRecipe>
    implements IRecipeSingleOutput {

  private static final Map<ResourceLocation, WorktableRecipe> CACHE = new HashMap<>();
  private static final Set<ResourceLocation> WHITELIST = new HashSet<>();
  private static final Set<ResourceLocation> BLACKLIST = new HashSet<>();

  @Nullable
  public static WorktableRecipe getRecipe(InventoryCrafting inventory, World world) {

    IRecipe recipe = CraftingManager.findMatchingRecipe(inventory, world);

    if (recipe != null) {
      ResourceLocation resourceLocation = recipe.getRegistryName();
      WorktableRecipe cachedRecipe = CACHE.get(resourceLocation);

      if (cachedRecipe != null) {
        return cachedRecipe;
      }

      if (WorktableRecipe.hasWhitelist()
          && WorktableRecipe.isWhitelisted(resourceLocation)) {
        WorktableRecipe worktableRecipe = new WorktableRecipe(recipe);
        CACHE.put(resourceLocation, worktableRecipe);
        return worktableRecipe;

      } else if (WorktableRecipe.hasBlacklist()
          && !WorktableRecipe.isBlacklisted(resourceLocation)) {
        WorktableRecipe worktableRecipe = new WorktableRecipe(recipe);
        CACHE.put(resourceLocation, worktableRecipe);
        return worktableRecipe;
      }

      WorktableRecipe worktableRecipe = new WorktableRecipe(recipe);
      CACHE.put(resourceLocation, worktableRecipe);
      return worktableRecipe;
    }

    // Finally, check the custom recipes.

    for (WorktableRecipe worktableRecipe : ModPyrotechRegistries.WORKTABLE_RECIPE) {

      if (worktableRecipe.matches(inventory, world)) {
        return worktableRecipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModPyrotechRegistries.WORKTABLE_RECIPE, output);
  }

  public static void blacklistVanillaRecipe(ResourceLocation resourceLocation) {

    BLACKLIST.add(resourceLocation);
  }

  public static void whitelistVanillaRecipe(ResourceLocation resourceLocation) {

    WHITELIST.add(resourceLocation);
  }

  public static boolean hasBlacklist() {

    return !BLACKLIST.isEmpty()
        && ModulePyrotechConfig.WORKTABLE_COMMON.RECIPE_BLACKLIST.length > 0;
  }

  public static boolean hasWhitelist() {

    return !WHITELIST.isEmpty()
        && ModulePyrotechConfig.WORKTABLE_COMMON.RECIPE_WHITELIST.length > 0;
  }

  public static boolean isBlacklisted(ResourceLocation resourceLocation) {

    return BLACKLIST.contains(resourceLocation)
        || ArrayHelper.contains(ModulePyrotechConfig.WORKTABLE_COMMON.RECIPE_BLACKLIST, resourceLocation.toString());
  }

  public static boolean isWhitelisted(ResourceLocation resourceLocation) {

    return WHITELIST.contains(resourceLocation)
        || ArrayHelper.contains(ModulePyrotechConfig.WORKTABLE_COMMON.RECIPE_WHITELIST, resourceLocation.toString());
  }

  private final IRecipe recipe;

  public WorktableRecipe(
      IRecipe recipe
  ) {

    this.recipe = recipe;
  }

  @Nonnull
  public IRecipe getRecipe() {

    return this.recipe;
  }

  @Override
  public ItemStack getOutput() {

    return this.recipe.getRecipeOutput().copy();
  }

  public boolean matches(InventoryCrafting inventory, World world) {

    return this.recipe.matches(inventory, world);
  }
}
