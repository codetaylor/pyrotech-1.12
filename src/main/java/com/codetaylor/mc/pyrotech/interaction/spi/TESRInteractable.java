package com.codetaylor.mc.pyrotech.interaction.spi;

import com.codetaylor.mc.pyrotech.interaction.util.InteractionRayTraceData;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("WeakerAccess")
public class TESRInteractable<T extends TileEntity & ITileInteractable>
    extends TileEntitySpecialRenderer<T> {

  @Override
  public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

    World world = te.getWorld();
    IBlockState blockState = world.getBlockState(te.getPos());

    int renderPass = MinecraftForgeClient.getRenderPass();

    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);

    GlStateManager.pushMatrix();
    GlStateManager.translate(0.5, 0, 0.5);

    EnumFacing facing = te.getTileFacing(world, te.getPos(), blockState);
    int yaw = 0;

    switch (facing) {
      case EAST:
        GlStateManager.rotate(-90, 0, 1, 0);
        yaw = -90;
        break;
      case SOUTH:
        GlStateManager.rotate(-180, 0, 1, 0);
        yaw = -180;
        break;
      case WEST:
        GlStateManager.rotate(-270, 0, 1, 0);
        yaw = -270;
        break;
    }

    GlStateManager.translate(-0.5, 0, -0.5);

    if (renderPass == 0) {
      this.renderSolidPass(te, world, blockState, partialTicks);

    } else if (renderPass == 1) {
      this.renderAdditivePass(te, partialTicks, world, blockState);
    }

    RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;

    if (rayTraceResult != null
        && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
        && (te.getPos().equals(rayTraceResult.getBlockPos())
        || te.isExtendedInteraction(world, rayTraceResult.getBlockPos(), world.getBlockState(rayTraceResult.getBlockPos())))) {

      Minecraft minecraft = Minecraft.getMinecraft();

      if (minecraft.player.isSneaking()
          || ModuleCoreConfig.CLIENT.ALWAYS_SHOW_QUANTITIES) {
        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
        IInteraction[] interactions = te.getInteractions();
        EnumFacing horizontalFacing = minecraft.player.getHorizontalFacing();

        for (int i = 0; i < interactions.length; i++) {
          Vec3d textOffset = interactions[i].getTextOffset(facing, horizontalFacing, rayTraceResult.sideHit);

          if (textOffset != null) {
            interactions[i].renderSolidPassText(world, fontrenderer, yaw, textOffset, te.getPos(), blockState, partialTicks);
          }
        }
      }
    }

    GlStateManager.popMatrix();
    GlStateManager.popMatrix();
  }

  protected void renderSolidPass(T te, World world, IBlockState blockState, float partialTicks) {

    Minecraft minecraft = Minecraft.getMinecraft();
    RenderItem renderItem = minecraft.getRenderItem();

    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

    GlStateManager.scale(1.0, 1.0, 1.0);

    IInteraction[] interactions = te.getInteractions();

    for (int i = 0; i < interactions.length; i++) {
      interactions[i].renderSolidPass(world, renderItem, te.getPos(), blockState, partialTicks);
    }
  }

  protected void renderAdditivePass(T te, float partialTicks, World world, IBlockState blockState) {

    // TODO: move to event handler?
    RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

    EntityPlayerSP player = Minecraft.getMinecraft().player;

    RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;

    if (rayTraceResult != null
        && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
        && rayTraceResult.hitInfo instanceof InteractionRayTraceData.List
        && (te.getPos().equals(rayTraceResult.getBlockPos())
        || te.isExtendedInteraction(world, rayTraceResult.getBlockPos(), world.getBlockState(rayTraceResult.getBlockPos())))) {

      InteractionRayTraceData.List results = (InteractionRayTraceData.List) rayTraceResult.hitInfo;

      for (int i = 0; i < results.size(); i++) {

        InteractionRayTraceData data = results.get(i);
        IInteraction interaction = data.getInteraction();
        RayTraceResult result = data.getRayTraceResult();

        if (!player.isSneaking()
            || interaction.forceRenderAdditivePassWhileSneaking()) {

          if (interaction.allowInteractionWithSide(result.sideHit)) {

            if (interaction.renderAdditivePass(world, renderItem, result.sideHit, result.hitVec, result.getBlockPos(), blockState, player.getHeldItemMainhand(), partialTicks)) {
              // Only keep rendering if nothing was rendered.
              break;
            }
          }
        }
      }

      if (!results.isEmpty()
          && ModuleCoreConfig.CLIENT.SHOW_INTERACTION_BOUNDS) {

        // setup additive gl state
        {
          GlStateManager.enableBlend();
          GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
          GlStateManager.enableAlpha();
          GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
          GlStateManager.enableRescaleNormal();
        }

        GlStateManager.glLineWidth(8.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.color(1, 1, 1, 1);

        for (int i = 0; i < results.size(); i++) {

          BlockPos pos = rayTraceResult.getBlockPos();
          InteractionRayTraceData data = results.get(i);
          IInteraction interaction = data.getInteraction();

          AxisAlignedBB bounds = interaction.getInteractionBounds(world, pos, blockState)
              .expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D);

          RenderGlobal.drawSelectionBoundingBox(bounds, 0, 1, 0, 1);
        }

        // cleanup additive gl state
        {
          GlStateManager.disableAlpha();
          GlStateManager.disableBlend();
          GlStateManager.disableRescaleNormal();
        }

        GlStateManager.enableTexture2D();
      }
    }
  }

}
