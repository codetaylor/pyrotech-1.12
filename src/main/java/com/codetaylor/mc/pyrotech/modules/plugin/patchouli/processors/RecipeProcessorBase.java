package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;

import javax.annotation.Nullable;

public abstract class RecipeProcessorBase<R>
    implements IComponentProcessor {

  @Nullable
  protected R recipe;

  @Override
  public void setup(IVariableProvider<String> variables) {

    String recipe = variables.get("recipe");
    ResourceLocation key = new ResourceLocation(recipe);
    this.recipe = this.getRecipe(key);
  }

  @Nullable
  protected abstract R getRecipe(ResourceLocation key);

}
