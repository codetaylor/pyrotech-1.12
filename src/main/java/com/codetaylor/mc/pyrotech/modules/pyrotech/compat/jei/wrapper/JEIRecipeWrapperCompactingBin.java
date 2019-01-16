package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper;

import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.CompactingBinRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
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

public class JEIRecipeWrapperCompactingBin
    implements IRecipeWrapper {

  private final List<List<ItemStack>> inputs;
  private final ItemStack output;
  private final String amount;
  private final int[] uses;

  public JEIRecipeWrapperCompactingBin(CompactingBinRecipe recipe) {

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));
    this.output = recipe.getOutput();
    this.amount = String.valueOf(recipe.getAmount());
    this.uses = recipe.getRequiredToolUses();

    // Ensure that the inputs never render a quantity.
    for (List<ItemStack> inputList : this.inputs) {

      for (ItemStack itemStack : inputList) {
        itemStack.setCount(1);
      }
    }
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    int levels = this.uses.length;
    int index = (int) ((minecraft.world.getTotalWorldTime() / 39) % levels);
    int uses = ArrayHelper.getOrLast(this.uses, index);
    String locationString = ArrayHelper.getOrLast(ModulePyrotechConfig.COMPACTING_BIN.JEI_HARVEST_LEVEL_ITEM, index);
    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(locationString));

    if (item != null) {
      RenderItem renderItem = minecraft.getRenderItem();
      ItemStack stack = new ItemStack(item);
      IBakedModel model = renderItem.getItemModelWithOverrides(stack, null, null);

      net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

      GlStateManager.pushMatrix();
      {
        int stage = (int) (1 - (minecraft.world.getTotalWorldTime() % 39) / 20);
        GlStateManager.translate(8, 5 + (stage * 4), 100);
        GlStateManager.rotate(-90 + (stage * -90), 0, 0, 1);
        GlStateManager.scale(16.0F, -16.0F, 16.0F);
        RenderHelper.renderItemModel(stack, model, ItemCameraTransforms.TransformType.GUI, false, false);
      }
      GlStateManager.popMatrix();

      net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
    }

    GlStateManager.pushMatrix();
    {
      GlStateManager.translate(0, 0, 250);
      {
        int stringWidth = minecraft.fontRenderer.getStringWidth(this.amount);
        minecraft.fontRenderer.drawString(this.amount, 18 - stringWidth, 27, 0xFFFFFFFF, true);
      }
      {
        String usesString = "x" + uses;
        minecraft.fontRenderer.drawString(usesString, 18, 2, 0xFFFFFFFF, true);
      }
    }
    GlStateManager.popMatrix();

  }
}
