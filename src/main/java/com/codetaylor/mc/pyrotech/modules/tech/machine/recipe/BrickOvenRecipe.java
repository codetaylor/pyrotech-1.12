package com.codetaylor.mc.pyrotech.modules.tech.machine.recipe;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInItemOutBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrickOvenRecipe
    extends StoneMachineRecipeItemInItemOutBase<BrickOvenRecipe> {

  private static final Map<String, BrickOvenRecipe> SMELTING_RECIPES = new HashMap<>();
  private static final List<Ingredient> WHITELIST = new ArrayList<>();
  private static final List<Ingredient> BLACKLIST = new ArrayList<>();

  @Nullable
  public static BrickOvenRecipe getRecipe(ItemStack input) {

    String key = BrickOvenRecipe.getRecipeKey(input);

    BrickOvenRecipe result = SMELTING_RECIPES.get(key);

    // If the recipe is cached, return it.

    if (result != null) {
      return result;
    }

    // If the recipe has a smelting output that is food, check against the
    // lists, cache the result and return it.

    if (RecipeHelper.hasFurnaceFoodRecipe(input)) {

      FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
      ItemStack output = furnaceRecipes.getSmeltingResult(input);

      if (BrickOvenRecipe.hasWhitelist()
          && BrickOvenRecipe.isWhitelisted(output)) {
        result = new BrickOvenRecipe(output, Ingredient.fromStacks(input));
        SMELTING_RECIPES.put(key, result);
        return result;

      } else if (BrickOvenRecipe.hasBlacklist()
          && !BrickOvenRecipe.isBlacklisted(output)) {
        result = new BrickOvenRecipe(output, Ingredient.fromStacks(input));
        SMELTING_RECIPES.put(key, result);
        return result;

      } else {
        result = new BrickOvenRecipe(output, Ingredient.fromStacks(input));
        SMELTING_RECIPES.put(key, result);
        return result;
      }
    }

    // Finally, check the custom recipes.

    for (BrickOvenRecipe recipe : ModuleTechMachine.Registries.BRICK_OVEN_RECIPES) {

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

    return RecipeHelper.removeRecipesByOutput(ModuleTechMachine.Registries.BRICK_OVEN_RECIPES, output);
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

  public BrickOvenRecipe(ItemStack output, Ingredient input) {

    super(input, output, ModuleTechMachineConfig.BRICK_OVEN.COOK_TIME_TICKS);
  }

  public BrickOvenRecipe(ItemStack output, Ingredient input, int cookTimeTicks) {

    super(input, output, cookTimeTicks);
  }

}
