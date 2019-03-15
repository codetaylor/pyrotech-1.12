package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import javax.annotation.Nullable;

public class AnvilRecipeProcessor
    implements IComponentProcessor {

  @Nullable
  private AnvilRecipe recipe;

  @Override
  public void setup(IVariableProvider<String> variables) {

    String recipe = variables.get("recipe");
    ResourceLocation key = new ResourceLocation(recipe);
    this.recipe = ModuleTechBasic.Registries.ANVIL_RECIPE.getValue(key);
  }

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("input".equals(key)) {
        return ItemStackUtil.serializeIngredient(this.recipe.getInput());

      } else if ("output".equals(key)) {
        return ItemStackUtil.serializeStack(this.recipe.getOutput());

      } else if ("pickaxe".equals(key)) {
        return (this.recipe.getType() == AnvilRecipe.EnumType.PICKAXE) ? "t" : "";

      } else if ("hammer".equals(key)) {
        return (this.recipe.getType() == AnvilRecipe.EnumType.HAMMER) ? "t" : "";

      } else if ("granite".equals(key)) {
        return this.recipe.isTier(AnvilRecipe.EnumTier.GRANITE) ? "t" : "";

      } else if ("ironclad".equals(key)) {
        return (this.recipe.isTier(AnvilRecipe.EnumTier.IRONCLAD)
            && !this.recipe.isTier(AnvilRecipe.EnumTier.GRANITE)) ? "t" : "";
      }
    }

    return null;
  }
}
