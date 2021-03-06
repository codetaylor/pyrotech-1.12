package com.codetaylor.mc.pyrotech.modules.core.event;

import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = ModPyrotech.MOD_ID, value = Side.CLIENT)
public class NoHungerEventHandler {

  private static final ResourceLocation TEXTURE = new ResourceLocation(ModuleCore.MOD_ID, "textures/gui/no_hunger.png");
  private static final int DISPLAY_TICKS = 2 * 20;

  private static int displayTickCounter;

  @SubscribeEvent
  public static void on(NoHungerEvent event) {

    displayTickCounter = DISPLAY_TICKS;
  }

  @SubscribeEvent
  public static void on(TickEvent.ClientTickEvent event) {

    if (displayTickCounter > 0) {
      displayTickCounter -= 1;
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void on(RenderGameOverlayEvent event) {

    if (!(event instanceof RenderGameOverlayEvent.Text)) {
      return;
    }

    if (displayTickCounter > 0) {

      ScaledResolution resolution = event.getResolution();
      int centerX = resolution.getScaledWidth() / 2;
      int centerY = resolution.getScaledHeight() / 2;

      Minecraft minecraft = Minecraft.getMinecraft();
      GlStateManager.enableAlpha();

      GuiHelper.drawTexturedRect(
          minecraft,
          TEXTURE,
          centerX - 9, centerY - 9,
          18, 18,
          0,
          0, 0,
          18 / 32.0, 18 / 32.0
      );
    }
  }

  public static class NoHungerEvent
      extends Event {
    //
  }
}
