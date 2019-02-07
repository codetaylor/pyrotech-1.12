package com.codetaylor.mc.pyrotech.modules.tech.machine.client.render;

import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.interaction.api.InteractionRenderers;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileSawmillBase;
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

public class MillInteractionBladeRenderer
    implements IInteractionRenderer<TileSawmillBase.InteractionBlade> {

  public static final MillInteractionBladeRenderer INSTANCE = new MillInteractionBladeRenderer();

  @Override
  public void renderSolidPass(TileSawmillBase.InteractionBlade interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    // If the handler is not empty, render the handler's item.

    if (!interaction.isEmpty()) {
      ItemStack itemStack = interaction.getStackInSlot();
      Transform transform = interaction.getTransform(world, pos, blockState, itemStack, partialTicks);

      GlStateManager.pushMatrix();
      {
        InteractionRenderers.setupItemTransforms(transform);

        if (interaction.getTile().workerIsActive()) {
          GlStateManager.rotate((world.getTotalWorldTime() % 360 + partialTicks) * 64, 0, 0, 1);
        }
        IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
        RenderHelper.renderItemModel(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
      }
      GlStateManager.popMatrix();
    }
  }

  @Override
  public void renderSolidPassText(TileSawmillBase.InteractionBlade interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {
    // TODO
  }

  @Override
  public boolean renderAdditivePass(TileSawmillBase.InteractionBlade interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

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

          if (interaction.getTile().workerIsActive()) {
            GlStateManager.rotate((world.getTotalWorldTime() % 360 + partialTicks) * 64, 0, 0, 1);
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
}
