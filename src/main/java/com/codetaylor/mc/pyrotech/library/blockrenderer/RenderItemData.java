package com.codetaylor.mc.pyrotech.library.blockrenderer;

import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RenderItemData {

  private final ItemStack itemStack;
  private final List<Offset> offsets;

  public RenderItemData(ItemStack itemStack) {

    this.itemStack = itemStack;
    this.offsets = Collections.singletonList(Offset.DEFAULT);
  }

  public RenderItemData(ItemStack itemStack, List<Offset> offsets) {

    this.itemStack = itemStack;
    this.offsets = offsets;
  }

  public ItemStack getItemStack() {

    return this.itemStack;
  }

  public List<Offset> getOffsets() {

    return this.offsets;
  }

  public static class Offset {

    public static final Offset DEFAULT = new Offset(0, 0, 0, 0);

    private final int renderX;
    private final int renderY;
    private final int stitchX;
    private final int stitchY;

    public Offset(int renderX, int renderY, int stitchX, int stitchY) {

      this.renderX = renderX;
      this.renderY = renderY;
      this.stitchX = stitchX;
      this.stitchY = stitchY;
    }

    public int getRenderX() {

      return this.renderX;
    }

    public int getRenderY() {

      return this.renderY;
    }

    public int getStitchX() {

      return this.stitchX;
    }

    public int getStitchY() {

      return this.stitchY;
    }
  }
}
