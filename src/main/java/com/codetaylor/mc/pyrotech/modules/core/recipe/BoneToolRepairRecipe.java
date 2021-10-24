package com.codetaylor.mc.pyrotech.modules.core.recipe;

import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleTool;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
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

public class BoneToolRepairRecipe
    extends ShapelessOreRecipe {

  private final double repairPercentage;

  public BoneToolRepairRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result, double repairPercentage) {

    super(group, input, result);
    this.repairPercentage = repairPercentage;
    this.isSimple = false;
  }

  @Nonnull
  @Override
  public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventoryCrafting) {

    int size = inventoryCrafting.getSizeInventory();
    Item outputItem = this.output.getItem();

    for (int i = 0; i < size; i++) {
      ItemStack itemStack = inventoryCrafting.getStackInSlot(i);

      if (itemStack.getItem() == outputItem) {

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

    int hammerRepairDamage = 0;

    for (int i = 0; i < result.size(); i++) {
      ItemStack itemStack = inv.getStackInSlot(i);
      Item item = itemStack.getItem();

      if (item == ModuleTool.Items.BONE_TOOL_REPAIR_KIT) {
        hammerRepairDamage = ModuleToolConfig.BONE_TOOL_REPAIR_KIT.HAMMER_REPAIR_DAMAGE;
      }
    }

    for (int i = 0; i < result.size(); i++) {
      ItemStack itemStack = inv.getStackInSlot(i);

      if (OreDictHelper.contains("toolHammer", itemStack)) {

        itemStack = itemStack.copy();

        if (itemStack.attemptDamageItem(hammerRepairDamage, RandomHelper.random(), null)) {
          itemStack.shrink(1);
        }
        result.set(i, itemStack);

      } else if (itemStack.getItem() == ModuleTool.Items.BONE_TOOL_REPAIR_KIT) {

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
      return new BoneToolRepairRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, itemstack, ModuleToolConfig.BONE_TOOL_REPAIR_KIT.PERCENTAGE_DURABILITY_REPAIRED);
    }
  }
}
