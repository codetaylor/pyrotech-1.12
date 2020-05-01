package com.codetaylor.mc.pyrotech.modules.core.recipe;

import com.codetaylor.mc.athenaeum.util.OreDictHelper;
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
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.Random;

public class MarshmallowStickRecipe
    extends ShapelessOreRecipe {

  private MarshmallowStickRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result) {

    super(group, input, result);
    this.isSimple = false;
  }

  @Nonnull
  @Override
  public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {

    NonNullList<ItemStack> result = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack itemStack = inv.getStackInSlot(i);

      if (OreDictHelper.contains("toolSharp", itemStack)) {

        ItemStack copy = itemStack.copy();

        if (!copy.attemptDamageItem(1, new Random(), null)) {
          result.set(i, copy);
        }

      } else {
        result.set(i, ForgeHooks.getContainerItem(inv.getStackInSlot(i)));
      }
    }

    return result;
  }

  /**
   * @see ShapelessOreRecipe#factory(JsonContext, JsonObject)
   */
  public static class Factory
      implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {

      String group = JsonUtils.getString(json, "group", "");

      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients")) {
        ingredients.add(CraftingHelper.getIngredient(ele, context));
      }

      if (ingredients.isEmpty()) {
        throw new JsonParseException("No ingredients for shapeless recipe");
      }

      ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
      return new MarshmallowStickRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, itemstack);
    }
  }
}
