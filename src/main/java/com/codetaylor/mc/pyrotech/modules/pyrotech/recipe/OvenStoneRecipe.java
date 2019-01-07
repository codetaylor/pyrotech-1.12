package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OvenStoneRecipe
    extends StoneMachineRecipeItemInItemOutBase<OvenStoneRecipe> {

  private static final Map<String, OvenStoneRecipe> SMELTING_RECIPES = new HashMap<>();
  private static final List<Ingredient> WHITELIST = new ArrayList<>();
  private static final List<Ingredient> BLACKLIST = new ArrayList<>();

  @Nullable
  public static OvenStoneRecipe getRecipe(ItemStack input) {

    String key = OvenStoneRecipe.getRecipeKey(input);

    OvenStoneRecipe result = SMELTING_RECIPES.get(key);

    // If the recipe is cached, return it.

    if (result != null) {
      return result;
    }

    // If the recipe has a smelting output that is food, check against the
    // lists, cache the result and return it.

    if (RecipeHelper.hasFurnaceFoodRecipe(input)) {

      FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
      ItemStack output = furnaceRecipes.getSmeltingResult(input);

      if (OvenStoneRecipe.hasWhitelist()
          && OvenStoneRecipe.isWhitelisted(output)) {
        result = new OvenStoneRecipe(output, Ingredient.fromStacks(input), ModulePyrotechConfig.STONE_OVEN.COOK_TIME_TICKS);
        SMELTING_RECIPES.put(key, result);
        return result;

      } else if (OvenStoneRecipe.hasBlacklist()
          && !OvenStoneRecipe.isBlacklisted(output)) {
        result = new OvenStoneRecipe(output, Ingredient.fromStacks(input), ModulePyrotechConfig.STONE_OVEN.COOK_TIME_TICKS);
        SMELTING_RECIPES.put(key, result);
        return result;
      }
    }

    // Finally, check the custom campfire recipes.

    for (OvenStoneRecipe recipe : ModulePyrotechRegistries.OVEN_STONE_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  private static String getRecipeKey(ItemStack itemStack) {

    return itemStack.getItem().getUnlocalizedName() + ":" + itemStack.getItemDamage();
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.OVEN_STONE_RECIPE, output);
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

  public OvenStoneRecipe(ItemStack output, Ingredient input, int timeTicks) {

    super(input, output, timeTicks);
  }

}
