package com.codetaylor.mc.pyrotech.modules.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SCPacketRestartRequired
    implements IMessage,
    IMessageHandler<SCPacketRestartRequired, IMessage> {

  @SuppressWarnings("unused")
  public SCPacketRestartRequired() {
    // Serialization
  }

  @Override
  public void fromBytes(ByteBuf buffer) {
    //
  }

  @Override
  public void toBytes(ByteBuf buffer) {
    //
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(SCPacketRestartRequired message, MessageContext ctx) {

    Minecraft.getMinecraft().displayGuiScreen(new GuiErrorScreen("", "") {

      private final ResourceLocation resourceLocation = new ResourceLocation("pyrotech", "textures/gui/logo.png");

      @Override
      public void initGui() {

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 170, I18n.format("gui.pyrotech.restart.button")));
      }

      @Override
      public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawBackground(0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture((this.width / 2) - 142, 8, 0, 0, 284, 67, 284, 67);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.pyrotech.restart1"), this.width / 2, 80, 16777215);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.pyrotech.restart2"), this.width / 2, 100, 16777215);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.pyrotech.restart3"), this.width / 2, 120, 16777215);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.pyrotech.restart4"), this.width / 2, 140, 16777215);

        for (int i = 0; i < this.buttonList.size(); ++i) {
          this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (int j = 0; j < this.labelList.size(); ++j) {
          this.labelList.get(j).drawLabel(this.mc, mouseX, mouseY);
        }
      }
    });

    return null;
  }
}
