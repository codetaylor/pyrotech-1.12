package com.codetaylor.mc.pyrotech.modules.tech.machine.client.render;

import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionRenderers;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCogWorkerBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class InteractionCogRenderer
    implements IInteractionRenderer<TileCogWorkerBase.InteractionCog> {

  public static final InteractionCogRenderer INSTANCE = new InteractionCogRenderer();

  @Override
  public void renderSolidPass(TileCogWorkerBase.InteractionCog interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    // If the handler is not empty, render the handler's item.

    if (!interaction.isEmpty()) {
      ItemStack itemStack = interaction.getStackInSlot();
      Transform transform = interaction.getTransform(world, pos, blockState, itemStack, partialTicks);

      GlStateManager.pushMatrix();
      {
        InteractionRenderers.setupItemTransforms(transform);

        TileCogWorkerBase.ClientRenderData data = interaction.getTile().getClientRenderData();

        if (data.remainingAnimationTime > 0) {
          data.remainingAnimationTime -= partialTicks;
          int previousCogRotationStage = (data.cogRotationStage == 0) ? 7 : data.cogRotationStage - 1;
          double bounce = bounce(data.totalAnimationTime - data.remainingAnimationTime, previousCogRotationStage * 45, 45, data.totalAnimationTime);
          GlStateManager.rotate((float) bounce, 0, 0, 1);

        } else {
          GlStateManager.rotate(data.cogRotationStage * 45, 0, 0, 1);
        }
        IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
        RenderHelper.renderItemModel(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
      }
      GlStateManager.popMatrix();
    }
  }

  @Override
  public void renderSolidPassText(TileCogWorkerBase.InteractionCog interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {
    //
  }

  @Override
  public boolean renderAdditivePass(TileCogWorkerBase.InteractionCog interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

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
        GlStateManager.pushMatrix();
        {
          InteractionRenderers.setupItemTransforms(transform);

          TileCogWorkerBase.ClientRenderData data = interaction.getTile().getClientRenderData();

          if (data.remainingAnimationTime > 0) {
            int previousCogRotationStage = (data.cogRotationStage == 0) ? 7 : data.cogRotationStage - 1;
            double bounce = bounce(data.totalAnimationTime - data.remainingAnimationTime, previousCogRotationStage * 45, 45, data.totalAnimationTime);
            GlStateManager.rotate((float) bounce, 0, 0, 1);

          } else {
            GlStateManager.rotate(data.cogRotationStage * 45, 0, 0, 1);
          }

          IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
          RenderHelper.renderItemModelCustom(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
        }
        GlStateManager.popMatrix();
        InteractionRenderers.cleanupAdditiveGLState();
        return true;
      }
    }

    return false;
  }

  /**
   * Bounce easing out, decelerating to zero velocity.
   *
   * @param t current time (milliseconds or frames)
   * @param b beginning value
   * @param c change in value
   * @param d duration
   */
  @SuppressWarnings("SameParameterValue")
  private double bounce(double t, double b, double c, double d) {

    if ((t /= d) < (1 / 2.75)) {
      return c * (7.5625 * t * t) + b;

    } else if (t < (2 / 2.75)) {
      return c * (7.5625 * (t -= (1.5 / 2.75)) * t + 0.75) + b;

    } else if (t < (2.5 / 2.75)) {
      return c * (7.5625 * (t -= (2.25 / 2.75)) * t + 0.9375) + b;

    } else {
      return c * (7.5625 * (t -= (2.625 / 2.75)) * t + 0.984375) + b;
    }
  }
}
