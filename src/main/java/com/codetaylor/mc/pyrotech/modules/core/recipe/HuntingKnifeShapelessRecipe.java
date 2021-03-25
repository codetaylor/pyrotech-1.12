package com.codetaylor.mc.pyrotech.modules.core.recipe;

import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class HuntingKnifeShapelessRecipe
    extends ShapelessOreRecipe {

  private final int damage;

  public HuntingKnifeShapelessRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result, int damage) {

    super(group, input, result);
    this.damage = damage;
    this.isSimple = false;
  }

  @Nonnull
  @Override
  public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {

    NonNullList<ItemStack> result = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

    for (int i = 0; i < result.size(); i++) {
      ItemStack itemStack = inv.getStackInSlot(i);

      if (OreDictHelper.contains("toolHuntingKnife", itemStack)) {

        itemStack = itemStack.copy();

        if (itemStack.attemptDamageItem(this.damage, RandomHelper.random(), null)) {
          itemStack.shrink(1);
        }
        result.set(i, itemStack);

      } else {
        ItemStack containerItem = ForgeHooks.getContainerItem(itemStack);
        result.set(i, containerItem);
      }
    }

    return result;
  }

  public static class Factory
      implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {

      String group = JsonUtils.getString(json, "group", "");
      int damage = JsonUtils.getInt(json, "toolDamage", 1);

      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients")) {
        ingredients.add(CraftingHelper.getIngredient(ele, context));
      }

      if (ingredients.isEmpty()) {
        throw new JsonParseException("No ingredients for shapeless recipe");
      }

      ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
      return new HuntingKnifeShapelessRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, itemstack, damage);
    }
  }
}
