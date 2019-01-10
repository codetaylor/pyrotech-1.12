package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileSoakingPot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

public class SoakingPotFluidRenderer
    implements IInteractionRenderer<TileSoakingPot.InteractionInputFluid> {

  public static final SoakingPotFluidRenderer INSTANCE = new SoakingPotFluidRenderer();

  private static final float PX = 0.0625f;
  private static final float INSET = 2 * PX;

  @Override
  public void renderSolidPass(TileSoakingPot.InteractionInputFluid interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    // If the handler is not empty, render the handler's item.

    FluidTank fluidTank = interaction.getFluidTank();
    FluidStack fluidStack = fluidTank.getFluid();

    if (fluidStack != null) {

      if (world.getTotalWorldTime() % 20 == 0) {
        //System.out.println(fluidStack.getFluid().getLocalizedName(fluidStack));
        //System.out.println(pos);
      }

      BlockModelShapes bm = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
      TextureAtlasSprite still = bm.getTexture(fluidStack.getFluid().getBlock().getDefaultState());

      BlockPos blockpos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
      int i = world.isBlockLoaded(blockpos.up()) ? world.getCombinedLight(blockpos.up(), 0) : 0;
      int j = i >> 0x10 & 0xFFFF;
      int k = i & 0xFFFF;

      float level = (PX * 7) * (fluidTank.getFluidAmount() / (float) fluidTank.getCapacity()) + (1 * PX);

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

      //buffer.setTranslation(0, 0, 0);
      tessellator.draw();

      RenderHelper.enableStandardItemLighting();
      //GlStateManager.enableCull();

    }
  }

  @Override
  public void renderSolidPassText(TileSoakingPot.InteractionInputFluid interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {
    // no-op
  }

  @Override
  public boolean renderAdditivePass(TileSoakingPot.InteractionInputFluid interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    return false;
  }
}
