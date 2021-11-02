package com.codetaylor.mc.pyrotech.modules.hunting.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ModelMud
    extends ModelBase {

  ModelRenderer bodies;

  public ModelMud() {

    this.bodies = new ModelRenderer(this, 0, 0);
    this.bodies.addBox(-4f, 16f, -4f, 8, 8, 8);
  }

  @Override
  public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

    this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
    GlStateManager.translate(0, 0.001f, 0);
    this.bodies.render(scale);
  }
}
