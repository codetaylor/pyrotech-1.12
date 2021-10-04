package com.codetaylor.mc.pyrotech.modules.tech.refractory.client.render;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarCollectorBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;

public class TESRTarCollector
    extends FastTESR<TileTarCollectorBase> {

  private static final float PX = 0.0625f;
  private static final float INSET = 2 * PX;

  @Override
  public void renderTileEntityFast(
      TileTarCollectorBase tile,
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
      ResourceLocation resourceLocation = fluid.getStill(fluidStack);
      TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
      TextureAtlasSprite sprite = textureMapBlocks.getAtlasSprite(resourceLocation.toString());

      int color = fluid.getColor(fluidStack);
      float r = ((color >> 16) & 0xFF) / 255f;
      float g = ((color >> 8) & 0xFF) / 255f;
      //noinspection PointlessBitwiseExpression
      float b = ((color >> 0) & 0xFF) / 255f;

      BlockPos blockpos = new BlockPos(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
      int i = tile.getWorld().isBlockLoaded(blockpos.up()) ? tile.getWorld().getCombinedLight(blockpos.up(), 0) : 0;
      int j = i >> 0x10 & 0xFFFF;
      int k = i & 0xFFFF;

      float level = (PX * 13) * fluidTank.getFluidAmount() / (float) fluidTank.getCapacity() + (PX * 2);

      buffer.setTranslation(x, y, z);

      buffer
          .pos(INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(sprite.getMinU(), sprite.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, INSET)
          .color(r, g, b, 1f)
          .tex(sprite.getMaxU(), sprite.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(sprite.getMaxU(), sprite.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, level, 1 - INSET)
          .color(r, g, b, 1f)
          .tex(sprite.getMinU(), sprite.getMaxV())
          .lightmap(j, k)
          .endVertex();
    }
  }
}
