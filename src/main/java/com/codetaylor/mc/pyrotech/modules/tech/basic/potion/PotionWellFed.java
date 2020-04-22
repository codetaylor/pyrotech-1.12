package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class PotionWellFed
    extends PotionCampfireBase {

  public static final String NAME = "well_fed";

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/well_fed.png"
  );

  public PotionWellFed() {

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
}
