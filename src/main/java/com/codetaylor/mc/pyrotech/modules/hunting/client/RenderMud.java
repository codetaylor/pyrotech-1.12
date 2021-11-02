package com.codetaylor.mc.pyrotech.modules.hunting.client;

import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntityMud;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderMud
    extends RenderLiving<EntityMud> {

  private static final ResourceLocation TEXTURE = new ResourceLocation(ModuleHunting.MOD_ID, "textures/entity/mud/mud.png");

  public RenderMud(RenderManager p_i47193_1_) {

    super(p_i47193_1_, new ModelMud(), 0.25F);
  }

  @Override
  public void doRender(EntityMud entity, double x, double y, double z, float entityYaw, float partialTicks) {

    this.shadowSize = 0.25F * (float) entity.getSlimeSize();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }

  @Override
  protected void preRenderCallback(EntityMud entity, float partialTickTime) {

    float scale = 0.999f;
    GlStateManager.scale(scale, scale, scale);
    float size = (float) entity.getSlimeSize();
    float f2 = (entity.prevSquishFactor + (entity.squishFactor - entity.prevSquishFactor) * partialTickTime) / (size * 0.5f + 1.0f);
    float f3 = 1.0F / (f2 + 1.0F);
    GlStateManager.scale(f3 * size, 1.0F / f3 * size, f3 * size);
  }

  @Override
  protected ResourceLocation getEntityTexture(@Nonnull EntityMud entity) {

    return TEXTURE;
  }
}