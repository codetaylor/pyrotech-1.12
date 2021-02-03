package com.codetaylor.mc.pyrotech.modules.core.recipe;

import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.init.Items;
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

public class LeatherRepairRecipe
    extends ShapelessOreRecipe {

  private final double repairPercentage;

  public LeatherRepairRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result, double repairPercentage) {

    super(group, input, result);
    this.repairPercentage = repairPercentage;
    this.isSimple = false;
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

        if (itemStack.getItemDamage() == 0) {
          return ItemStack.EMPTY;
        }

        ItemStack copy = itemStack.copy();
        copy.setItemDamage(Math.max(0, copy.getItemDamage() - (int) (copy.getMaxDamage() * this.repairPercentage)));
        return copy;
      }
    }

    return ItemStack.EMPTY;
  }

  @Nonnull
  @Override
  public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {

    NonNullList<ItemStack> result = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

    for (int i = 0; i < result.size(); i++) {
      ItemStack itemStack = inv.getStackInSlot(i);

      if (OreDictHelper.contains("toolHuntingKnife", itemStack)) {

        itemStack = itemStack.copy();

        if (itemStack.attemptDamageItem(ModuleHuntingConfig.LEATHER_KITS.HUNTING_KNIFE_REPAIR_DAMAGE, RandomHelper.random(), null)) {
          itemStack.shrink(1);
        }
        result.set(i, itemStack);

      } else if (itemStack.getItem() == ModuleHunting.Items.LEATHER_REPAIR_KIT
          || itemStack.getItem() == ModuleHunting.Items.LEATHER_DURABLE_REPAIR_KIT) {

        itemStack = itemStack.copy();

        if (itemStack.attemptDamageItem(1, RandomHelper.random(), null)) {
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

      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients")) {
        ingredients.add(CraftingHelper.getIngredient(ele, context));
      }

      if (ingredients.isEmpty()) {
        throw new JsonParseException("No ingredients for shapeless recipe");
      }

      ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
      return new LeatherRepairRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, itemstack, ModuleHuntingConfig.LEATHER_KITS.PERCENTAGE_DURABILITY_REPAIRED);
    }
  }
}
