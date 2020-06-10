package com.codetaylor.mc.pyrotech.modules.tech.bloomery.client.render;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.athenaeum.interaction.api.InteractionRenderers;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloomery;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class BloomeryFuelRenderer
    implements IInteractionRenderer<TileBloomery.InteractionFuel> {

  public static final BloomeryFuelRenderer INSTANCE = new BloomeryFuelRenderer();

  private static final float PX = 0.0625f;
  private static final float INSET = 3 * PX;

  @Override
  public void renderSolidPass(TileBloomery.InteractionFuel interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    // If the handler is not empty, render the stuff.

    TileBloomery.FuelStackHandler fuelStackHandler = interaction.getFuelStackHandler();
    boolean hasFuel = !fuelStackHandler.getFirstNonEmptyItemStack().isEmpty();
    TileBloomery tile = interaction.getTile();
    boolean isActive = tile.isActive();
    boolean hasAsh = tile.getAshCount() > 0;
    boolean shouldRender = hasFuel || hasAsh || isActive;

    if (shouldRender) {

      float fuelLevel = tile.getFuelCount() / (float) tile.getMaxFuelCount();
      float ashLevel = tile.getAshCount() / (float) tile.getMaxAshCapacity();

      TextureAtlasSprite sprite;

      if (hasFuel) {
        sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.COAL_BLOCK.getDefaultState());

      } else if (hasAsh) {
        sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("pyrotech:blocks/ash_block");

      } else {
        sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("pyrotech:blocks/active_pile");
      }

      BlockPos blockpos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
      int i = world.isBlockLoaded(blockpos.up()) ? world.getCombinedLight(blockpos.up(), 0) : 0;
      int j = i >> 0x10 & 0xFFFF;
      int k = i & 0xFFFF;

      float level = Math.max(fuelLevel, ashLevel);
      float adjustedLevel = (PX * 9) * level + (14 * PX);

      Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GlStateManager.enableBlend();
      GlStateManager.disableCull();

      if (Minecraft.isAmbientOcclusionEnabled()) {
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

      } else {
        GlStateManager.shadeModel(GL11.GL_FLAT);
      }

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.getBuffer();
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

      //GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
      //buffer.setTranslation(pos.getX(), pos.getY(), pos.getZ());

      buffer
          .pos(INSET, adjustedLevel, INSET)
          .color(1f, 1f, 1f, 1f)
          .tex(sprite.getMinU(), sprite.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, adjustedLevel, INSET)
          .color(1f, 1f, 1f, 1f)
          .tex(sprite.getMaxU(), sprite.getMinV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(1 - INSET, adjustedLevel, 1 - INSET)
          .color(1f, 1f, 1f, 1f)
          .tex(sprite.getMaxU(), sprite.getMaxV())
          .lightmap(j, k)
          .endVertex();
      buffer
          .pos(INSET, adjustedLevel, 1 - INSET)
          .color(1f, 1f, 1f, 1f)
          .tex(sprite.getMinU(), sprite.getMaxV())
          .lightmap(j, k)
          .endVertex();

      tessellator.draw();

      if (isActive) {
        GlStateManager.pushMatrix();
        {
          GlStateManager.translate(3.0 / 16.0, adjustedLevel, 3.0 / 16.0);

          float airflow = tile.getAirflow();

          if (airflow > 1) {
            GlStateManager.pushMatrix();
            double scaleY = 2 * ((0.25 * (airflow + 3) - 1) / (0.25 * (airflow + 3)));
            GlStateManager.scale(0.5, MathHelper.clamp(scaleY, 0, 1), 0.5);
            GlStateManager.translate(3.0 / 16.0, 0, 3.0 / 16.0);
            GlStateManager.enableCull();

            BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBlockState fireState = Blocks.FIRE.getDefaultState();
            IBakedModel model = renderer.getModelForState(fireState);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            renderer.getBlockModelRenderer().renderModel(world, model, fireState, BlockPos.ORIGIN, buffer, true);
            tessellator.draw();
            GlStateManager.popMatrix();
          }

          GlStateManager.scale(10.0 / 16.0, 10.0 / 16.0, 10.0 / 16.0);
          GlStateManager.enableCull();

          BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
          IBlockState fireState = Blocks.FIRE.getDefaultState();
          IBakedModel model = renderer.getModelForState(fireState);
          buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
          renderer.getBlockModelRenderer().renderModel(world, model, fireState, BlockPos.ORIGIN, buffer, true);
          tessellator.draw();
        }
        GlStateManager.popMatrix();
      }

      RenderHelper.enableStandardItemLighting();
    }
  }

  @Override
  public void renderSolidPassText(TileBloomery.InteractionFuel interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {
    // no-op
  }

  @Override
  public boolean renderAdditivePass(TileBloomery.InteractionFuel interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    // If the tile isn't full of fuel, render the held item.

    TileBloomery tile = interaction.getTile();

    if (!tile.isFuelFull()
        && StackHelper.getItemBurnTime(heldItemMainHand) + tile.getBurnTime() <= tile.getMaxBurnTime()
        && interaction.shouldRenderAdditivePassForHeldItem(heldItemMainHand)) {

      // Only render the held item if it is valid for the handler.
      if (interaction.isItemStackValid(heldItemMainHand)) {
        Transform transform = interaction.getTransform(world, hitPos, blockState, heldItemMainHand, partialTicks);

        // Since only one item will be rendered, it is better to wrap the
        // GL setup calls as late as possible so we're not setting it up
        // if the item isn't going to be rendered.

        InteractionRenderers.setupAdditiveGLState();
        InteractionRenderers.renderItemModelCustom(renderItem, heldItemMainHand, transform);
        InteractionRenderers.cleanupAdditiveGLState();
        return true;
      }
    }

    return false;
  }
}
