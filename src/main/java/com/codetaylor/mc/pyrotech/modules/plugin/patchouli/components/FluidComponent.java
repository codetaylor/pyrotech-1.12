package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.components;

import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.VariableHolder;

public class FluidComponent
    implements ICustomComponent {

  @VariableHolder
  public String fluid;

  public int amount;
  public int width, height;
  public int x, y;

  private transient FluidStack fluidStack;
  private TextureAtlasSprite fluidSprite;

  public FluidComponent() {

    this.amount = 1000;
    this.width = 16;
    this.height = 16;
  }

  @Override
  public void build(int componentX, int componentY, int pageNum) {

    this.x = componentX;
    this.y = componentY;

    this.fluidStack = FluidRegistry.getFluidStack(this.fluid, this.amount);

    if (this.fluidStack != null) {
      ResourceLocation resourceLocation = this.fluidStack.getFluid().getStill();
      this.fluidSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resourceLocation.toString());
    }
  }

  @Override
  public void render(IComponentRenderContext context, float partialTicks, int mouseX, int mouseY) {

    if (this.fluidStack != null) {

      int color = this.fluidStack.getFluid().getColor();

      GlStateManager.color(
          ((color >> 16) & 0xFF) / 255f,
          ((color >> 8) & 0xFF) / 255f,
          (color & 0xFF) / 255f,
          ((color >> 24) & 0xFF) / 255f
      );
    }

    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

    GuiHelper.drawVerticalScaledTexturedModalRectFromIconAnchorBottomLeft(this.x, this.y, 0, this.fluidSprite, this.width, this.height);

    if (this.fluidStack != null) {
      GlStateManager.color(1, 1, 1, 1);
    }
  }
}
