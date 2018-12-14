package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api;

import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteractionItemStack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteractionRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public final class InteractionRenderers {

  public static final InteractionRenderer_ItemStack ITEM_STACK = new InteractionRenderer_ItemStack();

  public static class InteractionRenderer_ItemStack
      implements IInteractionRenderer<IInteractionItemStack> {

    @Override
    public void renderSolidPass(IInteractionItemStack interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      // If the handler is not empty, render the handler's item.

      if (!interaction.isEmpty()) {
        ItemStack itemStack = interaction.getStackInSlot();
        Transform transform = interaction.getTransform(world, pos, blockState, itemStack, partialTicks);
        InteractionRenderers.renderItemModel(renderItem, itemStack, transform);
      }
    }

    @Override
    public boolean renderAdditivePass(IInteractionItemStack interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      // If the handler is empty, render the held item.
      // Else, render the handler's item if the player's hand is empty.

      if (interaction.isEmpty()
          && !heldItemMainHand.isEmpty()) {

        // Only render the held item if it is valid for the handler.
        if (interaction.isItemStackValid(heldItemMainHand)) {
          Transform transform = interaction.getTransform(world, hitPos, blockState, heldItemMainHand, partialTicks);

          // Since only one item will be rendered, it is better to wrap the
          // GL setup calls as late as possible so we're not setting it up
          // if the item isn't going to be rendered.

          InteractionRenderers.setupAdditiveGLState();
          InteractionRenderers.renderItemModelCustom(renderItem, heldItemMainHand, transform);
          InteractionRenderers.cleanupAdditiveGLState();
          return true;
        }

      } else if (!interaction.isEmpty()
          && heldItemMainHand.isEmpty()) {

        ItemStack itemStack = interaction.getStackInSlot();
        Transform transform = interaction.getTransform(world, hitPos, blockState, itemStack, partialTicks);

        if (!itemStack.isEmpty()) {

          // Since only one item will be rendered, it is better to wrap the
          // GL setup calls as late as possible so we're not setting it up
          // if the item isn't going to be rendered.

          InteractionRenderers.setupAdditiveGLState();
          InteractionRenderers.renderItemModelCustom(renderItem, itemStack, transform);
          InteractionRenderers.cleanupAdditiveGLState();
          return true;
        }
      }

      return false;
    }
  }

  /**
   * Renders the given item with the given transform using the normal GL state.
   *
   * @param renderItem the instance of {@link RenderItem}
   * @param itemStack  the {@link ItemStack} to render
   * @param transform  the transform to apply to the GL state
   */
  public static void renderItemModel(RenderItem renderItem, ItemStack itemStack, Transform transform) {

    GlStateManager.pushMatrix();
    {
      InteractionRenderers.setupItemTransforms(transform);
      IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
      RenderHelper.renderItemModel(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
    }
    GlStateManager.popMatrix();
  }

  /**
   * Render the given item with the given transform without applying the
   * normal GL state.
   *
   * @param renderItem the instance of {@link RenderItem}
   * @param itemStack  the {@link ItemStack} to render
   * @param transform  the transform to apply to the GL state
   */
  public static void renderItemModelCustom(RenderItem renderItem, ItemStack itemStack, Transform transform) {

    GlStateManager.pushMatrix();
    {
      InteractionRenderers.setupItemTransforms(transform);
      IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
      RenderHelper.renderItemModelCustom(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
    }
    GlStateManager.popMatrix();
  }

  public static void setupAdditiveGLState() {

    GlStateManager.color(1, 1, 1, 0.2f);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    GlStateManager.enableAlpha();
    GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
    GlStateManager.enableRescaleNormal();
  }

  public static void cleanupAdditiveGLState() {

    GlStateManager.disableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.disableRescaleNormal();
  }

  /**
   * Applies the given {@link Transform} to the GL state.
   *
   * @param transform the transform to apply to the GL state
   */
  public static void setupItemTransforms(Transform transform) {

    if (transform != Transform.IDENTITY) {
      GlStateManager.translate(transform.translation.x, transform.translation.y, transform.translation.z);
      GlStateManager.rotate(transform.rotation);
      GlStateManager.scale(transform.scale.x, transform.scale.y, transform.scale.z);
    }
  }

  private InteractionRenderers() {
    //
  }

}
