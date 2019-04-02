package com.codetaylor.mc.pyrotech.library.spi.plugin.jei;

import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.startup.ForgeModIdHelper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class PyrotechRecipeCategory<T extends IPyrotechRecipeWrapper>
    implements IRecipeCategory<T> {

  @ParametersAreNonnullByDefault
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, T recipeWrapper, IIngredients ingredients) {

    ResourceLocation registryName = recipeWrapper.getRegistryName();

    if (registryName != null) {
      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

      guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {

        if (slotIndex == PyrotechRecipeCategory.this.getOutputSlotIndex()) {
          String recipeModId = registryName.getResourceDomain();
          boolean modIdDifferent = false;
          ResourceLocation itemRegistryName = ingredient.getItem().getRegistryName();

          if (itemRegistryName != null) {
            String itemModId = itemRegistryName.getResourceDomain();
            modIdDifferent = !recipeModId.equals(itemModId);
          }

          if (modIdDifferent) {
            String modName = ForgeModIdHelper.getInstance()
                .getFormattedModNameForModId(recipeModId);

            if (modName != null) {
              tooltip.add(TextFormatting.GRAY + Translator.translateToLocalFormatted("jei.tooltip.recipe.by", modName));
            }
          }

          boolean showAdvanced = Minecraft.getMinecraft().gameSettings.advancedItemTooltips || GuiScreen.isShiftKeyDown();
          if (showAdvanced) {
            tooltip.add(TextFormatting.DARK_GRAY + Translator.translateToLocalFormatted("jei.tooltip.recipe.id", registryName.toString()));
          }
        }
      });
    }
  }

  protected abstract int getOutputSlotIndex();
}
