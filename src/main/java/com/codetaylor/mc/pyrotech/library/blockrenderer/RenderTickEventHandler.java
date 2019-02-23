package com.codetaylor.mc.pyrotech.library.blockrenderer;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class RenderTickEventHandler {

  private final List<BlockRenderer> blockRenderers;

  public RenderTickEventHandler(List<BlockRenderer> blockRenderers) {

    this.blockRenderers = blockRenderers;
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onFrameStart(TickEvent.RenderTickEvent e) {

    /*
     * Quick primer: OpenGL is double-buffered. This means, where we draw to is
     * /not/ on the screen. As such, we are free to do whatever we like before
     * Minecraft renders, as long as we put everything back the way it was.
     */

    if (e.phase == TickEvent.Phase.START) {

      for (BlockRenderer blockRenderer : this.blockRenderers) {
        blockRenderer.onFrameStart();
      }
    }
  }
}
