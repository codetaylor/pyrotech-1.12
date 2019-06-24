package com.codetaylor.mc.pyrotech.interaction.api;

import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionItemStack;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.interaction.spi.ITileInteractable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;

import java.awt.*;

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
    public void renderSolidPassText(IInteractionItemStack interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

      // If the handler is not empty, render the handler's item count.

      if (!interaction.isEmpty()) {
        ItemStack itemStack = interaction.getStackInSlot();
        Transform transform = interaction.getTransform(world, pos, blockState, itemStack, partialTicks);
        InteractionRenderers.renderItemCount(fontRenderer, yaw, itemStack, transform, offset);
      }
    }

    @Override
    public boolean renderAdditivePass(IInteractionItemStack interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      // If the handler is empty, render the held item.
      // Else, render the handler's item if the player's hand is empty.

      if (interaction.isEmpty()
          && interaction.shouldRenderAdditivePassForHeldItem(heldItemMainHand)) {

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
          && interaction.shouldRenderAdditivePassForStackInSlot(Minecraft.getMinecraft().player.isSneaking(), heldItemMainHand)) {

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

  public static void renderItemCount(FontRenderer fontRenderer, int yaw, ItemStack itemStack, Transform transform, Vec3d offset) {

    GlStateManager.pushMatrix();
    {
      if (transform != Transform.IDENTITY) {
        GlStateManager.translate(transform.translation.x, transform.translation.y, transform.translation.z);
      }
      InteractionRenderers.renderItemCount(fontRenderer, yaw, itemStack.getCount(), offset);
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

      if (transform.lwjglRotation == null) {
        transform.lwjglRotation = new Quaternion();
      }

      transform.lwjglRotation.x = transform.rotation.x;
      transform.lwjglRotation.y = transform.rotation.y;
      transform.lwjglRotation.z = transform.rotation.z;
      transform.lwjglRotation.w = transform.rotation.w;

      GlStateManager.translate(transform.translation.x, transform.translation.y, transform.translation.z);
      GlStateManager.rotate(transform.lwjglRotation);
      GlStateManager.scale(transform.scale.x, transform.scale.y, transform.scale.z);
    }
  }

  public static void renderItemCount(FontRenderer fontRenderer, int yaw, int count, Vec3d offset) {

    if (count <= 1) {
      return;
    }

    int verticalShift = 0;
    String countString = String.valueOf(count);
    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
    float viewerYaw = renderManager.playerViewY;
    float viewerPitch = renderManager.playerViewX;
    int i = fontRenderer.getStringWidth(countString) / 2;

    GlStateManager.pushMatrix();
    GlStateManager.translate(offset.x, offset.y, offset.z);
    GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-viewerYaw - yaw, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(viewerPitch, 1.0F, 0.0F, 0.0F);
    GlStateManager.scale(-0.0125F, -0.0125F, 0.0125F);
    GlStateManager.disableLighting();
    GlStateManager.depthMask(false);
    GlStateManager.disableDepth();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.disableTexture2D();

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos((double) (-i - 1), (double) (-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.5f).endVertex();
    bufferbuilder.pos((double) (-i - 1), (double) (8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.5f).endVertex();
    bufferbuilder.pos((double) (i + 1), (double) (8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.5f).endVertex();
    bufferbuilder.pos((double) (i + 1), (double) (-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.5f).endVertex();
    tessellator.draw();

    GlStateManager.enableTexture2D();
    GlStateManager.depthMask(true);

    fontRenderer.drawString(countString, -fontRenderer.getStringWidth(countString) / 2, verticalShift, Color.WHITE.getRGB());

    GlStateManager.enableLighting();
    GlStateManager.disableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.enableDepth();
    GlStateManager.popMatrix();
  }

  private InteractionRenderers() {
    //
  }

}
