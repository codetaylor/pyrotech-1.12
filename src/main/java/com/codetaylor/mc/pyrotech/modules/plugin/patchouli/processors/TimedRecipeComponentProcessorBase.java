package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;

import javax.annotation.Nullable;

public abstract class TimedRecipeComponentProcessorBase<R extends IRecipeTimed>
    implements IComponentProcessor {

  @Nullable
  protected R recipe;

  @Override
  public void setup(IVariableProvider<String> variables) {

    String recipe = variables.get("recipe");
    ResourceLocation key = new ResourceLocation(recipe);
    this.setup(key);
  }

  protected abstract void setup(ResourceLocation key);

  @Override
  public String process(String key) {

    if (this.recipe != null) {

      if ("ticks_hms".equals(key)) {
        return StringHelper.ticksToHMS(this.recipe.getTimeTicks());

      } else if ("ticks".equals(key)) {
        return String.valueOf(this.recipe.getTimeTicks());
      }
    }

    return null;
  }
}
