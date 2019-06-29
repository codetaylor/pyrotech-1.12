package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.WorktableRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;
import java.util.List;

public class WorktableRecipeProcessor
    implements IComponentProcessor {

  @Nullable
  private WorktableRecipe recipe;

  @Override
  public void setup(IVariableProvider<String> variables) {

    String recipe = variables.get("recipe");
    ResourceLocation key = new ResourceLocation(recipe);
    this.recipe = ModuleTechBasic.Registries.WORKTABLE_RECIPE.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {
      IRecipe iRecipe = this.recipe.getRecipe();

      if (key.startsWith("item")) {
        int index = Integer.parseInt(key.substring(4)) - 1;
        Ingredient ingredient = iRecipe.getIngredients().get(index);
        ItemStack[] stacks = ingredient.getMatchingStacks();
        ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];

        return ItemStackUtil.serializeStack(stack);

      } else if (key.equals("output")) {
        return ItemStackUtil.serializeStack(iRecipe.getRecipeOutput());

      } else if (key.equals("tool")) {
        List<Item> toolList = this.recipe.getToolList();

        if (!toolList.isEmpty()) {
          Ingredient ingredient = Ingredient.fromItems(toolList.toArray(new Item[0]));
          return ItemStackUtil.serializeIngredient(ingredient);
        }

        return "";

      } else if (key.equals("tool_damage")) {
        List<Item> toolList = this.recipe.getToolList();

        if (!toolList.isEmpty()) {
          return String.valueOf(this.recipe.getToolDamage());
        }

        return "";
      }
    }

    return null;
  }
}
