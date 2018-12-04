package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionRenderers;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCampfire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CampfireInteractionLogRenderer
    implements IInteractionRenderer<TileCampfire.InteractionLog> {

  public static final CampfireInteractionLogRenderer INSTANCE = new CampfireInteractionLogRenderer();

  @Override
  public void renderSolidPass(TileCampfire.InteractionLog interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    // If the handler is not empty, render the handler's item.

    int logCount = interaction.getLogCount();

    for (int i = 0; i < logCount; i++) {
      this.renderLog(renderItem, i, interaction.getLog(i));
    }
  }

  @Override
  public boolean renderAdditivePass(TileCampfire.InteractionLog interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    // If the handler is empty, render the held item.
    // Else, render the handler's item if the player's hand is empty.

    int logCount = interaction.getLogCount();
    EntityPlayerSP player = Minecraft.getMinecraft().player;

    if (!player.isSneaking()
        && logCount < 8
        && !heldItemMainHand.isEmpty()) {

      // Only render the held item if it is valid for the handler.
      if (interaction.isItemStackValid(heldItemMainHand)) {

        // Since only one item will be rendered, it is better to wrap the
        // GL setup calls as late as possible so we're not setting it up
        // if the item isn't going to be rendered.

        InteractionRenderers.setupAdditiveGLState();
        this.renderLogCustom(logCount, heldItemMainHand, renderItem);
        InteractionRenderers.cleanupAdditiveGLState();
        return true;
      }

    } else if (player.isSneaking()
        && logCount > 0
        && heldItemMainHand.isEmpty()) {

      ItemStack log = interaction.getLog(logCount - 1);

      if (!log.isEmpty()) {
        // Since only one item will be rendered, it is better to wrap the
        // GL setup calls as late as possible so we're not setting it up
        // if the item isn't going to be rendered.

        InteractionRenderers.setupAdditiveGLState();
        this.renderLogCustom(logCount - 1, log, renderItem);
        InteractionRenderers.cleanupAdditiveGLState();
        return true;
      }
    }

    return false;
  }

  private void renderLog(RenderItem renderItem, int i, ItemStack log) {

    this.renderLogPre(i);
    InteractionRenderers.renderItemModel(renderItem, log, Transform.IDENTITY);
    this.renderLogPost();
  }

  private void renderLogCustom(int i, ItemStack log, RenderItem renderItem) {

    this.renderLogPre(i);
    InteractionRenderers.renderItemModelCustom(renderItem, log, Transform.IDENTITY);
    this.renderLogPost();
  }

  private void renderLogPre(int i) {

    GlStateManager.pushMatrix();

    if (i < 4) {

      GlStateManager.translate(0.5, 0.20, 0.5);
      GlStateManager.rotate(90 * (i % 4), 0, 1, 0);

      GlStateManager.pushMatrix();

      GlStateManager.translate(0.25 + 0.125, 0, 0);
      GlStateManager.rotate(45 + 22.5f, 0, 0, 1);

    } else {

      GlStateManager.translate(0.5, 0.125, 0.5);
      GlStateManager.rotate(90 * (i % 4) + 45, 0, 1, 0);

      GlStateManager.pushMatrix();

      GlStateManager.translate(0.25 + 0.125 + 0.0625, 0, 0);
      GlStateManager.rotate(90, 0, 0, 1);
    }

    GlStateManager.scale(4.0 / 16.0, 8.0 / 16.0, 4.0 / 16.0);
  }

  private void renderLogPost() {

    GlStateManager.popMatrix();
    GlStateManager.popMatrix();
  }

  private CampfireInteractionLogRenderer() {
    //
  }
}
