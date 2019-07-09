package com.codetaylor.mc.pyrotech.modules.core.plugin.top;

import com.codetaylor.mc.pyrotech.library.util.Util;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

  private static int ELEMENT_TEXT_LOCALIZED;

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      PluginTOP.ELEMENT_TEXT_LOCALIZED = top.registerElementFactory(ElementTextLocalized::new);
      return null;
    }
  }

  public static class ElementTextLocalized
      implements IElement {

    private static final byte DATA_TYPE_STRING = 0;
    private static final byte DATA_TYPE_FLOAT = 1;
    private static final byte DATA_TYPE_DOUBLE = 2;
    private static final byte DATA_TYPE_INT = 3;
    private static final byte DATA_TYPE_LONG = 4;

    private String textFormatting;
    private String langKey;
    private Object[] args;

    private String renderText;

    public ElementTextLocalized(String langKey, Object... args) {

      this(null, langKey, args);
    }

    public ElementTextLocalized(@Nullable TextFormatting textFormatting, String langKey, Object... args) {

      this.textFormatting = (textFormatting != null) ? textFormatting.toString() : null;
      this.langKey = langKey;
      this.args = args;
    }

    /* package */ ElementTextLocalized(ByteBuf buf) {

      this.fromBytes(buf);

      if (this.textFormatting != null) {
        this.renderText = this.textFormatting + Util.translateFormatted(this.langKey, this.args);

      } else {
        this.renderText = Util.translateFormatted(this.langKey, this.args);
      }
    }

    @Override
    public void render(int x, int y) {

      ElementTextRender.render(this.renderText, x, y);
    }

    @Override
    public int getWidth() {

      return ElementTextRender.getWidth(this.renderText);
    }

    @Override
    public int getHeight() {

      return 10;
    }

    @Override
    public void toBytes(ByteBuf buf) {

      PacketBuffer b = new PacketBuffer(buf);

      // lang key

      b.writeInt(this.langKey.length());
      b.writeString(this.langKey);

      // text formatting

      if (this.textFormatting == null) {
        b.writeInt(0);

      } else {
        b.writeInt(this.textFormatting.length());
        b.writeString(this.textFormatting);
      }

      // args

      if (this.args == null || this.args.length == 0) {
        b.writeInt(0);

      } else {
        b.writeInt(this.args.length);

        for (Object arg : this.args) {

          if (arg instanceof String) {
            b.writeByte(DATA_TYPE_STRING);
            b.writeInt(((String) arg).length());
            b.writeString((String) arg);

          } else if (arg instanceof Float) {
            b.writeByte(DATA_TYPE_FLOAT);
            b.writeFloat((Float) arg);

          } else if (arg instanceof Double) {
            b.writeByte(DATA_TYPE_DOUBLE);
            b.writeDouble((Double) arg);

          } else if (arg instanceof Integer) {
            b.writeByte(DATA_TYPE_INT);
            b.writeInt((Integer) arg);

          } else if (arg instanceof Long) {
            b.writeByte(DATA_TYPE_LONG);
            b.writeLong((Long) arg);

          } else {
            throw new RuntimeException("Unknown data type: " + arg.getClass());
          }
        }
      }

    }

    private void fromBytes(ByteBuf buf) {

      PacketBuffer b = new PacketBuffer(buf);

      // lang key

      this.langKey = b.readString(b.readInt());

      // text formatting

      int textFormattingStringLength = b.readInt();

      if (textFormattingStringLength > 0) {
        this.textFormatting = b.readString(textFormattingStringLength);
      }

      // args

      int argCount = b.readInt();
      this.args = new Object[argCount];

      for (int i = 0; i < argCount; i++) {
        byte dataType = b.readByte();

        if (dataType == DATA_TYPE_STRING) {
          int length = b.readInt();
          String key = b.readString(length);
          this.args[i] = Util.translateFormatted(key);

        } else if (dataType == DATA_TYPE_FLOAT) {
          this.args[i] = b.readFloat();

        } else if (dataType == DATA_TYPE_DOUBLE) {
          this.args[i] = b.readDouble();

        } else if (dataType == DATA_TYPE_INT) {
          this.args[i] = b.readInt();

        } else if (dataType == DATA_TYPE_LONG) {
          this.args[i] = b.readLong();

        } else {
          throw new RuntimeException("Unknown data type: " + dataType);
        }
      }
    }

    @Override
    public int getID() {

      return PluginTOP.ELEMENT_TEXT_LOCALIZED;
    }
  }

}
