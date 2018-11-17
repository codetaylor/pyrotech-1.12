package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

public class TESRInteractable<T extends TileEntity & ITileInteractable>
    extends TileEntitySpecialRenderer<T> {

  @Override
  public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

    World world = te.getWorld();
    IBlockState blockState = world.getBlockState(te.getPos());

    int renderPass = MinecraftForgeClient.getRenderPass();

    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);

    if (renderPass == 0) {
      this.renderSolid(te, world, blockState);

    } else if (renderPass == 1) {
      this.renderTransparent(te, partialTicks, world, blockState);
    }

    GlStateManager.popAttrib();
    GlStateManager.popMatrix();
  }

  private void renderTransparent(T te, float partialTicks, World world, IBlockState blockState) {

    EntityPlayerSP player = Minecraft.getMinecraft().player;

    if (!player.isSneaking()) {

      ItemStack heldItemMainHand = player.getHeldItemMainhand();

      // TODO: Can we cache the raytrace result so we're only calling this once per frame?

      RayTraceResult rayTraceResult = player
          .rayTrace(Reference.INTERACTION_BLOCK_REACH, partialTicks);

      if (rayTraceResult != null
          && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
          && rayTraceResult.getBlockPos().equals(te.getPos())) {

        InteractionHandler[] interactionHandlers = te.getInteractionHandlers();

        for (int i = 0; i < interactionHandlers.length; i++) {

          InteractionHandler interactionHandler = interactionHandlers[i];

          if (interactionHandler.intersects(rayTraceResult)) {

            // If the handler is empty, render the held item.
            // Else, render the handler's item if the player's hand is empty.

            if (interactionHandler.isEmpty()
                && !heldItemMainHand.isEmpty()) {

              InteractionHandler.Transforms transforms = interactionHandler.getTransforms(world, te.getPos(), blockState, heldItemMainHand);
              this.renderGhostItem(heldItemMainHand, transforms);

            } else if (!interactionHandler.isEmpty()
                && heldItemMainHand.isEmpty()) {

              ItemStack itemStack = interactionHandler.getStackInSlot();
              InteractionHandler.Transforms transforms = interactionHandler.getTransforms(world, te.getPos(), blockState, itemStack);
              this.renderGhostItem(itemStack, transforms);
            }

            break;
          }
        }
      }
    }
  }

  private void renderSolid(T te, World world, IBlockState blockState) {

    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

    GlStateManager.scale(1.0, 1.0, 1.0);

    InteractionHandler[] interactionHandlers = te.getInteractionHandlers();

    for (int i = 0; i < interactionHandlers.length; i++) {

      InteractionHandler interactionHandler = interactionHandlers[i];

      // If the handler is not empty, render the handler's item.

      if (!interactionHandler.isEmpty()) {
        ItemStack itemStack = interactionHandler.getStackInSlot();
        InteractionHandler.Transforms transforms = interactionHandler.getTransforms(world, te.getPos(), blockState, itemStack);
        this.renderSolidItem(itemStack, transforms);
      }
    }
  }

  private void renderGhostItem(ItemStack itemStack, InteractionHandler.Transforms transforms) {

    GlStateManager.pushMatrix();
    {
      this.setupItemTransforms(transforms);
      GlStateManager.color(1, 1, 1, 0.2f);
      this.renderItem(itemStack);
    }
    GlStateManager.popMatrix();
  }

  private void renderSolidItem(ItemStack itemStack, InteractionHandler.Transforms transforms) {

    GlStateManager.pushMatrix();
    {
      this.setupItemTransforms(transforms);
      RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
      IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
      RenderHelper.renderItemModel(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
    }
    GlStateManager.popMatrix();
  }

  private void setupItemTransforms(InteractionHandler.Transforms transforms) {

    GlStateManager.translate(transforms.translation.x, transforms.translation.y, transforms.translation.z);
    GlStateManager.rotate(transforms.rotation);
    GlStateManager.scale(transforms.scale.x, transforms.scale.y, transforms.scale.z);
  }

  private void renderItem(ItemStack itemStack) {

    if (!itemStack.isEmpty()) {

      RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);

      GlStateManager.enableRescaleNormal();

      IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
      RenderHelper.renderItemModelCustom(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);

      GlStateManager.disableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.disableRescaleNormal();
    }
  }

}
