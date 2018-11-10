package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCampfire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class TESRCampfire
    extends TileEntitySpecialRenderer<TileCampfire> {

  @Override
  public void render(TileCampfire te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

    int renderPass = MinecraftForgeClient.getRenderPass();

    if (renderPass != 0) {
      return;
    }

    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();

    World world = te.getWorld();
    IBlockState blockState = world.getBlockState(te.getPos());

    if (blockState.getBlock() == ModuleBlocks.CAMPFIRE) {

      ItemStack stack = te.getStackHandler().getStackInSlot(0);
      boolean hasOutput = !te.getOutputStackHandler().getStackInSlot(0).isEmpty();

      if (!stack.isEmpty()
          || hasOutput) {

        RenderHelper.enableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0, 1.0, 1.0);

        if (!stack.isEmpty()) {
          EntityItem item = te.getEntityItem();
          item.hoverStart = -2;
          Minecraft.getMinecraft()
              .getRenderManager()
              .renderEntity(item, 0.5, 0.2, 0.5, 0, 0, false);

        } else if (hasOutput) {

          EntityItem entityItemOutput = te.getEntityItemOutput();

          if (entityItemOutput != null) {
            entityItemOutput.hoverStart = -2;
            Minecraft.getMinecraft()
                .getRenderManager()
                .renderEntity(entityItemOutput, 0.5, 0.2, 0.5, 0, 0, false);
          }
        }

        GlStateManager.popMatrix();
      }
    }

    GlStateManager.popAttrib();
    GlStateManager.popMatrix();
  }
}
