package com.codetaylor.mc.pyrotech.library.blockrenderer;

import com.google.common.io.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Derived from https://github.com/elytra/BlockRenderer
 */
public class BlockRenderer {

  protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

  protected final IBulkRenderItemSupplier bulkRenderItemSupplier;

  protected boolean pendingBulkRender;
  protected int pendingBulkRenderSize;

  public BlockRenderer(IBulkRenderItemSupplier bulkRenderItemSupplier) {

    this.bulkRenderItemSupplier = bulkRenderItemSupplier;
  }

  public void render(int size) {

    this.pendingBulkRenderSize = size;
    this.pendingBulkRender = true;
  }

  public void onFrameStart() {

    /*
     * Quick primer: OpenGL is double-buffered. This means, where we draw to is
     * /not/ on the screen. As such, we are free to do whatever we like before
     * Minecraft renders, as long as we put everything back the way it was.
     */

    if (this.pendingBulkRender) {
      // We *must* call render code in pre-render. If we don't, it won't work right.
      this.bulkRender(this.bulkRenderItemSupplier, this.pendingBulkRenderSize);
      this.pendingBulkRender = false;
    }
  }

  private void bulkRender(IBulkRenderItemSupplier renderItemSupplier, int size) {

    Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());

    List<ItemStack> toRender = renderItemSupplier.get();

    File folder = new File("renders/" + DATE_FORMAT.format(new Date()) + "/");

    this.setUpRenderState(size);
    this.renderItemStacks(size, toRender, folder);
    this.tearDownRenderState();

