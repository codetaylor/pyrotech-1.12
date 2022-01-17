package com.codetaylor.mc.pyrotech.modules.tool.client.render;

import com.codetaylor.mc.pyrotech.library.IModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class TEISRShield
    extends TileEntityItemStackRenderer {

  private final IModelRenderer model;
  private final ResourceLocation texture;

  public TEISRShield(IModelRenderer model, ResourceLocation texture) {

    this.model = model;
    this.texture = texture;
  }

  @Override
  public void renderByItem(@Nonnull ItemStack itemStack, float partialTicks) {

    Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);

    GlStateManager.pushMatrix();
    GlStateManager.scale(1, -1, -1);
    this.model.render();
    GlStateManager.popMatrix();
  }
}
