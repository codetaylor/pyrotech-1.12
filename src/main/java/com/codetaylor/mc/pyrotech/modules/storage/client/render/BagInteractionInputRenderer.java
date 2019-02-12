package com.codetaylor.mc.pyrotech.modules.storage.client.render;

import com.codetaylor.mc.pyrotech.interaction.api.InteractionRenderers;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.storage.tile.TileBagBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class BagInteractionInputRenderer
    implements IInteractionRenderer<TileBagBase.InteractionInput> {

  public static final BagInteractionInputRenderer INSTANCE = new BagInteractionInputRenderer();

  @Override
  public void renderSolidPass(TileBagBase.InteractionInput interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    TileBagBase tile = interaction.getTile();

    TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
    TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    TextureAtlasSprite still = textureMapBlocks.getAtlasSprite("minecraft:blocks/gravel");

    BlockPos blockpos = new BlockPos(tile.getPos());
    int i = tile.getWorld().isBlockLoaded(blockpos) ? tile.getWorld().getCombinedLight(blockpos, 0) : 0;
    int j = i >> 0x10 & 0xFFFF;
    int k = i & 0xFFFF;

    float percent = tile.getItemCount() / (float) tile.getItemCapacity();
    float level = (7f / 16f) * percent + (1f / 16f);

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    RenderHelper.disableStandardItemLighting();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GlStateManager.enableBlend();
    GlStateManager.disableCull();

    if (Minecraft.isAmbientOcclusionEnabled()) {
      GlStateManager.shadeModel(GL11.GL_SMOOTH);
    } else {
      GlStateManager.shadeModel(GL11.GL_FLAT);
    }

    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

    double insetX = 4.0 / 16.0;
    double insetZ = 6.0 / 16.0;

    // TOP
    buffer
        .pos(insetX, level, insetZ)
        .color(1f, 1f, 1f, 1f)
        .tex(still.getInterpolatedU(insetX * 16), still.getInterpolatedV(insetZ * 16))
        .lightmap(j, k)
        .endVertex();
    buffer
        .pos(1 - insetX, level, insetZ)
        .color(1f, 1f, 1f, 1f)
        .tex(still.getInterpolatedU((1 - insetX) * 16), still.getInterpolatedV(insetZ * 16))
        .lightmap(j, k)
        .endVertex();
    buffer
        .pos(1 - insetX, level, 1 - insetZ)
        .color(1f, 1f, 1f, 1f)
        .tex(still.getInterpolatedU((1 - insetX) * 16), still.getInterpolatedV((1 - insetZ) * 16))
        .lightmap(j, k)
        .endVertex();
    buffer
        .pos(insetX, level, 1 - insetZ)
        .color(1f, 1f, 1f, 1f)
        .tex(still.getInterpolatedU(insetX * 16), still.getInterpolatedV((1 - insetZ) * 16))
        .lightmap(j, k)
        .endVertex();

    tessellator.draw();

    RenderHelper.enableStandardItemLighting();
  }

  @Override
  public void renderSolidPassText(TileBagBase.InteractionInput interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

    TileBagBase tile = interaction.getTile();

    if (!interaction.isEmpty()) {

      int count = tile.getItemCount();

      ItemStack itemStack = interaction.getStackInSlot();
      Transform transform = interaction.getTransform(world, pos, blockState, itemStack, partialTicks);

      GlStateManager.pushMatrix();
      {
        if (transform != Transform.IDENTITY) {
          GlStateManager.translate(transform.translation.x, transform.translation.y, transform.translation.z);
        }
        InteractionRenderers.renderItemCount(fontRenderer, yaw, count, offset);
      }
      GlStateManager.popMatrix();
    }
  }

  @Override
  public boolean renderAdditivePass(TileBagBase.InteractionInput interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    TileBagBase tile = interaction.getTile();

    if (tile.getItemCount() < tile.getItemCapacity()
        && interaction.isItemStackValid(heldItemMainHand)) {
      Transform transform = interaction.getTransform(world, hitPos, blockState, heldItemMainHand, partialTicks);
      InteractionRenderers.setupAdditiveGLState();
      InteractionRenderers.renderItemModelCustom(renderItem, heldItemMainHand, transform);
      InteractionRenderers.cleanupAdditiveGLState();
      return true;
    }

    return false;
  }
}
