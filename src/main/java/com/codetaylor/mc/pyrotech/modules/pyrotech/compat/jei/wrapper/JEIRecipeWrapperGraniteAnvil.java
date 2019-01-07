package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.jei.wrapper;

import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.GraniteAnvilRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JEIRecipeWrapperGraniteAnvil
    implements IRecipeWrapper {

  private final List<List<ItemStack>> inputs;
  private final ItemStack output;
  private final int hits;
  private final GraniteAnvilRecipe.EnumType type;

  private String[] pickaxeDisplayList;

  public JEIRecipeWrapperGraniteAnvil(GraniteAnvilRecipe recipe) {

    this.inputs = Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks()));
    this.output = recipe.getOutput();
    this.hits = recipe.getHits();
    this.type = recipe.getType();

    this.pickaxeDisplayList = new String[]{
        Items.STONE_PICKAXE.getRegistryName().toString(),
        Items.IRON_PICKAXE.getRegistryName().toString(),
        Items.DIAMOND_PICKAXE.getRegistryName().toString()
    };
  }

  @Override
  public void getIngredients(@Nonnull IIngredients ingredients) {

    ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
    ingredients.setOutput(VanillaTypes.ITEM, this.output);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    String[] toolWhitelist;

    if (this.type == GraniteAnvilRecipe.EnumType.HAMMER) {
      toolWhitelist = ModulePyrotechConfig.GRANITE_ANVIL.HAMMER_LIST;

    } else if (this.type == GraniteAnvilRecipe.EnumType.PICKAXE) {
      toolWhitelist = this.pickaxeDisplayList;

    } else {
      throw new RuntimeException("Unknown recipe type: " + this.type);
    }

    int length = toolWhitelist.length;
    int index = (int) ((minecraft.world.getTotalWorldTime() / 40) % length);
    String locationString = toolWhitelist[index];
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
    minecraft.fontRenderer.drawString("x" + this.hits, 18, 2, 0xFFFFFFFF, true);
    GlStateManager.popMatrix();
  }
}
