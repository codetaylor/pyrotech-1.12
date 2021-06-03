package com.codetaylor.mc.pyrotech.modules.tech.machine.client.render;

import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBellows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;

import javax.annotation.Nonnull;

public class TESRMechanicalBellows
    extends FastTESR<TileBellows> {

  private static final float PX = 0.0625f;

  @Override
  public void renderTileEntityFast(
      @Nonnull TileBellows tile,
      double x,
      double y,
      double z,
      float partialTicks,
      int destroyStage,
      float partial,
      @Nonnull BufferBuilder buffer
  ) {

    TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
    TextureAtlasSprite bellows = textureMapBlocks.getAtlasSprite("pyrotech:blocks/mechanical_bellows_side");
    TextureAtlasSprite side = textureMapBlocks.getAtlasSprite("pyrotech:blocks/crate_stone_top");
    TextureAtlasSprite top = textureMapBlocks.getAtlasSprite("pyrotech:blocks/crate_stone_top");
    TextureAtlasSprite arm = textureMapBlocks.getAtlasSprite("pyrotech:blocks/mechanical_bellows_arm");

    BlockPos blockpos = new BlockPos(tile.getPos());
    int i = tile.getWorld().isBlockLoaded(blockpos) ? tile.getWorld().getCombinedLight(blockpos, 0) : 0;
    int j = i >> 0x10 & 0xFFFF;
    int k = i & 0xFFFF;

    float percent = tile.getProgress();
    float level = (PX * 16) - (PX * 8) * percent;

    buffer.setTranslation(x, y, z);

    // --- Top

    // TOP
    {
      buffer
          .pos(0, level, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(top.getMinU(), top.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(top.getMaxU(), top.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(top.getMaxU(), top.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(0, level, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(top.getMinU(), top.getMaxV())
          .lightmap(j, k)
          .endVertex();
    }

    // BOTTOM
    {
      buffer
          .pos(0, level - PX * 2, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(top.getMinU(), top.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level - PX * 2, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(top.getMaxU(), top.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level - PX * 2, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(top.getMaxU(), top.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(0, level - PX * 2, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(top.getMinU(), top.getMaxV())
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE X+
    {
      float maxV = side.getInterpolatedV(16);
      float minV = side.getInterpolatedV(14);
      buffer
          .pos(1, level - PX * 2, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMinU(), maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level - PX * 2, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMaxU(), maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMaxU(), minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMinU(), minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE X-
    {
      float maxV = side.getInterpolatedV(16);
      float minV = side.getInterpolatedV(14);
      buffer
          .pos(0, level - PX * 2, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMinU(), maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(0, level - PX * 2, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMaxU(), maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(0, level, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMaxU(), minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(0, level, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMinU(), minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE Z-
    {
      float maxV = side.getInterpolatedV(16);
      float minV = side.getInterpolatedV(14);
      buffer
          .pos(0, level - PX * 2, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMinU(), maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level - PX * 2, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMaxU(), maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMaxU(), minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(0, level, 1)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMinU(), minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE Z+
    {
      float maxV = side.getInterpolatedV(16);
      float minV = side.getInterpolatedV(14);
      buffer
          .pos(0, level - PX * 2, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMinU(), maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level - PX * 2, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMaxU(), maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1, level, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMaxU(), minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(0, level, 0)
          .color(1f, 1f, 1f, 1f)
          .tex(side.getMinU(), minV)
          .lightmap(j, k)
          .endVertex();
    }

    // Leather Part

    // SIDE X+
    {
      float minV = bellows.getInterpolatedV(2);
      float maxV = bellows.getInterpolatedV(10);
      float minU = bellows.getInterpolatedU(2);
      float maxU = bellows.getInterpolatedU(14);
      float inset = PX * 2;
      buffer
          .pos(1 - inset, PX * 6, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, PX * 6, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level - PX * 2, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level - PX * 2, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE X-
    {
      float minV = bellows.getInterpolatedV(2);
      float maxV = bellows.getInterpolatedV(10);
      float minU = bellows.getInterpolatedU(2);
      float maxU = bellows.getInterpolatedU(14);
      float inset = PX * 2;
      buffer
          .pos(inset, PX * 6, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, PX * 6, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level - PX * 2, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level - PX * 2, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE Z-
    {
      float minV = bellows.getInterpolatedV(2);
      float maxV = bellows.getInterpolatedV(10);
      float minU = bellows.getInterpolatedU(2);
      float maxU = bellows.getInterpolatedU(14);
      float inset = PX * 2;
      buffer
          .pos(inset, PX * 6, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, PX * 6, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level - PX * 2, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level - PX * 2, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE Z+
    {
      float minV = bellows.getInterpolatedV(2);
      float maxV = bellows.getInterpolatedV(10);
      float minU = bellows.getInterpolatedU(2);
      float maxU = bellows.getInterpolatedU(14);
      float inset = PX * 2;
      buffer
          .pos(inset, PX * 6, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, PX * 6, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level - PX * 2, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level - PX * 2, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, minV)
          .lightmap(j, k)
          .endVertex();
    }

    // Arm Part

    // SIDE X+
    {
      float minV = arm.getInterpolatedV(0);
      float maxV = arm.getInterpolatedV(8);
      float minU = arm.getInterpolatedU(6);
      float maxU = arm.getInterpolatedU(10);
      float inset = PX * 6;
      buffer
          .pos(1 - inset, level, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level + PX * 8, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level + PX * 8, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE X-
    {
      float minV = arm.getInterpolatedV(0);
      float maxV = arm.getInterpolatedV(8);
      float minU = arm.getInterpolatedU(6);
      float maxU = arm.getInterpolatedU(10);
      float inset = PX * 6;
      buffer
          .pos(inset, level, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level + PX * 8, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level + PX * 8, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE Z-
    {
      float minV = arm.getInterpolatedV(0);
      float maxV = arm.getInterpolatedV(8);
      float minU = arm.getInterpolatedU(6);
      float maxU = arm.getInterpolatedU(10);
      float inset = PX * 6;
      buffer
          .pos(inset, level, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level + PX * 8, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level + PX * 8, 1 - inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, minV)
          .lightmap(j, k)
          .endVertex();
    }

    // SIDE Z+
    {
      float minV = arm.getInterpolatedV(0);
      float maxV = arm.getInterpolatedV(8);
      float minU = arm.getInterpolatedU(6);
      float maxU = arm.getInterpolatedU(10);
      float inset = PX * 6;
      buffer
          .pos(inset, level, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, maxV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - inset, level + PX * 8, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(maxU, minV)
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(inset, level + PX * 8, inset)
          .color(1f, 1f, 1f, 1f)
          .tex(minU, minV)
          .lightmap(j, k)
          .endVertex();
    }
  }
}
