package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.wrapper;

import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
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

public class JEIRecipeWrapperAnvil
    implements IRecipeWrapper {

  private final List<List<ItemStack>> inputs;
  private final ItemStack output;
  private final int hits;
  private final AnvilRecipe.EnumType type;

  public JEIRecipeWrapperAnvil(AnvilRecipe recipe) {

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));
    this.output = recipe.getOutput();
    this.hits = recipe.getHits();
    this.type = recipe.getType();
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    String[] toolWhitelist;

    if (this.type == AnvilRecipe.EnumType.HAMMER) {
      toolWhitelist = ModulePyrotechConfig.HAMMERS.HAMMER_LIST;

    } else if (this.type == AnvilRecipe.EnumType.PICKAXE) {
      toolWhitelist = ModuleTechBasicConfig.ANVIL_COMMON.JEI_HARVEST_LEVEL_PICKAXE;

    } else {
      throw new RuntimeException("Unknown recipe type: " + this.type);
    }

    int length = toolWhitelist.length;
    int index = (int) ((minecraft.world.getTotalWorldTime() / 39) % length);
    String locationString = ArrayHelper.getOrLast(toolWhitelist, index).split(";")[0];
    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(locationString));
    int hits = this.hits;

    if (item != null) {

      if (this.type == AnvilRecipe.EnumType.HAMMER) {
        hits -= ModuleTechBasicConfig.ANVIL_COMMON.getHammerHitReduction(item.getRegistryName());

      } else {
        hits -= item.getHarvestLevel(null, "pickaxe", null, null);
      }

      hits = Math.max(1, hits);

      RenderItem renderItem = minecraft.getRenderItem();
      ItemStack stack = new ItemStack(item);
      IBakedModel model = renderItem.getItemModelWithOverrides(stack, null, null);

      net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

      GlStateManager.pushMatrix();
      {
        int stage = (int) ((minecraft.world.getTotalWorldTime() % 39) / 20);
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
    minecraft.fontRenderer.drawString("x" + hits, 18, 2, 0xFFFFFFFF, true);
    GlStateManager.popMatrix();
  }
}
