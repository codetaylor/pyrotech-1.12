package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.athenaeum.util.RenderHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockDryingRack;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileDryingRackCrude;
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

public class TESRDryingRackCrude
    extends TileEntitySpecialRenderer<TileDryingRackCrude> {

  @Override
  public void render(TileDryingRackCrude te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

    int renderPass = MinecraftForgeClient.getRenderPass();

    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);

    if (renderPass == 0) {

      World world = te.getWorld();
      IBlockState blockState = world.getBlockState(te.getPos());

      if (blockState.getBlock() == ModuleBlocks.DRYING_RACK) {

        ItemStackHandler stackHandler = te.getStackHandler();
        ItemStackHandler outputStackHandler = te.getOutputStackHandler();

        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

        GlStateManager.scale(1.0, 1.0, 1.0);

        ItemStack inputItemStack = stackHandler.getStackInSlot(0);
        ItemStack outputItemStack = outputStackHandler.getStackInSlot(0);

        if (!inputItemStack.isEmpty()) {

          GlStateManager.pushMatrix();
          {
            this.setupItemPosition(te);
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            IBakedModel model = renderItem.getItemModelWithOverrides(inputItemStack, null, null);
            RenderHelper.renderItemModel(inputItemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
          }
          GlStateManager.popMatrix();

        } else if (!outputItemStack.isEmpty()) {

          GlStateManager.pushMatrix();
          {
            this.setupItemPosition(te);
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            IBakedModel model = renderItem.getItemModelWithOverrides(outputItemStack, null, null);
            RenderHelper.renderItemModel(outputItemStack, model, ItemCameraTransforms.TransformType.NONE, false, false);
          }
          GlStateManager.popMatrix();
        }

      }

    } else if (renderPass == 1) {

      EntityPlayerSP player = Minecraft.getMinecraft().player;

      ItemStack heldItemMainhand = player.getHeldItemMainhand();

      RayTraceResult rayTraceResult = player
          .rayTrace(4, partialTicks);

      if (rayTraceResult != null
          && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
          //&& rayTraceResult.sideHit == EnumFacing.UP
          && rayTraceResult.getBlockPos().equals(te.getPos())) {

        ItemStackHandler stackHandler = te.getStackHandler();
        ItemStackHandler outputStackHandler = te.getOutputStackHandler();
        ItemStack inputItemStack = stackHandler.getStackInSlot(0);
        ItemStack outputItemStack = outputStackHandler.getStackInSlot(0);

        if (inputItemStack.isEmpty()
            && outputItemStack.isEmpty()) {

          if (!heldItemMainhand.isEmpty()) {

            GlStateManager.pushMatrix();
            {
              this.setupItemPosition(te);
              GlStateManager.color(1, 1, 1, 0.2f);
              this.renderItem(heldItemMainhand);
            }
            GlStateManager.popMatrix();
          }

        } else if (!inputItemStack.isEmpty()) {

          if (heldItemMainhand.isEmpty()) {

            GlStateManager.pushMatrix();
            {
              this.setupItemPosition(te);
              GlStateManager.color(1, 1, 1, 0.2f);
              this.renderItem(inputItemStack);
            }
            GlStateManager.popMatrix();
          }

        } else if (!outputItemStack.isEmpty()) {

          if (heldItemMainhand.isEmpty()) {

            GlStateManager.pushMatrix();
            {
              this.setupItemPosition(te);
              GlStateManager.color(1, 1, 1, 0.2f);
              this.renderItem(outputItemStack);
            }
            GlStateManager.popMatrix();
          }
        }

      }
    }

    GlStateManager.popAttrib();
    GlStateManager.popMatrix();
  }

  private void setupItemPosition(TileDryingRackCrude te) {

    World world = te.getWorld();
    IBlockState blockState = world.getBlockState(te.getPos());
    EnumFacing facing = blockState.getValue(BlockDryingRack.FACING);

    switch (facing) {
      case NORTH:
        GlStateManager.translate(0.5, 0.5, 0.15);
        //GlStateManager.rotate(90, 1, 0, 0);
        break;
      case SOUTH:
        GlStateManager.translate(0.5, 0.5, 0.85);
        GlStateManager.rotate(180, 0, 1, 0);
        break;
      case EAST:
        GlStateManager.translate(0.85, 0.5, 0.5);
        GlStateManager.rotate(270, 0, 1, 0);
        break;
      case WEST:
        GlStateManager.translate(0.15, 0.5, 0.5);
        GlStateManager.rotate(90, 0, 1, 0);
        break;
    }

    GlStateManager.scale(0.75, 0.75, 0.75);
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
