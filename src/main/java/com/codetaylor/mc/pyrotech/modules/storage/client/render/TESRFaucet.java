package com.codetaylor.mc.pyrotech.modules.storage.client.render;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockFaucetBase;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileFaucetBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TESRFaucet
    extends TileEntitySpecialRenderer<TileFaucetBase> {

  private static final float PX = 0.0625f;

  @Override
  public final void render(TileFaucetBase tile, double x, double y, double z, float partialTicks, int destroyStage, float partial) {

    if (!tile.isActive()) {
      return;
    }

    World world = this.getWorld();
    IBlockState blockState = world.getBlockState(tile.getPos());

    if (!(blockState.getBlock() instanceof BlockFaucetBase)) {
      return;
    }

    EnumFacing facing = blockState.getValue(Properties.FACING_HORIZONTAL);

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    RenderHelper.disableStandardItemLighting();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GlStateManager.enableBlend();
    GlStateManager.disableCull();

    if (Minecraft.isAmbientOcclusionEnabled()) {
      GlStateManager.shadeModel(GL11.GL_SMOOTH);
    } else {
      GlStateManager.shadeModel(GL11.GL_FLAT);
    }

    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);

    GlStateManager.pushMatrix();
    GlStateManager.translate(0.5, 0, 0.5);

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

    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    this.renderTileEntityFast(tile, x, y, z, partialTicks, destroyStage, partial, buffer);
    buffer.setTranslation(0, 0, 0);
    tessellator.draw();

    GlStateManager.popMatrix();
    GlStateManager.popMatrix();

    RenderHelper.enableStandardItemLighting();
  }

  @Override
  public void renderTileEntityFast(
      TileFaucetBase tile,
      double x,
      double y,
      double z,
      float partialTicks,
      int destroyStage,
      float partial,
      BufferBuilder buffer
  ) {

    FluidStack fluidStack = tile.getFluid();

    if (fluidStack == null) {
      return;
    }

    World world = tile.getWorld();
    BlockPos downPos = tile.getPos().down();
    IBlockState downState = world.getBlockState(downPos);
    Block downBlock = downState.getBlock();
    AxisAlignedBB boundingBox = downBlock.getCollisionBoundingBox(downState, world, downPos);
    double maxHeight = (boundingBox == null) ? -1 : Math.max(-1, boundingBox.maxY - 1);

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

    {
      // TOP
      buffer
          .pos(10 * PX, 10 * PX, 16 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(5), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, 10 * PX, 16 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(3), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, 10 * PX, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(3), flowing.getInterpolatedV(4))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(10 * PX, 10 * PX, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(5), flowing.getInterpolatedV(4))
          .lightmap(j, k)
          .endVertex();
    }

    {
      // BOTTOM
      buffer
          .pos(10 * PX, maxHeight, 10 * PX)
          .color(r, g, b, 1f)
          .tex(still.getInterpolatedU(7), still.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, maxHeight, 10 * PX)
          .color(r, g, b, 1f)
          .tex(still.getInterpolatedU(3), still.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, maxHeight, 8 * PX)
          .color(r, g, b, 1f)
          .tex(still.getInterpolatedU(3), still.getInterpolatedV(2))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(10 * PX, maxHeight, 8 * PX)
          .color(r, g, b, 1f)
          .tex(still.getInterpolatedU(7), still.getInterpolatedV(2))
          .lightmap(j, k)
          .endVertex();
    }

    {
      // SIDE X+
      buffer
          .pos(10 * PX, 10 * PX, 10 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(1), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(10 * PX, 10 * PX, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(10 * PX, maxHeight, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(5 + (-maxHeight) * 16))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(10 * PX, maxHeight, 10 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(1), flowing.getInterpolatedV(5 + (-maxHeight) * 16))
          .lightmap(j, k)
          .endVertex();
    }

    {
      // SIDE X-
      buffer
          .pos(6 * PX, 10 * PX, 10 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(1), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, 10 * PX, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, maxHeight, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(5 + (-maxHeight) * 16))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, maxHeight, 10 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(1), flowing.getInterpolatedV(5 + (-maxHeight) * 16))
          .lightmap(j, k)
          .endVertex();
    }

    {
      // SIDE Z-
      buffer
          .pos(10 * PX, 10 * PX, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(2), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, 10 * PX, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, maxHeight, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(5 + (-maxHeight) * 16))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(10 * PX, maxHeight, 8 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(2), flowing.getInterpolatedV(5 + (-maxHeight) * 16))
          .lightmap(j, k)
          .endVertex();
    }

    {
      // SIDE Z+
      buffer
          .pos(10 * PX, 4 * PX, 10 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(2), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, 4 * PX, 10 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, maxHeight, 10 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(2 + (-maxHeight) * 16))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(10 * PX, maxHeight, 10 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(2), flowing.getInterpolatedV(2 + (-maxHeight) * 16))
          .lightmap(j, k)
          .endVertex();
    }

    {
      // SIDE Z+far
      buffer
          .pos(10 * PX, 10 * PX, 16 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(2), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, 10 * PX, 16 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(0))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(6 * PX, 6 * PX, 16 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(0), flowing.getInterpolatedV(2))
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(10 * PX, 6 * PX, 16 * PX)
          .color(r, g, b, 1f)
          .tex(flowing.getInterpolatedU(2), flowing.getInterpolatedV(2))
          .lightmap(j, k)
          .endVertex();
    }
  }
}
