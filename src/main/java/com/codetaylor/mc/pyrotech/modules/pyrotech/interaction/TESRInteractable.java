package com.codetaylor.mc.pyrotech.modules.pyrotech.interaction;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

@SuppressWarnings("WeakerAccess")
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

    GlStateManager.pushMatrix();
    GlStateManager.translate(0.5, 0, 0.5);

    EnumFacing facing = te.getTileFacing(world, te.getPos(), blockState);

    switch (facing) {
      case EAST:
        GlStateManager.rotate(-90, 0, 1, 0);
        break;
      case SOUTH:
        GlStateManager.rotate(-180, 0, 1, 0);
        break;
      case WEST:
        GlStateManager.rotate(-270, 0, 1, 0);
        break;
    }

    GlStateManager.translate(-0.5, 0, -0.5);

    if (renderPass == 0) {
      this.renderSolidPass(te, world, blockState, partialTicks);

    } else if (renderPass == 1) {
      this.renderAdditivePass(te, partialTicks, world, blockState);
    }

    GlStateManager.popMatrix();

    GlStateManager.popAttrib();
    GlStateManager.popMatrix();
  }

  protected void renderSolidPass(T te, World world, IBlockState blockState, float partialTicks) {

    RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

    GlStateManager.scale(1.0, 1.0, 1.0);

    IInteraction[] interactions = te.getInteractions();

    for (int i = 0; i < interactions.length; i++) {
      interactions[i].renderSolidPass(world, renderItem, te.getPos(), blockState, partialTicks);
    }
  }

  protected void renderAdditivePass(T te, float partialTicks, World world, IBlockState blockState) {

    EntityPlayerSP player = Minecraft.getMinecraft().player;

    if (player.isSneaking()) {
      return;
    }

    RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;

    if (rayTraceResult != null
        && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
        && rayTraceResult.hitInfo instanceof InteractionRayTraceData.List
        && (te.getPos().equals(rayTraceResult.getBlockPos()) || te.isExtendedInteraction(world, rayTraceResult.getBlockPos(), world.getBlockState(rayTraceResult.getBlockPos())))) {

      InteractionRayTraceData.List results = (InteractionRayTraceData.List) rayTraceResult.hitInfo;

      for (int i = 0; i < results.size(); i++) {

        InteractionRayTraceData data = results.get(i);
        IInteraction interaction = data.getInteraction();
        RayTraceResult result = data.getRayTraceResult();

        if (interaction.allowInteractionWithSide(result.sideHit)) {

          if (interaction.renderAdditivePass(world, result.sideHit, result.hitVec, result.getBlockPos(), blockState, player.getHeldItemMainhand(), partialTicks)) {
            // Only keep rendering if nothing was rendered.
            break;
          }
        }

      }
    }
  }

}
