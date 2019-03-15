package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.processors;

import com.codetaylor.mc.athenaeum.util.StringHelper;
import com.codetaylor.mc.pyrotech.library.spi.recipe.IRecipeTimed;

public abstract class TimedRecipeBase<R extends IRecipeTimed>
    extends RecipeProcessorBase<R> {

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
