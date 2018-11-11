package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileDryingRack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.opengl.GL11;

public class TESRDryingRack
    extends TileEntitySpecialRenderer<TileDryingRack> {

  @Override
  public void render(TileDryingRack te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

    int renderPass = MinecraftForgeClient.getRenderPass();

    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);
    //GlStateManager.disableRescaleNormal();

    if (renderPass == 0) {

      World world = te.getWorld();
      IBlockState blockState = world.getBlockState(te.getPos());

      if (blockState.getBlock() == ModuleBlocks.DRYING_RACK) {

        ItemStackHandler stackHandler = te.getStackHandler();
        ItemStackHandler outputStackHandler = te.getOutputStackHandler();

        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0, 1.0, 1.0);

        for (int i = 0; i < 4; i++) {
          ItemStack inputItemStack = stackHandler.getStackInSlot(i);
          ItemStack outputItemStack = outputStackHandler.getStackInSlot(i);

          if (!inputItemStack.isEmpty()) {
            Minecraft.getMinecraft()
                .getRenderItem()
                .renderItem(inputItemStack, ItemCameraTransforms.TransformType.GROUND);

          } else if (!outputItemStack.isEmpty()) {
            Minecraft.getMinecraft()
                .getRenderItem()
                .renderItem(outputItemStack, ItemCameraTransforms.TransformType.GROUND);
          }
        }
      }

      GlStateManager.popMatrix();

    } else if (renderPass == 1) {

      EntityPlayerSP player = Minecraft.getMinecraft().player;

      ItemStack heldItemMainhand = player.getHeldItemMainhand();
      if (!heldItemMainhand.isEmpty()) {

        RayTraceResult rayTraceResult = player
            .rayTrace(3, partialTicks);

        if (rayTraceResult != null
            && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
            && rayTraceResult.sideHit == EnumFacing.UP
            && rayTraceResult.getBlockPos().equals(te.getPos())) {

          double hitX = rayTraceResult.hitVec.x - rayTraceResult.getBlockPos().getX();
          double hitZ = rayTraceResult.hitVec.z - rayTraceResult.getBlockPos().getZ();

          int qX = (hitX < 0.5) ? 0 : 1;
          int qZ = (hitZ < 0.5) ? 0 : 1;
          int index = qX + qZ * 2;

          GlStateManager.pushMatrix();

          GlStateManager.translate(
              qX * 0.375 + 0.25 + 0.0625,
              0.75 + 0.03125,
              qZ * 0.375 + 0.25 + 0.0625
          );

          GlStateManager.rotate(90, 1, 0, 0);
          GlStateManager.scale(0.25, 0.25, 0.25);
          GlStateManager.color(1, 1, 1, 0.2f);

          this.renderItem(heldItemMainhand);

          GlStateManager.popMatrix();
        }
      }
    }

    GlStateManager.popAttrib();
    GlStateManager.popMatrix();
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
