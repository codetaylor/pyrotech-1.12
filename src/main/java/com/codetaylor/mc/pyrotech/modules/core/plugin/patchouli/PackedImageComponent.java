package com.codetaylor.mc.pyrotech.modules.core.plugin.patchouli;

import com.codetaylor.mc.pyrotech.packer.PackAPI;
import com.codetaylor.mc.pyrotech.packer.PackedData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.VariableHolder;

public class PackedImageComponent
    implements ICustomComponent {

  @VariableHolder()
  public String image;
  public float scale = 1f;

  private transient PackedData.AtlasData atlasData;
  private transient PackedData.ImageData imageData;
  private transient ResourceLocation atlasResourceLocation;
  private transient int x, y;

  /**
   * Called when this component is built. Take the chance to read variables and set
   * any local positions here.
   */
  @Override
  public void build(int componentX, int componentY, int pageNum) {

    ResourceLocation resourceLocation = new ResourceLocation(this.image);
    this.imageData = PackAPI.getImageData(resourceLocation);
    String resourceDomain = resourceLocation.getResourceDomain();
    this.atlasResourceLocation = new ResourceLocation(resourceDomain, this.imageData.atlas);
    this.atlasData = PackAPI.getAtlasData(this.atlasResourceLocation);

    this.x = componentX;
    this.y = componentY;
  }

  /**
   * Called every render tick. No special transformations are applied, so you're responsible
   * for putting everything in the right place.
   */
  @Override
  public void render(IComponentRenderContext context, float partialTicks, int mouseX, int mouseY) {

    if (MathHelper.epsilonEquals(this.scale, 0)
        || this.scale <= 0) {
      return;
    }

    Minecraft.getMinecraft().renderEngine.bindTexture(this.atlasResourceLocation);
    GlStateManager.pushMatrix();
    GlStateManager.translate(this.x, this.y, 0);
    GlStateManager.scale(this.scale, this.scale, this.scale);
    GlStateManager.color(1F, 1F, 1F, 1F);
    GlStateManager.enableBlend();
    Gui.drawModalRectWithCustomSizedTexture(0, 0, this.imageData.u, this.imageData.v, this.imageData.width, this.imageData.height, this.atlasData.width, this.atlasData.height);
    GlStateManager.popMatrix();
  }
}
