package com.codetaylor.mc.pyrotech.modules.tech.refractory.client.render;

import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarCollectorBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

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
      BufferBuilder buffer
  ) {

    FluidTank fluidTank = tile.getFluidTank();
    FluidStack fluidStack = fluidTank.getFluid();

    if (fluidStack != null) {

      BlockModelShapes bm = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
      TextureAtlasSprite still = bm.getTexture(fluidStack.getFluid().getBlock().getDefaultState());

      BlockPos blockpos = new BlockPos(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
      int i = tile.getWorld().isBlockLoaded(blockpos.up()) ? tile.getWorld().getCombinedLight(blockpos.up(), 0) : 0;
      int j = i >> 0x10 & 0xFFFF;
      int k = i & 0xFFFF;

      float level = (PX * 13) * fluidTank.getFluidAmount() / (float) fluidTank.getCapacity() + (PX * 2);

      buffer.setTranslation(x, y, z);

      buffer
          .pos(INSET, level, INSET)
          .color(1f, 1f, 1f, 1f)
          .tex(still.getMinU(), still.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, INSET)
          .color(1f, 1f, 1f, 1f)
          .tex(still.getMaxU(), still.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, level, 1 - INSET)
          .color(1f, 1f, 1f, 1f)
          .tex(still.getMaxU(), still.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, level, 1 - INSET)
          .color(1f, 1f, 1f, 1f)
          .tex(still.getMinU(), still.getMaxV())
          .lightmap(j, k)
          .endVertex();
    }
  }
}
