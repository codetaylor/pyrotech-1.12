package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class PotionComfort
    extends PotionCampfireBase {

  public static final String NAME = "comfort";

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/comfort.png"
  );

  public PotionComfort() {

    super(false, Color.BLACK.getRGB());
  }

  @Override
  protected ResourceLocation getTexture() {

    return TEXTURE;
  }

  @Override
  public boolean isReady(int duration, int amplifier) {

    return false;
  }

  @Nonnull
  @Override
  public List<ItemStack> getCurativeItems() {

    return Collections.emptyList();
  }
}