    try {
      Thread.sleep(1500);

    } catch (InterruptedException ignored) {
      //
    }
  }

  private void renderItemStacks(int size, List<ItemStack> itemStacks, File folder) {

    int rendered = 0;
    long lastUpdate = 0;

    for (ItemStack itemStack : itemStacks) {

      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
        break;
      }

      this.render(itemStack, folder);

      rendered++;

      if (Minecraft.getSystemTime() - lastUpdate > 33) {
        this.tearDownRenderState();
        this.renderLoading(
            String.format("Rendering %1$s items", itemStacks.size()),
            String.format("(%1$s/%2$s, %3$s to go)", rendered, itemStacks.size(), (itemStacks.size() - rendered)),
            itemStack,
            (float) rendered / itemStacks.size()
        );
        lastUpdate = Minecraft.getSystemTime();
        this.setUpRenderState(size);
      }
    }

    if (rendered >= itemStacks.size()) {
      this.renderLoading(String.format("Rendered %1$s items", itemStacks.size()), "", null, 1);

    } else {
      this.renderLoading(
          "Rendering Cancelled",
          String.format("(%1$s/%2$s, %3$s to go)", rendered, itemStacks.size(), (itemStacks.size() - rendered)),
          null, (float) rendered / itemStacks.size()
      );
    }
  }

  private void renderLoading(String title, String subtitle, ItemStack is, float progress) {

    Minecraft mc = Minecraft.getMinecraft();
    mc.getFramebuffer().unbindFramebuffer();
    GlStateManager.pushMatrix();
    ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
    mc.entityRenderer.setupOverlayRendering();
    // Draw the dirt background and status text...
    Rendering.drawBackground(res.getScaledWidth(), res.getScaledHeight());
    Rendering.drawCenteredString(mc.fontRenderer, title, res.getScaledWidth() / 2, res.getScaledHeight() / 2 - 24, -1);
    Rendering.drawRect(res.getScaledWidth() / 2 - 50, res.getScaledHeight() / 2 - 1, res.getScaledWidth() / 2 + 50, res.getScaledHeight() / 2 + 1, 0xFF001100);
    Rendering.drawRect(res.getScaledWidth() / 2 - 50, res.getScaledHeight() / 2 - 1, (res.getScaledWidth() / 2 - 50) + (int) (progress * 100), res.getScaledHeight() / 2 + 1, 0xFF55FF55);
    GlStateManager.pushMatrix();
    GlStateManager.scale(0.5f, 0.5f, 1);
    Rendering.drawCenteredString(mc.fontRenderer, subtitle, res.getScaledWidth(), res.getScaledHeight() - 20, -1);
    // ...and draw the tooltip.
    if (is != null) {
      try {
        List<String> list = is.getTooltip(mc.player, ITooltipFlag.TooltipFlags.NORMAL);

        // This code is copied from the tooltip renderer, so we can properly center it.
        for (int i = 0; i < list.size(); ++i) {
          if (i == 0) {
            list.set(i, is.getRarity().rarityColor + list.get(i));
          } else {
            list.set(i, TextFormatting.GRAY + list.get(i));
          }
        }

        FontRenderer font = is.getItem().getFontRenderer(is);
        if (font == null) {
          font = mc.fontRenderer;
        }
        int width = 0;

        for (String s : list) {
          int j = font.getStringWidth(s);

          if (j > width) {
            width = j;
          }
        }
        // End copied code.
        GlStateManager.translate((res.getScaledWidth() - width / 2) - 12, res.getScaledHeight() + 30, 0);
        Rendering.drawHoveringText(list, 0, 0, font);
      } catch (Throwable t) {
      }
    }
    GlStateManager.popMatrix();
    GlStateManager.popMatrix();
    mc.updateDisplay();
    /*
     * While OpenGL itself is double-buffered, Minecraft is actually *triple*-buffered.
     * This is to allow shaders to work, as shaders are only available in "modern" GL.
     * Minecraft uses "legacy" GL, so it renders using a separate GL context to this
     * third buffer, which is then flipped to the back buffer with this call.
     */
    mc.getFramebuffer().bindFramebuffer(false);
  }

  private String render(ItemStack is, File folder) {

    Minecraft mc = Minecraft.getMinecraft();
    String filename = Util.sanitize(is.getDisplayName()).toLowerCase().replaceAll(" ", "_");
    GlStateManager.pushMatrix();
    GlStateManager.clearColor(0, 0, 0, 0);
    GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    mc.getRenderItem().renderItemAndEffectIntoGUI(is, 0, 16);
    GlStateManager.popMatrix();

    try {

      /*
       * We need to flip the image over here, because again, GL Y-zero is
       * the bottom, so it's "Y-up". Minecraft's Y-zero is the top, so it's
       * "Y-down". Since readPixels is Y-up, our Y-down render is flipped.
       * It's easier to do this operation on the resulting image than to
       * do it with GL transforms. Not faster, just easier.
       */
      BufferedImage img = Util.createFlipped(this.readPixels(size, size));

      File f = new File(folder, filename + ".png");
      int i = 2;
      while (f.exists()) {
        f = new File(folder, filename + "_" + i + ".png");
        i++;
      }

      //noinspection UnstableApiUsage
      Files.createParentDirs(f);

      //noinspection ResultOfMethodCallIgnored
      f.createNewFile();

      ImageIO.write(img, "PNG", f);
      return String.format("Successfully rendered to %s", f.getPath());

    } catch (Exception ex) {
      ex.printStackTrace();
      return I18n.format("Rendering failed, see game output for details");
    }
  }

  private int size;
  private float oldZLevel;

  private void setUpRenderState(int desiredSize) {

    Minecraft mc = Minecraft.getMinecraft();
    ScaledResolution res = new ScaledResolution(mc);
    /*
     * As we render to the back-buffer, we need to cap our render size
     * to be within the window's bounds. If we didn't do this, the results
     * of our readPixels up ahead would be undefined. And nobody likes
     * undefined behavior.
     */
    this.size = Math.min(Math.min(mc.displayHeight, mc.displayWidth), desiredSize);

    // Switches from 3D to 2D
    mc.entityRenderer.setupOverlayRendering();
    RenderHelper.enableGUIStandardItemLighting();
    /*
     * The GUI scale affects us due to the call to setupOverlayRendering
     * above. As such, we need to counteract this to always get a 512x512
     * render. We could manually switch to orthogonal mode, but it's just
     * more convenient to leverage setupOverlayRendering.
     */
    float scale = this.size / (16f * res.getScaleFactor());
    GlStateManager.translate(0, 0, -(scale * 100));

    GlStateManager.scale(scale, scale, scale);

    oldZLevel = mc.getRenderItem().zLevel;
    mc.getRenderItem().zLevel = -50;

    GlStateManager.enableRescaleNormal();
    GlStateManager.enableColorMaterial();
    GlStateManager.enableDepth();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GlStateManager.disableAlpha();
  }

  private void tearDownRenderState() {

    GlStateManager.disableLighting();
    GlStateManager.disableColorMaterial();
    GlStateManager.disableDepth();
    GlStateManager.disableBlend();

    Minecraft.getMinecraft().getRenderItem().zLevel = oldZLevel;
  }

  public BufferedImage readPixels(int width, int height) {
    /*
     * Make sure we're reading from the back buffer, not the front buffer.
     * The front buffer is what is currently on-screen, and is useful for
     * screenshots.
     */
    GL11.glReadBuffer(GL11.GL_BACK);
    // Allocate a native data array to fit our pixels
    ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
    // And finally read the pixel data from the GPU...
    GL11.glReadPixels(0, Minecraft.getMinecraft().displayHeight - height, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buf);
    // ...and turn it into a Java object we can do things to.
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    int[] pixels = new int[width * height];
    buf.asIntBuffer().get(pixels);
    img.setRGB(0, 0, width, height, pixels, 0, width);
    return img;
  }

}
