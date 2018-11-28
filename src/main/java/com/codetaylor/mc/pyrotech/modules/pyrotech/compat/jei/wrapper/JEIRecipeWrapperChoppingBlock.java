package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper;

import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.ChoppingBlockRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperChoppingBlock
    implements IRecipeWrapper {

  private final List<List<ItemStack>> inputs;
  private final ItemStack output;

  public JEIRecipeWrapperChoppingBlock(ChoppingBlockRecipe recipe) {

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));
    this.output = recipe.getOutput();
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(ItemStack.class, this.inputs);
    ingredients.setOutput(ItemStack.class, this.output);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    int levels = Math.max(ModulePyrotechConfig.CHOPPING_BLOCK.CHOPS_REQUIRED_PER_HARVEST_LEVEL.length, ModulePyrotechConfig.CHOPPING_BLOCK.RECIPE_RESULT_QUANTITY_PER_HARVEST_LEVEL.length);
    int index = (int) ((minecraft.world.getTotalWorldTime() / 40) % levels);
    int chops = ArrayHelper.getOrLast(ModulePyrotechConfig.CHOPPING_BLOCK.CHOPS_REQUIRED_PER_HARVEST_LEVEL, index);
    int quantity = ArrayHelper.getOrLast(ModulePyrotechConfig.CHOPPING_BLOCK.RECIPE_RESULT_QUANTITY_PER_HARVEST_LEVEL, index);
    String locationString = ArrayHelper.getOrLast(ModulePyrotechConfig.CHOPPING_BLOCK.JEI_HARVEST_LEVEL_ITEM, index);
    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(locationString));

    if (item != null) {
      RenderItem renderItem = minecraft.getRenderItem();
      ItemStack stack = new ItemStack(item);
      IBakedModel model = renderItem.getItemModelWithOverrides(stack, null, null);

      net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

      GlStateManager.pushMatrix();
      {
        int stage = (int) ((minecraft.world.getTotalWorldTime() % 40) / 20);
        GlStateManager.translate(8, 5 + (stage * 4), 100);
        GlStateManager.rotate(-90 + (stage * -90), 0, 0, 1);
        GlStateManager.scale(16.0F, -16.0F, 16.0F);
        RenderHelper.renderItemModel(stack, model, ItemCameraTransforms.TransformType.GUI, false, false);
      }
      GlStateManager.popMatrix();

      net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

    }

    GlStateManager.pushMatrix();
    GlStateManager.translate(0, 0, 250);
    minecraft.fontRenderer.drawString("x" + chops, 18, 2, 0xFFFFFFFF, true);
    String quantityLabel = String.valueOf(quantity);
    minecraft.fontRenderer.drawString(quantityLabel, 80 - minecraft.fontRenderer.getStringWidth(quantityLabel), 30, 0xFFFFFFFF, true);
    GlStateManager.popMatrix();
  }
}
