package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorktableRecipe
    extends IForgeRegistryEntry.Impl<WorktableRecipe>
    implements IRecipeSingleOutput {

  private static final Map<ResourceLocation, WorktableRecipe> CACHE = new HashMap<>();
  private static final Set<ResourceLocation> WHITELIST = new HashSet<>();
  private static final Set<ResourceLocation> BLACKLIST = new HashSet<>();
  private static boolean BLACKLIST_ALL = false;

  public static void blacklistAll() {

    BLACKLIST_ALL = true;
  }

  @Nullable
  public static WorktableRecipe getRecipe(InventoryCrafting inventory, World world) {

    if (!BLACKLIST_ALL) {
      IRecipe recipe = CraftingManager.findMatchingRecipe(inventory, world);

      if (recipe != null) {
        WorktableRecipe result = WorktableRecipe.getWrappedRecipe(recipe);

        if (result != null) {
          return result;
        }

        return WorktableRecipe.getCustomRecipe(inventory, world);
      }
    }

    // Finally, check the custom recipes.
    return WorktableRecipe.getCustomRecipe(inventory, world);
  }

  @Nullable
  public static WorktableRecipe getRecipe(ResourceLocation resourceLocation) {

    // If the resource location is cached, return it.
    WorktableRecipe cachedRecipe = CACHE.get(resourceLocation);

    if (cachedRecipe != null) {
      return cachedRecipe;
    }

    // If the resource location is a vanilla crafting recipe, wrap and return it.
    IRecipe vanillaRecipe = ForgeRegistries.RECIPES.getValue(resourceLocation);

    if (vanillaRecipe != null) {
      return WorktableRecipe.getWrappedRecipe(vanillaRecipe);
    }

    // Finally, check the custom recipes.
    return WorktableRecipe.getCustomRecipe(resourceLocation);
  }

  @Nullable
  private static WorktableRecipe getWrappedRecipe(IRecipe recipe) {

    ResourceLocation resourceLocation = recipe.getRegistryName();
    WorktableRecipe cachedRecipe = CACHE.get(resourceLocation);

    if (cachedRecipe != null) {
      return cachedRecipe;
    }

    if (WorktableRecipe.hasWhitelist()) {

      if (WorktableRecipe.isWhitelisted(resourceLocation)) {
        WorktableRecipe worktableRecipe = new WorktableRecipe(recipe, resourceLocation);
        CACHE.put(resourceLocation, worktableRecipe);
        return worktableRecipe;
      }

      return null;

    } else if (WorktableRecipe.hasBlacklist()) {

      if (!WorktableRecipe.isBlacklisted(resourceLocation)) {
        WorktableRecipe worktableRecipe = new WorktableRecipe(recipe, resourceLocation);
        CACHE.put(resourceLocation, worktableRecipe);
        return worktableRecipe;
      }

      return null;

    } else {
      WorktableRecipe worktableRecipe = new WorktableRecipe(recipe, resourceLocation);
      CACHE.put(resourceLocation, worktableRecipe);
      return worktableRecipe;
    }
  }

  public static boolean hasRecipeWithTool(Item tool) {

    for (WorktableRecipe worktableRecipe : ModuleTechBasic.Registries.WORKTABLE_RECIPE) {
      List<Item> toolList = worktableRecipe.getToolList();

      if (toolList.contains(tool)) {
        return true;
      }
    }

    return false;
  }

  @Nullable
  private static WorktableRecipe getCustomRecipe(InventoryCrafting inventory, World world) {

    for (WorktableRecipe worktableRecipe : ModuleTechBasic.Registries.WORKTABLE_RECIPE) {

      if (worktableRecipe.matches(inventory, world)) {
        return worktableRecipe;
      }
    }

    return null;
  }

  @Nullable
  private static WorktableRecipe getCustomRecipe(ResourceLocation resourceLocation) {

    return ModuleTechBasic.Registries.WORKTABLE_RECIPE.getValue(resourceLocation);
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.WORKTABLE_RECIPE, output);
  }

  public static void blacklistVanillaRecipe(ResourceLocation resourceLocation) {

    BLACKLIST.add(resourceLocation);
  }

  public static void whitelistVanillaRecipe(ResourceLocation resourceLocation) {

    WHITELIST.add(resourceLocation);
  }

  public static boolean hasBlacklist() {

    return !BLACKLIST.isEmpty()
        && ModuleTechBasicConfig.WORKTABLE_COMMON.RECIPE_BLACKLIST.length > 0;
  }

  public static boolean hasWhitelist() {

    return !WHITELIST.isEmpty()
        && ModuleTechBasicConfig.WORKTABLE_COMMON.RECIPE_WHITELIST.length > 0;
  }

  public static boolean isBlacklisted(ResourceLocation resourceLocation) {

    return BLACKLIST.contains(resourceLocation)
        || ArrayHelper.contains(ModuleTechBasicConfig.WORKTABLE_COMMON.RECIPE_BLACKLIST, resourceLocation.toString());
  }

  public static boolean isWhitelisted(ResourceLocation resourceLocation) {

    return WHITELIST.contains(resourceLocation)
        || ArrayHelper.contains(ModuleTechBasicConfig.WORKTABLE_COMMON.RECIPE_WHITELIST, resourceLocation.toString());
  }

  private final IRecipe recipe;
  private final List<Item> toolList;
  private final int toolDamage;
  private final Stages stages;

  public WorktableRecipe(
      IRecipe recipe,
      ResourceLocation resourceLocation
  ) {

    this(recipe, null, 0, null);
    this.setRegistryName(resourceLocation);
  }

  public WorktableRecipe(IRecipe recipe, @Nullable Ingredient tool, int toolDamage, @Nullable Stages stages) {

    this.recipe = recipe;
    this.toolDamage = toolDamage;
    this.toolList = new ArrayList<>();
    this.stages = stages;

    if (tool != null) {
      ItemStack[] matchingStacks = tool.getMatchingStacks();

      if (matchingStacks.length > 0) {
        Stream.of(matchingStacks).map(ItemStack::getItem).collect(Collectors.toCollection(() -> this.toolList));
      }
    }
  }

  @Nonnull
  public IRecipe getRecipe() {

    return this.recipe;
  }

  public List<Item> getToolList() {

    return this.toolList;
  }

  public int getToolDamage() {

    return this.toolDamage;
  }

  public Stages getStages() {

    return this.stages;
  }

  @Override
  public ItemStack getOutput() {

    return this.recipe.getRecipeOutput().copy();
  }

  public boolean matches(InventoryCrafting inventory, World world) {

    return this.recipe.matches(inventory, world);
  }
}
