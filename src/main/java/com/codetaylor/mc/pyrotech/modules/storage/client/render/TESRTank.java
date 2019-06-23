package com.codetaylor.mc.pyrotech.modules.storage.client.render;

import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;

public class TESRTank
    extends FastTESR<TileTankBase> {

  private static final float PX = 0.0625f;
  private static final float INSET = PX * 0.1f;

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

    FluidTank fluidTank = tile.getFluidTank();
    FluidStack fluidStack = fluidTank.getFluid();

    if (fluidStack != null) {

      Fluid fluid = fluidStack.getFluid();
      TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
      TextureAtlasSprite still = textureMapBlocks.getAtlasSprite(fluid.getStill(fluidStack).toString());
      TextureAtlasSprite flowing = textureMapBlocks.getAtlasSprite(fluid.getFlowing(fluidStack).toString());

      int color = fluid.getColor(fluidStack);
      float r = ((color >> 16) & 0xFF) / 255f;
      float g = ((color >> 8) & 0xFF) / 255f;
      float b = ((color >> 0) & 0xFF) / 255f;

      BlockPos blockpos = new BlockPos(tile.getPos());
      int i = tile.getWorld().isBlockLoaded(blockpos) ? tile.getWorld().getCombinedLight(blockpos, 0) : 0;
      int j = i >> 0x10 & 0xFFFF;
      int k = i & 0xFFFF;

      float percent = fluidTank.getFluidAmount() / (float) fluidTank.getCapacity();
      float level = (PX * 14) * percent + PX;

      buffer.setTranslation(x, y, z);

      // TOP
      buffer
          .pos(INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(still.getMinU(), still.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(still.getMaxU(), still.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(still.getMaxU(), still.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(still.getMinU(), still.getMaxV())
          .lightmap(j, k)
          .endVertex();

      float interpolatedV = flowing.getInterpolatedV((1 - percent) * 16);

      // SIDE X+
      buffer
          .pos(1 - INSET, PX, INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMinU(), flowing.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, PX, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMaxU(), flowing.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMaxU(), interpolatedV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMinU(), interpolatedV)
          .lightmap(j, k)
          .endVertex();

      // SIDE X-
      buffer
          .pos(INSET, PX, INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMinU(), flowing.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, PX, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMaxU(), flowing.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMaxU(), interpolatedV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMinU(), interpolatedV)
          .lightmap(j, k)
          .endVertex();

      // SIDE Z-
      buffer
          .pos(INSET, PX, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMinU(), flowing.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, PX, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMaxU(), flowing.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMaxU(), interpolatedV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMinU(), interpolatedV)
          .lightmap(j, k)
          .endVertex();

      // SIDE Z+
      buffer
          .pos(INSET, PX, INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMinU(), flowing.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, PX, INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMaxU(), flowing.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMaxU(), interpolatedV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(flowing.getMinU(), interpolatedV)
          .lightmap(j, k)
          .endVertex();
    }
  }
}
