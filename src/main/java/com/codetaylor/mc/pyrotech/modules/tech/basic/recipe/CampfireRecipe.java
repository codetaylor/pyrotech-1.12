package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampfireRecipe
    extends IForgeRegistryEntry.Impl<CampfireRecipe>
    implements IRecipeSingleOutput {

  private static final Map<String, CampfireRecipe> SMELTING_RECIPES = new HashMap<>();
  private static final List<Ingredient> WHITELIST = new ArrayList<>();
  private static final List<Ingredient> BLACKLIST = new ArrayList<>();
  private static boolean BLACKLIST_ALL = false;

  public static void blacklistAll() {

    BLACKLIST_ALL = true;
  }

  @Nullable
  public static CampfireRecipe getRecipe(ItemStack input) {

    String key = CampfireRecipe.getRecipeKey(input);

    CampfireRecipe result = SMELTING_RECIPES.get(key);

    // If the recipe is cached, return it.

    if (result != null) {
      return result;
    }

    result = CampfireRecipe.getCustomRecipe(input);

    if (result != null) {
      // User has defined a custom recipe, return it.
      return result;
    }

    // If the recipe has a smelting output that is food, check against the
    // lists, cache the result and return it.

    if (!BLACKLIST_ALL
        && RecipeHelper.hasFurnaceFoodRecipe(input)) {

      FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
      ItemStack output = furnaceRecipes.getSmeltingResult(input);

      if (CampfireRecipe.hasWhitelist()) {

        if (CampfireRecipe.isWhitelisted(output)) {
          result = new CampfireRecipe(output, Ingredient.fromStacks(input), ModuleTechBasicConfig.CAMPFIRE.COOK_TIME_TICKS);
          SMELTING_RECIPES.put(key, result);
          return result;
        }

      } else if (CampfireRecipe.hasBlacklist()) {

        if (!CampfireRecipe.isBlacklisted(output)) {
          result = new CampfireRecipe(output, Ingredient.fromStacks(input), ModuleTechBasicConfig.CAMPFIRE.COOK_TIME_TICKS);
          SMELTING_RECIPES.put(key, result);
          return result;
        }

      } else {
        result = new CampfireRecipe(output, Ingredient.fromStacks(input), ModuleTechBasicConfig.CAMPFIRE.COOK_TIME_TICKS);
        SMELTING_RECIPES.put(key, result);
        return result;
      }
    }

    return null;
  }

  @Nullable
  private static CampfireRecipe getCustomRecipe(ItemStack input) {

    for (CampfireRecipe recipe : ModuleTechBasic.Registries.CAMPFIRE_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  private static String getRecipeKey(ItemStack itemStack) {

    return itemStack.getItem().getRegistryName() + ":" + itemStack.getItemDamage();
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.CAMPFIRE_RECIPE, output);
  }

  public static void blacklistSmeltingRecipe(Ingredient output) {

    BLACKLIST.add(output);
  }

  public static void whitelistSmeltingRecipe(Ingredient output) {

    WHITELIST.add(output);
  }

  public static boolean hasBlacklist() {

    return !BLACKLIST.isEmpty();
  }

  public static boolean hasWhitelist() {

    return !WHITELIST.isEmpty();
  }

  public static void registerConfigBlacklist() {

    for (String itemString : ModuleTechBasicConfig.CAMPFIRE.RECIPE_BLACKLIST) {

      try {
        ParseResult result = RecipeItemParser.INSTANCE.parse(itemString);
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(result.getDomain(), result.getPath()));

        if (item == null) {
          throw new NullPointerException("Item not found: " + result.toString());
        }

        BLACKLIST.add(Ingredient.fromStacks(new ItemStack(item, 1, result.getMeta())));

      } catch (Exception e) {
        ModuleTechBasic.LOGGER.error("Error parsing campfire blacklist config entry", e);
      }
    }
  }

  public static boolean isBlacklisted(ItemStack output) {

    for (Ingredient ingredient : BLACKLIST) {

      if (ingredient.apply(output)) {
        return true;
      }
    }

    return false;
  }

  public static boolean isWhitelisted(ItemStack output) {

    for (Ingredient ingredient : WHITELIST) {

      if (ingredient.apply(output)) {
        return true;
      }
    }

    return false;
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int ticks;

  public CampfireRecipe(
      ItemStack output,
      Ingredient input,
      int ticks
  ) {

    this.input = input;
    this.output = output;
    this.ticks = ticks;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getTicks() {

    return this.ticks;
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
