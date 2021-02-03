package com.codetaylor.mc.pyrotech.modules.core.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class LeatherArmorFireProtectionRecipe
    extends ShapedOreRecipe {

  private LeatherArmorFireProtectionRecipe(ResourceLocation group, @Nonnull ItemStack result, CraftingHelper.ShapedPrimer primer) {

    super(group, result, primer);
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
        ItemStack newItemStack = this.applyFireResistanceEnchantment(itemStack);

        if (!newItemStack.isEmpty()) {
          NBTTagCompound displayTag = newItemStack.getOrCreateSubCompound("display");
          displayTag.setInteger("color", 10068616);
        }

        return newItemStack;
      }
    }

    return ItemStack.EMPTY;
  }

  private ItemStack applyFireResistanceEnchantment(ItemStack itemStack) {

    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

    for (Enchantment enchantment : enchantments.keySet()) {

      if (enchantment == Enchantments.FIRE_PROTECTION) {
        return ItemStack.EMPTY;
      }

      if (!com.codetaylor.mc.athenaeum.util.EnchantmentHelper.isCompatible(Enchantments.FIRE_PROTECTION, enchantment)) {
        return ItemStack.EMPTY;
      }
    }

    ItemStack copy = itemStack.copy();
    enchantments.put(Enchantments.FIRE_PROTECTION, 1);
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

  /**
   * @see ShapedOreRecipe#factory(net.minecraftforge.common.crafting.JsonContext, com.google.gson.JsonObject)
   */
  public static class Factory
      implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {

      String group = JsonUtils.getString(json, "group", "");
      Map<Character, Ingredient> ingMap = Maps.newHashMap();

      for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet()) {

        if (entry.getKey().length() != 1) {
          throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
        }

        if (" ".equals(entry.getKey())) {
          throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
        }

        ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
      }

      ingMap.put(' ', Ingredient.EMPTY);

      JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

      if (patternJ.size() == 0) {
        throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
      }

      String[] pattern = new String[patternJ.size()];
      for (int x = 0; x < pattern.length; ++x) {
        String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
        if (x > 0 && pattern[0].length() != line.length()) {
          throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
        }
        pattern[x] = line;
      }

      CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
      primer.width = pattern[0].length();
      primer.height = pattern.length;
      primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
      primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);

      Set<Character> keys = Sets.newHashSet(ingMap.keySet());
      keys.remove(' ');

      int x = 0;
      for (String line : pattern) {
        for (char chr : line.toCharArray()) {
          Ingredient ing = ingMap.get(chr);
          if (ing == null) {
            throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
          }
          primer.input.set(x++, ing);
          keys.remove(chr);
        }
      }

      if (!keys.isEmpty()) {
        throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
      }

      ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
      return new LeatherArmorFireProtectionRecipe(group.isEmpty() ? null : new ResourceLocation(group), result, primer);
    }
  }
}
