package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.config.Constants;
import mezz.jei.startup.ForgeModIdHelper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class JEIRecipeCategoryWorktable
    implements IRecipeCategory<IRecipeWrapper> {

  public static final String UID = ModuleTechBasic.MOD_ID + ".worktable";

  private static final int CRAFT_OUTPUT_SLOT_INDEX = 0;
  private static final int CRAFT_INPUT_SLOT_INDEX = 1;

  private final IDrawable background;
  private final String title;
  private final ICraftingGridHelper craftingGridHelper;

  public JEIRecipeCategoryWorktable(IGuiHelper guiHelper) {

    ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
    background = guiHelper.createDrawable(location, 0, 60, 116, 54);
    this.title = Translator.translateToLocal("gui." + ModuleTechBasic.MOD_ID + ".jei.category.worktable");
    this.craftingGridHelper = guiHelper.createCraftingGridHelper(CRAFT_INPUT_SLOT_INDEX, CRAFT_OUTPUT_SLOT_INDEX);
  }

  @Nonnull
  @Override
  public String getUid() {

    return UID;
  }

  @Nonnull
  @Override
  public String getTitle() {

    return title;
  }

  @Nonnull
  @Override
  public String getModName() {

    return ModuleTechBasic.MOD_ID;
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {

    return background;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {

    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

    guiItemStacks.init(CRAFT_OUTPUT_SLOT_INDEX, false, 94, 18);

    for (int y = 0; y < 3; ++y) {

      for (int x = 0; x < 3; ++x) {
        int index = CRAFT_INPUT_SLOT_INDEX + x + (y * 3);
        guiItemStacks.init(index, true, x * 18, y * 18);
      }
    }

    if (recipeWrapper instanceof ICustomCraftingRecipeWrapper) {
      ICustomCraftingRecipeWrapper customWrapper = (ICustomCraftingRecipeWrapper) recipeWrapper;
      customWrapper.setRecipe(recipeLayout, ingredients);
      return;
    }

    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

    if (recipeWrapper instanceof IShapedCraftingRecipeWrapper) {
      IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) recipeWrapper;
      this.craftingGridHelper.setInputs(guiItemStacks, inputs, wrapper.getWidth(), wrapper.getHeight());

    } else {
      this.craftingGridHelper.setInputs(guiItemStacks, inputs);
      recipeLayout.setShapeless();
    }

    guiItemStacks.set(CRAFT_OUTPUT_SLOT_INDEX, outputs.get(0));

    if (recipeWrapper instanceof ICraftingRecipeWrapper) {
      ICraftingRecipeWrapper craftingRecipeWrapper = (ICraftingRecipeWrapper) recipeWrapper;
      ResourceLocation registryName = craftingRecipeWrapper.getRegistryName();

      if (registryName != null) {
        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {

          if (slotIndex == CRAFT_OUTPUT_SLOT_INDEX) {
            String recipeModId = registryName.getResourceDomain();
            boolean modIdDifferent = false;
            ResourceLocation itemRegistryName = ingredient.getItem().getRegistryName();

            if (itemRegistryName != null) {
              String itemModId = itemRegistryName.getResourceDomain();
              modIdDifferent = !recipeModId.equals(itemModId);
            }

            if (modIdDifferent) {
              String modName = ForgeModIdHelper.getInstance().getFormattedModNameForModId(recipeModId);

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
  }
}
