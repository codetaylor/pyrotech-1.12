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

@SuppressWarnings("WeakerAccess")
public class TESRInteractable<T extends TileEntity & ITileInteractionHandler_ItemStack_Provider>
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
      this.renderSolidPass(te, world, blockState);

    } else if (renderPass == 1) {
      this.renderTransparentPass(te, partialTicks, world, blockState);
    }

    GlStateManager.popAttrib();
    GlStateManager.popMatrix();
  }

  protected void renderSolidPass(T te, World world, IBlockState blockState) {

    RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

    GlStateManager.scale(1.0, 1.0, 1.0);

    InteractionHandler_ItemStack_Base[] interactionHandlers = te.getInteractionHandlers();

    for (int i = 0; i < interactionHandlers.length; i++) {

      InteractionHandler_ItemStack_Base interactionHandler = interactionHandlers[i];

      // If the handler is not empty, render the handler's item.

      if (!interactionHandler.isEmpty()) {
        ItemStack itemStack = interactionHandler.getStackInSlot();
        Transform transform = interactionHandler.getTransform(world, te.getPos(), blockState, itemStack);
        this.renderItemModel(renderItem, itemStack, transform);
      }
    }
  }

  protected void renderTransparentPass(T te, float partialTicks, World world, IBlockState blockState) {

    EntityPlayerSP player = Minecraft.getMinecraft().player;

    if (player.isSneaking()) {
      return;
    }

    // TODO: cache raytrace result
    // Can we cache the raytrace result so we're only calling this once per
    // frame for all renderers?
    // Cache in static somewhere?

    RayTraceResult rayTraceResult = player
        .rayTrace(Reference.INTERACTION_BLOCK_REACH, partialTicks);

    if (rayTraceResult != null
        && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
        && rayTraceResult.getBlockPos().equals(te.getPos())) {

      this.renderGhostItem(te, world, blockState, player.getHeldItemMainhand(), rayTraceResult);
    }
  }

  protected void setupGLStateForGhostItems() {

    GlStateManager.color(1, 1, 1, 0.2f);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    GlStateManager.enableAlpha();
    GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
    GlStateManager.enableRescaleNormal();
  }

  protected void cleanupGLStateForGhostItems() {

    GlStateManager.disableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.disableRescaleNormal();
  }

  protected void renderGhostItem(T te, World world, IBlockState blockState, ItemStack heldItemMainHand, RayTraceResult rayTraceResult) {

    InteractionHandler_ItemStack_Base[] interactionHandlers = te.getInteractionHandlers();

    for (int i = 0; i < interactionHandlers.length; i++) {

      InteractionHandler_ItemStack_Base interactionHandler = interactionHandlers[i];

      if (interactionHandler.intersects(rayTraceResult)) {

        // If the handler is empty, render the held item.
        // Else, render the handler's item if the player's hand is empty.

        if (interactionHandler.isEmpty()
            && !heldItemMainHand.isEmpty()) {

          // Only render the held item if it is valid for the handler.
          if (interactionHandler.isItemStackValid(heldItemMainHand)) {
            Transform transform = interactionHandler.getTransform(world, te.getPos(), blockState, heldItemMainHand);

            // Since only one item will be rendered, it is better to wrap the
            // GL setup calls as late as possible so we're not setting it up
            // if the item isn't going to be rendered.
            this.setupGLStateForGhostItems();
            this.renderItemModelCustom(heldItemMainHand, transform);
            this.cleanupGLStateForGhostItems();
          }

        } else if (!interactionHandler.isEmpty()
            && heldItemMainHand.isEmpty()) {

          ItemStack itemStack = interactionHandler.getStackInSlot();
          Transform transform = interactionHandler.getTransform(world, te.getPos(), blockState, itemStack);

          if (!itemStack.isEmpty()) {

            // Since only one item will be rendered, it is better to wrap the
            // GL setup calls as late as possible so we're not setting it up
            // if the item isn't going to be rendered.
            this.setupGLStateForGhostItems();
            this.renderItemModelCustom(itemStack, transform);
            this.cleanupGLStateForGhostItems();
          }
        }

        break;
      }
    }
  }

  /**
   * Render the given item with the given transform without applying the
   * normal GL state.
   *
   * @param itemStack the {@link ItemStack} to render
   * @param transform the transform to apply to the GL state
   */
  protected void renderItemModelCustom(ItemStack itemStack, Transform transform) {

    GlStateManager.pushMatrix();
    {
      this.setupItemTransforms(transform);
      RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
      IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
      RenderHelper.renderItemModelCustom(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
    }
    GlStateManager.popMatrix();
  }

  /**
   * Renders the given item with the given transform using the normal GL state.
   *
   * @param renderItem the instance of {@link RenderItem}
   * @param itemStack  the {@link ItemStack} to render
   * @param transform  the transform to apply to the GL state
   */
  protected void renderItemModel(RenderItem renderItem, ItemStack itemStack, Transform transform) {

    GlStateManager.pushMatrix();
    {
      this.setupItemTransforms(transform);
      IBakedModel model = renderItem.getItemModelWithOverrides(itemStack, null, null);
      RenderHelper.renderItemModel(itemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
    }
    GlStateManager.popMatrix();
  }

  /**
   * Applies the given {@link Transform} to the GL state.
   *
   * @param transform the transform to apply to the GL state
   */
  protected void setupItemTransforms(Transform transform) {

    GlStateManager.translate(transform.translation.x, transform.translation.y, transform.translation.z);
    GlStateManager.rotate(transform.rotation);
    GlStateManager.scale(transform.scale.x, transform.scale.y, transform.scale.z);
  }

}
