package com.codetaylor.mc.pyrotech.modules.core.plugin.top;

import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

  private static int ELEMENT_TEXT_LOCALIZED;
  private static int ELEMENT_ITEM_LABEL;
  private static int ELEMENT_TANK_LABEL;

  public static class Callback
      implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {

      PluginTOP.ELEMENT_TEXT_LOCALIZED = top.registerElementFactory(ElementTextLocalized::new);
      PluginTOP.ELEMENT_ITEM_LABEL = top.registerElementFactory(ElementItemLabel::new);
      PluginTOP.ELEMENT_TANK_LABEL = top.registerElementFactory(ElementTankLabel::new);
      return null;
    }
  }

  public static class ElementItemLabel
      implements IElement {

    private final String textFormatting;
    private final ItemStack itemStack;

    public ElementItemLabel(@Nullable TextFormatting textFormatting, ItemStack itemStack) {

      if (textFormatting == null) {
        this.textFormatting = null;

      } else {
        this.textFormatting = textFormatting.toString();
      }

      this.itemStack = itemStack;
    }

    public ElementItemLabel(ByteBuf buf) {

      int length = buf.readInt();

      if (length > 0) {
        this.textFormatting = new PacketBuffer(buf).readString(length);

      } else {
        this.textFormatting = null;
      }

      if (buf.readBoolean()) {
        this.itemStack = NetworkTools.readItemStack(buf);

      } else {
        this.itemStack = ItemStack.EMPTY;
      }
    }

    @Override
    public void render(int x, int y) {

      if (!this.itemStack.isEmpty()) {
        String text = this.itemStack.getDisplayName();

        if (this.textFormatting == null) {
          ElementTextRender.render(text, x, y);

        } else {
          ElementTextRender.render(this.textFormatting + text, x, y);
        }
      }
    }

    @Override
    public int getWidth() {

      if (!this.itemStack.isEmpty()) {
        String text = this.itemStack.getDisplayName();
        return ElementTextRender.getWidth(text);

      } else {
        return 10;
      }
    }

    @Override
    public int getHeight() {

      return 10;
    }

    @Override
    public void toBytes(ByteBuf buf) {

      if (this.textFormatting == null) {
        buf.writeInt(0);

      } else {
        buf.writeInt(this.textFormatting.length());
        new PacketBuffer(buf).writeString(this.textFormatting);
      }

      if (!this.itemStack.isEmpty()) {
        buf.writeBoolean(true);
        NetworkTools.writeItemStack(buf, this.itemStack);

      } else {
        buf.writeBoolean(false);
      }
    }

    @Override
    public int getID() {

      return PluginTOP.ELEMENT_ITEM_LABEL;
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

  public static class ElementTankLabel
      implements IElement {

    private final String textFormatting;
    private final FluidStack fluidStack;
    private final int capacity;

    private String renderText;
    public static final String LANG_KEY = "gui." + ModuleCore.MOD_ID + ".waila.tank.fluid";

    public ElementTankLabel(@Nullable TextFormatting textFormatting, FluidStack fluidStack, int capacity) {

      if (textFormatting == null) {
        this.textFormatting = null;

      } else {
        this.textFormatting = textFormatting.toString();
      }

      this.fluidStack = Preconditions.checkNotNull(fluidStack);
      this.capacity = capacity;
    }

    public ElementTankLabel(ByteBuf buf) {

      int length = buf.readInt();

      if (length > 0) {
        this.textFormatting = new PacketBuffer(buf).readString(length);

      } else {
        this.textFormatting = null;
      }

      NBTTagCompound compound;

      try {
        compound = new PacketBuffer(buf).readCompoundTag();

      } catch (IOException e) {
        this.fluidStack = null;
        throw new RuntimeException("", e);
      }

      this.capacity = buf.readInt();

      if (compound != null) {
        this.fluidStack = FluidStack.loadFluidStackFromNBT(compound);

      } else {
        this.fluidStack = null;
      }

      if (this.fluidStack != null) {
        StringBuilder builder = new StringBuilder();

        if (this.textFormatting != null) {
          String localizedName = this.fluidStack.getLocalizedName();
          this.renderText = this.textFormatting
              + Util.translateFormatted(LANG_KEY, localizedName, this.fluidStack.amount, this.capacity);
        }
      }
    }

    @Override
    public void render(int x, int y) {

      if (this.renderText != null) {
        ElementTextRender.render(this.renderText, x, y);
      }
    }

    @Override
    public int getWidth() {

      if (this.renderText != null) {
        return ElementTextRender.getWidth(this.renderText);

      } else {
        return 10;
      }
    }

    @Override
    public int getHeight() {

      return 10;
    }

    @Override
    public void toBytes(ByteBuf buf) {

      if (this.textFormatting == null) {
        buf.writeInt(0);

      } else {
        buf.writeInt(this.textFormatting.length());
        new PacketBuffer(buf).writeString(this.textFormatting);
      }

      NBTTagCompound compound = this.fluidStack.writeToNBT(new NBTTagCompound());
      new PacketBuffer(buf).writeCompoundTag(compound);

      buf.writeInt(this.capacity);
    }

    @Override
    public int getID() {

      return PluginTOP.ELEMENT_TANK_LABEL;
    }
  }

}
