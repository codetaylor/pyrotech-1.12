package com.codetaylor.mc.pyrotech.modules.tech.basic.potion;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public class PotionWellRested
    extends PotionCampfireBase {

  public static final String NAME = "well_rested";

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleTechBasic.MOD_ID, "textures/potions/well_rested.png"
  );

  public PotionWellRested() {

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

  @Override
  public void removeAttributesModifiersFromEntity(EntityLivingBase entity, @Nonnull AbstractAttributeMap attributeMap, int amplifier) {

    int value = Math.max(0, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.WELL_RESTED_ABSORPTION_HALF_HEARTS);
    entity.setAbsorptionAmount(entity.getAbsorptionAmount() - value);
    super.removeAttributesModifiersFromEntity(entity, attributeMap, amplifier);
  }

  @Override
  public void applyAttributesModifiersToEntity(EntityLivingBase entity, @Nonnull AbstractAttributeMap attributeMap, int amplifier) {

    int value = Math.max(0, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.WELL_RESTED_ABSORPTION_HALF_HEARTS);
    entity.setAbsorptionAmount(entity.getAbsorptionAmount() + value);
    super.applyAttributesModifiersToEntity(entity, attributeMap, amplifier);
  }
}
