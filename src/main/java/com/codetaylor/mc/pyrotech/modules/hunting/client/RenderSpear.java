package com.codetaylor.mc.pyrotech.modules.hunting.client;

import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntitySpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderSpear
    extends Render<EntitySpear> {

  public RenderSpear(RenderManager renderManager) {

    super(renderManager);
  }

  @Nullable
  @Override
  protected ResourceLocation getEntityTexture(@Nonnull EntitySpear entity) {

    return TextureMap.LOCATION_MISSING_TEXTURE;
  }

  @Override
  public void doRender(EntitySpear entity, double x, double y, double z, float entityYaw, float partialTicks) {

    GlStateManager.pushMatrix();
    {
      GlStateManager.translate(x, y, z);
      GlStateManager.scale(1, 1, 1);
      GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
      GlStateManager.rotate(-entity.rotationPitch, 1, 0, 0);
      GlStateManager.rotate(-90, 0, 1, 0);
      GlStateManager.rotate(-45, 0, 0, 1);
      Minecraft.getMinecraft().getRenderItem().renderItem(entity.getItemStack(), ItemCameraTransforms.TransformType.NONE);
    }
    GlStateManager.popMatrix();
  }
}
