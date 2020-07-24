package com.codetaylor.mc.pyrotech.modules.storage.client.render;

import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.List;

public class TESRTank
    extends TileEntitySpecialRenderer<TileTankBase> {

  private static final float PX = 0.0625f;
  private static final float INSET = PX * 0.55f;

  @Override
  public void render(TileTankBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    RenderHelper.disableStandardItemLighting();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GlStateManager.enableBlend();
    GlStateManager.depthMask(true);

    if (Minecraft.isAmbientOcclusionEnabled()) {
      GlStateManager.shadeModel(GL11.GL_SMOOTH);

    } else {
      GlStateManager.shadeModel(GL11.GL_FLAT);
    }

    for (int i = 0; i <= 1; i++) {
      GlStateManager.cullFace(i == 0 ? CullFace.FRONT : CullFace.BACK);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
      this.renderTileEntityFast(te, x, y, z, partialTicks, destroyStage, partialTicks, buffer);
      buffer.setTranslation(0, 0, 0);
      tessellator.draw();
    }

    RenderHelper.enableStandardItemLighting();
  }

  @Override
  public void renderTileEntityFast(
      @Nonnull TileTankBase tile,
      double x,
      double y,
      double z,
      float partialTicks,
      int destroyStage,
      float partial,
      @Nonnull BufferBuilder buffer
  ) {

    if (tile.isConnectedDown()) {
      return;
    }

    FluidTank fluidTank = tile.getFluidTank();
    FluidStack fluidStack = fluidTank.getFluid();

    if (fluidStack != null) {

      World world = tile.getWorld();
      Fluid fluid = fluidStack.getFluid();
      TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
      TextureAtlasSprite still = textureMapBlocks.getAtlasSprite(fluid.getStill(fluidStack).toString());

      int color = fluid.getColor(fluidStack);
      float r = ((color >> 16) & 0xFF) / 255f;
      float g = ((color >> 8) & 0xFF) / 255f;
      float b = ((color >> 0) & 0xFF) / 255f;

      BlockPos blockpos = new BlockPos(tile.getPos());
      int i = world.isBlockLoaded(blockpos) ? world.getCombinedLight(blockpos, 0) : 0;
      int j = i >> 0x10 & 0xFFFF;
      int k = i & 0xFFFF;

      buffer.setTranslation(x, y, z);

      float topHeight = PX * 16;
      List<TileTankBase> tankGroup = tile.getTankGroup();
      int tankCount = tankGroup.size();

      if (tile.isConnectedUp()) {
        topHeight *= tankCount;
      }

      float percent = tile.getActualFluidAmount() / (float) tile.getActualFluidCapacity();
      float level = topHeight * percent - INSET;

      // TOP
      buffer
          .pos(INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(still.getMinU(), still.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(still.getMinU(), still.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(still.getMaxU(), still.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(still.getMaxU(), still.getMinV())
          .lightmap(j, k)
          .endVertex();

      for (int l = 0; l < tankCount; l++) {

        float yMin = l;
        float yMax = level;
        float interpolatedV = still.getInterpolatedV((1 - (level - l)) * 16);

        // if level < l, break
        // if level >= l + 1, yMax = l + 1
        // if level < l + 1, yMax = level - l

        if (level <= l) {
          break;

        } else if (level >= l + 1) {
          yMax = l + 1;
          interpolatedV = still.getInterpolatedV(0);
        }

        // SIDE X+
        buffer
            .pos(1 - INSET, yMin, INSET)
            .color(r, g, b, 1f)
            .tex(still.getMinU(), still.getMaxV())
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(1 - INSET, yMax, INSET)
            .color(r, g, b, 1f)
            .tex(still.getMinU(), interpolatedV)
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(1 - INSET, yMax, 1 - INSET)
            .color(r, g, b, 1f)
            .tex(still.getMaxU(), interpolatedV)
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(1 - INSET, yMin, 1 - INSET)
            .color(r, g, b, 1f)
            .tex(still.getMaxU(), still.getMaxV())
            .lightmap(j, k)
            .endVertex();

        // SIDE X-
        buffer
            .pos(INSET, yMin, INSET)
            .color(r, g, b, 1f)
            .tex(still.getMinU(), still.getMaxV())
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(INSET, yMin, 1 - INSET)
            .color(r, g, b, 1f)
            .tex(still.getMaxU(), still.getMaxV())
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(INSET, yMax, 1 - INSET)
            .color(r, g, b, 1f)
            .tex(still.getMaxU(), interpolatedV)
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(INSET, yMax, INSET)
            .color(r, g, b, 1f)
            .tex(still.getMinU(), interpolatedV)
            .lightmap(j, k)
            .endVertex();

        // SIDE Z-
        buffer
            .pos(INSET, yMin, 1 - INSET)
            .color(r, g, b, 1f)
            .tex(still.getMinU(), still.getMaxV())
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(1 - INSET, yMin, 1 - INSET)
            .color(r, g, b, 1f)
            .tex(still.getMaxU(), still.getMaxV())
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(1 - INSET, yMax, 1 - INSET)
            .color(r, g, b, 1f)
            .tex(still.getMaxU(), interpolatedV)
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(INSET, yMax, 1 - INSET)
            .color(r, g, b, 1f)
            .tex(still.getMinU(), interpolatedV)
            .lightmap(j, k)
            .endVertex();

        // SIDE Z+
        buffer
            .pos(INSET, yMin, INSET)
            .color(r, g, b, 1f)
            .tex(still.getMinU(), still.getMaxV())
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(INSET, yMax, INSET)
            .color(r, g, b, 1f)
            .tex(still.getMinU(), interpolatedV)
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(1 - INSET, yMax, INSET)
            .color(r, g, b, 1f)
            .tex(still.getMaxU(), interpolatedV)
            .lightmap(j, k)
            .endVertex();
        buffer
            .pos(1 - INSET, yMin, INSET)
            .color(r, g, b, 1f)
            .tex(still.getMaxU(), still.getMaxV())
            .lightmap(j, k)
            .endVertex();
      }
    }

  }
}
