package com.codetaylor.mc.pyrotech.modules.core.recipe;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
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
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class CraftingTableRecipe
    extends ShapedOreRecipe {

  private CraftingTableRecipe(ResourceLocation group, @Nonnull ItemStack result, CraftingHelper.ShapedPrimer primer) {

    super(group, result, primer);
  }

  @Nonnull
  @Override
  public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {

    NonNullList<ItemStack> result = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    Item itemToMatch = Item.getItemFromBlock(ModuleCore.Blocks.CRAFTING_TABLE_TEMPLATE);

    for (int i = 0; i < result.size(); i++) {
      ItemStack itemStack = inv.getStackInSlot(i);
      Item item = itemStack.getItem();

      if (item == itemToMatch) {
        result.set(i, itemStack.copy());

      } else {
        ItemStack containerItem = ForgeHooks.getContainerItem(itemStack);
        result.set(i, containerItem);
      }
    }

    return result;
  }

  @Override
  public boolean matches(@Nonnull InventoryCrafting inventoryCrafting, @Nonnull World world) {

    if (this.getCraftingResult(inventoryCrafting) == ItemStack.EMPTY) {
      return false;
    }

    return super.matches(inventoryCrafting, world);
  }

  /**
   * @see ShapedOreRecipe#factory(JsonContext, JsonObject)
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
      return new CraftingTableRecipe(group.isEmpty() ? null : new ResourceLocation(group), result, primer);
    }
  }
}
