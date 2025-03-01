package com.codetaylor.mc.pyrotech.modules.core.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class TankFlushRecipe
    extends ShapelessRecipes {

  public TankFlushRecipe(String group, ItemStack tank) {

    super(group, tank, NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(tank.getItem())));
  }

  @Nonnull
  @Override
  public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {

    return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
  }

  public static class Factory
      implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {

      String group = JsonUtils.getString(json, "group", "");
      ItemStack itemStack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "tank"), context);
      return new TankFlushRecipe(group, itemStack);
    }
  }
}
