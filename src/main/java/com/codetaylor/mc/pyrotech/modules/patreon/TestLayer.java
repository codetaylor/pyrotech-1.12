package com.codetaylor.mc.pyrotech.modules.patreon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TestLayer
    implements LayerRenderer<EntityLivingBase> {

  private final ModelRenderer modelRenderer;

  public TestLayer(ModelRenderer modelRenderer) {

    this.modelRenderer = modelRenderer;
  }

  public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

    GlStateManager.pushMatrix();

    if (entity.isSneaking()) {
      GlStateManager.translate(0.0F, 0.2F, 0.0F);
    }

    this.modelRenderer.postRender(0.0625F);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    float scalar = 0.9f;
    GlStateManager.scale(1.1875F * scalar, -1.1875F * scalar, -1.1875F * scalar);

    RenderLivingBase<EntityMagmaCube> render = (RenderLivingBase<EntityMagmaCube>) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntityMagmaCube.class);
    EntityMagmaCube magmaCube = new EntityMagmaCube(Minecraft.getMinecraft().world);
    render.doRender(magmaCube, 0.0, 0, 0.0, netHeadYaw, partialTicks);
//    TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, 5, null, -1, limbSwing);

    GlStateManager.popMatrix();
  }

  public boolean shouldCombineTextures() {

    return false;
  }

}
