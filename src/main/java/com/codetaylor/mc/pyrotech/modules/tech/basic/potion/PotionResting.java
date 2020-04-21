package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public class PotionResting
    extends PotionCampfireBase {

  public static final String NAME = "resting";

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/resting.png"
  );

  public PotionResting() {

    super(false, Color.BLACK.getRGB());
  }

  @Override
  protected ResourceLocation getTexture() {

    return TEXTURE;
  }

  @Override
  public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {

    if (entity.getHealth() < entity.getMaxHealth()) {
      int amount = Math.max(0, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.RESTING_REGEN_HALF_HEARTS);

      if (amount > 0) {
        entity.heal(amount);
      }
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {

    int rate = Math.max(1, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.RESTING_REGEN_INTERVAL_TICKS);
    return duration % rate == 0;
  }
}
