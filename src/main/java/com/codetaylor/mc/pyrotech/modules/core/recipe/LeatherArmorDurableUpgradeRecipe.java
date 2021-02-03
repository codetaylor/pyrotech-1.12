package com.codetaylor.mc.pyrotech.modules.core.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Map;

public class LeatherArmorDurableUpgradeRecipe
    extends ShapelessOreRecipe {

  private LeatherArmorDurableUpgradeRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result) {

    super(group, input, result);
  }

  @Nonnull
  @Override
  public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventoryCrafting) {

    int size = inventoryCrafting.getSizeInventory();

    for (int i = 0; i < size; i++) {
      ItemStack itemStack = inventoryCrafting.getStackInSlot(i);

      if (itemStack.getItem() == Items.LEATHER_HELMET
          || itemStack.getItem() == Items.LEATHER_CHESTPLATE
          || itemStack.getItem() == Items.LEATHER_LEGGINGS
          || itemStack.getItem() == Items.LEATHER_BOOTS) {
        ItemStack newItemStack = this.applyUnbreakingEnchantment(itemStack);

        if (!newItemStack.isEmpty()) {
          NBTTagCompound displayTag = newItemStack.getOrCreateSubCompound("display");
          displayTag.setInteger("color", 4791302);
        }

        return newItemStack;
      }
    }

    return ItemStack.EMPTY;
  }

  private ItemStack applyUnbreakingEnchantment(ItemStack itemStack) {

    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

    for (Enchantment enchantment : enchantments.keySet()) {

      if (enchantment == Enchantments.UNBREAKING) {
        return ItemStack.EMPTY;
      }

      if (!com.codetaylor.mc.athenaeum.util.EnchantmentHelper.isCompatible(Enchantments.UNBREAKING, enchantment)) {
        return ItemStack.EMPTY;
      }
    }

    ItemStack copy = itemStack.copy();
    enchantments.put(Enchantments.UNBREAKING, 1);
    EnchantmentHelper.setEnchantments(enchantments, copy);
    return copy;
  }

  @Override
  public boolean matches(@Nonnull InventoryCrafting inventoryCrafting, @Nonnull World world) {

    if (this.getCraftingResult(inventoryCrafting) == ItemStack.EMPTY) {
      return false;
    }

    return super.matches(inventoryCrafting, world);
  }

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
      return new LeatherArmorDurableUpgradeRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, itemstack);
    }
  }
}
